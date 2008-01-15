/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.hibernate.ejb.TransactionImpl;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoPersistenceManager {

    private EntityManagerFactory emf = null;
    private EntityManager em = null;
    private TransactionImpl transaction;
    private boolean transactionOk = false,  developerMode = false;
    public static XincoConfigSingletonServer config;

    public XincoPersistenceManager() {
        config = XincoConfigSingletonServer.getInstance();
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
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
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
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
            getEntityManager().getTransaction().rollback();
            return updateTransactionStatus(false);
        }
    }

    public List executeQuery(String query) {
        try {
            Query q = getEntityManager().createQuery(query);
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.INFO, "Executing: " + query);
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.INFO, "Result size: " + q.getResultList().size());
            }
            return q.getResultList();
        } catch (Throwable e) {
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
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
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.INFO, "Executing: " + createdQuery);
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.INFO, "Result size: " + q.getResultList().size());
            }
            return q.getResultList();
        } catch (Exception e) {
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
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
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.INFO, "Executing: " + namedQuery);
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.INFO, "Result size: " + q.getResultList().size());
            }
            return q.getResultList();
        } catch (Exception e) {
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    private EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = javax.persistence.Persistence.createEntityManagerFactory("XincoPU");
        }
        return emf;
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
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean commitAndClose() {
        try {
            getEntityManager().getTransaction().commit();
            getEntityManager().close();
            getEntityManagerFactory().close();
            em = null;
            transaction = null;
            return updateTransactionStatus(true);
        } catch (Exception e) {
            if (isDeveloperMode()) {
                Logger.getLogger(XincoPersistenceManager.class.getName()).log(Level.SEVERE, null, e);
            }
            getEntityManager().getTransaction().rollback();
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

    public boolean isDeveloperMode() {
        return developerMode;
    }

    public void setDeveloperMode(boolean developerMode) {
        this.developerMode = developerMode;
    }
}
