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

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSetting;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoSettingServer extends XincoSetting{
    private Vector xinco_settings=null;
    private XincoCoreAuditServer audit= new XincoCoreAuditServer();
    
    /** Creates a new instance of XincoSettingServer */
    public XincoSettingServer(int id,java.lang.String description,int int_value,
            java.lang.String string_value,boolean bool_value,long long_value, int changerID) throws XincoException{
        this.setId(id);
        this.setDescription(description);
        this.setInt_value(int_value);
        this.setString_value(string_value);
        this.setBool_value(bool_value);
        this.setChangerID(changerID);
        this.setLong_value(long_value);
        XincoDBManager DBM =null;
        try {
            DBM =new XincoDBManager();
            write2DB(DBM);
            setXinco_settings(DBM.getXincoSettingServer().getXinco_settings());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public XincoSettingServer(int id,XincoDBManager DBM)throws XincoException{
        try {
            String sql="select * from xinco_setting where id="+id;
            if(DBM.getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            ResultSet rs= DBM.getConnection().createStatement().executeQuery(sql);
            rs.next();
            this.setId(id);
            this.setDescription(rs.getString("description"));
            this.setInt_value(rs.getInt("int_value"));
            this.setString_value(rs.getString("string_value"));
            this.setBool_value(rs.getBoolean("bool_value"));
            this.setLong_value(rs.getInt("long_value"));
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
                        ", description='"+getDescription()+"', int_value="+getInt_value()+
                        ", string_value='"+getString_value()+"', bool_value="+isBool_value()+
                        " where id="+getId();
                if(DBM.getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                audit.updateAuditTrail("xinco_setting",new String [] {"id ="+getId()},
                        DBM,"audit.general.modified",getChangerID());
            } else{
                sql="insert into xinco_setting values("+getId()+
                        ", '"+getDescription()+"',"+getInt_value()+
                        ", '"+getString_value()+"',"+isBool_value()+","+getLong_value()+")";
                if(DBM.getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
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
        return (XincoSetting)getXinco_settings().get(i);
    }
    
    public XincoSetting getSetting(String s){
        for(int i=0;i<getXinco_settings().size();i++){
            if(((XincoSetting)getXinco_settings().get(i)).getDescription().equals(s))
                return (XincoSetting)getXinco_settings().get(i);
        }
        return null;
    }
    
    public Vector getXinco_settings() {
        if (xinco_settings == null)
            try {
                setXinco_settings(new XincoDBManager().getXincoSettingServer().getXinco_settings());
            }  catch (Exception ex) {
                ex.printStackTrace();
            }
        return xinco_settings;
    }

    
    public void setXinco_settings(Vector xinco_settings) {
        this.xinco_settings = xinco_settings;
    }

}
