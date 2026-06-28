package com.bluecubs.xinco.core.server.service;

import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.getRenderings;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoWebService;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for file-related operations like downloading and uploading data.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Service
public class XincoFileService {

  private static final Logger LOG = getLogger(XincoFileService.class.getName());
  private final XincoWebService webService;

  @Autowired
  public XincoFileService(XincoWebService webService) {
    this.webService = webService;
  }

  /**
   * Downloads the bytes for a given data item, optionally using a rendering (e.g. PDF).
   *
   * @param data The data item to download.
   * @param user The user requesting the download.
   * @param useRendering Whether to look for and use a rendering if available.
   * @return The file bytes.
   * @throws XincoException If an error occurs.
   */
  public byte[] downloadFile(XincoCoreData data, XincoCoreUserServer user, boolean useRendering)
      throws XincoException {
    try {
      XincoCoreData targetData = data;
      if (useRendering) {
        List<com.bluecubs.xinco.core.server.persistence.XincoCoreData> renderings =
            getRenderings(data.getId());
        for (com.bluecubs.xinco.core.server.persistence.XincoCoreData rendering : renderings) {
          if (!rendering.getXincoAddAttributeList().isEmpty()
              && rendering.getXincoAddAttributeList().get(0).getAttribVarchar().endsWith(".pdf")) {
            targetData = new XincoCoreDataServer(rendering.getId());
            break;
          }
        }
      }
      return webService.downloadXincoCoreData(targetData, user);
    } catch (Exception ex) {
      LOG.log(SEVERE, "Error downloading file", ex);
      throw new XincoException("Download failed: " + ex.getMessage());
    }
  }

  /**
   * Uploads file bytes for a data item.
   *
   * @param data The data item.
   * @param fileData The file bytes.
   * @param user The user uploading.
   * @return The number of bytes uploaded.
   * @throws XincoException If an error occurs.
   */
  public int uploadFile(XincoCoreData data, byte[] fileData, XincoCoreUserServer user)
      throws XincoException {
    try {
      return webService.uploadXincoCoreData(data, fileData, user);
    } catch (Exception ex) {
      LOG.log(SEVERE, "Error uploading file", ex);
      throw new XincoException("Upload failed: " + ex.getMessage());
    }
  }
}
