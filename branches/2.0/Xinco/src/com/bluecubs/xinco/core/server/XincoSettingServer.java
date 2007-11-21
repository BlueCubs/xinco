/**
 *Copyright 2007 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoSettingServer
 *
 * Description:     XincoSettingServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            February 19, 2007, 4:40 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.Vector;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSetting;
import com.bluecubs.xinco.core.XincoSettingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoSettingServer extends XincoSetting{
    private Vector xinco_settings=null;
    private XincoCoreAuditTrail audit= new XincoCoreAuditTrail();
    private ResourceBundle rb;
    
    /** Creates a new instance of XincoSettingServer */
    public XincoSettingServer(int id,String description,int int_value,
            String string_value,boolean bool_value, int changerID,long long_value, Vector v) throws XincoException{
        this.setId(id);
        this.setDescription(description);
        this.setIntValue(int_value);
        this.setStringValue(string_value);
        this.setBoolValue(bool_value);
        this.setChangerID(changerID);
        this.setLongValue(long_value);
        XincoDBManager DBM =null;
        try {
            DBM =new XincoDBManager();
            write2DB(DBM);
            setXincoSettings(DBM.getXincoSettingServer().getXincoSettings());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public XincoSettingServer(int id,XincoDBManager DBM)throws XincoException{
        try {
            String sql="select * from xinco_setting where id="+id;
            if(DBM.getSetting("setting.enable.developermode").isBoolValue())
                System.out.println(sql);
            ResultSet rs= DBM.getConnection().createStatement().executeQuery(sql);
            rs.next();
            this.setId(id);
            this.setDescription(rs.getString("description"));
            this.setIntValue(rs.getInt("int_value"));
            this.setStringValue(rs.getString("string_value"));
            this.setBoolValue(rs.getBoolean("bool_value"));
            this.setLongValue(rs.getInt("long_value"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public XincoSettingServer(){
    }
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            String sql="";
            if(getId()>0){
                sql="update xinco_setting set id="+getId()+
                        ", description='"+getDescription()+"', int_value="+getIntValue()+
                        ", string_value='"+getStringValue()+"', bool_value="+isBoolValue()+
                        " where id="+getId();
                if(DBM.getSetting("setting.enable.developermode").isBoolValue())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                audit.updateAuditTrail("xinco_setting",new String [] {"id ="+getId()},
                        DBM,"audit.general.modified",getChangerID());
            } else{
                sql="insert into xinco_setting values("+getId()+
                        ", '"+getDescription()+"',"+getIntValue()+
                        ", '"+getStringValue()+"',"+isBoolValue()+","+getLongValue()+")";
                if(DBM.getSetting("setting.enable.developermode").isBoolValue())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                audit.updateAuditTrail("xinco_setting",new String [] {"id ="+getId()},
                        DBM,"audit.general.created",getChangerID());
            }
            DBM.getConnection().commit();
            DBM.getConnection().close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return this.getId();
    }
    
    public XincoSetting getSetting(int i){
        return (XincoSetting)getXincoSettings().elementAt(i);
    }
    
    public XincoSetting getSetting(String s) throws XincoSettingException{
        for(int i=0;i<getXincoSettings().size();i++){
            if(((XincoSetting)getXincoSettings().elementAt(i)).getDescription().equals(s))
                return (XincoSetting)getXincoSettings().elementAt(i);
        }
        throw new XincoSettingException();
    }
    
    @Override
    public Vector getXincoSettings() {
        if (xinco_settings == null)
            try {
                setXincoSettings(new XincoDBManager().getXincoSettingServer().getXincoSettings());
            }  catch (Exception ex) {
                ex.printStackTrace();
            }
        return xinco_settings;
    }

    
    @Override
    public void setXincoSettings(Vector xinco_settings) {
        this.xinco_settings = xinco_settings;
    }

}
