/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoCoreLogJpaController implements Serializable {

    public XincoCoreLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreLog xincoCoreLog) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreData xincoCoreData = xincoCoreLog.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData = em.getReference(xincoCoreData.getClass(), xincoCoreData.getId());
                xincoCoreLog.setXincoCoreData(xincoCoreData);
            }
            XincoCoreUser xincoCoreUser = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreLog.setXincoCoreUser(xincoCoreUser);
            }
            em.persist(xincoCoreLog);
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreData = em.merge(xincoCoreData);
            }
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreLog xincoCoreLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLog persistentXincoCoreLog = em.find(XincoCoreLog.class, xincoCoreLog.getId());
            XincoCoreData xincoCoreDataOld = persistentXincoCoreLog.getXincoCoreData();
            XincoCoreData xincoCoreDataNew = xincoCoreLog.getXincoCoreData();
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreLog.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreDataNew != null) {
                xincoCoreDataNew = em.getReference(xincoCoreDataNew.getClass(), xincoCoreDataNew.getId());
                xincoCoreLog.setXincoCoreData(xincoCoreDataNew);
            }
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreLog.setXincoCoreUser(xincoCoreUserNew);
            }
            xincoCoreLog = em.merge(xincoCoreLog);
            if (xincoCoreDataOld != null && !xincoCoreDataOld.equals(xincoCoreDataNew)) {
                xincoCoreDataOld.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreDataOld = em.merge(xincoCoreDataOld);
            }
            if (xincoCoreDataNew != null && !xincoCoreDataNew.equals(xincoCoreDataOld)) {
                xincoCoreDataNew.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreDataNew = em.merge(xincoCoreDataNew);
            }
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreLog.getId();
                if (findXincoCoreLog(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreLog with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLog xincoCoreLog;
            try {
                xincoCoreLog = em.getReference(XincoCoreLog.class, id);
                xincoCoreLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreLog with id " + id + " no longer exists.", enfe);
            }
            XincoCoreData xincoCoreData = xincoCoreLog.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreData = em.merge(xincoCoreData);
            }
            XincoCoreUser xincoCoreUser = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.remove(xincoCoreLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreLog> findXincoCoreLogEntities() {
        return findXincoCoreLogEntities(true, -1, -1);
    }

    public List<XincoCoreLog> findXincoCoreLogEntities(int maxResults, int firstResult) {
        return findXincoCoreLogEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreLog> findXincoCoreLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreLog.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public XincoCoreLog findXincoCoreLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreLog> rt = cq.from(XincoCoreLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
