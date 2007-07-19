/*
 * SettingServer.java
 *
 * Created on July 19, 2007, 9:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.general;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSetting;
import java.util.Vector;

/**
 *
 * @author Javier A. Ortiz Bultrón
 */
public abstract class SettingServer extends XincoSetting {
    Vector settings=new Vector();
    /** Creates a new instance of SettingServer */
    public SettingServer() {
    }

    
    public SettingServer getSetting(String s){
        for (int i = 0; i<getSettings().size(); i++){
            if (((SettingServer)getSettings().get(i)).getDescription().equals(s))
                return ((SettingServer)getSettings().get(i));
        }
        return null;
    }
    
    public Vector getSettings(DBManager DBM) {
        if (settings == null)
            try {
                setSettings(DBM.getSettingServer().getSettings());
            }   catch (Exception ex) {
                ex.printStackTrace();
            }
        return settings;
    }


    
    public void setSettings(Vector settings) {
        this.settings = settings;
    }

    public abstract int write2DB(DBManager DBM) throws XincoException;
    
}
