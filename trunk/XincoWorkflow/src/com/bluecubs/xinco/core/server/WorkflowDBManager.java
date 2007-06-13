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

import com.bluecubs.xinco.conf.XincoWorkflowConfigSingletonServer;
import com.bluecubs.xinco.core.XincoSetting;
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

public class WorkflowDBManager {
    public XincoWorkflowConfigSingletonServer config;
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
        config = XincoWorkflowConfigSingletonServer.getInstance();
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
    
    public int getNewID(String attrTN) throws Exception {
        int newID = 0;
        Statement stmt;
        stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_id WHERE tablename='" + attrTN + "'");
        while (rs.next()) {
            newID = rs.getInt("last_id") + 1;
        }
        stmt.close();
        stmt = getConnection().createStatement();
        stmt.executeUpdate("UPDATE xinco_id SET last_id=last_id+1 WHERE tablename='" + attrTN + "'");
        stmt.close();
        return newID;
    }
    
    /*Replace a string with contents of resource bundle is applicable
     *Used to transform db contents to human readable form.
     */
    private String canReplace(String s){
        if(s==null)
            return null;
        try{
            getResourceBundle().getString(s);
        }catch (MissingResourceException e){
            return s;
        }
        return getResourceBundle().getString(s);
    }
    
    public void setLocale(Locale loc) {
        this.loc = loc;
        if (loc==null)
            loc = Locale.getDefault();
        else
            try {
                setResourceBundle(ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",loc));
            } catch (Exception e) {
                e.printStackTrace();
                printStats();
            }
    }
    
    public WorkflowSettingServer getWorkflowSettingServer() {
        if(wss==null)
            wss=new WorkflowSettingServer();
        return wss;
    }
    
    public Connection getConnection() {
        try {
            if(con==null || con.isClosed())
                con = getDatasource().getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            printStats();
        }
        return con;
    }
    
    public DataSource getDatasource() {
        return datasource;
    }
    
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }
    
    public void setConnection(Connection con) {
        if(con==null)
            getConnection();
        this.con = con;
    }
    
    public void printStats(){
        System.out.println("Number Active: "+((BasicDataSource)getDatasource()).getNumActive());
        System.out.println("Number Idle: "+((BasicDataSource)getDatasource()).getNumIdle());
    }
    
    public WorkflowSetting getSetting(String name){
        return getWorkflowSettingServer().getSetting(name);
    }
    
    public ResourceBundle getResourceBundle() {
        return lrb;
    }
    
    public void setResourceBundle(ResourceBundle lrb) {
        this.lrb = lrb;
    }
    
    public Statement getStatement() {
        try {
            if(st==null)
                st=getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            else {
                st=getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return st;
    }
    
    //----------------------------------------------------------------------------------------------
    /**
     * Determines the number of rows in a <code>ResultSet</code>. Upon exit, if the cursor was not
     * currently on a row, it is just before the first row in the result set (a call to
     * {@link ResultSet#next()} will go to the first row).
     * @param set The <code>ResultSet</code> to check (must be scrollable).
     * @return The number of rows.
     * @throws SQLException If the <code>ResultSet</code> is not scrollable.
     * @see #hasSingleRow(ResultSet)
     */
    public static int getRowCount(ResultSet set) throws SQLException {
        int rowCount;
        int currentRow = set.getRow();              // Get current row
        rowCount = set.last() ? set.getRow() : 0;   // Determine number of rows
        if (currentRow == 0)                        // If there was no current row
            set.beforeFirst();                      // We want next() to go to first row
        else                                        // If there WAS a current row
            set.absolute(currentRow);               // Restore it
        return rowCount;
    }
}
