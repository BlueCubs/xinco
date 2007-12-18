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

import com.bluecubs.xinco.core.XincoSettingException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSetting;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Server-side database manager
 * @author XincoDBManager
 */
public class XincoDBManager {

    private Connection con = null;
    public XincoConfigSingletonServer config;
    private int EmailLink = 1,  DataLink = 2;
    private ResourceBundle lrb = null;
    private Locale loc = null;
    private XincoSettingServer xss = null;
    private DataSource datasource = null;
    public static int count = 0;
    private ResultSet rs = null;
    private boolean settingsFilled = false;
    private Statement stmt = null;

    /**
     * Server-side database manager
     * @throws java.lang.Exception
     */
    public XincoDBManager() throws Exception {
        try {
            setResourceBundle(ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages"));
            //load connection configuartion
            config = XincoConfigSingletonServer.getInstance();
            setDatasource((DataSource) (new InitialContext()).lookup(config.getJNDIDB()));
            //load configuration from database
            fillSettings();
            config.init(getXincoSettingServer());
            count++;
            printStats();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills setting vector from DB.
     */
    @SuppressWarnings("unchecked")
    protected void fillSettings() {
        getXincoSettingServer().setXinco_settings(new Vector());
        String string_value = "";
        try {
            rs = executeQuery("select * from xinco_setting order by id");
            while (rs.next()) {
                if (rs.getString("string_value") == null) {
                    string_value = "";
                } else {
                    string_value = rs.getString("string_value");
                }
                getXincoSettingServer().getXinco_settings().addElement(new XincoSetting(rs.getInt("id"),
                        rs.getString("description"), rs.getInt("int_value"), string_value,
                        rs.getBoolean("bool_value"), 0, rs.getLong("long_value"), null));
            }
            setSettingsFilled(true);
        } catch (Throwable ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
            printStats();
            setSettingsFilled(false);
        }
    }

    /**
     * Reset database tables keeping the standard inserts (assuming those inserts have id's greater than the ones specified in the xinco_id table
     * @param user
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public void resetDB(XincoCoreUserServer user) throws XincoException {
        Vector groups = user.getXinco_core_groups();
        boolean isAdmin = false;
        for (int i = 0; i < groups.size(); i++) {
            if (((XincoCoreGroup) groups.elementAt(i)).getId() == 1) {
                isAdmin = true;
            }
        }
        if (isAdmin) {
            String[] types = {"TABLE"};
            String column = "id", sql = null, condition = null;
            int number;
            DatabaseMetaData meta;
            try {
                meta = getConnection().getMetaData();
                //Get table names
                rs = meta.getTables(null, null, null, types);
                //Delete content of tables
                while (rs.next()) {
                    if (!rs.getString("TABLE_NAME").equals("xinco_id") &&
                            rs.getString("TABLE_NAME").contains("xinco")) {
                        number = 1000;
                        column = "id";
                        //Modify primary key name if needed
                        if (rs.getString("TABLE_NAME").endsWith("_t")) {
                            column = "record_id";
                            number = 0;
                        } else {
                            number = 1000;
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_add_attribute")) {
                            column = "xinco_core_data_id";
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_core_data_type_attribute")) {
                            column = "xinco_core_data_type_id";
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_scheduled_audit_has_xinco_core_group")) {
                            column = "xinco_scheduled_audit_schedule_id";
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_core_user_has_xinco_core_group")) {
                            column = "xinco_core_user_id";
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_scheduled_audit_type")) {
                            column = "scheduled_type_id";
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_scheduled_audit")) {
                            column = "schedule_id";
                        }
                        if (rs.getString("TABLE_NAME").equals("xinco_core_user_modified_record") ||
                                rs.getString("TABLE_NAME").equals("xinco_scheduled_audit")) {
                            number = -1;
                        }
                        condition = column + " > " + number;
                        sql = "delete from " + rs.getString("TABLE_NAME") + " where " + condition;
                        executeUpdate(sql);
                    }
                    if (rs.getString("TABLE_NAME").equals("xinco_id")) {
                        condition = " where last_id > 1000";
                        sql = "update " + rs.getString("TABLE_NAME") + " set last_id=1000" + condition;
                        executeUpdate(sql);
                        condition = " where last_id < 1000";
                        sql = "update " + rs.getString("TABLE_NAME") + " set last_id=0" + condition;
                        executeUpdate(sql);
                    }
                }
                executeUpdate("update xinco_id set last_id = 1000 where last_id >1000");
                executeUpdate("update xinco_id set last_id = 0 where last_id < 1000");
                executeUpdate("delete from xinco_core_user_modified_record");
                getConnection().commit();
                rs = null;
            } catch (SQLException ex) {
                try {
                    getConnection().rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                ex.printStackTrace();
                printStats();
            }
        } else {
            throw new XincoException(getResourceBundle().getString("error.noadminpermission"));
        }
    }

    /**
     * Get XincoSettingServer
     * @return XincoSettingServer
     */
    public XincoSettingServer getXincoSettingServer() {
        if (xss == null) {
            xss = new XincoSettingServer();
        }
        return xss;
    }

    /**
     * Get setting value
     * @param name
     * @return XincoSetting
     */
    public XincoSetting getSetting(String name) {
        if (!isSettingsFilled()) {
            fillSettings();
        }
        try {
            return getXincoSettingServer().getSetting(name);
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE,
                    lrb.getString("menu.connection.error.settings").replace("%s", name), ex);
        }
        return null;
    }

    /**
     * Draws a table with results of the query stored in the ResultSet rs in the PrintWriter out
     * @param rs
     * @param out
     * @param header
     * @param title
     * @param columnAsLink
     * @param details
     * @param linkType
     */
    public void drawTable(ResultSet rs, PrintWriter out, String header, String title, int columnAsLink, boolean details, int linkType) {
        try {
            int size = rs.getMetaData().getColumnCount();
            out.println(title);
            out.println("<center><table border =1 ><tr>");
            out.println(header + "</td></tr><tr>");
            while (rs.next()) {
                for (int i = 1; i <= size; i++) {
                    String value = localizeString(rs.getString(i));
                    if (rs.getMetaData().getColumnName(i).contains("password")) {
                        value = "******************************";
                    }
                    if (i == size && details) {
                        out.println("<td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='" +
                                value + "'><input type='hidden' name='Page' value='ProcessData.jsp'></form></td>");
                    } else {
                        if (i == columnAsLink && linkType == this.EmailLink) {
                            if (value == null) {
                                out.println("<td>No email address available</td>");
                            } else {
                                out.println("<td><a href= mailto:" + value + ">Email this person</a></td>");
                            }
                        }
                        if (i == columnAsLink && linkType == this.DataLink) {
                            if (value == null) {
                                out.println("<td>No code available</td>");
                            } else {
                                out.println("<td>" + value + "</td><td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='" +
                                        value + "'><input type='hidden' name='Page' value='Codes.jsp'></form></td>");
                            }
                        } else {
                            if (value == null) {
                                out.println("<td>" + getResourceBundle().getString("general.nodata") + "</td>");
                            } else {
                                out.println("<td>" + value + "</td>");
                            }
                        }
                    }
                }
                out.println("</tr><tr>");
            }
            out.println("</tr></table></center>");
        } catch (Throwable e) {
            out.println(getResourceBundle().getString("general.nodata"));
            System.out.println("Exception drawing table: " + e.getMessage());
        }
    }

    /**
     * Replace a string with contents of resource bundle is applicable
     * Used to transform db contents to human readable form.
     * @param s
     * @return String
     */
    public String localizeString(String s) {
        if (s == null) {
            return null;
        }
        try {
            getResourceBundle().getString(s);
        } catch (MissingResourceException e) {
            return s;
        }
        return getResourceBundle().getString(s);
    }

    @Override
    public void finalize() throws Throwable {
        try {
            count--;
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            con.close();
            ((BasicDataSource) getDatasource()).close();
        } finally {
            if (!con.isClosed()) {
                count++;
            }
            printStats();
            super.finalize();
        }
    }

    /**
     * Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method
     * @param rs
     * @return String
     */
    public String getColumnNames(ResultSet rs) {
        String header = "";
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // Get the column names; column indices start from 1
            for (int i = 1; i < numColumns + 1; i++) {
                header += "<td><b>" + rsmd.getColumnName(i) + "</b>";
            }
            header += "</td>";
        } catch (SQLException e) {
            e.printStackTrace();
            printStats();
        }
        return header;
    }

    /**
     * Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.
     * @param rs
     * @return StringTokenizer
     */
    public StringTokenizer getColumnNamesList(ResultSet rs) {
        String list = "";
        StringTokenizer t;
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // Get the column names; column indices start from 1
            for (int i = 1; i < numColumns + 1; i++) {
                list += rsmd.getColumnName(i) + ",";
            }
        } catch (SQLException e) {
            System.err.println("Error getting names from result set. " + e);
        }
        t = new StringTokenizer(list, ",");
        return t;
    }

