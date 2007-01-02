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
 * -                -                 -
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import java.io.PrintWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class XincoDBManager {
    
    public Connection con;
    public XincoConfigSingletonServer config;
    public static int count = 0;
    private int EmailLink=1,DataLink=2;
    private ResourceBundle lrb = null;
    
    public XincoDBManager() throws Exception {
        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        //load compiled configuartion
        config = XincoConfigSingletonServer.getInstance();
        DataSource datasource = (DataSource)(new InitialContext()).lookup(config.JNDIDB);
        con = datasource.getConnection();
        con.setAutoCommit(false);
        count++;
    }
    
    public int getNewID(String attrTN) throws Exception {
        
        int newID = 0;
        Statement stmt;
        
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_id WHERE tablename='" + attrTN + "'");
        while (rs.next()) {
            newID = rs.getInt("last_id") + 1;
        }
        stmt.close();
        
        stmt = con.createStatement();
        stmt.executeUpdate("UPDATE xinco_id SET last_id=last_id+1 WHERE tablename='" + attrTN + "'");
        stmt.close();
        
        return newID;
        
    }
    
    protected void finalize() throws Throwable {
        try {
            count--;
            con.close();
        } finally {
            if (!con.isClosed()) {
                count++;
            }
            super.finalize();
        }
    }
    
    /**Draws a table with results of the query stored in the ResultSet rs in the PrintWriter out*/
    public void drawTable(ResultSet rs, PrintWriter out , String header, String title , int columnAsLink, boolean details, int linkType) {
        try{
            out.println(title);
            out.println("<center><table border =1 ><tr>");
            out.println(header+"</td></tr><tr>");
            int size = rs.getMetaData().getColumnCount();
            while(rs.next()){
                for(int i=1;i<=size;i++) {
                    if(i==size && details) {
                        out.println("<td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='"+
                                canReplace(rs.getString(i))+"'><input type='hidden' name='Page' value='ProcessData.jsp'></form></td>");
                    } else {
                        if(i==columnAsLink && linkType==this.EmailLink){
                            if(canReplace(rs.getString(i))==null)
                                out.println("<td>No email address available</td>");
                            else
                                out.println("<td><a href= mailto:"+canReplace(rs.getString(i))+">Email this person</a></td>");
                        }
                        if(i==columnAsLink && linkType==this.DataLink){
                            if(canReplace(rs.getString(i))==null)
                                out.println("<td>No code available</td>");
                            else
                                out.println("<td>"+canReplace(rs.getString(i))+"</td><td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='"+
                                        canReplace(rs.getString(i))+"'><input type='hidden' name='Page' value='Codes.jsp'></form></td>");
                        } else {
                            if(canReplace(rs.getString(i))==null)
                                out.println("<td>No data available</td>");
                            else
                                out.println("<td>"+canReplace(rs.getString(i))+"</td>");
                        }
                    }
                }
                out.println("</tr><tr>");
            }
            out.println("</tr></table></center>");
        } catch(Exception e) {
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
            System.err.println("Error getting names from result set. "+e);
        }
        return header;
    }
    
    private String canReplace(String s){
        try{
            lrb.getString(s);
        }catch (MissingResourceException e){
            return s;
        }
        return lrb.getString(s);
    }
}
