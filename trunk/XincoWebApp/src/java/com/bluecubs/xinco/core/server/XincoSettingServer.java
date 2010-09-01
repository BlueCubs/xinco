package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.controller.XincoSettingJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.server.service.XincoSetting;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the XincoSetting CRUD operations.
 * 
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoSettingServer extends XincoSetting {

    public XincoSettingServer(int id) throws XincoException {
        XincoSettingJpaController controller = new XincoSettingJpaController(
                XincoDBManager.getEntityManagerFactory());
        com.bluecubs.xinco.core.server.persistence.XincoSetting setting =
                controller.findXincoSetting(id);
        if (setting != null) {
            setBoolValue(setting.getBoolValue());
            setId(id);
            setIntValue(setting.getIntValue());
            setLongValue(setting.getLongValue());
            setStringValue(setting.getStringValue());
            setDescription(setting.getDescription());
        } else {
            throw new XincoException("Setting with id: " + id + " not found!");
        }
    }

    public XincoSettingServer() {
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            com.bluecubs.xinco.core.server.persistence.XincoSetting setting =
                    new com.bluecubs.xinco.core.server.persistence.XincoSetting();
            setBoolValue(setting.getBoolValue());
            if (id > 0) {
                setId(id);
            }
            setIntValue(setting.getIntValue());
            setLongValue(setting.getLongValue());
            setStringValue(setting.getStringValue());
            setDescription(setting.getDescription());
            XincoSettingJpaController controller = 
                    new XincoSettingJpaController(
                    XincoDBManager.getEntityManagerFactory());
            if (id == 0) {
                controller.create(setting);
            } else {
                controller.edit(setting);
            }
        } catch (Exception ex) {
            //no commit or rollback -> CoreData manages exceptions!
            Logger.getLogger(XincoAddAttributeServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException();
        }
        return 1;
    }
    
    public int deleteFromDB() throws XincoException {
        XincoSettingJpaController controller = new XincoSettingJpaController(
                XincoDBManager.getEntityManagerFactory());
        com.bluecubs.xinco.core.server.persistence.XincoSetting setting =
                controller.findXincoSetting(getId());
        if(setting!=null){
            try {
                controller.destroy(getId());
            } catch (NonexistentEntityException ex) {
                throw new XincoException();
            }
        }
        return 0;
    }
    
    public static XincoSettingServer getSetting(String desc) throws XincoException{
        HashMap parameters = new HashMap();
        parameters.put("description",desc);
        List result = XincoDBManager.namedQuery("XincoSetting.findByDescription",parameters);
        if(result.isEmpty()){
            throw new XincoException();
        }else{
            return new XincoSettingServer(((XincoSetting)result.get(0)).getId());
        }
    }
}
