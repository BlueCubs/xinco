package com.bluecubs.xinco.core.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.ejb.TransactionImpl;

/**
 *
 * @author Javier A. Ortiz  <javier.ortiz.78@gmail.com>
 */
public class PersistenceManager {

    private EntityManagerFactory emf = null;
    private EntityManager em = null;
    private TransactionImpl transaction;
    private boolean transactionOk = false,  showQueries = false;
    private Vector settings = new Vector();
    private SessionFactory sessions;
    private HibernateConfiguration conf;
    private String persistenceUnitName = null;
    protected List result;
    protected HashMap parameters;

    public PersistenceManager() {
        init(getDefaultConfig());
    }

    public PersistenceManager(Configuration c) {
        init(c);
    }

    public PersistenceManager(Properties p) {
        Configuration c = new Configuration();
        c.setProperties(p);
        init(c);
    }

    public PersistenceManager(String persistanceUnit) {
        setPersistenceUnitName(persistanceUnit);
        init(getDefaultConfig());
    }

    public PersistenceManager(String persistanceUnit, Configuration c) {
        setPersistenceUnitName(persistanceUnit);
        init(c);
    }

    public void clearTable(String table) {
        executeQuery("delete from " + table);
    }

    private Configuration getDefaultConfig() {
        Properties props = new Properties();
        props.put("hibernate.connection.url", conf.get("jdbc_url"));
        props.put("hibernate.connection.driver_class", conf.get("jdbc_class"));
        props.put("hibernate.connection.username", conf.get("jdbc_user"));
        props.put("hibernate.connection.password", conf.get("jdbc_pwd"));
        props.put("hibernate.c3p0.min_size", 5);
        props.put("hibernate.c3p0.max_size", 20);
        props.put("hibernate.c3p0.timeout", 1800);
        props.put("hibernate.c3p0.max_statements", 50);
        //"org.hibernate.dialect.MySQLDialect"
        props.put("hibernate.dialect", conf.get("dialect"));
        return new org.hibernate.cfg.Configuration().addProperties(props);
    }

    private void init(Configuration c) {
        sessions = c.buildSessionFactory();
        sessions.openSession();
        setShowQueries(false);
    }

    public boolean persist(Object object, boolean exists, boolean atomic) {
        try {
            if (atomic) {
                startTransaction();
            }
            if (exists) {
                getEntityManager().merge(object);
            } else {
                getEntityManager().persist(object);
            }
            if (atomic) {
                commitAndClose();
            }
            return updateTransactionStatus(true);
        } catch (Exception e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            getEntityManager().getTransaction().rollback();
            return updateTransactionStatus(false);
        }
    }

    public boolean delete(Object object, boolean atomic) {
        try {
            if (atomic) {
                startTransaction();
            }
            Object o = getEntityManager().merge(object);
            getEntityManager().remove(o);
            if (atomic) {
                commitAndClose();
            }
            return updateTransactionStatus(true);
        } catch (Exception e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            getEntityManager().getTransaction().rollback();
            return updateTransactionStatus(false);
        }
    }

    public List executeQuery(String query) {
        try {
            Query q = getEntityManager().createQuery(query);
            if (isShowQueries()) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Executing: " + query);
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Result size: " + q.getResultList().size());
            }
            return q.getResultList();
        } catch (Throwable e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public List createdQuery(String createdQuery) {
        return createdQuery(createdQuery, null);
    }

    public List createdQuery(String createdQuery, HashMap parameters) {
        Query q = getEntityManager().createQuery(createdQuery);
        try {
            if (parameters != null) {
                Set keys = parameters.keySet();
                for (Iterator iter = keys.iterator(); iter.hasNext();) {
                    String key = iter.next().toString();
                    q.setParameter(key, parameters.get(key));
                }
            }
            if (isShowQueries()) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Executing: " + createdQuery);
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Parameters: " + parameters);
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Result size: " + q.getResultList().size());
            }
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public List namedQuery(String namedQuery, HashMap parameters) {
        Query q = getEntityManager().createNamedQuery(namedQuery);
        try {
            if (parameters != null) {
                Set keys = parameters.keySet();
                for (Iterator iter = keys.iterator(); iter.hasNext();) {
                    String key = iter.next().toString();
                    q.setParameter(key, parameters.get(key));
                }
            }
            if (isShowQueries()) {
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Executing: " + namedQuery);
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Parameters: " + parameters);
                Logger.getLogger(PersistenceManager.class.getName()).log(Level.INFO, "Result size: " + q.getResultList().size());
            }
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private EntityManagerFactory getEntityManagerFactory() {
        if (emf == null && !getPersistenceUnitName().equals(null)) {
            emf = javax.persistence.Persistence.createEntityManagerFactory(getPersistenceUnitName());
        }
        return emf;
    }

    /**
     * Get Session Manager
     * @return SessionFactory
     */
    public SessionFactory getSessionFactory() {
        return ((HibernateEntityManagerFactory) getEntityManagerFactory()).getSessionFactory();
    }

    private EntityManager getEntityManager() {
        if (em == null) {
            em = getEntityManagerFactory().createEntityManager();
        }
        return em;
    }

    public TransactionImpl getTransaction() {
        if (transaction == null) {
            transaction = (TransactionImpl) getEntityManager().getTransaction();
        }
        return transaction;
    }

    public boolean startTransaction() {
        try {
            updateTransactionStatus(false);
            getTransaction().begin();
            return true;
        } catch (Exception e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public boolean commitAndClose() {
        try {
            getTransaction().commit();
            getEntityManager().close();
            getEntityManagerFactory().close();
            em = null;
            transaction = null;
            return updateTransactionStatus(true);
        } catch (Exception e) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            if (getEntityManager().getTransaction() != null && getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
            return updateTransactionStatus(false);
        }
    }

    private boolean updateTransactionStatus(boolean val) {
        if (isTransactionOk() != val) {
            setTransactionOk(val);
        }
        return isTransactionOk();
    }

    public boolean isTransactionOk() {
        return transactionOk;
    }

    private void setTransactionOk(boolean transactionOk) {
        this.transactionOk = transactionOk;
    }

    public boolean isShowQueries() {
        return showQueries;
    }

    public void setShowQueries(boolean showQueries) {
        this.showQueries = showQueries;
    }

    public Vector getSettings() {
        return settings;
    }

    public void setSettings(Vector settings) {
        this.settings = settings;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }
}
