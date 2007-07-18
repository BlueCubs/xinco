/**
 *Copyright June 12, 2007 blueCubs.com
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
 * Name:            XincoWorkflowDBManager.java
 *
 * Description:     XincoWorkflowDBManager
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 12, 2007, 8:42 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.conf.WorkflowConfigSingletonServer;
import com.bluecubs.xinco.general.DBManager;
import com.bluecubs.xinco.workflow.WorkflowSetting;
import com.bluecubs.xinco.workflow.server.WorkflowSettingServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public class WorkflowDBManager extends DBManager{
    public WorkflowConfigSingletonServer config;
    private Connection con=null;
    public static int count = 0;
    private ResourceBundle lrb = null;
    private Locale loc=null;
    private WorkflowSettingServer wss=new WorkflowSettingServer();
    private DataSource datasource=null;
    private String JNDIDB=null;
    private Statement st =null;
    
    /**
     * Creates a new instance of WorkflowDBManager
     */
    public WorkflowDBManager() throws Exception {
        setResourceBundle(ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoWorkflowMessages"));
        //load connection configuartion
        config = WorkflowConfigSingletonServer.getInstance();
        while((DataSource)(new InitialContext()).lookup(config.getJNDIDB())==null);
        setDatasource((DataSource)(new InitialContext()).lookup(config.getJNDIDB()));
        getConnection().setAutoCommit(false);
        //load configuration from database
        fillSettings();
        config.init(getWorkflowSettingServer());
        count++;
    }
    
    protected void fillSettings(){
        ResultSet rs=null;
        getWorkflowSettingServer().setWorkflow_settings(new Vector());
        String string_value="";
        try {
            Statement stm=getConnection().createStatement();
            rs=stm.executeQuery("select * from xinco_setting order by id");
            while(rs.next()){
                if(rs.getString("string_value")==null)
                    string_value="";
                else
                    string_value=rs.getString("string_value");
                getWorkflowSettingServer().getWorkflow_settings().addElement(new WorkflowSetting(rs.getInt("id"),
                        rs.getString("description"),rs.getInt("int_value"),string_value,
                        rs.getBoolean("bool_value"),0,rs.getLong("long_value"),null,null));
            }
            stm.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            printStats();
        }
    }
    
    public WorkflowSettingServer getWorkflowSettingServer() {
        if(wss==null)
            wss=new WorkflowSettingServer();
        return wss;
    }
    
    public WorkflowSetting getSetting(String name){
        return getWorkflowSettingServer().getSetting(name);
    }
}
