/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
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

    public void create(XincoCoreAce xincoCoreAce) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreGroup xincoCoreGroup = xincoCoreAce.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup = em.getReference(xincoCoreGroup.getClass(), xincoCoreGroup.getId());
                xincoCoreAce.setXincoCoreGroup(xincoCoreGroup);
            }
            XincoCoreUser xincoCoreUser = xincoCoreAce.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreAce.setXincoCoreUser(xincoCoreUser);
            }
            em.persist(xincoCoreAce);
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.getTransaction().commit();
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
            XincoCoreGroup xincoCoreGroupOld = persistentXincoCoreAce.getXincoCoreGroup();
            XincoCoreGroup xincoCoreGroupNew = xincoCoreAce.getXincoCoreGroup();
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreAce.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreAce.getXincoCoreUser();
            if (xincoCoreGroupNew != null) {
                xincoCoreGroupNew = em.getReference(xincoCoreGroupNew.getClass(), xincoCoreGroupNew.getId());
                xincoCoreAce.setXincoCoreGroup(xincoCoreGroupNew);
            }
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreAce.setXincoCoreUser(xincoCoreUserNew);
            }
            xincoCoreAce = em.merge(xincoCoreAce);
            if (xincoCoreGroupOld != null && !xincoCoreGroupOld.equals(xincoCoreGroupNew)) {
                xincoCoreGroupOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreGroupOld = em.merge(xincoCoreGroupOld);
            }
            if (xincoCoreGroupNew != null && !xincoCoreGroupNew.equals(xincoCoreGroupOld)) {
                xincoCoreGroupNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreGroupNew = em.merge(xincoCoreGroupNew);
            }
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
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
            XincoCoreGroup xincoCoreGroup = xincoCoreAce.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            XincoCoreUser xincoCoreUser = xincoCoreAce.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreUser = em.merge(xincoCoreUser);
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
