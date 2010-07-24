/**
 *Copyright 2010 blueCubs.com
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

import java.util.Map;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import scriptella.execution.EtlExecutor;

public class XincoDBManager {

    private static EntityManagerFactory emf;
    private static Map<String, Object> properties;
    //load compiled configuartion
    public final static XincoConfigSingletonServer config = XincoConfigSingletonServer.getInstance();
    public static int count = 0;
    private int EmailLink = 1, DataLink = 2;
    private static ResourceBundle lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    private Locale loc = null;
    protected static String puName;
    private static HashMap<String, Object> parameters = new HashMap<String, Object>();
    private static boolean locked = false;
    private static boolean usingContext = false;
    private static boolean initDone = false;

    public XincoDBManager() throws Exception {
        count++;
        config.loadSettings();
        //Test: create pdf rendering
//        FileConverter.createPDFRendering(1);
    }

    /**
     * @return the locked
     */
    public static boolean isLocked() {
        return locked;
    }

    /**
     * @param aLocked the locked to set
     */
    public static void setLocked(boolean aLocked) {
        locked = aLocked;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            count--;
        } finally {
            super.finalize();
        }
    }
//TODO: Replace with a report

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
                    String value = canReplace(rs.getString(i));
                    if (rs.getMetaData().getColumnName(i).contains("password")) {
                        value = "******************************";
                    }
                    if (i == size && details) {
                        out.println("<td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='" + value + "'><input type='hidden' name='Page' value='ProcessData.jsp'></form></td>");
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
                                out.println("<td>" + value + "</td><td><form action='Detail.jsp' method='post'><input type='submit' value='Get Details' onclick='Detail.jsp'><input type='hidden' name = 'key' value='" + value + "'><input type='hidden' name='Page' value='Codes.jsp'></form></td>");
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

    /**
     * Returns the column names of the query in an HTML table format for use
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

    /*
     * Replace a string with contents of resource bundle if applicable
     * Used to transform db contents to human readable form.
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

    public void setLoc(Locale loc) {
        this.loc = loc;
        if (loc == null) {
            loc = Locale.getDefault();
        } else {
            try {
                lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
            } catch (Exception e) {
                Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.SEVERE,
                        e.getLocalizedMessage());
            }
        }
    }

    /**
     * @param aPU the PU to set
     */
    public static void setPU(String aPU) {
        puName = aPU;
        Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.INFO,
                "Changed persistence unit name to: {0}", puName);
        //Set it to null so it's recreated with new Persistence Unit next time is requested.
        emf = null;
        getEntityManagerFactory();
    }

    private static void runInitSQL() {
        if (!initDone) {
            try {
                Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.INFO,
                        "Running initialization script...");
                EtlExecutor.newExecutor(XincoDBManager.class.getResource("db/scripts/init.xml"),
                        XincoBackupManager.getScriptellaProperties(emf, 1)).execute();
                Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.INFO,
                        "Done!");
                initDone = true;
            } catch (Exception e) {
                Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.SEVERE, null, e);
                throw new XincoException(e.getLocalizedMessage());
            }
        }
    }

    private static ArrayList<String> readFileAsString(String filePath) throws java.io.IOException {
        InputStream in = XincoDBManager.class.getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        ArrayList<String> statements = new ArrayList<String>();
        String temp = "";
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.startsWith("-") && line.endsWith(";")) {
                if (temp.isEmpty()) {
                    //We got a one line statement, just add it
                    statements.add(line);
                } else {
                    //We have some accumulation
                    statements.add(temp + line);
                    temp = "";
                }
            } else if (!line.startsWith("-")) {
                //We got part of a multine statement, accumulate until we have the whole statement
                temp += line;
            }
        }
        return statements;
    }

    /**
     * @return the Entity Manager Factory
     * @throws XincoException
     */
    public static EntityManagerFactory getEntityManagerFactory() throws XincoException {
        if (emf == null) {
            try {
                //Use the context defined Database connection
                (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
                emf = Persistence.createEntityManagerFactory(config.JNDIDB);
                Logger.getLogger(XincoDBManager.class.getSimpleName()).
                        log(Level.FINER, "Using context defined database connection: {0}",
                        config.JNDIDB);
                usingContext = true;
                runInitSQL();
            } catch (Exception e) {
                if (!usingContext) {
                    Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.WARNING,
                            "Manually specified connection parameters. "
                            + "Using pre-defined persistence unit: {0}", puName);
                    emf = Persistence.createEntityManagerFactory(puName);
                    runInitSQL();
                } else {
                    Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.SEVERE,
                            "Context doesn't exist. Check your configuration.", e);
                }
            }
        }
        return emf;
    }

    private static EntityManager getEntityManager() throws XincoException {
        if (!isLocked()) {
            return getProtectedEntityManager();
        } else {
            throw new XincoException(lrb.getString("message.locked"));
        }
    }

    protected static EntityManager getProtectedEntityManager() {
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            properties = em.getProperties();
        } catch (XincoException ex) {
            Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
        return em;
    }

    public static List<Object> createdQuery(String query) throws XincoException {
        return createdQuery(query, null);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> createdQuery(String query, HashMap<String, Object> parameters) throws XincoException {
        return protectedCreatedQuery(query, parameters, false);
    }

    public static List<Object> protectedCreatedQuery(String query, HashMap<String, Object> parameters, boolean locked) throws XincoException {
        Query q = null;
        if (isLocked() && locked) {
            getProtectedEntityManager().getTransaction().begin();
            q = getProtectedEntityManager().createQuery(query);
        } else {
            getEntityManager().getTransaction().begin();
            q = getEntityManager().createQuery(query);
        }
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        return q.getResultList();
    }

    public static List<Object> namedQuery(String query) throws XincoException {
        return protectedNamedQuery(query, null, false);
    }

    public static List<Object> namedQuery(String query, HashMap<String, Object> parameters) throws XincoException {
        return protectedNamedQuery(query, parameters, false);
    }

    @SuppressWarnings("unchecked")
    protected static List<Object> protectedNamedQuery(String query, HashMap<String, Object> parameters, boolean locked) throws XincoException {
        Query q = null;
        if (isLocked() && locked) {
            getProtectedEntityManager().getTransaction().begin();
            q = getProtectedEntityManager().createNamedQuery(query);
        } else {
            getEntityManager().getTransaction().begin();
            q = getEntityManager().createNamedQuery(query);
        }
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        return q.getResultList();
    }

    static void close() {
        try {
            getEntityManager().close();
            getEntityManagerFactory().close();
        } catch (XincoException ex) {
            Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    public static EntityTransaction getTransaction() throws XincoException {
        return getEntityManager().getTransaction();
    }

    /**
     * @return the puName
     */
    public String getPersistenceUnitName() {
        return puName;
    }
}
