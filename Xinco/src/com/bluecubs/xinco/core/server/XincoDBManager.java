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

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSetting;
import com.bluecubs.xinco.core.server.email.XincoMailer;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.MessagingException;
import org.apache.commons.dbcp.BasicDataSource;

public class XincoDBManager {
    
    private Connection con=null;
    public XincoConfigSingletonServer config;
    public static int count = 0;
    private int EmailLink=1,DataLink=2;
    private ResourceBundle lrb = null;
    private Locale loc=null;
    private XincoSettingServer xss=null;
    private DataSource datasource=null;
    
    public XincoDBManager() throws Exception {
        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        //load connection configuartion
        config = XincoConfigSingletonServer.getInstance();
        while((DataSource)(new InitialContext()).lookup(config.JNDIDB)==null);
        setDatasource((DataSource)(new InitialContext()).lookup(config.JNDIDB));
        getConnection().setAutoCommit(false);
        //load configuration from database
        fillSettings();
        config.init(getXincoSettingServer());
        count++;
    }
    
    private void fillSettings(){
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
    
    public void finalize() throws Throwable {
        try {
            count--;
            getConnection().close();
        } finally {
            if (!getConnection().isClosed()) {
                count++;
            }
            super.finalize();
        }
    }
    
    /**Draws a table with results of the query stored in the ResultSet rs in the PrintWriter out*/
    public void drawTable(ResultSet rs, PrintWriter out , String header, String title , int columnAsLink, boolean details, int linkType) {
        try{
            int size = rs.getMetaData().getColumnCount();
            out.println(title);
            out.println("<center><table border =1 ><tr>");
            out.println(header+"</td></tr><tr>");
            while(rs.next()){
                for(int i=1;i<=size;i++) {
                    String value=canReplace(rs.getString(i));
                    if(rs.getMetaData().getColumnName(i).contains("password"))
                        value = "******************************";
                    if(i==size && details) {
                        out.println("<td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='"+
                                value+"'><input type='hidden' name='Page' value='ProcessData.jsp'></form></td>");
                    } else {
                        if(i==columnAsLink && linkType==this.EmailLink){
                            if(value==null)
                                out.println("<td>No email address available</td>");
                            else
                                out.println("<td><a href= mailto:"+value+">Email this person</a></td>");
                        }
                        if(i==columnAsLink && linkType==this.DataLink){
                            if(value==null)
                                out.println("<td>No code available</td>");
                            else
                                out.println("<td>"+value+"</td><td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='"+
                                        value+"'><input type='hidden' name='Page' value='Codes.jsp'></form></td>");
                        } else {
                            if(value==null)
                                out.println("<td>"+lrb.getString("general.nodata")+"</td>");
                            else
                                out.println("<td>"+value+"</td>");
                        }
                    }
                }
                out.println("</tr><tr>");
            }
            out.println("</tr></table></center>");
        } catch(Exception e) {
            out.println(lrb.getString("general.nodata"));
            System.out.println("Exception drawing table: " + e.getMessage());
        }
    }
    
    /** Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.*/
    public StringTokenizer getColumnNamesList(ResultSet rs) {
        String list="";
        StringTokenizer t;
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            
            // Get the column names; column indices start from 1
            for (int i=1; i<numColumns+1; i++) {
                list+=rsmd.getColumnName(i)+",";
            }
        } catch(SQLException e) {
            System.err.println("Error getting names from result set. "+e);
        }
        t=new StringTokenizer(list,",");
        return t;
    }
    
    /** Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.*/
    public String getColumnNames(ResultSet rs) {
        String header="";
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // Get the column names; column indices start from 1
            for (int i=1; i<numColumns+1; i++) {
                header += "<td><b>"+rsmd.getColumnName(i)+"</b>";
            }
            header +="</td>";
        } catch(SQLException e) {
            e.printStackTrace();
            printStats();
        }
        return header;
    }
    
    /*Replace a string with contents of resource bundle is applicable
     *Used to transform db contents to human readable form.
     */
    private String canReplace(String s){
        if(s==null)
            return null;
        try{
            lrb.getString(s);
        }catch (MissingResourceException e){
            return s;
        }
        return lrb.getString(s);
    }
    
    public void setLocale(Locale loc) {
        this.loc = loc;
        if (loc==null)
            loc = Locale.getDefault();
        else
            try {
                lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",loc);
            } catch (Exception e) {
                e.printStackTrace();
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
                    if(!rs.getString("TABLE_NAME").equals("xinco_id")){
                        number=1000;
                        column="id";
                        //Modify primary key name if needed
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
                        if(rs.getString("TABLE_NAME").endsWith("_t"))
                            column="record_id";
                        if(rs.getString("TABLE_NAME").equals("xinco_core_user_modified_record")||
                                rs.getString("TABLE_NAME").equals("xinco_scheduled_audit"))
                            number = -1;
                        condition = column +" > "+number;
                        sql="delete from "+rs.getString("TABLE_NAME")+" where "+condition;
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
            throw new XincoException(lrb.getString("error.noadminpermission"));
    }
    
    public XincoSettingServer getXincoSettingServer() {
        if(xss==null)
            xss=new XincoSettingServer();
        return xss;
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
    
    public XincoSetting getSetting(String name){
        return getXincoSettingServer().getSetting(name);
    }
}
