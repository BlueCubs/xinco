/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoAddAttributeT;
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoAddAttributeTJpaController {

    public XincoAddAttributeTJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoAddAttributeT xincoAddAttributeT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoAddAttributeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoAddAttributeT(xincoAddAttributeT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoAddAttributeT " + xincoAddAttributeT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoAddAttributeT xincoAddAttributeT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoAddAttributeT = em.merge(xincoAddAttributeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoAddAttributeT.getRecordId();
                if (findXincoAddAttributeT(id) == null) {
                    throw new NonexistentEntityException("The xincoAddAttributeT with id " + id + " no longer exists.");
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
            XincoAddAttributeT xincoAddAttributeT;
            try {
                xincoAddAttributeT = em.getReference(XincoAddAttributeT.class, id);
                xincoAddAttributeT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoAddAttributeT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoAddAttributeT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoAddAttributeT> findXincoAddAttributeTEntities() {
        return findXincoAddAttributeTEntities(true, -1, -1);
    }

    public List<XincoAddAttributeT> findXincoAddAttributeTEntities(int maxResults, int firstResult) {
        return findXincoAddAttributeTEntities(false, maxResults, firstResult);
    }

    private List<XincoAddAttributeT> findXincoAddAttributeTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoAddAttributeT.class));
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

    public XincoAddAttributeT findXincoAddAttributeT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoAddAttributeT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoAddAttributeTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoAddAttributeT> rt = cq.from(XincoAddAttributeT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
