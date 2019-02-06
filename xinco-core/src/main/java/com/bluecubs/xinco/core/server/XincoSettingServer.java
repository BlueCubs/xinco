package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreUserServer.validCredentials;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import com.bluecubs.xinco.core.server.persistence.controller.XincoSettingJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import com.bluecubs.xinco.core.server.service.XincoSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * Handles the XincoSetting CRUD operations.
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public class XincoSettingServer extends XincoSetting {

    public XincoSettingServer(int id) throws XincoException {
        XincoSettingJpaController controller = new XincoSettingJpaController(
                getEntityManagerFactory());
        com.bluecubs.xinco.core.server.persistence.XincoSetting setting =
                controller.findXincoSetting(id);
        if (setting != null) {
            if (setting.getBoolValue() != null) {
                setBoolValue(setting.getBoolValue());
            }
            setId(id);
            if (setting.getIntValue() != null) {
                setIntValue(setting.getIntValue());
            }
            setLongValue(setting.getLongValue());
            if (setting.getStringValue() != null) {
                setStringValue(setting.getStringValue());
            }
            if (setting.getDescription() != null) {
                setDescription(setting.getDescription());
            }
        } else {
            throw new XincoException("Setting with id: " + id + " not found!");
        }
    }

    public XincoSettingServer(int id, String description, int intVal,
            String stringVal, boolean boolVal, long longVal) {
        setBoolValue(boolVal);
        setId(id);
        setIntValue(intVal);
        setLongValue(longVal);
        setStringValue(stringVal);
        setDescription(description);
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoSettingJpaController controller = new XincoSettingJpaController(getEntityManagerFactory());
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
            //no commit or rollback -> CoreData manages exceptions!
            getLogger(XincoAddAttributeServer.class.getSimpleName()).log(SEVERE, null, ex);
            throw new XincoException();
        }
        return 1;
    }

    public static int deleteFromDB(XincoSetting setting) throws XincoException {
        XincoSettingJpaController controller = new XincoSettingJpaController(
                getEntityManagerFactory());
        if (setting != null) {
            try {
                controller.destroy(setting.getId());
            } catch (NonexistentEntityException ex) {
                throw new XincoException();
            }
        }
        return 0;
    }

    public static XincoSettingServer getSetting(XincoCoreUser user, String desc) throws XincoException {
        return getSetting(user, desc, false);
    }

    public static XincoSettingServer getSetting(XincoCoreUser user, String desc, boolean encrypt) throws XincoException {
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
            return new XincoSettingServer(((com.bluecubs.xinco.core.server.persistence.XincoSetting) result.get(0)).getId());
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

    protected static List<XincoSettingServer> getSettings() throws XincoException {
        ArrayList<XincoSettingServer> settings = new ArrayList<>();
        List result = namedQuery("XincoSetting.findAll", null);
        if (!result.isEmpty()) {
            for (Object o : result) {
                settings.add(new XincoSettingServer(((com.bluecubs.xinco.core.server.persistence.XincoSetting) o).getId()));
            }
        }
        return settings;
    }
}
