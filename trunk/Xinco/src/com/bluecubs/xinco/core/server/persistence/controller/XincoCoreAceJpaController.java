/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreAceJpaController {

    public XincoCoreAceJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreAce xincoCoreAce) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreData xincoCoreDataId = xincoCoreAce.getXincoCoreDataId();
            if (xincoCoreDataId != null) {
                xincoCoreDataId = em.getReference(xincoCoreDataId.getClass(), xincoCoreDataId.getId());
                xincoCoreAce.setXincoCoreDataId(xincoCoreDataId);
            }
            XincoCoreGroup xincoCoreGroupId = xincoCoreAce.getXincoCoreGroupId();
            if (xincoCoreGroupId != null) {
                xincoCoreGroupId = em.getReference(xincoCoreGroupId.getClass(), xincoCoreGroupId.getId());
                xincoCoreAce.setXincoCoreGroupId(xincoCoreGroupId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreAce.getXincoCoreNodeId();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId = em.getReference(xincoCoreNodeId.getClass(), xincoCoreNodeId.getId());
                xincoCoreAce.setXincoCoreNodeId(xincoCoreNodeId);
            }
            XincoCoreUser xincoCoreUserId = xincoCoreAce.getXincoCoreUserId();
            if (xincoCoreUserId != null) {
                xincoCoreUserId = em.getReference(xincoCoreUserId.getClass(), xincoCoreUserId.getId());
                xincoCoreAce.setXincoCoreUserId(xincoCoreUserId);
            }
            em.persist(xincoCoreAce);
            if (xincoCoreDataId != null) {
                xincoCoreDataId.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreDataId = em.merge(xincoCoreDataId);
            }
            if (xincoCoreGroupId != null) {
                xincoCoreGroupId.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreGroupId = em.merge(xincoCoreGroupId);
            }
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            if (xincoCoreUserId != null) {
                xincoCoreUserId.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreUserId = em.merge(xincoCoreUserId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreAce(xincoCoreAce.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreAce " + xincoCoreAce + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreAce xincoCoreAce) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreAce persistentXincoCoreAce = em.find(XincoCoreAce.class, xincoCoreAce.getId());
            XincoCoreData xincoCoreDataIdOld = persistentXincoCoreAce.getXincoCoreDataId();
            XincoCoreData xincoCoreDataIdNew = xincoCoreAce.getXincoCoreDataId();
            XincoCoreGroup xincoCoreGroupIdOld = persistentXincoCoreAce.getXincoCoreGroupId();
            XincoCoreGroup xincoCoreGroupIdNew = xincoCoreAce.getXincoCoreGroupId();
            XincoCoreNode xincoCoreNodeIdOld = persistentXincoCoreAce.getXincoCoreNodeId();
            XincoCoreNode xincoCoreNodeIdNew = xincoCoreAce.getXincoCoreNodeId();
            XincoCoreUser xincoCoreUserIdOld = persistentXincoCoreAce.getXincoCoreUserId();
            XincoCoreUser xincoCoreUserIdNew = xincoCoreAce.getXincoCoreUserId();
            if (xincoCoreDataIdNew != null) {
                xincoCoreDataIdNew = em.getReference(xincoCoreDataIdNew.getClass(), xincoCoreDataIdNew.getId());
                xincoCoreAce.setXincoCoreDataId(xincoCoreDataIdNew);
            }
            if (xincoCoreGroupIdNew != null) {
                xincoCoreGroupIdNew = em.getReference(xincoCoreGroupIdNew.getClass(), xincoCoreGroupIdNew.getId());
                xincoCoreAce.setXincoCoreGroupId(xincoCoreGroupIdNew);
            }
            if (xincoCoreNodeIdNew != null) {
                xincoCoreNodeIdNew = em.getReference(xincoCoreNodeIdNew.getClass(), xincoCoreNodeIdNew.getId());
                xincoCoreAce.setXincoCoreNodeId(xincoCoreNodeIdNew);
            }
            if (xincoCoreUserIdNew != null) {
                xincoCoreUserIdNew = em.getReference(xincoCoreUserIdNew.getClass(), xincoCoreUserIdNew.getId());
                xincoCoreAce.setXincoCoreUserId(xincoCoreUserIdNew);
            }
            xincoCoreAce = em.merge(xincoCoreAce);
            if (xincoCoreDataIdOld != null && !xincoCoreDataIdOld.equals(xincoCoreDataIdNew)) {
                xincoCoreDataIdOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreDataIdOld = em.merge(xincoCoreDataIdOld);
            }
            if (xincoCoreDataIdNew != null && !xincoCoreDataIdNew.equals(xincoCoreDataIdOld)) {
                xincoCoreDataIdNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreDataIdNew = em.merge(xincoCoreDataIdNew);
            }
            if (xincoCoreGroupIdOld != null && !xincoCoreGroupIdOld.equals(xincoCoreGroupIdNew)) {
                xincoCoreGroupIdOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreGroupIdOld = em.merge(xincoCoreGroupIdOld);
            }
            if (xincoCoreGroupIdNew != null && !xincoCoreGroupIdNew.equals(xincoCoreGroupIdOld)) {
                xincoCoreGroupIdNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreGroupIdNew = em.merge(xincoCoreGroupIdNew);
            }
            if (xincoCoreNodeIdOld != null && !xincoCoreNodeIdOld.equals(xincoCoreNodeIdNew)) {
                xincoCoreNodeIdOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreNodeIdOld = em.merge(xincoCoreNodeIdOld);
            }
            if (xincoCoreNodeIdNew != null && !xincoCoreNodeIdNew.equals(xincoCoreNodeIdOld)) {
                xincoCoreNodeIdNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreNodeIdNew = em.merge(xincoCoreNodeIdNew);
            }
            if (xincoCoreUserIdOld != null && !xincoCoreUserIdOld.equals(xincoCoreUserIdNew)) {
                xincoCoreUserIdOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreUserIdOld = em.merge(xincoCoreUserIdOld);
            }
            if (xincoCoreUserIdNew != null && !xincoCoreUserIdNew.equals(xincoCoreUserIdOld)) {
                xincoCoreUserIdNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreUserIdNew = em.merge(xincoCoreUserIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreAce.getId();
                if (findXincoCoreAce(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreAce with id " + id + " no longer exists.");
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
            XincoCoreAce xincoCoreAce;
            try {
                xincoCoreAce = em.getReference(XincoCoreAce.class, id);
                xincoCoreAce.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreAce with id " + id + " no longer exists.", enfe);
            }
            XincoCoreData xincoCoreDataId = xincoCoreAce.getXincoCoreDataId();
            if (xincoCoreDataId != null) {
                xincoCoreDataId.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreDataId = em.merge(xincoCoreDataId);
            }
            XincoCoreGroup xincoCoreGroupId = xincoCoreAce.getXincoCoreGroupId();
            if (xincoCoreGroupId != null) {
                xincoCoreGroupId.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreGroupId = em.merge(xincoCoreGroupId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreAce.getXincoCoreNodeId();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            XincoCoreUser xincoCoreUserId = xincoCoreAce.getXincoCoreUserId();
            if (xincoCoreUserId != null) {
                xincoCoreUserId.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreUserId = em.merge(xincoCoreUserId);
            }
            em.remove(xincoCoreAce);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreAce> findXincoCoreAceEntities() {
        return findXincoCoreAceEntities(true, -1, -1);
    }

    public List<XincoCoreAce> findXincoCoreAceEntities(int maxResults, int firstResult) {
        return findXincoCoreAceEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreAce> findXincoCoreAceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreAce.class));
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

    public XincoCoreAce findXincoCoreAce(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreAce.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreAceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreAce> rt = cq.from(XincoCoreAce.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
