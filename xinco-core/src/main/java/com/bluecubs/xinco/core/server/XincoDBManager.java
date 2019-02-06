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
 * $Date$
 * $Author$
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoConfigSingletonServer.getInstance;
import static com.bluecubs.xinco.core.server.XincoIdServer.getIds;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import com.bluecubs.xinco.core.server.db.DBState;
import static com.bluecubs.xinco.core.server.db.DBState.ERROR;
import static com.bluecubs.xinco.core.server.db.DBState.NEED_INIT;
import static com.bluecubs.xinco.core.server.db.DBState.NEED_MANUAL_UPDATE;
import static com.bluecubs.xinco.core.server.db.DBState.START_UP;
import static com.bluecubs.xinco.core.server.db.DBState.UPDATED;
import static com.bluecubs.xinco.core.server.db.DBState.VALID;
import static com.bluecubs.xinco.tools.MD5.encrypt;
import java.io.*;
import static java.lang.Class.forName;
import static java.lang.Thread.sleep;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import static java.util.Locale.getDefault;
import java.util.Map.Entry;
import static java.util.ResourceBundle.getBundle;
import java.util.logging.Level;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import static javax.persistence.Persistence.createEntityManagerFactory;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import static org.flywaydb.core.Flyway.configure;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfo;
import static org.flywaydb.core.api.MigrationState.SUCCESS;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.h2.jdbcx.JdbcDataSource;

public class XincoDBManager {

