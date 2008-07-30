package com.bluecubs.xinco.core.hibernate.conf;

import com.bluecubs.xinco.core.hibernate.PersistenceManager;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.hibernate.cfg.Configuration;

/**
 * This class handles the server configuration of POE.
 * Edit values in context.xml
 */
public class ConfigSingletonServer {

    protected String JNDIDB = null;
    private static ConfigSingletonServer instance = null;
    private static PersistenceManager pm = null;
    private Vector settings = null;

    /**
     * Gets a singleton instance. Creates one if needed.
     * @return
     */
    public static ConfigSingletonServer getInstance() {
        if (instance == null) {
            instance = new ConfigSingletonServer();
        }
        instance.init();
        return instance;
    }

    /**
     * private constructor to avoid instance generation with new-operator!
     */
    protected ConfigSingletonServer() {
        try {
            JNDIDB = (String) (new InitialContext()).lookup("dreamer/JNDIDB");
        } catch (NamingException ex) {
            JNDIDB = "java:comp/env/jdbc/DB";
        }
    }

    /**
     * Initializes this singleton from a POESettingServer object
     * @return True if initialization was successfull
     */
    @SuppressWarnings("unchecked")
    public boolean init() {
        try {
            //init PersistanceManger
            Configuration c = new Configuration().setProperty("hibernate.dialect",
                    (String) (new InitialContext()).lookup("java:comp/env/hibernate.dialect")).
                    setProperty("hibernate.connection.datasource",
                    (String) (new InitialContext()).lookup("java:comp/env/hibernate.connection.datasource")).
                    setProperty("hibernate.show_sql",
                    (String) (new InitialContext()).lookup("java:comp/env/hibernate.show_sql")).
                    setProperty("hibernate.format_sql",
                    (String) (new InitialContext()).lookup("java:comp/env/hibernate.format_sql")).
                    setProperty("hibernate.order_updates",
                    (String) (new InitialContext()).lookup("java:comp/env/hibernate.order_updates"));
            setPersistenceManager(new PersistenceManager(c));
        } catch (Exception ex) {
            //Default values
            setJNDIDB("java:comp/env/jdbc/DB");
            return true;
        }
        return true;
    }

    /**
     * Get JNDIDB
     * @return String
     */
    public String getJNDIDB() {
        return JNDIDB;
    }

    /**
     * Set JNDIDB
     * @param JNDIDB
     */
    public void setJNDIDB(String JNDIDB) {
        this.JNDIDB = JNDIDB;
    }

    public Vector getSettings() {
        return settings;
    }

    public void setSettings(Vector settings) {
        this.settings = settings;
    }

    @SuppressWarnings("static-access")
    public static PersistenceManager getPersistenceManager() {
        return getInstance().pm;
    }

    protected static void setPersistenceManager(PersistenceManager aPm) {
        pm = aPm;
    }
}
