/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupT;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupTJpaController implements Serializable {

    public XincoCoreUserHasXincoCoreGroupTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUserHasXincoCoreGroupT xincoCoreUserHasXincoCoreGroupT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreUserHasXincoCoreGroupT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreUserHasXincoCoreGroupT(xincoCoreUserHasXincoCoreGroupT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreUserHasXincoCoreGroupT " + xincoCoreUserHasXincoCoreGroupT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreUserHasXincoCoreGroupT xincoCoreUserHasXincoCoreGroupT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreUserHasXincoCoreGroupT = em.merge(xincoCoreUserHasXincoCoreGroupT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreUserHasXincoCoreGroupT.getRecordId();
                if (findXincoCoreUserHasXincoCoreGroupT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreUserHasXincoCoreGroupT with id " + id + " no longer exists.");
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
            XincoCoreUserHasXincoCoreGroupT xincoCoreUserHasXincoCoreGroupT;
            try {
                xincoCoreUserHasXincoCoreGroupT = em.getReference(XincoCoreUserHasXincoCoreGroupT.class, id);
                xincoCoreUserHasXincoCoreGroupT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreUserHasXincoCoreGroupT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreUserHasXincoCoreGroupT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreUserHasXincoCoreGroupT> findXincoCoreUserHasXincoCoreGroupTEntities() {
        return findXincoCoreUserHasXincoCoreGroupTEntities(true, -1, -1);
    }

    public List<XincoCoreUserHasXincoCoreGroupT> findXincoCoreUserHasXincoCoreGroupTEntities(int maxResults, int firstResult) {
        return findXincoCoreUserHasXincoCoreGroupTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreUserHasXincoCoreGroupT> findXincoCoreUserHasXincoCoreGroupTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreUserHasXincoCoreGroupT.class));
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

    public XincoCoreUserHasXincoCoreGroupT findXincoCoreUserHasXincoCoreGroupT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreUserHasXincoCoreGroupT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreUserHasXincoCoreGroupTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreUserHasXincoCoreGroupT> rt = cq.from(XincoCoreUserHasXincoCoreGroupT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