    private static EntityManagerFactory emf;
    //load compiled configuartion
    public final static XincoConfigSingletonServer CONFIG
            = getInstance();
    private static ResourceBundle lrb
            = getBundle("com.bluecubs.xinco.messages.XincoMessages");
    private static final ResourceBundle SETTINGS
            = getBundle("com.bluecubs.xinco.settings.settings");
    protected static String puName = "XincoPU";
    private static boolean locked = false;
    private static boolean usingContext = false;
    private static boolean initDone = false;
    private static XincoDBManager instance;
    private static DBState state = START_UP;
    private static final Logger LOG = getLogger(XincoDBManager.class.getName());
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
        while (getState() != VALID
                && getState() != UPDATED
                && getState() != ERROR) {
            LOG.log(INFO,
                    "Waiting for DB initialization. Current state: {0}",
                    (getState() != null
                            ? getState().name() : null));
            try {
                sleep(10_000);
            }
            catch (InterruptedException ex) {
                LOG.log(SEVERE, null, ex);
            }
        }
        LOG.log(INFO, "DB ready, resuming...");
    }

    public static void reload(boolean close) throws XincoException {
        if (close) {
            close();
        }
        updateDBState();
        getEntityManager();
        generateIDs();
        CONFIG.loadSettings();
    }

    private static void processFields(Field[] fields) {
        try {
            XincoIdServer temp;
            for (Field field : fields) {
                if (field.isAnnotationPresent(TableGenerator.class)) {
                    field.setAccessible(true);
                    TableGenerator annotation
                            = field.getAnnotation(TableGenerator.class);
                    field.setAccessible(false);
                    HashMap parameters = new HashMap();
                    String tableName = annotation.pkColumnValue();
                    parameters.put("tablename", tableName);
                    if (namedQuery("XincoId.findByTablename",
                            parameters).isEmpty()) {
                        temp = new XincoIdServer(tableName,
                                annotation.initialValue() - 1);
                        temp.write2DB();
                    } else {
                        LOG.fine("Already defined!");
                    }
                }
            }
        }
        finally {
            try {
                if (LOG.isLoggable(Level.CONFIG)) {
                    getIds().forEach((next) -> {
                        LOG.log(Level.CONFIG,
                                "{0}, {1}, {2}", new Object[]{next.getId(),
                                    next.getTablename(), next.getLastId()});
                    });
                }
            }
            catch (XincoException ex1) {
                LOG.log(SEVERE, null, ex1);
            }
        }
    }

    private static void generateIDs() {
        LOG.log(FINE,
                "Creating ids to work around eclipse issue "
                + "(https://bugs.eclipse.org/bugs/show_bug.cgi?id=366852)...");
        LOG.log(FINE, "Embeddables:");
        getEntityManager().getMetamodel()
                .getEmbeddables().forEach((et) -> {
                    processFields(et.getJavaType().getDeclaredFields());
        });
        LOG.log(FINE, "Entities:");
        getEntityManager().getMetamodel()
                .getEntities().forEach((et) -> {
                    processFields(et.getBindableJavaType().getDeclaredFields());
        });
        LOG.log(FINE, "Done!");
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
        LOG.log(WARNING, "{0} the database.",
                (aLocked ? "Locking" : "Unlocking"));
        locked = aLocked;
    }

    public void setLoc(Locale loc) {
        try {
            lrb = getBundle(
                    "com.bluecubs.xinco.messages.XincoMessages", loc);
        }
        catch (NullPointerException e) {
            LOG.log(FINE, null, e);
            lrb = getBundle("com.bluecubs.xinco.messages.XincoMessages",
                    getDefault());
        }
    }

    /**
     * @param aPU the Persistence Unit to set
     * @throws XincoException when something goes wrong
     */
    public static void setPU(String aPU) throws XincoException {
        puName = aPU;
        LOG.log(INFO,
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
            DataSource ds = null;
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                ds = (javax.sql.DataSource) new InitialContext()
                        .lookup("java:comp/env/jdbc/XincoDB");
            }
            catch (NamingException ne) {
                try {
                    LOG.log(FINE, null, ne);
                    //It might be the tests, use an H2 Database
                    ds = new JdbcDataSource();
                    ((JdbcDataSource) ds).setPassword("xinco");
                    ((JdbcDataSource) ds).setUser("root");
                    ((JdbcDataSource) ds).setURL(
                            "jdbc:h2:file:./target/data/xinco-test;AUTO_SERVER=TRUE");
                    //Load the H2 driver
                    forName("org.h2.Driver");
                }
                catch (ClassNotFoundException ex) {
                    LOG.log(SEVERE, null, ex);
                }
            }
            try {
                if (ds != null) {
                    conn = ds.getConnection();
                    stmt = conn.prepareStatement("select * from xinco_setting");
                    rs = stmt.executeQuery();
                    if (!rs.next()) {
                        //Tables there but empty? Not safe to proceed
                        setState(NEED_MANUAL_UPDATE);
                    } else {
                        ResultSetMetaData metadata = rs.getMetaData();
                        LOG.log(INFO, "Amount of settings: {0}",
                                metadata.getColumnCount());
                    }
                } else {
                    throw new RuntimeException("Null Datasource!");
                }
            }
            catch (SQLException ex) {
                LOG.log(FINE, null, ex);
                //Need INIT, probably nothing there
                setState(NEED_INIT);
                //Create the database
                getEntityManager();
            }
            finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                }
                catch (SQLException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
                catch (SQLException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                }
                catch (SQLException ex) {
                    LOG.log(SEVERE, null, ex);
                }
            }

            if (ds != null) {
                updateDatabase(ds);
            } else {
                state = ERROR;
            }

            if (state != VALID) {
                waitForDB();
            }
        }
        catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
            state = ERROR;
        }
    }

    public static String getVersionNumber() {
        StringBuilder version = new StringBuilder();
        version.append(SETTINGS.getString("version.high"));
        version.append(".");
        version.append(SETTINGS.getString("version.mid"));
        version.append(".");
        version.append(SETTINGS.getString("version.low"));
        return version.toString();
    }

    public static String getVersion() {
        return getVersionNumber()
                + ((SETTINGS.getString("version.postfix").isEmpty()
                ? "" : " " + SETTINGS.getString("version.postfix")));
    }

    private static String getDBVersionNumber() {
        try {
            StringBuilder version = new StringBuilder();
            version.append(getSetting("version.high")
                    .getIntValue());
            version.append(".");
            version.append(getSetting("version.mid")
                    .getIntValue());
            version.append(".");
            version.append(getSetting("version.low")
                    .getIntValue());
            return version.toString();
        }
        catch (XincoException ex) {
            LOG.log(FINE, null, ex);
            return null;
        }
    }

    public static String getDBVersion() {
        try {
            return getDBVersionNumber()
                    + (getSetting("version.postfix")
                            .getStringValue().isEmpty()
                            ? "" : " " + getSetting("version.postfix").getStringValue());
        }
        catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
            return null;
        }
    }

    private static void updateDatabase(DataSource dataSource) {
        FluentConfiguration configuration = configure();
        configuration.baselineOnMigrate(true);
        configuration.dataSource(dataSource);
        configuration.locations("db.migration");
        Flyway flyway = new Flyway(configuration);
        try {
            LOG.info("Starting migration...");
            flyway.migrate();
            LOG.info("Done!");
        }
        catch (FlywayException fe) {
            LOG.log(SEVERE, "Unable to migrate data", fe);
            setState(ERROR);
        }
        try {
            LOG.info("Validating migration...");
            flyway.validate();
            LOG.info("Done!");
            displayDBStatus(flyway.info().current());
            setState(flyway.info().current().getState() == SUCCESS
                    ? VALID : ERROR);
        }
        catch (FlywayException fe) {
            LOG.log(SEVERE, "Unable to validate", fe);
            setState(ERROR);
        }
    }

    private static void displayDBStatus(MigrationInfo status) {
        LOG.log(INFO, "Description: {0}\nState: {1}\nVersion: {2}",
                new Object[]{status.getDescription(), status.getState(),
                    status.getVersion()});
    }

    /**
     * @return the Entity Manager Factory
     * @throws XincoException when something goes wrong
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
                }
                catch (NamingException e) {
                    LOG.log(SEVERE, null, e);
                    demo = false;
                }
                if (isDemo()) {
                    try {
                        demoResetPeriod = (Long) (new InitialContext()).lookup(
                                "java:comp/env/xinco/demo-period");
                    }
                    catch (NamingException e) {
                        LOG.log(SEVERE, null, e);
                        demoResetPeriod = 0;
                    }
                    if (demoResetPeriod > 0) {
                        LOG.log(WARNING,
                                "Instance configured as demo, "
                                + "database will reset"
                                + " each {0} milliseconds", demoResetPeriod);
                    }
                }
                emf = createEntityManagerFactory(CONFIG.getJNDIDB());
                LOG.log(INFO,
                        "Using context defined database connection: {0}",
                        CONFIG.getJNDIDB());
                usingContext = true;

            }
            catch (NamingException e) {
                demo = false;
                if (!usingContext) {
                    LOG.log(WARNING,
                            "Manually specified connection parameters. "
                            + "Using pre-defined persistence unit: {0}",
                            puName);
                    emf = createEntityManagerFactory(puName);
                } else {
                    LOG.log(SEVERE,
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
        }
        catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
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
            Iterator<Map.Entry<String, Object>> entries
                    = parameters.entrySet().iterator();
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
            Iterator<Map.Entry<String, Object>> entries
                    = parameters.entrySet().iterator();
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
        while (query.toLowerCase(getDefault()).contains("md5")) {
            int start = query.toLowerCase(getDefault()).indexOf("md5");
            int end = query.toLowerCase(getDefault()).indexOf(')', start);
            String toEncrypt = query.substring(start, end);
            toEncrypt = toEncrypt.substring(toEncrypt.indexOf('\'') + 1,
                    toEncrypt.lastIndexOf('\''));
            LOG.log(FINE,
                    "To encrypt: {0}", toEncrypt);
            query = query.substring(0, start) + "'" + encrypt(toEncrypt)
                    + "'" + query.substring(end + 1);
            LOG.log(FINE,
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
        }
        catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
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
        try ( //use buffering
                Writer output = new BufferedWriter(fw)) {
            //FileWriter always assumes default encoding is OK!
            for (String line : aContents) {
                output.write(line);
                output.write("\n");
            }
        }
        finally {
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
