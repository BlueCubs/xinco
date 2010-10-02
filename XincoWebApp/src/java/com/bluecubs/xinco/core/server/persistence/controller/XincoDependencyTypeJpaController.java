/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
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
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoDependencyTypeJpaController implements Serializable {

    public XincoDependencyTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyType xincoDependencyType) throws PreexistingEntityException, Exception {
        if (xincoDependencyType.getXincoCoreDataHasDependencyCollection() == null) {
            xincoDependencyType.setXincoCoreDataHasDependencyCollection(new ArrayList<XincoCoreDataHasDependency>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyBehavior xincoDependencyBehaviorId = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehaviorId != null) {
                xincoDependencyBehaviorId = em.getReference(xincoDependencyBehaviorId.getClass(), xincoDependencyBehaviorId.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehaviorId);
            }
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollection = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach : xincoDependencyType.getXincoCoreDataHasDependencyCollection()) {
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollection.add(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach);
            }
            xincoDependencyType.setXincoCoreDataHasDependencyCollection(attachedXincoCoreDataHasDependencyCollection);
            em.persist(xincoDependencyType);
            if (xincoDependencyBehaviorId != null) {
                xincoDependencyBehaviorId.getXincoDependencyTypeCollection().add(xincoDependencyType);
                xincoDependencyBehaviorId = em.merge(xincoDependencyBehaviorId);
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
        } catch (Exception ex) {
            if (findXincoDependencyType(xincoDependencyType.getId()) != null) {
                throw new PreexistingEntityException("XincoDependencyType " + xincoDependencyType + " already exists.", ex);
            }
            throw ex;
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
            XincoDependencyBehavior xincoDependencyBehaviorIdOld = persistentXincoDependencyType.getXincoDependencyBehavior();
            XincoDependencyBehavior xincoDependencyBehaviorIdNew = xincoDependencyType.getXincoDependencyBehavior();
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
            if (xincoDependencyBehaviorIdNew != null) {
                xincoDependencyBehaviorIdNew = em.getReference(xincoDependencyBehaviorIdNew.getClass(), xincoDependencyBehaviorIdNew.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehaviorIdNew);
            }
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollectionNew = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyCollectionNew) {
                xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollectionNew.add(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyCollectionNew = attachedXincoCoreDataHasDependencyCollectionNew;
            xincoDependencyType.setXincoCoreDataHasDependencyCollection(xincoCoreDataHasDependencyCollectionNew);
            xincoDependencyType = em.merge(xincoDependencyType);
            if (xincoDependencyBehaviorIdOld != null && !xincoDependencyBehaviorIdOld.equals(xincoDependencyBehaviorIdNew)) {
                xincoDependencyBehaviorIdOld.getXincoDependencyTypeCollection().remove(xincoDependencyType);
                xincoDependencyBehaviorIdOld = em.merge(xincoDependencyBehaviorIdOld);
            }
            if (xincoDependencyBehaviorIdNew != null && !xincoDependencyBehaviorIdNew.equals(xincoDependencyBehaviorIdOld)) {
                xincoDependencyBehaviorIdNew.getXincoDependencyTypeCollection().add(xincoDependencyType);
                xincoDependencyBehaviorIdNew = em.merge(xincoDependencyBehaviorIdNew);
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
            XincoDependencyBehavior xincoDependencyBehaviorId = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehaviorId != null) {
                xincoDependencyBehaviorId.getXincoDependencyTypeCollection().remove(xincoDependencyType);
                xincoDependencyBehaviorId = em.merge(xincoDependencyBehaviorId);
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
