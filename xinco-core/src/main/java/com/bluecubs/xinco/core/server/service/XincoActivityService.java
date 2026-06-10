package com.bluecubs.xinco.core.server.service;

import static com.bluecubs.xinco.core.OPCode.CHECKIN;
import static com.bluecubs.xinco.core.OPCode.CHECKOUT;
import static com.bluecubs.xinco.core.OPCode.CHECKOUT_UNDONE;
import static com.bluecubs.xinco.core.OPCode.getOPCode;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreLog;
import com.bluecubs.xinco.server.service.XincoVersion;
import com.bluecubs.xinco.server.service.XincoWebService;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for handling high-level Xinco activities like check-in, check-out, and log management.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Service
public class XincoActivityService {

  private static final Logger LOG = getLogger(XincoActivityService.class.getName());
  private final XincoWebService webService;
  private final ResourceBundle resourceBundle;

  @Autowired
  public XincoActivityService(XincoWebService webService) {
    this.webService = webService;
    this.resourceBundle = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
  }

  /**
   * Performs a file checkout operation.
   *
   * @param data The data item to checkout.
   * @param user The user performing the checkout.
   * @return The updated data item, or null if failed.
   * @throws XincoException If an error occurs during the operation.
   */
  public XincoCoreData checkoutFile(XincoCoreDataServer data, XincoCoreUserServer user)
      throws XincoException {
    return performLogAndActivity(data, user, CHECKOUT);
  }

  /**
   * Performs an undo file checkout operation.
   *
   * @param data The data item to undo checkout.
   * @param user The user performing the operation.
   * @return The updated data item, or null if failed.
   * @throws XincoException If an error occurs during the operation.
   */
  public XincoCoreData undoCheckoutFile(XincoCoreDataServer data, XincoCoreUserServer user)
      throws XincoException {
    return performLogAndActivity(data, user, CHECKOUT_UNDONE);
  }

  /**
   * Performs a file check-in operation.
   *
   * @param data The data item to check-in.
   * @param user The user performing the check-in.
   * @param newVersion The new version for the data.
   * @param fileData The file content bytes.
   * @param logReason The reason for the check-in (stored in log).
   * @return The updated data item, or null if failed.
   * @throws XincoException If an error occurs during the operation.
   */
  public XincoCoreData checkinFile(
      XincoCoreDataServer data,
      XincoCoreUserServer user,
      XincoVersion newVersion,
      byte[] fileData,
      String logReason)
      throws XincoException {
    try {
      XincoCoreLog newlog = createActivityLog(data, user, CHECKIN);
      if (logReason != null && !logReason.isEmpty()) {
        newlog.setOpDescription(newlog.getOpDescription() + " - " + logReason);
      }
      newlog.setVersion(newVersion);

      // save log to server
      newlog = webService.setXincoCoreLog(newlog, user);
      if (newlog != null) {
        data.getXincoCoreLogs().add(newlog);
      }

      // Upload file data
      int uploaded = webService.uploadXincoCoreData(data, fileData, user);
      if (uploaded <= 0) {
        throw new XincoException("File upload failed during checkin.");
      }

      return webService.doXincoCoreDataCheckin(data, user);
    } catch (Exception ex) {
      LOG.log(SEVERE, "Error during checkinFile", ex);
      throw new XincoException("Checkin failed: " + ex.getMessage());
    }
  }

  private XincoCoreData performLogAndActivity(
      XincoCoreDataServer data, XincoCoreUserServer user, com.bluecubs.xinco.core.OPCode opCode)
      throws XincoException {
    try {
      XincoCoreLog newlog = createActivityLog(data, user, opCode);

      // save log to server
      newlog = webService.setXincoCoreLog(newlog, user);
      if (newlog != null) {
        data.getXincoCoreLogs().add(newlog);
      }

      switch (opCode) {
        case CHECKOUT:
          return webService.doXincoCoreDataCheckout(data, user);
        case CHECKOUT_UNDONE:
          return webService.undoXincoCoreDataCheckout(data, user);
        default:
          throw new XincoException("Unsupported operation code: " + opCode);
      }
    } catch (Exception ex) {
      LOG.log(SEVERE, "Error during " + opCode.getName(), ex);
      throw new XincoException(opCode.getName() + " failed: " + ex.getMessage());
    }
  }

  private XincoCoreLog createActivityLog(
      XincoCoreDataServer data, XincoCoreUserServer user, com.bluecubs.xinco.core.OPCode opCode) {
    XincoCoreLog newlog = new XincoCoreLog();
    newlog.setOpCode(opCode.ordinal() + 1);
    String opName = getOPCode(newlog.getOpCode()).getName();
    newlog.setOpDescription(resourceBundle.getString(opName));
    newlog.setXincoCoreUserId(user.getId());
    newlog.setXincoCoreDataId(data.getId());

    if (!data.getXincoCoreLogs().isEmpty()) {
      newlog.setVersion(
          ((XincoCoreLog) data.getXincoCoreLogs().get(data.getXincoCoreLogs().size() - 1))
              .getVersion());
    }

    newlog.setOpDescription(
        newlog.getOpDescription()
            + " ("
            + resourceBundle.getString("general.user")
            + ": "
            + user.getUsername()
            + ")");
    return newlog;
  }
}
