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

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.persistence.XincoID;
import com.dreamer.hibernate.PersistenceManager;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class XincoDBManager extends PersistenceManager {

    public static XincoConfigSingletonServer config;
    public static int count = 0;
    private int EmailLink = 1,  DataLink = 2;
    private ResourceBundle lrb = null;
    private Locale loc = null;
    private boolean testing = false;
    private static Properties props;
    
    static {
        try {
            //load configuartion
            config = XincoConfigSingletonServer.getInstance();
            props=config.getProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XincoDBManager() throws Exception {
        super("XincoPU", props);
        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",
                new Locale("en"));
        count++;
    }

    @SuppressWarnings("unchecked")
    public int getNewID(String attrTN) throws Exception {
        int newID = 0;
        parameters.clear();
        parameters.put("tablename", attrTN);
        result = namedQuery("XincoID.findByTablename", parameters);
        XincoID xid = ((XincoID) result.get(0));
        newID = xid.getLastId();
        xid.setLastId(xid.getLastId() + 1);
        persist(xid, true, true);
        return newID;

    }

    //TODO Remove to use jasperreports instead

    /**Draws a table with results of the query stored in the ResultSet rs in the PrintWriter out
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
                    String value = canReplace(rs.getString(i));
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
                                out.println("<td>" + lrb.getString("general.nodata") + "</td>");
                            } else {
                                out.println("<td>" + value + "</td>");
                            }
                        }
                    }
                }
                out.println("</tr><tr>");
            }
            out.println("</tr></table></center>");
        } catch (Exception e) {
            out.println(lrb.getString("general.nodata"));
            System.out.println("Exception drawing table: " + e.getMessage());
        }
    }

    //TODO Remove to use jasperreports instead
    /** Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.
     * @param rs
     * @return 
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

    //TODO Remove to use jasperreports instead
    /** Returns the column names of the query in an HTML table format for use
     * as header for a table produced by the drawTable method.
     * @param rs
     * @return 
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
            System.err.println("Error getting names from result set. " + e);
        }
        return header;
    }

    /*Replace a string with contents of resource bundle is applicable
     *Used to transform db contents to human readable form.
     */
    private String canReplace(String s) {
        if (s == null) {
            return null;
        }
        try {
            lrb.getString(s);
        } catch (MissingResourceException e) {
            return s;
        }
        return lrb.getString(s);
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

    /**
     * Get Resource bundle
     * @return ResourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return lrb;
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
            }
        }
    }

    /**
     * Set Resource bundle
     * @param lrb
     */
    public void setResourceBundle(ResourceBundle lrb) {
        this.lrb = lrb;
    }

    public boolean isTesting() {
        return testing;
    }

    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    
}
