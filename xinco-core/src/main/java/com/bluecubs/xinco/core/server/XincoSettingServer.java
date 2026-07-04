package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreUserServer.validCredentials;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.controller.XincoSettingJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.server.service.XincoCoreUser;
import com.bluecubs.xinco.server.service.XincoSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the XincoSetting CRUD operations.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class XincoSettingServer extends XincoSetting {

  public static final String TYPE_BOOL = "bool";
  public static final String TYPE_INT = "int";
  public static final String TYPE_STRING = "string";

  private String settingType = TYPE_BOOL;

  public String getSettingType() {
    return settingType;
  }

  public XincoSettingServer(int id) throws XincoException {
    XincoSettingJpaController controller = new XincoSettingJpaController(getEntityManagerFactory());
    com.bluecubs.xinco.core.server.persistence.XincoSetting setting =
        controller.findXincoSetting(id);
    if (setting != null) {
      setId(id);
      if (setting.getBoolValue() != null) {
        setBoolValue(setting.getBoolValue());
        settingType = TYPE_BOOL;
      }
      if (setting.getIntValue() != null) {
        setIntValue(setting.getIntValue());
        settingType = TYPE_INT;
      }
      setLongValue(setting.getLongValue());
      if (setting.getStringValue() != null) {
        setStringValue(setting.getStringValue());
        settingType = TYPE_STRING;
      }
      if (setting.getDescription() != null) {
        setDescription(setting.getDescription());
      }
    } else {
      throw new XincoException("Setting with id: " + id + " not found!");
    }
  }

  /**
   * Direct constructor for in-memory use and tests. Pass null for unused type fields; the first
   * non-null value among intVal/stringVal determines the type (bool is the fallback).
   */
  public XincoSettingServer(
      int id, String description, Integer intVal, String stringVal, boolean boolVal, long longVal) {
    setId(id);
    setDescription(description);
    setLongValue(longVal);
    setBoolValue(boolVal);
    if (intVal != null) {
      setIntValue(intVal);
      settingType = TYPE_INT;
    } else if (stringVal != null) {
      setStringValue(stringVal);
      settingType = TYPE_STRING;
    }
  }

  // write to db
  public int write2DB() throws XincoException {
    try {
      XincoSettingJpaController controller =
          new XincoSettingJpaController(getEntityManagerFactory());
      com.bluecubs.xinco.core.server.persistence.XincoSetting setting;
      if (getId() > 0) {
        setting = controller.findXincoSetting(getId());
        setting.setBoolValue(isBoolValue());
        setting.setIntValue(getIntValue());
        setting.setLongValue(getLongValue());
        setting.setStringValue(getStringValue());
        setting.setDescription(getDescription());
        controller.edit(setting);
      } else {
        setting = new com.bluecubs.xinco.core.server.persistence.XincoSetting();
        setting.setBoolValue(isBoolValue());
        setting.setIntValue(getIntValue());
        setting.setLongValue(getLongValue());
        setting.setStringValue(getStringValue());
        setting.setDescription(getDescription());
        controller.create(setting);
      }
      setId(setting.getId());
    } catch (Exception ex) {
      // no commit or rollback -> CoreData manages exceptions!
      getLogger(XincoAddAttributeServer.class.getSimpleName()).log(SEVERE, null, ex);
      throw new XincoException();
    }
    return 1;
  }

  public static int deleteFromDB(XincoSetting setting) throws XincoException {
    XincoSettingJpaController controller = new XincoSettingJpaController(getEntityManagerFactory());
    if (setting != null) {
      try {
        controller.destroy(setting.getId());
      } catch (NonexistentEntityException ex) {
        throw new XincoException();
      }
    }
    return 0;
  }

  public static XincoSettingServer getSetting(XincoCoreUser user, String desc)
      throws XincoException {
    return getSetting(user, desc, false);
  }

  public static XincoSettingServer getSetting(XincoCoreUser user, String desc, boolean encrypt)
      throws XincoException {
    if (validCredentials(user.getUsername(), user.getUserpassword(), encrypt)) {
      return getSetting(desc);
    } else {
      return null;
    }
  }

  protected static XincoSettingServer getSetting(String desc) throws XincoException {
    HashMap parameters = new HashMap();
    parameters.put("description", desc);
    List result = namedQuery("XincoSetting.findByDescription", parameters);
    if (!result.isEmpty()) {
      return new XincoSettingServer(
          ((com.bluecubs.xinco.core.server.persistence.XincoSetting) result.get(0)).getId());
    } else {
      throw new XincoException("Unable to find setting: " + desc);
    }
  }

  public static List<XincoSettingServer> getSettings(XincoCoreUser user) throws XincoException {
    ArrayList<XincoSettingServer> settings = new ArrayList<>();
    if (validCredentials(user.getUsername(), user.getUserpassword())) {
      settings.addAll(getSettings());
    }
    return settings;
  }

  public static List<XincoSettingServer> getAllSettings() {
    try {
      return getSettings();
    } catch (XincoException e) {
      return List.of();
    }
  }

  protected static List<XincoSettingServer> getSettings() throws XincoException {
    ArrayList<XincoSettingServer> settings = new ArrayList<>();
    List result = namedQuery("XincoSetting.findAll", null);
    if (!result.isEmpty()) {
      for (Object o : result) {
        settings.add(
            new XincoSettingServer(
                ((com.bluecubs.xinco.core.server.persistence.XincoSetting) o).getId()));
      }
    }
    return settings;
  }
}
