/**
 *Copyright 2004 blueCubs.com
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
 * Name:            XincoDBManager
 *
 * Description:     server-side database manager
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * Javier A. Ortiz  01/04/2007        Added methods for result set manipulation and formating
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.general.DBManager;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSetting;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public class XincoDBManager extends DBManager {
    
    private Connection con=null;
    public XincoConfigSingletonServer config;
    private int EmailLink=1,DataLink=2;
    private ResourceBundle lrb = null;
    private Locale loc=null;
    private XincoSettingServer xss=null;
    private DataSource datasource=null;
    
    public XincoDBManager() throws Exception {
        setResourceBundle(ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages"));
        //load connection configuartion
        config = XincoConfigSingletonServer.getInstance();
        while((DataSource)(new InitialContext()).lookup(config.getJNDIDB())==null);
        setDatasource((DataSource)(new InitialContext()).lookup(config.getJNDIDB()));
        getConnection().setAutoCommit(false);
        //load configuration from database
        fillSettings();
        config.init(getXincoSettingServer());
        count++;
    }
    
    protected void fillSettings(){
        ResultSet rs=null;
        getXincoSettingServer().setXinco_settings(new Vector());
        String string_value="";
        try {
            Statement stm=getConnection().createStatement();
            rs=stm.executeQuery("select * from xinco_setting order by id");
            while(rs.next()){
                if(rs.getString("string_value")==null)
                    string_value="";
                else
                    string_value=rs.getString("string_value");
                getXincoSettingServer().getXinco_settings().addElement(new XincoSetting(rs.getInt("id"),
                        rs.getString("description"),rs.getInt("int_value"),string_value,
                        rs.getBoolean("bool_value"),0,rs.getLong("long_value"),null));
            }
            stm.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            printStats();
        }
    }
    
    /*
     *Reset database tables keeping the standard inserts (assuming those inserts have id's greater than the ones specified in the xinco_id table
     */
    public void resetDB(XincoCoreUserServer user) throws XincoException{
        Vector groups=user.getXinco_core_groups();
        boolean isAdmin=false;
        for(int i=0;i<groups.size();i++){
            if(((XincoCoreGroup)groups.elementAt(i)).getId()==1)
                isAdmin=true;
        }
        if(isAdmin){
            ResultSet rs;
            String[] types =  {"TABLE"};
            Statement s=null;
            String column="id",sql=null,condition=null;
            int number;
            DatabaseMetaData meta;
            try {
                meta = getConnection().getMetaData();
                //Get table names
                rs = meta.getTables(null, null, null, types);
                //Delete content of tables
                s=getConnection().createStatement();
                while(rs.next()){
                    if(!rs.getString("TABLE_NAME").equals("xinco_id") &&
                            rs.getString("TABLE_NAME").contains("xinco")){
                        number=1000;
                        column="id";
                        //Modify primary key name if needed
                        if(rs.getString("TABLE_NAME").endsWith("_t")){
                            column="record_id";
                            number=0;
                        }
                        else
                            number=1000;
                        if(rs.getString("TABLE_NAME").equals("xinco_add_attribute"))
                            column="xinco_core_data_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_core_data_type_attribute"))
                            column="xinco_core_data_type_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_scheduled_audit_has_xinco_core_group"))
                            column="xinco_scheduled_audit_schedule_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_core_user_has_xinco_core_group"))
                            column="xinco_core_user_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_scheduled_audit_type"))
                            column="scheduled_type_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_scheduled_audit"))
                            column="schedule_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_core_user_modified_record")||
                                rs.getString("TABLE_NAME").equals("xinco_scheduled_audit"))
                            number = -1;
                        condition = column +" > "+number;
                        sql="delete from "+rs.getString("TABLE_NAME")+" where "+condition;
                        if(getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                            System.out.println(sql);
                        s.executeUpdate(sql);
                    }
                    if(rs.getString("TABLE_NAME").equals("xinco_id")){
                        condition=" where last_id > 1000";
                        sql="update "+rs.getString("TABLE_NAME")+" set last_id=1000"+condition;
                        if(getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                            System.out.println(sql);
                        s.executeUpdate(sql);
                        condition=" where last_id < 1000";
                        sql="update "+rs.getString("TABLE_NAME")+" set last_id=0"+condition;
                        if(getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                            System.out.println(sql);
                        s.executeUpdate(sql);
                    }
                }
                s.close();
                s=getConnection().createStatement();
                s.executeUpdate("update xinco_id set last_id = 1000 where last_id >1000");
                s.executeUpdate("update xinco_id set last_id = 0 where last_id < 1000");
                s.executeUpdate("delete from xinco_core_user_modified_record");
                getConnection().commit();
                s.close();
                rs=null;
            } catch (SQLException ex) {
                try {
                    getConnection().rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                ex.printStackTrace();
                printStats();
            }
        } else
            throw new XincoException(getResourceBundle().getString("error.noadminpermission"));
    }
    
    public XincoSettingServer getXincoSettingServer() {
        if(xss==null)
            xss=new XincoSettingServer();
        return xss;
    }
    
    public XincoSetting getSetting(String name){
        return getXincoSettingServer().getSetting(name);
    }

}
