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
import com.bluecubs.xinco.core.server.tools.Tool;
import com.bluecubs.xinco.tools.MD5;
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
import javax.persistence.*;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;

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
    private static final Logger logger = Logger.getLogger(XincoDBManager.class.getName());
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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                while (XincoDBManager.getState() != DBState.VALID
                        && XincoDBManager.getState() != DBState.UPDATED
                        && XincoDBManager.getState() != DBState.ERROR) {
                    Logger.getLogger(XincoDBManager.class.getName()).log(Level.INFO,
                            "Waiting for DB initialization. Current state: {0}",
                            (XincoDBManager.getState() != null ? XincoDBManager.getState().name() : null));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Logger.getLogger(XincoDBManager.class.getName()).log(Level.INFO, "DB ready, resuming...");
//            }
//        }, "Waiting for DB").start();
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
                    TableGenerator annotation = field.getAnnotation(TableGenerator.class);
                    field.setAccessible(false);
                    HashMap parameters = new HashMap();
                    String tableName = annotation.pkColumnValue();
                    parameters.put("tablename", tableName);
                    if (XincoDBManager.namedQuery("XincoId.findByTablename", parameters).isEmpty()) {
                        temp = new XincoIdServer(tableName, annotation.initialValue() - 1);
                        temp.write2DB();
                    } else {
                        Logger.getLogger(XincoDBManager.class.getName()).fine("Already defined!");
                    }
                }
            }
        } catch (Exception ex1) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex1);
        } finally {
            try {
                for (Iterator<XincoIdServer> it = XincoIdServer.getIds().iterator(); it.hasNext();) {
                    XincoIdServer next = it.next();
                    Logger.getLogger(XincoDBManager.class.getName()).log(Level.CONFIG,
                            "{0}, {1}, {2}", new Object[]{next.getId(),
                                next.getTablename(), next.getLastId()});
                }
            } catch (XincoException ex1) {
                Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private static void generateIDs() {
        Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE,
                "Creating ids to work around eclipse issue "
                + "(https://bugs.eclipse.org/bugs/show_bug.cgi?id=366852)...");
        Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE, "Embeddables:");
        for (Iterator<EmbeddableType<?>> it = getEntityManager().getMetamodel().getEmbeddables().iterator(); it.hasNext();) {
            EmbeddableType et = it.next();
            processFields(et.getJavaType().getDeclaredFields());
        }
        Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE, "Entities:");
        for (Iterator<EntityType<?>> it = getEntityManager().getMetamodel().getEntities().iterator(); it.hasNext();) {
            EntityType et = it.next();
            processFields(et.getBindableJavaType().getDeclaredFields());
        }
        Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE, "Done!");
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
        logger.log(Level.WARNING, "{0} the database.",
                (aLocked ? "Locking" : "Unlocking"));
        locked = aLocked;
    }

    public void setLoc(Locale loc) {
        try {
            lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        } catch (Exception e) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE, null, e);
            lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault());
        }
    }

    /**
     * @param aPU the Persistence Unit to set
     * @throws XincoException
     */
    public static void setPU(String aPU) throws XincoException {
        puName = aPU;
        logger.log(Level.INFO,
                "Changed persistence unit name to: {0}", puName);
        //Set it to null so it's recreated with new Persistence Unit next time is requested.
        emf = null;
        initDone = false;
        reload();
    }

    public static String displayDBStatus() {
        return state.getMessage();
    }

    public static void updateDBState() {
        try {
            String version = getDBVersionNumber();
            if (namedQuery("XincoCoreNode.findAll").isEmpty()) {
                //Database empty
                state = DBState.NEED_INIT;
                logger.warning(state.getMessage());
                //Initialize database
                runInitSQL();
            } //There's something in the database, let's check the version
            else if (version == null) {
                //Doesn't exist. Need to do manual update. Can't do anything else.
                state = DBState.NEED_MANUAL_UPDATE;
                logger.warning(state.getMessage());
            } else if (!version.equals(getVersionNumber())
                    //Try again it might be a string difference but still the same number
                    && !Tool.compareNumberStrings(version, version)) {
                //Needs to be updated
                updateDatabase(version, getVersion());
                state = DBState.NEED_UPDATE;
                logger.log(Level.WARNING, "{0}({1} vs. {2})",
                        new Object[]{state.getMessage(), version, getVersion()});
            } else {
                //Nothing to do
                state = DBState.VALID;
                logger.log(Level.INFO, "{0}: {1}",
                        new Object[]{state.getMessage(), getVersion()});
            }
            if (state != DBState.VALID) {
                waitForDB();
            }
        } catch (XincoException ex) {
            logger.log(Level.SEVERE, null, ex);
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
        return getVersionNumber() + ((settings.getString("version.postfix").isEmpty()
                ? "" : " " + settings.getString("version.postfix")));
    }

    private static String getDBVersionNumber() {
        try {
            StringBuilder version = new StringBuilder();
            version.append(XincoSettingServer.getSetting("version.high").getIntValue());
            version.append(".");
            version.append(XincoSettingServer.getSetting("version.mid").getIntValue());
            version.append(".");
            version.append(XincoSettingServer.getSetting("version.low").getIntValue());
            return version.toString();
        } catch (XincoException ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.FINE, null, ex);
            return null;
        }
    }

    public static String getDBVersion() {
        try {
            return getDBVersionNumber() + (XincoSettingServer.getSetting("version.postfix").getStringValue().isEmpty()
                    ? "" : " " + XincoSettingServer.getSetting("version.postfix").getStringValue());
        } catch (XincoException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static void runInitSQL() throws XincoException {
        if (!isInitDone() && state != DBState.UPDATING) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!isInitDone() && state != DBState.UPDATING) {
                        try {
                            state = DBState.UPDATING;
                            if (XincoSettingServer.getSettings().isEmpty()) {
                                logger.log(Level.INFO,
                                        "Running initialization script...");
                                executeSQL("db/script/init.sql", null);
                                logger.log(Level.INFO,
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
            }, DBState.UPDATING.getMessage()).start();
        }
    }

    protected static void executeSQL(String filePath, Class relativeTo) throws XincoException, java.io.IOException {
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
                logger.log(
                        Level.CONFIG, "Executing statement: {0}", statement);
                XincoDBManager.nativeQuery(statement);
                logger.log(
                        Level.CONFIG, "Done!", statement);
            }
        } else {
            throw new XincoException("Nothing to execute!");
        }
    }

    protected static ArrayList<String> readFileAsString(String filePath) throws java.io.IOException, XincoException {
        return readFileAsString(filePath, XincoDBManager.class);
    }

    protected static ArrayList<String> readFileAsString(String filePath, Class relativeTo) throws java.io.IOException, XincoException {
        InputStream in = relativeTo == null ? new FileInputStream(new File(filePath))
                : relativeTo.getResourceAsStream(filePath);
        InputStreamReader is = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(is);
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
        br.close();
        is.close();
        in.close();
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
                try {
                    demo = (Boolean) (new InitialContext()).lookup("java:comp/env/xinco/demo");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, null, e);
                    demo = false;
                }
                if (isDemo()) {
                    try {
                        demoResetPeriod = (Long) (new InitialContext()).lookup("java:comp/env/xinco/demo-period");
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, null, e);
                        demoResetPeriod = 0;
                    }
                    if (demoResetPeriod > 0) {
                        logger.log(Level.WARNING,
                                "Instance configured as demo, database will reset"
                                + " each {0} milliseconds", demoResetPeriod);
                    }
                }
                emf = Persistence.createEntityManagerFactory(config.getJNDIDB());
                logger.log(Level.INFO, "Using context defined database connection: {0}", config.getJNDIDB());
                usingContext = true;
            } catch (Exception e) {
                demo = false;
                if (!usingContext) {
                    logger.log(Level.WARNING,
                            "Manually specified connection parameters. "
                            + "Using pre-defined persistence unit: {0}", puName);
                    emf = Persistence.createEntityManagerFactory(puName);
                } else {
                    logger.log(Level.SEVERE,
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
        Query q;
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

    public static List<Object> namedQuery(String query, HashMap<String, Object> parameters) throws XincoException {
        return protectedNamedQuery(query, parameters, false);
    }

    @SuppressWarnings("unchecked")
    protected static List<Object> protectedNamedQuery(String query, HashMap<String, Object> parameters, boolean locked) throws XincoException {
        Query q;
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
            int start = query.toLowerCase().indexOf("md5");
            int end = query.toLowerCase().indexOf(')', start);
            String toEncrypt = query.substring(start, end);
            toEncrypt = toEncrypt.substring(toEncrypt.indexOf('\'') + 1,
                    toEncrypt.lastIndexOf('\''));
            logger.log(Level.FINE,
                    "To encrypt: {0}", toEncrypt);
            query = query.substring(0, start) + "'" + MD5.encrypt(toEncrypt)
                    + "'" + query.substring(end + 1);
            logger.log(Level.FINE,
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
            logger.log(Level.SEVERE, null, ex);
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
                            + System.getProperty("file.separator") + "main"
                            + System.getProperty("file.separator") + "resources"
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
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        if (!aFile.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + aFile);
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
    }

    //TODO: Update system. Won't be really needed until next DB change after 2.1.0
    /**
     * Update database to current version
     *
     * @param dbVersion Current DB version
     * @param configVersion Latest version
     */
    private static void updateDatabase(String dbVersion, final String configVersion) {
        if (state != DBState.UPDATING) {
            //TODO: Look for the proper update script(s) and run them
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (state != DBState.UPDATING) {
                        try {
                            state = DBState.UPDATING;
                            //Lock Database
                            setLocked(true);
                            //TODO: Create back up
                            //Set to current version (this should be the last step)
                            XincoSettingServer setting = XincoSettingServer.getSetting("version.high");
                            setting.setIntValue(Integer.valueOf(settings.getString("version.high")));
                            setting.write2DB();
                            setting = XincoSettingServer.getSetting("version.mid");
                            setting.setIntValue(Integer.valueOf(settings.getString("version.mid")));
                            setting.write2DB();
                            setting = XincoSettingServer.getSetting("version.low");
                            setting.setIntValue(Integer.valueOf(settings.getString("version.low")));
                            setting.write2DB();
                            setting = XincoSettingServer.getSetting("version.mid");
                            setting.setIntValue(Integer.valueOf(settings.getString("version.mid")));
                            setting.write2DB();
                            setting = XincoSettingServer.getSetting("version.postfix");
                            setting.setStringValue(settings.getString("version.postfix"));
                            setting.write2DB();
                            state = DBState.UPDATED;
                            logger.info(state.getMessage().replaceAll("%v", configVersion));
                            //Unlock
                            setLocked(false);
                        } catch (XincoException ex) {
                            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
                            //Unlock
                            setLocked(false);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * @return the initDone
     */
    public static boolean isInitDone() {
        return initDone;
    }
}