package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoDependencyTypeJpaController {

    public XincoDependencyTypeJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyType xincoDependencyType) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyBehavior xincoDependencyBehavior = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior = em.getReference(xincoDependencyBehavior.getClass(), xincoDependencyBehavior.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
            }
            em.persist(xincoDependencyType);
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior.getXincoDependencyTypeList().add(xincoDependencyType);
                xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoDependencyType xincoDependencyType) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyType persistentXincoDependencyType = em.find(XincoDependencyType.class, xincoDependencyType.getId());
            XincoDependencyBehavior xincoDependencyBehaviorOld = persistentXincoDependencyType.getXincoDependencyBehavior();
            XincoDependencyBehavior xincoDependencyBehaviorNew = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehaviorNew != null) {
                xincoDependencyBehaviorNew = em.getReference(xincoDependencyBehaviorNew.getClass(), xincoDependencyBehaviorNew.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehaviorNew);
            }
            xincoDependencyType = em.merge(xincoDependencyType);
            if (xincoDependencyBehaviorOld != null && !xincoDependencyBehaviorOld.equals(xincoDependencyBehaviorNew)) {
                xincoDependencyBehaviorOld.getXincoDependencyTypeList().remove(xincoDependencyType);
                xincoDependencyBehaviorOld = em.merge(xincoDependencyBehaviorOld);
            }
            if (xincoDependencyBehaviorNew != null && !xincoDependencyBehaviorNew.equals(xincoDependencyBehaviorOld)) {
                xincoDependencyBehaviorNew.getXincoDependencyTypeList().add(xincoDependencyType);
                xincoDependencyBehaviorNew = em.merge(xincoDependencyBehaviorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoDependencyType.getId();
                if (findXincoDependencyType(id) == null) {
                    throw new NonexistentEntityException("The xincoDependencyType with id " + id + " no longer exists.");
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
            XincoDependencyType xincoDependencyType;
            try {
                xincoDependencyType = em.getReference(XincoDependencyType.class, id);
                xincoDependencyType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoDependencyType with id " + id + " no longer exists.", enfe);
            }
            XincoDependencyBehavior xincoDependencyBehavior = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior.getXincoDependencyTypeList().remove(xincoDependencyType);
                xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            }
            em.remove(xincoDependencyType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoDependencyType> findXincoDependencyTypeEntities() {
        return findXincoDependencyTypeEntities(true, -1, -1);
    }

    public List<XincoDependencyType> findXincoDependencyTypeEntities(int maxResults, int firstResult) {
        return findXincoDependencyTypeEntities(false, maxResults, firstResult);
    }

    private List<XincoDependencyType> findXincoDependencyTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoDependencyType.class));
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

    public XincoDependencyType findXincoDependencyType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoDependencyType.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoDependencyTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoDependencyType> rt = cq.from(XincoDependencyType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
