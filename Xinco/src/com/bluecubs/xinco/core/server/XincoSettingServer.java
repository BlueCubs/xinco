package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoSetting;
import com.bluecubs.xinco.core.server.persistence.controller.XincoSettingJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoSettingServer extends XincoSetting {

    private static HashMap<String, Object> parameters = new HashMap<String, Object>();
    private static List<Object> result;

    public XincoSettingServer(int id, String description, int int_value, String string_value, boolean bool_value, int changerID, long long_value, Vector xinco_settings) {
        super(id, description, int_value, string_value, bool_value, changerID, long_value, xinco_settings);
    }

    public XincoSettingServer(com.bluecubs.xinco.core.server.persistence.XincoSetting setting) {
        setDescription(setting.getDescription());
        setBool_value(setting.getBoolValue() == null ? false : setting.getBoolValue());
        setId(setting.getId());
        setInt_value(setting.getIntValue() == null ? 0 : setting.getIntValue());
        setLong_value(setting.getLongValue() == null ? 0 : setting.getLongValue());
        setString_value(setting.getStringValue() == null ? "" : setting.getStringValue());
    }

    public XincoSettingServer(int id) throws XincoException {
        parameters.clear();
        parameters.put("id", id);
        result = XincoDBManager.namedQuery("XincoSetting.findById", parameters);
        com.bluecubs.xinco.core.server.persistence.XincoSetting setting =
                (com.bluecubs.xinco.core.server.persistence.XincoSetting) result.get(0);
        setDescription(setting.getDescription());
        setBool_value(setting.getBoolValue() == null ? false : setting.getBoolValue());
        setId(id);
        setInt_value(setting.getIntValue() == null ? 0 : setting.getIntValue());
        setLong_value(setting.getLongValue() == null ? 0 : setting.getLongValue());
        setString_value(setting.getStringValue() == null ? "" : setting.getStringValue());
    }

    public int write2DB() throws XincoException {
        try {
            XincoSettingJpaController controller = new XincoSettingJpaController();
            com.bluecubs.xinco.core.server.persistence.XincoSetting s;
            if (getId() > 0) {
                parameters.clear();
                parameters.put("id", getId());
                result = XincoDBManager.namedQuery("XincoSetting.findById", parameters);
                s = (com.bluecubs.xinco.core.server.persistence.XincoSetting) result.get(0);
                s.setDescription(getDescription());
                s.setBoolValue(isBool_value());
                s.setIntValue(getInt_value());
                s.setLongValue(getLong_value());
                s.setStringValue(getString_value());
                s.setModifierId(getChangerID());
                controller.edit(s);
            } else {
                s = new com.bluecubs.xinco.core.server.persistence.XincoSetting();
                s.setId(XincoDBManager.getNewID("xinco_setting"));
                s.setDescription(getDescription());
                s.setBoolValue(isBool_value());
                s.setIntValue(getInt_value());
                s.setLongValue(getLong_value());
                s.setStringValue(getString_value());
                s.setModifierId(getChangerID());
                controller.create(s);
                setId(s.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static int deleteFromDB(XincoSetting setting) {
        try {
            new XincoSettingJpaController().destroy(setting.getId());
            return 0;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public static XincoSettingServer getSetting(String desc) throws XincoException {
        parameters.clear();
        parameters.put("description", desc);
        result = XincoDBManager.namedQuery("XincoSetting.findByDescription", parameters);
        return new XincoSettingServer((com.bluecubs.xinco.core.server.persistence.XincoSetting) result.get(0));
    }

    public static Vector<XincoSettingServer> getSettings() throws XincoException {
        Vector<XincoSettingServer> settings = new Vector<XincoSettingServer>();
        result = XincoDBManager.namedQuery("XincoSetting.findAll");
        for (Object o : result) {
            settings.add(new XincoSettingServer((com.bluecubs.xinco.core.server.persistence.XincoSetting) o));
        }
        return settings;
    }
}
