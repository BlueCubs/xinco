/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNodeT;
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
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeTJpaController implements Serializable {

    public XincoCoreNodeTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreNodeT xincoCoreNodeT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreNodeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreNodeT(xincoCoreNodeT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreNodeT " + xincoCoreNodeT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreNodeT xincoCoreNodeT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreNodeT = em.merge(xincoCoreNodeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreNodeT.getRecordId();
                if (findXincoCoreNodeT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreNodeT with id " + id + " no longer exists.");
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
            XincoCoreNodeT xincoCoreNodeT;
            try {
                xincoCoreNodeT = em.getReference(XincoCoreNodeT.class, id);
                xincoCoreNodeT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreNodeT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreNodeT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreNodeT> findXincoCoreNodeTEntities() {
        return findXincoCoreNodeTEntities(true, -1, -1);
    }

    public List<XincoCoreNodeT> findXincoCoreNodeTEntities(int maxResults, int firstResult) {
        return findXincoCoreNodeTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreNodeT> findXincoCoreNodeTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreNodeT.class));
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

    public XincoCoreNodeT findXincoCoreNodeT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreNodeT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreNodeTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreNodeT> rt = cq.from(XincoCoreNodeT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
