/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroupT;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoCoreGroupTJpaController implements Serializable {

    public XincoCoreGroupTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreGroupT xincoCoreGroupT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreGroupT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreGroupT(xincoCoreGroupT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreGroupT " + xincoCoreGroupT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreGroupT xincoCoreGroupT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreGroupT = em.merge(xincoCoreGroupT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreGroupT.getRecordId();
                if (findXincoCoreGroupT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreGroupT with id " + id + " no longer exists.");
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
            XincoCoreGroupT xincoCoreGroupT;
            try {
                xincoCoreGroupT = em.getReference(XincoCoreGroupT.class, id);
                xincoCoreGroupT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreGroupT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreGroupT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreGroupT> findXincoCoreGroupTEntities() {
        return findXincoCoreGroupTEntities(true, -1, -1);
    }

    public List<XincoCoreGroupT> findXincoCoreGroupTEntities(int maxResults, int firstResult) {
        return findXincoCoreGroupTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreGroupT> findXincoCoreGroupTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreGroupT.class));
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

    public XincoCoreGroupT findXincoCoreGroupT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreGroupT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreGroupTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreGroupT> rt = cq.from(XincoCoreGroupT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}