/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyT;
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
public class XincoCoreDataHasDependencyTJpaController implements Serializable {

    public XincoCoreDataHasDependencyTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataHasDependencyT xincoCoreDataHasDependencyT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreDataHasDependencyT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataHasDependencyT(xincoCoreDataHasDependencyT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreDataHasDependencyT " + xincoCoreDataHasDependencyT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataHasDependencyT xincoCoreDataHasDependencyT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreDataHasDependencyT = em.merge(xincoCoreDataHasDependencyT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreDataHasDependencyT.getRecordId();
                if (findXincoCoreDataHasDependencyT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataHasDependencyT with id " + id + " no longer exists.");
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
            XincoCoreDataHasDependencyT xincoCoreDataHasDependencyT;
            try {
                xincoCoreDataHasDependencyT = em.getReference(XincoCoreDataHasDependencyT.class, id);
                xincoCoreDataHasDependencyT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataHasDependencyT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreDataHasDependencyT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataHasDependencyT> findXincoCoreDataHasDependencyTEntities() {
        return findXincoCoreDataHasDependencyTEntities(true, -1, -1);
    }

    public List<XincoCoreDataHasDependencyT> findXincoCoreDataHasDependencyTEntities(int maxResults, int firstResult) {
        return findXincoCoreDataHasDependencyTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataHasDependencyT> findXincoCoreDataHasDependencyTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataHasDependencyT.class));
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

    public XincoCoreDataHasDependencyT findXincoCoreDataHasDependencyT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataHasDependencyT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataHasDependencyTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataHasDependencyT> rt = cq.from(XincoCoreDataHasDependencyT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
