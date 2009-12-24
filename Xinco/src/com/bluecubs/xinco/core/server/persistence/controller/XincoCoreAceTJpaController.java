/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreAceT;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreAceTJpaController {

    public XincoCoreAceTJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreAceT xincoCoreAceT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreAceT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreAceT(xincoCoreAceT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreAceT " + xincoCoreAceT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreAceT xincoCoreAceT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreAceT = em.merge(xincoCoreAceT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreAceT.getRecordId();
                if (findXincoCoreAceT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreAceT with id " + id + " no longer exists.");
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
            XincoCoreAceT xincoCoreAceT;
            try {
                xincoCoreAceT = em.getReference(XincoCoreAceT.class, id);
                xincoCoreAceT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreAceT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreAceT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreAceT> findXincoCoreAceTEntities() {
        return findXincoCoreAceTEntities(true, -1, -1);
    }

    public List<XincoCoreAceT> findXincoCoreAceTEntities(int maxResults, int firstResult) {
        return findXincoCoreAceTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreAceT> findXincoCoreAceTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreAceT.class));
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

    public XincoCoreAceT findXincoCoreAceT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreAceT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreAceTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreAceT> rt = cq.from(XincoCoreAceT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
