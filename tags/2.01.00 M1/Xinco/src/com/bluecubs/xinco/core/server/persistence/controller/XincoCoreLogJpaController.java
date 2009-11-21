/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLogJpaController {

    public XincoCoreLogJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreLog xincoCoreLog) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreData xincoCoreDataId = xincoCoreLog.getXincoCoreDataId();
            if (xincoCoreDataId != null) {
                xincoCoreDataId = em.getReference(xincoCoreDataId.getClass(), xincoCoreDataId.getId());
                xincoCoreLog.setXincoCoreDataId(xincoCoreDataId);
            }
            XincoCoreUser xincoCoreUserId = xincoCoreLog.getXincoCoreUserId();
            if (xincoCoreUserId != null) {
                xincoCoreUserId = em.getReference(xincoCoreUserId.getClass(), xincoCoreUserId.getId());
                xincoCoreLog.setXincoCoreUserId(xincoCoreUserId);
            }
            em.persist(xincoCoreLog);
            if (xincoCoreDataId != null) {
                xincoCoreDataId.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreDataId = em.merge(xincoCoreDataId);
            }
            if (xincoCoreUserId != null) {
                xincoCoreUserId.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreUserId = em.merge(xincoCoreUserId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreLog(xincoCoreLog.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreLog " + xincoCoreLog + " already exists.", ex);
            }
            throw ex;
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
            XincoCoreData xincoCoreDataIdOld = persistentXincoCoreLog.getXincoCoreDataId();
            XincoCoreData xincoCoreDataIdNew = xincoCoreLog.getXincoCoreDataId();
            XincoCoreUser xincoCoreUserIdOld = persistentXincoCoreLog.getXincoCoreUserId();
            XincoCoreUser xincoCoreUserIdNew = xincoCoreLog.getXincoCoreUserId();
            if (xincoCoreDataIdNew != null) {
                xincoCoreDataIdNew = em.getReference(xincoCoreDataIdNew.getClass(), xincoCoreDataIdNew.getId());
                xincoCoreLog.setXincoCoreDataId(xincoCoreDataIdNew);
            }
            if (xincoCoreUserIdNew != null) {
                xincoCoreUserIdNew = em.getReference(xincoCoreUserIdNew.getClass(), xincoCoreUserIdNew.getId());
                xincoCoreLog.setXincoCoreUserId(xincoCoreUserIdNew);
            }
            xincoCoreLog = em.merge(xincoCoreLog);
            if (xincoCoreDataIdOld != null && !xincoCoreDataIdOld.equals(xincoCoreDataIdNew)) {
                xincoCoreDataIdOld.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreDataIdOld = em.merge(xincoCoreDataIdOld);
            }
            if (xincoCoreDataIdNew != null && !xincoCoreDataIdNew.equals(xincoCoreDataIdOld)) {
                xincoCoreDataIdNew.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreDataIdNew = em.merge(xincoCoreDataIdNew);
            }
            if (xincoCoreUserIdOld != null && !xincoCoreUserIdOld.equals(xincoCoreUserIdNew)) {
                xincoCoreUserIdOld.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreUserIdOld = em.merge(xincoCoreUserIdOld);
            }
            if (xincoCoreUserIdNew != null && !xincoCoreUserIdNew.equals(xincoCoreUserIdOld)) {
                xincoCoreUserIdNew.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreUserIdNew = em.merge(xincoCoreUserIdNew);
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
            XincoCoreData xincoCoreDataId = xincoCoreLog.getXincoCoreDataId();
            if (xincoCoreDataId != null) {
                xincoCoreDataId.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreDataId = em.merge(xincoCoreDataId);
            }
            XincoCoreUser xincoCoreUserId = xincoCoreLog.getXincoCoreUserId();
            if (xincoCoreUserId != null) {
                xincoCoreUserId.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreUserId = em.merge(xincoCoreUserId);
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
