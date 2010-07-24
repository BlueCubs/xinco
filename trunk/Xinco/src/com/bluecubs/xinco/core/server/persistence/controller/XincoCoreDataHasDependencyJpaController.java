/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
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

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataHasDependencyJpaController {

    public XincoCoreDataHasDependencyJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataHasDependency xincoCoreDataHasDependency) throws PreexistingEntityException, Exception {
        if (xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK() == null) {
            xincoCoreDataHasDependency.setXincoCoreDataHasDependencyPK(new XincoCoreDataHasDependencyPK());
        }
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataParentId(xincoCoreDataHasDependency.getXincoCoreData1().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataChildrenId(xincoCoreDataHasDependency.getXincoCoreData().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setDependencyTypeId(xincoCoreDataHasDependency.getXincoDependencyType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreData xincoCoreData = xincoCoreDataHasDependency.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData = em.getReference(xincoCoreData.getClass(), xincoCoreData.getId());
                xincoCoreDataHasDependency.setXincoCoreData(xincoCoreData);
            }
            XincoCoreData xincoCoreData1 = xincoCoreDataHasDependency.getXincoCoreData1();
            if (xincoCoreData1 != null) {
                xincoCoreData1 = em.getReference(xincoCoreData1.getClass(), xincoCoreData1.getId());
                xincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData1);
            }
            em.persist(xincoCoreDataHasDependency);
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreData = em.merge(xincoCoreData);
            }
            if (xincoCoreData1 != null) {
                xincoCoreData1.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreData1 = em.merge(xincoCoreData1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataHasDependency(xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK()) != null) {
                throw new PreexistingEntityException("XincoCoreDataHasDependency " + xincoCoreDataHasDependency + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataHasDependency xincoCoreDataHasDependency) throws NonexistentEntityException, Exception {
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataParentId(xincoCoreDataHasDependency.getXincoCoreData1().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataChildrenId(xincoCoreDataHasDependency.getXincoCoreData().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setDependencyTypeId(xincoCoreDataHasDependency.getXincoDependencyType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataHasDependency persistentXincoCoreDataHasDependency = em.find(XincoCoreDataHasDependency.class, xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK());
            XincoCoreData xincoCoreDataOld = persistentXincoCoreDataHasDependency.getXincoCoreData();
            XincoCoreData xincoCoreDataNew = xincoCoreDataHasDependency.getXincoCoreData();
            XincoCoreData xincoCoreData1Old = persistentXincoCoreDataHasDependency.getXincoCoreData1();
            XincoCoreData xincoCoreData1New = xincoCoreDataHasDependency.getXincoCoreData1();
            if (xincoCoreDataNew != null) {
                xincoCoreDataNew = em.getReference(xincoCoreDataNew.getClass(), xincoCoreDataNew.getId());
                xincoCoreDataHasDependency.setXincoCoreData(xincoCoreDataNew);
            }
            if (xincoCoreData1New != null) {
                xincoCoreData1New = em.getReference(xincoCoreData1New.getClass(), xincoCoreData1New.getId());
                xincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData1New);
            }
            xincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependency);
            if (xincoCoreDataOld != null && !xincoCoreDataOld.equals(xincoCoreDataNew)) {
                xincoCoreDataOld.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreDataOld = em.merge(xincoCoreDataOld);
            }
            if (xincoCoreDataNew != null && !xincoCoreDataNew.equals(xincoCoreDataOld)) {
                xincoCoreDataNew.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreDataNew = em.merge(xincoCoreDataNew);
            }
            if (xincoCoreData1Old != null && !xincoCoreData1Old.equals(xincoCoreData1New)) {
                xincoCoreData1Old.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreData1Old = em.merge(xincoCoreData1Old);
            }
            if (xincoCoreData1New != null && !xincoCoreData1New.equals(xincoCoreData1Old)) {
                xincoCoreData1New.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreData1New = em.merge(xincoCoreData1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoCoreDataHasDependencyPK id = xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK();
                if (findXincoCoreDataHasDependency(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataHasDependency with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoCoreDataHasDependencyPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataHasDependency xincoCoreDataHasDependency;
            try {
                xincoCoreDataHasDependency = em.getReference(XincoCoreDataHasDependency.class, id);
                xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataHasDependency with id " + id + " no longer exists.", enfe);
            }
            XincoCoreData xincoCoreData = xincoCoreDataHasDependency.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreData = em.merge(xincoCoreData);
            }
            XincoCoreData xincoCoreData1 = xincoCoreDataHasDependency.getXincoCoreData1();
            if (xincoCoreData1 != null) {
                xincoCoreData1.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreData1 = em.merge(xincoCoreData1);
            }
            em.remove(xincoCoreDataHasDependency);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataHasDependency> findXincoCoreDataHasDependencyEntities() {
        return findXincoCoreDataHasDependencyEntities(true, -1, -1);
    }

    public List<XincoCoreDataHasDependency> findXincoCoreDataHasDependencyEntities(int maxResults, int firstResult) {
        return findXincoCoreDataHasDependencyEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataHasDependency> findXincoCoreDataHasDependencyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataHasDependency.class));
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

    public XincoCoreDataHasDependency findXincoCoreDataHasDependency(XincoCoreDataHasDependencyPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataHasDependency.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataHasDependencyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataHasDependency> rt = cq.from(XincoCoreDataHasDependency.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