    /**
     * Get DB connection
     * @return Connection
     */
    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = getDatasource().getConnection();
            }
            con.setAutoCommit(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return con;
    }

    /**
     * Get Data source
     * @return DataSource
     */
    public DataSource getDatasource() {
        return datasource;
    }

    /**
     * Get a new id
     * @param table_name
     * @return int
     * @throws java.lang.Exception
     */
    public int getNewID(String table_name) throws Exception {
        int newID = 0;
        rs = executeQuery("SELECT * FROM xinco_id WHERE tablename='" + table_name + "'");
        while (rs.next()) {
            newID = rs.getInt("last_id") + 1;
        }
        rs.close();
        executeUpdate("UPDATE xinco_id SET last_id=last_id+1 WHERE tablename='" + table_name + "'");
        return newID;
    }

    /**
     * Get Resource bundle
     * @return ResourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return lrb;
    }

    /**
     * Print connection stats
     */
    public void printStats() {
        if (getDatasource() != null) {
            try {
                System.out.println("Number Active: " + ((BasicDataSource) getDatasource()).getNumActive());
                System.out.println("Number Idle: " + ((BasicDataSource) getDatasource()).getNumIdle());
                if (!getDatasource().getConnection().isClosed()) {
                    getDatasource().getConnection().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Set Connection
     * @param con
     */
    public void setConnection(Connection con) {
        this.con = con;
        try {
            this.con.setAutoCommit(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set data source
     * @param datasource
     */
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    /**
     * Set locale
     * @param loc
     */
    public void setLocale(Locale loc) {
        this.loc = loc;
        if (loc == null) {
            loc = Locale.getDefault();
        } else {
            try {
                setResourceBundle(ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc));
            } catch (Throwable e) {
                e.printStackTrace();
                printStats();
            }
        }
    }

    /**
     * Create locale from string
     * @param locale
     */
    public void createAndLoadLocale(String locale) {
        Locale temp = null;
        try {
            String[] locales;
            locales = locale.split("_");
            switch (locales.length) {
                case 1:
                    temp = new Locale(locales[0]);
                    break;
                case 2:
                    temp = new Locale(locales[0], locales[1]);
                    break;
                case 3:
                    temp = new Locale(locales[0], locales[1], locales[2]);
                    break;
                default:
                    temp = Locale.getDefault();
            }
        } catch (Throwable e) {
            temp = Locale.getDefault();
        }
        setLocale(temp);
    }

    /**
     * Set Resource bundle
     * @param lrb
     */
    public void setResourceBundle(ResourceBundle lrb) {
        this.lrb = lrb;
    }

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
        if (currentRow == 0) // If there was no current row
        {
            set.beforeFirst();
        } // We want next() to go to first row
        else // If there WAS a current row
        {
            set.absolute(currentRow);
        }               // Restore it
        return rowCount;
    }

    /**
     * Get locale
     * @return
     */
    public Locale getLocale() {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        return loc;
    }

    /**
     * Script for removing right click functionality in web pages.
     * @return String
     */
    public String getWebBlockRightClickScript() {
        return "<script language=JavaScript> /n" +
                "<!--/n" +
                "var message='';/n" +
                "function clickIE() {if (document.all) {(message);return false;}}/n" +
                "function clickNS(e) {if /n" +
                "(document.layers||(document.getElementById&&!document.all)) {/n" +
                "if (e.which==2||e.which==3) {(message);return false;}}}/n" +
                "if (document.layers) /n" +
                "{document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;}/n" +
                "else{document.onmouseup=clickNS;document.oncontextmenu=clickIE;}/n" +
                "document.oncontextmenu=new Function('return false')/n" +
                "// --> /n" +
                "</script>";
    }

    /**
     * Execute query
     * @param sql
     * @return ResultSet
     */
    public ResultSet executeQuery(String sql) {
        try {
            if (isSettingsFilled()) {
                if (getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println(sql);
                }
            }
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            if (isSettingsFilled()) {
                if (getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println("Query excuted successfully!");
                }
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    /**
     * Execute update
     * @param sql
     * @return boolean
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public boolean executeUpdate(String sql) throws XincoException {
        try {
            if (isSettingsFilled()) {
                if (getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println(sql);
                }
            }
            stmt = getConnection().createStatement();
            stmt.executeUpdate(sql);
            getConnection().commit();
            if (isSettingsFilled()) {
                if (getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println("Update excuted successfully!");
                }
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException();
        }
        return true;
    }

    private boolean isSettingsFilled() {
        return settingsFilled;
    }

    private void setSettingsFilled(boolean settingsFilled) {
        this.settingsFilled = settingsFilled;
    }
}
