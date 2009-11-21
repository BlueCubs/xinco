/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeT;
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

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeTJpaController {

    public XincoCoreDataTypeTJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataTypeT xincoCoreDataTypeT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreDataTypeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataTypeT(xincoCoreDataTypeT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreDataTypeT " + xincoCoreDataTypeT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataTypeT xincoCoreDataTypeT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreDataTypeT = em.merge(xincoCoreDataTypeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreDataTypeT.getRecordId();
                if (findXincoCoreDataTypeT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataTypeT with id " + id + " no longer exists.");
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
            XincoCoreDataTypeT xincoCoreDataTypeT;
            try {
                xincoCoreDataTypeT = em.getReference(XincoCoreDataTypeT.class, id);
                xincoCoreDataTypeT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataTypeT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreDataTypeT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataTypeT> findXincoCoreDataTypeTEntities() {
        return findXincoCoreDataTypeTEntities(true, -1, -1);
    }

    public List<XincoCoreDataTypeT> findXincoCoreDataTypeTEntities(int maxResults, int firstResult) {
        return findXincoCoreDataTypeTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataTypeT> findXincoCoreDataTypeTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataTypeT.class));
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

    public XincoCoreDataTypeT findXincoCoreDataTypeT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataTypeT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataTypeTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataTypeT> rt = cq.from(XincoCoreDataTypeT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
