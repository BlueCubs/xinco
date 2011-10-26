/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoDependencyTypeJpaController implements Serializable {

    public XincoDependencyTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyType xincoDependencyType) {
        if (xincoDependencyType.getXincoCoreDataHasDependencyCollection() == null) {
            xincoDependencyType.setXincoCoreDataHasDependencyCollection(new ArrayList<XincoCoreDataHasDependency>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyBehavior xincoDependencyBehavior = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior = em.getReference(xincoDependencyBehavior.getClass(), xincoDependencyBehavior.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
            }
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollection = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach : xincoDependencyType.getXincoCoreDataHasDependencyCollection()) {
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollection.add(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach);
            }
            xincoDependencyType.setXincoCoreDataHasDependencyCollection(attachedXincoCoreDataHasDependencyCollection);
            em.persist(xincoDependencyType);
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior.getXincoDependencyTypeCollection().add(xincoDependencyType);
                xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency : xincoDependencyType.getXincoCoreDataHasDependencyCollection()) {
                XincoDependencyType oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency = xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency.getXincoDependencyType();
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency.setXincoDependencyType(xincoDependencyType);
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency);
                if (oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency != null) {
                    oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency.getXincoCoreDataHasDependencyCollection().remove(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency);
                    oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency = em.merge(oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoDependencyType xincoDependencyType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyType persistentXincoDependencyType = em.find(XincoDependencyType.class, xincoDependencyType.getId());
            XincoDependencyBehavior xincoDependencyBehaviorOld = persistentXincoDependencyType.getXincoDependencyBehavior();
            XincoDependencyBehavior xincoDependencyBehaviorNew = xincoDependencyType.getXincoDependencyBehavior();
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollectionOld = persistentXincoDependencyType.getXincoCoreDataHasDependencyCollection();
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollectionNew = xincoDependencyType.getXincoCoreDataHasDependencyCollection();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionOldXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollectionOld) {
                if (!xincoCoreDataHasDependencyCollectionNew.contains(xincoCoreDataHasDependencyCollectionOldXincoCoreDataHasDependency)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataHasDependency " + xincoCoreDataHasDependencyCollectionOldXincoCoreDataHasDependency + " since its xincoDependencyType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoDependencyBehaviorNew != null) {
                xincoDependencyBehaviorNew = em.getReference(xincoDependencyBehaviorNew.getClass(), xincoDependencyBehaviorNew.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehaviorNew);
            }
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollectionNew = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyCollectionNew) {
                xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollectionNew.add(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyCollectionNew = attachedXincoCoreDataHasDependencyCollectionNew;
            xincoDependencyType.setXincoCoreDataHasDependencyCollection(xincoCoreDataHasDependencyCollectionNew);
            xincoDependencyType = em.merge(xincoDependencyType);
            if (xincoDependencyBehaviorOld != null && !xincoDependencyBehaviorOld.equals(xincoDependencyBehaviorNew)) {
                xincoDependencyBehaviorOld.getXincoDependencyTypeCollection().remove(xincoDependencyType);
                xincoDependencyBehaviorOld = em.merge(xincoDependencyBehaviorOld);
            }
            if (xincoDependencyBehaviorNew != null && !xincoDependencyBehaviorNew.equals(xincoDependencyBehaviorOld)) {
                xincoDependencyBehaviorNew.getXincoDependencyTypeCollection().add(xincoDependencyType);
                xincoDependencyBehaviorNew = em.merge(xincoDependencyBehaviorNew);
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollectionNew) {
                if (!xincoCoreDataHasDependencyCollectionOld.contains(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency)) {
                    XincoDependencyType oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency = xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.getXincoDependencyType();
                    xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.setXincoDependencyType(xincoDependencyType);
                    xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency);
                    if (oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency != null && !oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.equals(xincoDependencyType)) {
                        oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.getXincoCoreDataHasDependencyCollection().remove(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency);
                        oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency = em.merge(oldXincoDependencyTypeOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency);
                    }
                }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollectionOrphanCheck = xincoDependencyType.getXincoCoreDataHasDependencyCollection();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionOrphanCheckXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoDependencyType (" + xincoDependencyType + ") cannot be destroyed since the XincoCoreDataHasDependency " + xincoCoreDataHasDependencyCollectionOrphanCheckXincoCoreDataHasDependency + " in its xincoCoreDataHasDependencyCollection field has a non-nullable xincoDependencyType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoDependencyBehavior xincoDependencyBehavior = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior.getXincoDependencyTypeCollection().remove(xincoDependencyType);
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
