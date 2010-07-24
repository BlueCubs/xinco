/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLogJpaController {

    public XincoCoreLogJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
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
            XincoCoreUser xincoCoreUser = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreLog.setXincoCoreUser(xincoCoreUser);
            }
            em.persist(xincoCoreLog);
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
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreLog.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreLog.setXincoCoreUser(xincoCoreUserNew);
            }
            xincoCoreLog = em.merge(xincoCoreLog);
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
