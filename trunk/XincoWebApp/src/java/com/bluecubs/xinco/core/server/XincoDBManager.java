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

import com.bluecubs.xinco.core.server.db.DBState;
import java.io.IOException;
import java.util.Map;
import com.bluecubs.xinco.tools.MD5;
import gudusoft.gsqlparser.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
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

public class XincoDBManager {

    private static EntityManagerFactory emf;
    private static Map<String, Object> properties;
    //load compiled configuartion
    public final static XincoConfigSingletonServer config = XincoConfigSingletonServer.getInstance();
    private int EmailLink = 1, DataLink = 2;
    private static ResourceBundle lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    private Locale loc = null;
    protected static String puName = "XincoPU";
    private static boolean locked = false;
    private static boolean usingContext = false;
    private static boolean initDone = false;
    private static XincoDBManager instance;
    private static DBState state;
    private static final Logger logger = Logger.getLogger(XincoDBManager.class.getName());

    static {
        updateDBState();
    }

    private XincoDBManager() throws Exception {
        config.loadSettings();
        //Test: create pdf rendering
//        FileConverter.createPDFRendering(1);
    }

    public static XincoDBManager get() throws Exception {
        if (instance == null) {
            instance = new XincoDBManager();
        }
        return instance;
    }

    /**
     * @return the locked
     */
    public static boolean isLocked() {
        return locked;
    }

    /**
     * @param aLocked the lock to set
     */
    public static void setLocked(boolean aLocked) {
        locked = aLocked;
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
     * @param aPU the Persistence Unit to set
     */
    public static void setPU(String aPU) throws XincoException {
        puName = aPU;
        Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.INFO,
                "Changed persistence unit name to: {0}", puName);
        //Set it to null so it's recreated with new Persistence Unit next time is requested.
        emf = null;
        initDone = false;
        getEntityManagerFactory();
    }

    public static String displayDBStatus() {
        return state.getMessage();
    }

    public static void updateDBState() {
        try {
            String version = getDBVersion();
            if (namedQuery("XincoCoreNode.findAll").isEmpty()) {
                //Database empty
                state = DBState.NEED_INIT;
                logger.warning(state.getMessage());
                //Initialize database
                runInitSQL();
                return;
            } 
            //There's something in the database, let's check the version
            else if (version == null) {
                //Doesn't exist. Need to do manual update. Can't do anything else.
                state = DBState.NEED_MANUAL_UPDATE;
                logger.warning(state.getMessage());
                return;
            } else if (!version.equals(getVersion())) {
                //Needs to be updated
                updateDatabase(version, getVersion());
                state = DBState.NEED_UPDATE;
                logger.log(Level.WARNING, "{0}({1} vs. {2})", 
                        new Object[]{state.getMessage(), version, getVersion()});
                return;
            } else {
                //Nothing to do
                state = DBState.VALID;
                logger.log(Level.INFO, "{0}: {1}",
                        new Object[]{state.getMessage(), getVersion()});
                return;
            }
        } catch (XincoException ex) {
            logger.log(Level.SEVERE, null, ex);
            state = DBState.ERROR;
        }
    }

    public static String getVersion() {
        ResourceBundle settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
        StringBuilder version = new StringBuilder();
        version.append(settings.getString("version.high"));
        version.append(".");
        version.append(settings.getString("version.mid"));
        version.append(".");
        version.append(settings.getString("version.low"));
        version.append((settings.getString("version.postfix").isEmpty()
                ? "" : " " + settings.getString("version.postfix")));
        return version.toString();
    }

    private static String getDBVersion() {
        try {
            StringBuilder version = new StringBuilder();
            version.append(XincoSettingServer.getSetting("version.high").getStringValue());
            version.append(".");
            version.append(XincoSettingServer.getSetting("version.mid").getStringValue());
            version.append(".");
            version.append(XincoSettingServer.getSetting("version.low").getStringValue());
            version.append(XincoSettingServer.getSetting("version.postfix").getStringValue().isEmpty()
                     ? "" : " " + XincoSettingServer.getSetting("version.postfix").getStringValue());
            return version.toString();
        } catch (XincoException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private static void runInitSQL() throws XincoException {
        if (!initDone) {
            try {
                if (XincoSettingServer.getSettings().isEmpty()) {
                    Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.INFO,
                            "Running initialization script...");
                    executeSQL("db/script/init.sql", null);
                    Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.INFO,
                            "Done!");
                }
                initDone = true;
                state = DBState.UPDATED;
                logger.info(state.getMessage().replaceAll("%v", getVersion()));
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
                state = DBState.ERROR;
            }
        }
    }

