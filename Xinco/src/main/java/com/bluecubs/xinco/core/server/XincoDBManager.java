/**
 * Copyright 2012 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoDBManager
 *
 * Description: server-side database manager
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? Javier A. Ortiz 01/04/2007 Added methods for result set
 * manipulation and formating
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.db.DBState;
import com.bluecubs.xinco.tools.MD5;
import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.exception.FlywayException;
import com.googlecode.flyway.core.metadatatable.MetaDataTableRow;
import com.googlecode.flyway.core.migration.MigrationState;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TGSqlParser;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class XincoDBManager {

    private static EntityManagerFactory emf;
    //load compiled configuartion
    public final static XincoConfigSingletonServer config = XincoConfigSingletonServer.getInstance();
    private static ResourceBundle lrb =
            ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    private static ResourceBundle settings =
            ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
    protected static String puName = "XincoPU";
    private static boolean locked = false;
    private static boolean usingContext = false;
    private static boolean initDone = false;
    private static XincoDBManager instance;
    private static DBState state = DBState.START_UP;
    private static final Logger LOG = Logger.getLogger(XincoDBManager.class.getName());
    private static boolean demo = false;
    private static long demoResetPeriod = 0;

    /**
     * @return the demo
     */
    public static boolean isDemo() {
        return demo;
    }

    /**
     * @return the demoResetPeriod
     */
    public static long getDemoResetPeriod() {
        return demoResetPeriod;
    }

    private XincoDBManager() throws Exception {
        reload();
    }

    public static void reload() throws XincoException {
        reload(false);
    }

    public static void waitForDB() {
        while (XincoDBManager.getState() != DBState.VALID
                && XincoDBManager.getState() != DBState.UPDATED
                && XincoDBManager.getState() != DBState.ERROR) {
            LOG.log(Level.INFO,
                    "Waiting for DB initialization. Current state: {0}",
                    (XincoDBManager.getState() != null ? XincoDBManager.getState().name() : null));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        LOG.log(Level.INFO, "DB ready, resuming...");
    }

    public static void reload(boolean close) throws XincoException {
        if (close) {
            close();
        }
        getEntityManagerFactory();
        updateDBState();
        generateIDs();
        config.loadSettings();
    }

    private static void processFields(Field[] fields) {
        try {
            XincoIdServer temp;
            for (Field field : fields) {
                if (field.isAnnotationPresent(TableGenerator.class)) {
                    field.setAccessible(true);
                    TableGenerator annotation =
                            field.getAnnotation(TableGenerator.class);
                    field.setAccessible(false);
                    HashMap parameters = new HashMap();
                    String tableName = annotation.pkColumnValue();
                    parameters.put("tablename", tableName);
                    if (XincoDBManager.namedQuery("XincoId.findByTablename",
                            parameters).isEmpty()) {
                        temp = new XincoIdServer(tableName,
                                annotation.initialValue() - 1);
                        temp.write2DB();
                    } else {
                        LOG.fine("Already defined!");
                    }
                }
            }
        } finally {
            try {
                if (LOG.isLoggable(Level.CONFIG)) {
                    for (Iterator<XincoIdServer> it = 
                            XincoIdServer.getIds().iterator(); it.hasNext();) {
                        XincoIdServer next = it.next();
                        LOG.log(Level.CONFIG,
                                "{0}, {1}, {2}", new Object[]{next.getId(),
                                    next.getTablename(), next.getLastId()});
                    }
                }
            } catch (XincoException ex1) {
                LOG.log(Level.SEVERE, null, ex1);
            }
        }
    }

    private static void generateIDs() {
        LOG.log(Level.FINE,
                "Creating ids to work around eclipse issue "
                + "(https://bugs.eclipse.org/bugs/show_bug.cgi?id=366852)...");
        LOG.log(Level.FINE, "Embeddables:");
        for (Iterator<EmbeddableType<?>> it = getEntityManager().getMetamodel()
                .getEmbeddables().iterator(); it.hasNext();) {
            EmbeddableType et = it.next();
            processFields(et.getJavaType().getDeclaredFields());
        }
        LOG.log(Level.FINE, "Entities:");
        for (Iterator<EntityType<?>> it = getEntityManager().getMetamodel()
                .getEntities().iterator(); it.hasNext();) {
            EntityType et = it.next();
            processFields(et.getBindableJavaType().getDeclaredFields());
        }
        LOG.log(Level.FINE, "Done!");
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
        LOG.log(Level.WARNING, "{0} the database.",
                (aLocked ? "Locking" : "Unlocking"));
        locked = aLocked;
    }

    public void setLoc(Locale loc) {
        try {
            lrb = ResourceBundle.getBundle(
                    "com.bluecubs.xinco.messages.XincoMessages", loc);
        } catch (NullPointerException e) {
            LOG.log(Level.FINE, null, e);
            lrb = ResourceBundle.getBundle(
                    "com.bluecubs.xinco.messages.XincoMessages",
                    Locale.getDefault());
        }
    }

    /**
     * @param aPU the Persistence Unit to set
     * @throws XincoException
     */
    public static void setPU(String aPU) throws XincoException {
        puName = aPU;
        LOG.log(Level.INFO,
                "Changed persistence unit name to: {0}", puName);
        //Set it to null so it's recreated with new Persistence Unit 
        //next time is requested.
        emf = null;
        initDone = false;
        reload();
    }

    public static String displayDBStatus() {
        return getState().getMessage();
    }

    public static void updateDBState() {
        try {
            DataSource ds;
            try {
                ds = (javax.sql.DataSource) new InitialContext()
                        .lookup("java:comp/env/jdbc/XincoDB");
            } catch (NamingException ne) {
                LOG.log(Level.FINE, null, ne);
                //It might be the tests, use an H2 Database
                ds = new JdbcDataSource();
                ((JdbcDataSource) ds).setPassword("");
                ((JdbcDataSource) ds).setUser("root");
                ((JdbcDataSource) ds).setURL(
                        "jdbc:h2:file:data/xinco-test;MODE=MySQL");
            }
            if (namedQuery("XincoCoreNode.findAll").isEmpty()) {
                //Database empty
                state = DBState.NEED_INIT;
                LOG.warning(state.getMessage());
                //Initialize database
                if (ds != null) {
                    initializeFlyway(ds);
                } else {
                    state = DBState.ERROR;
                }
            }

            if (ds != null) {
                updateDatabase(ds);
            } else {
                state = DBState.ERROR;
            }

            if (state != DBState.VALID) {
                waitForDB();
            }
        } catch (XincoException ex) {
            LOG.log(Level.SEVERE, null, ex);
            state = DBState.ERROR;
        }
    }

    public static String getVersionNumber() {
        StringBuilder version = new StringBuilder();
        version.append(settings.getString("version.high"));
        version.append(".");
        version.append(settings.getString("version.mid"));
        version.append(".");
        version.append(settings.getString("version.low"));
        return version.toString();
    }

    public static String getVersion() {
        return getVersionNumber()
                + ((settings.getString("version.postfix").isEmpty()
                ? "" : " " + settings.getString("version.postfix")));
    }

    private static String getDBVersionNumber() {
        try {
            StringBuilder version = new StringBuilder();
            version.append(XincoSettingServer.getSetting("version.high")
                    .getIntValue());
            version.append(".");
            version.append(XincoSettingServer.getSetting("version.mid")
                    .getIntValue());
            version.append(".");
            version.append(XincoSettingServer.getSetting("version.low")
                    .getIntValue());
            return version.toString();
        } catch (XincoException ex) {
            LOG.log(Level.FINE, null, ex);
            return null;
        }
    }

    public static String getDBVersion() {
        try {
            return getDBVersionNumber()
                    + (XincoSettingServer.getSetting("version.postfix")
                    .getStringValue().isEmpty()
                    ? "" : " " + XincoSettingServer
                    .getSetting("version.postfix").getStringValue());
        } catch (XincoException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static void initializeFlyway(DataSource dataSource) {
        assert dataSource != null;
        setState(DBState.START_UP);
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("com.bluecubs.xinco.core.server.db.script");
        MetaDataTableRow status = flyway.status();
        if (status == null) {
            setState(DBState.NEED_INIT);
            LOG.info("Initialize the metadata...");
            try {
                flyway.init();
                LOG.info("Done!");
            } catch (FlywayException fe) {
                LOG.log(Level.SEVERE, "Unable to initialize database", fe);
                setState(DBState.ERROR);
            }
        } else {
            LOG.info("Database has Flyway metadata already...");
            displayDBStatus(status);
        }
    }

    private static void updateDatabase(DataSource dataSource) {
        Flyway flyway = new Flyway();
        try {
            flyway.setDataSource(dataSource);
            flyway.setLocations("com.bluecubs.xinco.core.server.db.script");
            LOG.info("Starting migration...");
            flyway.migrate();
            LOG.info("Done!");
        } catch (FlywayException fe) {
            LOG.log(Level.SEVERE, "Unable to migrate data", fe);
            setState(DBState.ERROR);
        }
        try {
            LOG.info("Validating migration...");
            flyway.validate();
            LOG.info("Done!");
            setState(flyway.status().getState() == MigrationState.SUCCESS
                    ? DBState.VALID : DBState.ERROR);
        } catch (FlywayException fe) {
            LOG.log(Level.SEVERE, "Unable to validate", fe);
            setState(DBState.ERROR);
        }
    }

    private static void displayDBStatus(MetaDataTableRow status) {
        LOG.log(Level.INFO, "Description: {0}\nState: {1}\nVersion: {2}",
                new Object[]{status.getDescription(), status.getState(),
                    status.getVersion()});
    }

    protected static void executeSQL(String filePath, Class relativeTo)
            throws XincoException, java.io.IOException {
        //Get the statements to run
        ArrayList<String> statements;
        if (relativeTo == null) {
            //This assumes that the path is relative to XincoDBManager class
            statements = readFileAsString(filePath);
        } else {
            statements = readFileAsString(filePath, relativeTo);
        }
        //Now run them
        if (!statements.isEmpty()) {
            for (String statement : statements) {
                LOG.log(
                        Level.CONFIG, "Executing statement: {0}", statement);
                XincoDBManager.nativeQuery(statement);
                LOG.log(
                        Level.CONFIG, "Done!", statement);
            }
        } else {
            throw new XincoException("Nothing to execute!");
        }
    }

    protected static ArrayList<String> readFileAsString(String filePath)
            throws java.io.IOException, XincoException {
        return readFileAsString(filePath, XincoDBManager.class);
    }

    protected static ArrayList<String> readFileAsString(String filePath,
            Class relativeTo) throws java.io.IOException, XincoException {
        InputStream in = relativeTo == null
                ? new FileInputStream(new File(filePath))
                : relativeTo.getResourceAsStream(filePath);
        InputStreamReader is = new InputStreamReader(in, "utf8");
        BufferedReader br = new BufferedReader(is);
        String line;
        ArrayList<String> statements = new ArrayList<String>();
        StringBuilder sql = new StringBuilder();
        try {
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
                    if (!ignore.contains(
                            sqlparser.sqlstatements.get(i).sqlstatementtype
                            .toString())) {
                        statements.add(sqlparser.sqlstatements.get(i).toString()
                                .replaceAll("`xinco`.", ""));
                    }
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return statements;
    }

    /**
     * @return the Entity Manager Factory
     * @throws XincoException
     */
    public static EntityManagerFactory getEntityManagerFactory()
            throws XincoException {
        if (emf == null) {
            try {
                //Use the context defined Database connection
                (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
                try {
                    demo = (Boolean) (new InitialContext())
                            .lookup("java:comp/env/xinco/demo");
                } catch (NamingException e) {
                    LOG.log(Level.SEVERE, null, e);
                    demo = false;
                }
                if (isDemo()) {
                    try {
                        demoResetPeriod = (Long) (new InitialContext()).lookup(
                                "java:comp/env/xinco/demo-period");
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, null, e);
                        demoResetPeriod = 0;
                    }
                    if (demoResetPeriod > 0) {
                        LOG.log(Level.WARNING,
                                "Instance configured as demo, "
                                + "database will reset"
                                + " each {0} milliseconds", demoResetPeriod);
                    }
                }
                emf = Persistence.createEntityManagerFactory(
                        config.getJNDIDB());
                LOG.log(Level.INFO,
                        "Using context defined database connection: {0}",
                        config.getJNDIDB());
                usingContext = true;

            } catch (NamingException e) {
                demo = false;
                if (!usingContext) {
                    LOG.log(Level.WARNING,
                            "Manually specified connection parameters. "
                            + "Using pre-defined persistence unit: {0}",
                            puName);
                    emf = Persistence.createEntityManagerFactory(puName);
                } else {
                    LOG.log(Level.SEVERE,
                            "Context doesn't exist. Check your configuration.",
                            e);
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
        } catch (XincoException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return em;
    }

    public static List<Object> createdQuery(String query)
            throws XincoException {
        return createdQuery(query, null);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> createdQuery(String query,
            HashMap<String, Object> parameters) throws XincoException {
        return protectedCreatedQuery(query, parameters, false);
    }

    public static List<Object> protectedCreatedQuery(String query,
            HashMap<String, Object> parameters, boolean locked)
            throws XincoException {
        Query q;
        getTransaction().begin();
        if (isLocked() && locked) {
            q = getProtectedEntityManager().createQuery(query);
        } else {
            q = getEntityManager().createQuery(query);
        }
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries =
                    parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey(), e.getValue());
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

    public static List<Object> namedQuery(String query,
            HashMap<String, Object> parameters) throws XincoException {
        return protectedNamedQuery(query, parameters, false);
    }

    @SuppressWarnings("unchecked")
    protected static List<Object> protectedNamedQuery(String query,
            HashMap<String, Object> parameters, boolean locked)
            throws XincoException {
        Query q;
        getTransaction().begin();
        if (isLocked() && locked) {
            q = getProtectedEntityManager().createNamedQuery(query);
        } else {
            q = getEntityManager().createNamedQuery(query);
        }
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries =
                    parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey(), e.getValue());
            }
        }
        List result = q.getResultList();
        if (getTransaction().isActive()) {
            getTransaction().commit();
        }
        return result;
    }

    public static void nativeQuery(String query) throws XincoException {
        EntityManager em;
        if (isLocked() && locked) {
            em = getProtectedEntityManager();
        } else {
            em = getEntityManager();
        }
        while (query.toLowerCase(Locale.getDefault()).contains("md5")) {
            int start = query.toLowerCase(Locale.getDefault()).indexOf("md5");
            int end = query.toLowerCase(Locale.getDefault()).indexOf(')', start);
            String toEncrypt = query.substring(start, end);
            toEncrypt = toEncrypt.substring(toEncrypt.indexOf('\'') + 1,
                    toEncrypt.lastIndexOf('\''));
            LOG.log(Level.FINE,
                    "To encrypt: {0}", toEncrypt);
            query = query.substring(0, start) + "'" + MD5.encrypt(toEncrypt)
                    + "'" + query.substring(end + 1);
            LOG.log(Level.FINE,
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
            getEntityManagerFactory().close();
        } catch (XincoException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        emf = null;
        initDone = false;
    }

    public static EntityTransaction getTransaction() throws XincoException {
        EntityTransaction trans;
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
        File script = new File(new File(System.getProperty("user.dir"))
                .getParent()
                + System.getProperty("file.separator")
                + "DB" + System.getProperty("file.separator")
                + "xinco_MySQL.sql");
        if (script.exists()) {
            try {
                ArrayList<String> contents =
                        readFileAsString(script.getAbsolutePath(), null);
                if (!contents.isEmpty()) {
                    //Create the init.sql file 
                    //src\java\com\bluecubs\xinco\core\server\db\script
                    File initFile = new File(System.getProperty("user.dir")
                            + System.getProperty("file.separator") + "src"
                            + System.getProperty("file.separator") + "main"
                            + System.getProperty("file.separator") + "resources"
                            + System.getProperty("file.separator") + "com"
                            + System.getProperty("file.separator") + "bluecubs"
                            + System.getProperty("file.separator") + "xinco"
                            + System.getProperty("file.separator") + "core"
                            + System.getProperty("file.separator") + "server"
                            + System.getProperty("file.separator") + "db"
                            + System.getProperty("file.separator") + "script"
                            + System.getProperty("file.separator")
                            + "init.sql");
                    if (initFile.exists()) {
                        if (!initFile.delete()) {
                            throw new XincoException(
                                    "Unable to delete DB script");
                        }
                    }
                    if (initFile.createNewFile()) {
                        setContents(initFile, contents);
                    } else {
                        throw new XincoException(
                                "Unable to create/update DB script");
                    }
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            } catch (XincoException ex) {
                LOG.log(Level.SEVERE, null, ex);
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
     * @param aContents contents to set
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
            throw new IllegalArgumentException("Should not be a directory: "
                    + aFile);
        }
        if (!aFile.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: "
                    + aFile);
        }
        FileWriter fw = new FileWriter(aFile);
        //use buffering
        Writer output = new BufferedWriter(fw);
        try {
            //FileWriter always assumes default encoding is OK!
            for (String line : aContents) {
                output.write(line);
                output.write("\n");
            }
        } finally {
            output.close();
            fw.close();
        }
    }

    /**
     * @return the state
     */
    public static DBState getState() {
        return state;
    }

    protected static void setState(DBState newState) {
        state = newState;
        LOG.warning(state.getMessage());
    }

    /**
     * @return the initDone
     */
    public static boolean isInitDone() {
        return initDone;
    }
}
