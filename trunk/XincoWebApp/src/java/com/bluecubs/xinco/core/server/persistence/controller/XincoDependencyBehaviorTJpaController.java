/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehaviorT;
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
public class XincoDependencyBehaviorTJpaController implements Serializable {

    public XincoDependencyBehaviorTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyBehaviorT xincoDependencyBehaviorT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoDependencyBehaviorT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoDependencyBehaviorT(xincoDependencyBehaviorT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoDependencyBehaviorT " + xincoDependencyBehaviorT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoDependencyBehaviorT xincoDependencyBehaviorT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoDependencyBehaviorT = em.merge(xincoDependencyBehaviorT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoDependencyBehaviorT.getRecordId();
                if (findXincoDependencyBehaviorT(id) == null) {
                    throw new NonexistentEntityException("The xincoDependencyBehaviorT with id " + id + " no longer exists.");
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
            XincoDependencyBehaviorT xincoDependencyBehaviorT;
            try {
                xincoDependencyBehaviorT = em.getReference(XincoDependencyBehaviorT.class, id);
                xincoDependencyBehaviorT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoDependencyBehaviorT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoDependencyBehaviorT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoDependencyBehaviorT> findXincoDependencyBehaviorTEntities() {
        return findXincoDependencyBehaviorTEntities(true, -1, -1);
    }

    public List<XincoDependencyBehaviorT> findXincoDependencyBehaviorTEntities(int maxResults, int firstResult) {
        return findXincoDependencyBehaviorTEntities(false, maxResults, firstResult);
    }

    private List<XincoDependencyBehaviorT> findXincoDependencyBehaviorTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoDependencyBehaviorT.class));
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

    public XincoDependencyBehaviorT findXincoDependencyBehaviorT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoDependencyBehaviorT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoDependencyBehaviorTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoDependencyBehaviorT> rt = cq.from(XincoDependencyBehaviorT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
