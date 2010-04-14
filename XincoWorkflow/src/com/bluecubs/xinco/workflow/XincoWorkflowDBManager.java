package com.bluecubs.xinco.workflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
class XincoWorkflowDBManager {

    private static EntityManagerFactory emf;
    private static Map<String, Object> properties;
    public static int count = 0;
    private static ResourceBundle lrb = ResourceBundle.getBundle("com.bluecubs.xinco.workflow.messages.XincoMessages");
    private Locale loc = null;
    protected String puName;
    private static HashMap<String, Object> parameters = new HashMap<String, Object>();
    private static boolean locked = false;
    private static boolean usingContext = false;

    public XincoWorkflowDBManager() throws Exception {
        count++;
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

    public static void deleteEntity(Object o) {
        try {
            if (getEntityManager().contains(o)) {
                getEntityManager().detach(o);
            }
        } catch (XincoWorkflowException ex) {
            Logger.getLogger(XincoWorkflowDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLoc(Locale loc) {
        this.loc = loc;
        if (loc == null) {
            loc = Locale.getDefault();
        } else {
            try {
                lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the Entity Manager Factory
     * @throws XincoWorkflowException
     */
    public static EntityManagerFactory getEntityManagerFactory() throws XincoWorkflowException {
        try {
            //Use the context defined Database connection
            (new InitialContext()).lookup("java:comp/env/xinco/WorkflowJNDIDB");
            emf = Persistence.createEntityManagerFactory("java:comp/env/xinco/WorkflowJNDIDB");
            Logger.getLogger(XincoWorkflowDBManager.class.getName()).
                    log(Level.FINE, "Using context defined database connection: {0}",
                    "java:comp/env/xinco/WorkflowJNDIDB");
            usingContext = true;
        } catch (Exception e) {
            if (!usingContext) {
                Logger.getLogger(XincoWorkflowDBManager.class.getName()).log(Level.FINE,
                        "Manually specified connection parameters. "
                        + "Using pre-defined persistence unit: XincoWorkflowTestPU");
                emf = Persistence.createEntityManagerFactory("XincoWorkflowTestPU");
            } else {
                Logger.getLogger(XincoWorkflowDBManager.class.getName()).log(Level.SEVERE,
                        "Context doesn't exist. Check your configuration.", e);
            }
        }
        return emf;
    }

    private static EntityManager getEntityManager() throws XincoWorkflowException {
        if (!isLocked()) {
            return getProtectedEntityManager();
        } else {
            throw new XincoWorkflowException(lrb.getString("message.locked"));
        }
    }

    protected static EntityManager getProtectedEntityManager() {
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            properties = em.getProperties();
        } catch (XincoWorkflowException ex) {
            Logger.getLogger(XincoWorkflowDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return em;
    }

    public static List<Object> createdQuery(String query) throws XincoWorkflowException {
        return createdQuery(query, null);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> createdQuery(String query, HashMap<String, Object> parameters) throws XincoWorkflowException {
        return protectedCreatedQuery(query, parameters, false);
    }

    public static List<Object> protectedCreatedQuery(String query, HashMap<String, Object> parameters, boolean locked) throws XincoWorkflowException {
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

    public static List<Object> namedQuery(String query) throws XincoWorkflowException {
        return protectedNamedQuery(query, null, false);
    }

    public static List<Object> namedQuery(String query, HashMap<String, Object> parameters) throws XincoWorkflowException {
        return protectedNamedQuery(query, parameters, false);
    }

    @SuppressWarnings("unchecked")
    protected static List<Object> protectedNamedQuery(String query, HashMap<String, Object> parameters, boolean locked) throws XincoWorkflowException {
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
        } catch (XincoWorkflowException ex) {
            Logger.getLogger(XincoWorkflowDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static EntityTransaction getTransaction() throws XincoWorkflowException {
        return getEntityManager().getTransaction();
    }

    /**
     * @return the puName
     */
    public String getPersistenceUnitName() {
        return puName;
    }
}
