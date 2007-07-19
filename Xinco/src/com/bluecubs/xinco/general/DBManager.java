/*
 * DBManager.java
 *
 * Created on July 18, 2007, 3:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.general;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.general.SettingServer;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author Javier A. Ortiz Bultrón
 */
public abstract class DBManager {
    private Connection con=null;
    public XincoConfigSingletonServer config;
    private int EmailLink=1,DataLink=2;
    private ResourceBundle lrb = null;
    private Locale loc=null;
    protected SettingServer ss=null;
    private DataSource datasource=null;
    /**
     * Creates a new instance of DBManager
     */
    public DBManager() {
    }
    
    public static int count = 0;
    
    
    /**Draws a table with results of the query stored in the ResultSet rs in the PrintWriter out*/
    public void drawTable(ResultSet rs, PrintWriter out, String header, String title, int columnAsLink, boolean details, int linkType) {
        try{
            int size = rs.getMetaData().getColumnCount();
            out.println(title);
            out.println("<center><table border =1 ><tr>");
            out.println(header+"</td></tr><tr>");
            while (rs.next()){
                for (int i = 1; i<=size; i++) {
                    String value = localizeString(rs.getString(i));
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
                                out.println("<td>"+getResourceBundle().getString("general.nodata")+"</td>");
                            else
                                out.println("<td>"+value+"</td>");
                        }
                    }
                }
                out.println("</tr><tr>");
            }
            out.println("</tr></table></center>");
        }  catch (Exception e) {
            out.println(getResourceBundle().getString("general.nodata"));
            System.out.println("Exception drawing table: " + e.getMessage());
        }
    }
    
    protected abstract void fillSettings();
    
    /*
     *Replace a string with contents of resource bundle is applicable
     *Used to transform db contents to human readable form.
     */
    public String localizeString(String s){
        if(s==null)
            return null;
        try{
            getResourceBundle().getString(s);
        }catch (MissingResourceException e){
            return s;
        }
        return getResourceBundle().getString(s);
    }
    
    
    protected void finalize() throws Throwable {
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
    
    
    /** Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.*/
    public String getColumnNames(ResultSet rs) {
        String header = "";
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // Get the column names; column indices start from 1
            for (int i = 1; i<numColumns+1; i++) {
                header += "<td><b>"+rsmd.getColumnName(i)+"</b>";
            }
            header +="</td>";
        }  catch (SQLException e) {
            e.printStackTrace();
            printStats();
        }
        return header;
    }
    
    
    /** Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.*/
    public StringTokenizer getColumnNamesList(ResultSet rs) {
        String list = "";
        StringTokenizer t;
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            
            // Get the column names; column indices start from 1
            for (int i = 1; i<numColumns+1; i++) {
                list+=rsmd.getColumnName(i)+",";
            }
        }  catch (SQLException e) {
            System.err.println("Error getting names from result set. "+e);
        }
        t = new StringTokenizer(list, ",");
        return t;
    }
    
    
    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()){
                con = getDatasource().getConnection();
            }
            con.setAutoCommit(false);
        }  catch (SQLException ex) {
            ex.printStackTrace();
            printStats();
        }
        return con;
    }
    
    
    public DataSource getDatasource() {
        try {
            datasource.getConnection().setAutoCommit(false);
        }  catch (SQLException ex) {
            ex.printStackTrace();
        }
        return datasource;
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
    
    
    public ResourceBundle getResourceBundle() {
        return lrb;
    }
    
    public Statement getStatement(){
        try {
            return getDatasource().getConnection().createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public void printStats(){
        System.out.println("Number Active: " + ((BasicDataSource) getDatasource()).getNumActive());
        System.out.println("Number Idle: " + ((BasicDataSource) getDatasource()).getNumIdle());
    }
    
    
    public void setConnection(Connection con) {
        if(con==null)
            getConnection();
        this.con = con;
        try {
            this.con.setAutoCommit(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }
    
    
    public void setLocale(Locale loc) {
        this.setLoc(loc);
        if (loc==null)
            loc = Locale.getDefault();
        else
            try {
                setResourceBundle(ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",loc));
            }  catch (Exception e) {
                e.printStackTrace();
                printStats();
            }
    }
    
    
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
        if (currentRow == 0)                        // If there was no current row
            set.beforeFirst();                      // We want next() to go to first row
        else                                        // If there WAS a current row
            set.absolute(currentRow);               // Restore it
        return rowCount;
    }

    public abstract SettingServer getSettingServer();
    
    public abstract SettingServer getSetting(String name);

    public Locale getLocale() {
        if(loc==null)
            loc=Locale.getDefault();
        return loc;
    }

    public void setLoc(Locale loc) {
        this.loc = loc;
    }
}