    protected static void executeSQL(String filePath, Class relativeTo) throws XincoException, java.io.IOException {
        //Get the statements to run
        ArrayList<String> statements = new ArrayList<String>();
        if (relativeTo == null) {
            //This assumes that the path is relative to XincoDBManager class
            statements = readFileAsString(filePath);
        } else {
            statements = readFileAsString(filePath, relativeTo);
        }
        //Now run them
        if (!statements.isEmpty()) {
            for (String statement : statements) {
                Logger.getLogger(XincoDBManager.class.getSimpleName()).log(
                        Level.CONFIG, "Executing statement: {0}", statement);
                XincoDBManager.nativeQuery(statement);
            }
        }
    }

    protected static ArrayList<String> readFileAsString(String filePath) throws java.io.IOException, XincoException {
        return readFileAsString(filePath, XincoDBManager.class);
    }

    protected static ArrayList<String> readFileAsString(String filePath, Class relativeTo) throws java.io.IOException, XincoException {
        InputStream in = relativeTo == null ? new FileInputStream(new File(filePath))
                : relativeTo.getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        ArrayList<String> statements = new ArrayList<String>();
        StringBuilder sql = new StringBuilder();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            sql.append(line).append("\n");
        }
        //The list of statement types to ignore
        ArrayList<String> ignore = new ArrayList<String>();
        ignore.add(ESqlStatementType.sstmysqlset.toString());
        ignore.add(ESqlStatementType.sstinvalid.toString());
        ignore.add(ESqlStatementType.sstmysqluse.toString());
        ignore.add(ESqlStatementType.sstmysqldroptable.toString());
        ignore.add(ESqlStatementType.sstcreatetable.toString());
        ignore.add(ESqlStatementType.sstmysqlsetautocommit.toString());
        ignore.add(ESqlStatementType.sstmysqlcommit.toString());
        ignore.add(ESqlStatementType.sstmysqlstarttransaction.toString());
        //-------------------------------------
        if (!sql.toString().isEmpty()) {
            TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
            sqlparser.sqltext = sql.toString();
            //Check statements for correctness first
            sqlparser.parse();
            //Everything fine, keep going
            for (int i = 0; i < sqlparser.sqlstatements.size(); i++) {
                if (!ignore.contains(sqlparser.sqlstatements.get(i).sqlstatementtype.toString())) {
                    statements.add(sqlparser.sqlstatements.get(i).toString().replaceAll("`xinco`.", ""));
                }
            }
        }
        return statements;
    }

    private static void setDBSystemDir() {
        // Decide on the db system directory: <userhome>/.addressbook/
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir
                + System.getProperty("file.separator", "/") + ".xinco";

        // Set the db system directory.
        System.setProperty("derby.system.home", systemDir);
    }

    /**
     * @return the Entity Manager Factory
     * @throws XincoException
     */
    public static EntityManagerFactory getEntityManagerFactory() throws XincoException {
        if (emf == null) {
            try {
                setDBSystemDir();
                //Use the context defined Database connection
                (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
                emf = Persistence.createEntityManagerFactory(config.JNDIDB);
                Logger.getLogger(XincoDBManager.class.getSimpleName()).
                        log(Level.INFO, "Using context defined database connection: {0}",
                        config.JNDIDB);
                usingContext = true;
            } catch (Exception e) {
                if (!usingContext) {
                    Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.WARNING,
                            "Manually specified connection parameters. "
                            + "Using pre-defined persistence unit: {0}", puName);
                    emf = Persistence.createEntityManagerFactory(puName);
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
            logger.log(Level.SEVERE, null, ex);
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
        getTransaction().begin();
        if (isLocked() && locked) {
            q = getProtectedEntityManager().createQuery(query);
        } else {
            q = getEntityManager().createQuery(query);
        }
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        List result = q.getResultList();
        if (getTransaction().isActive()) {
            getTransaction().commit();
        }
        return result;
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
        getTransaction().begin();
        if (isLocked() && locked) {
            q = getProtectedEntityManager().createNamedQuery(query);
        } else {
            q = getEntityManager().createNamedQuery(query);
        }
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        List result = q.getResultList();
        if (getTransaction().isActive()) {
            getTransaction().commit();
        }
        return result;
    }

    private static void nativeQuery(String query) throws XincoException {
        EntityManager em = null;
        if (isLocked() && locked) {
            em = getProtectedEntityManager();
        } else {
            em = getEntityManager();
        }
        while (query.toLowerCase().contains("md5")) {
            int start = query.toLowerCase().indexOf("md5");
            int end = query.toLowerCase().indexOf(")", start);
            String toEncrypt = query.substring(start, end);
            toEncrypt = toEncrypt.substring(toEncrypt.indexOf("'") + 1,
                    toEncrypt.lastIndexOf("'"));
            Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.FINE,
                    "To encrypt: {0}", toEncrypt);
            query = query.substring(0, start) + "'" + MD5.encrypt(toEncrypt)
                    + "'" + query.substring(end + 1);
            Logger.getLogger(XincoDBManager.class.getSimpleName()).log(Level.FINE,
                    "Converted script: {0}", query);
        }
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery(query).executeUpdate();
        if (transaction.isActive()) {
            transaction.commit();
        }
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
        EntityTransaction trans = null;
        if (isLocked() && locked) {
            trans = getProtectedEntityManager().getTransaction();
        } else {
            trans = getEntityManager().getTransaction();
        }
        return trans;
    }

    /**
     * @return the puName
     */
    public String getPersistenceUnitName() {
        return puName;
    }

    public static void main(String[] args) {
        //Used to update the init script
        //Get the MySQL script file
        File script = new File(new File(System.getProperty("user.dir")).getParent()
                + System.getProperty("file.separator")
                + "DB" + System.getProperty("file.separator")
                + "xinco_MySQL.sql");
        if (script.exists()) {
            try {
                ArrayList<String> contents = readFileAsString(script.getAbsolutePath(), null);
                if (!contents.isEmpty()) {
                    //Create the init.sql file src\java\com\bluecubs\xinco\core\server\db\script
                    File initFile = new File(System.getProperty("user.dir")
                            + System.getProperty("file.separator") + "src"
                            + System.getProperty("file.separator") + "java"
                            + System.getProperty("file.separator") + "com"
                            + System.getProperty("file.separator") + "bluecubs"
                            + System.getProperty("file.separator") + "xinco"
                            + System.getProperty("file.separator") + "core"
                            + System.getProperty("file.separator") + "server"
                            + System.getProperty("file.separator") + "db"
                            + System.getProperty("file.separator") + "script"
                            + System.getProperty("file.separator") + "init.sql");
                    if (initFile.exists()) {
                        initFile.delete();
                    }
                    initFile.createNewFile();
                    setContents(initFile, contents);
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (XincoException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Change the contents of text file in its entirety, overwriting any
     * existing text.
     *
     * This style of implementation throws all exceptions to the caller.
     *
     * @param aFile is an existing file which can be written to.
     * @throws IllegalArgumentException if param does not comply.
     * @throws FileNotFoundException if the file does not exist.
     * @throws IOException if problem encountered during write.
     */
    static public void setContents(File aFile, ArrayList<String> aContents)
            throws FileNotFoundException, IOException {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        if (!aFile.exists()) {
            throw new FileNotFoundException("File does not exist: " + aFile);
        }
        if (!aFile.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        if (!aFile.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + aFile);
        }

        //use buffering
        Writer output = new BufferedWriter(new FileWriter(aFile));
        try {
            //FileWriter always assumes default encoding is OK!
            for (String line : aContents) {
                output.write(line);
                output.write("\n");
            }
        } finally {
            output.close();
        }
    }

    /**
     * @return the state
     */
    public static DBState getState() {
        return state;
    }

    //TODO: Update system. Won't be really needed until next DB change after 2.1.0
    /**
     * Update database to current version
     * @param dbVersion Current DB version
     * @param configVersion Latest version
     */
    private static void updateDatabase(String dbVersion, String configVersion) {
//        state = DBState.UPDATED;
//        logger.info(state.getMessage().replaceAll("%v", dbVersion));
    }
}
