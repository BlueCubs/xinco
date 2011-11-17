/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
public class XincoDependencyBehaviorJpaController implements Serializable {

    public XincoDependencyBehaviorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyBehavior xincoDependencyBehavior) {
        if (xincoDependencyBehavior.getXincoDependencyTypeCollection() == null) {
            xincoDependencyBehavior.setXincoDependencyTypeCollection(new ArrayList<XincoDependencyType>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<XincoDependencyType> attachedXincoDependencyTypeCollection = new ArrayList<XincoDependencyType>();
            for (XincoDependencyType xincoDependencyTypeCollectionXincoDependencyTypeToAttach : xincoDependencyBehavior.getXincoDependencyTypeCollection()) {
                xincoDependencyTypeCollectionXincoDependencyTypeToAttach = em.getReference(xincoDependencyTypeCollectionXincoDependencyTypeToAttach.getClass(), xincoDependencyTypeCollectionXincoDependencyTypeToAttach.getId());
                attachedXincoDependencyTypeCollection.add(xincoDependencyTypeCollectionXincoDependencyTypeToAttach);
            }
            xincoDependencyBehavior.setXincoDependencyTypeCollection(attachedXincoDependencyTypeCollection);
            em.persist(xincoDependencyBehavior);
            for (XincoDependencyType xincoDependencyTypeCollectionXincoDependencyType : xincoDependencyBehavior.getXincoDependencyTypeCollection()) {
                XincoDependencyBehavior oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionXincoDependencyType = xincoDependencyTypeCollectionXincoDependencyType.getXincoDependencyBehavior();
                xincoDependencyTypeCollectionXincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
                xincoDependencyTypeCollectionXincoDependencyType = em.merge(xincoDependencyTypeCollectionXincoDependencyType);
                if (oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionXincoDependencyType != null) {
                    oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionXincoDependencyType.getXincoDependencyTypeCollection().remove(xincoDependencyTypeCollectionXincoDependencyType);
                    oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionXincoDependencyType = em.merge(oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionXincoDependencyType);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoDependencyBehavior xincoDependencyBehavior) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyBehavior persistentXincoDependencyBehavior = em.find(XincoDependencyBehavior.class, xincoDependencyBehavior.getId());
            Collection<XincoDependencyType> xincoDependencyTypeCollectionOld = persistentXincoDependencyBehavior.getXincoDependencyTypeCollection();
            Collection<XincoDependencyType> xincoDependencyTypeCollectionNew = xincoDependencyBehavior.getXincoDependencyTypeCollection();
            List<String> illegalOrphanMessages = null;
            for (XincoDependencyType xincoDependencyTypeCollectionOldXincoDependencyType : xincoDependencyTypeCollectionOld) {
                if (!xincoDependencyTypeCollectionNew.contains(xincoDependencyTypeCollectionOldXincoDependencyType)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoDependencyType " + xincoDependencyTypeCollectionOldXincoDependencyType + " since its xincoDependencyBehavior field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<XincoDependencyType> attachedXincoDependencyTypeCollectionNew = new ArrayList<XincoDependencyType>();
            for (XincoDependencyType xincoDependencyTypeCollectionNewXincoDependencyTypeToAttach : xincoDependencyTypeCollectionNew) {
                xincoDependencyTypeCollectionNewXincoDependencyTypeToAttach = em.getReference(xincoDependencyTypeCollectionNewXincoDependencyTypeToAttach.getClass(), xincoDependencyTypeCollectionNewXincoDependencyTypeToAttach.getId());
                attachedXincoDependencyTypeCollectionNew.add(xincoDependencyTypeCollectionNewXincoDependencyTypeToAttach);
            }
            xincoDependencyTypeCollectionNew = attachedXincoDependencyTypeCollectionNew;
            xincoDependencyBehavior.setXincoDependencyTypeCollection(xincoDependencyTypeCollectionNew);
            xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            for (XincoDependencyType xincoDependencyTypeCollectionNewXincoDependencyType : xincoDependencyTypeCollectionNew) {
                if (!xincoDependencyTypeCollectionOld.contains(xincoDependencyTypeCollectionNewXincoDependencyType)) {
                    XincoDependencyBehavior oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionNewXincoDependencyType = xincoDependencyTypeCollectionNewXincoDependencyType.getXincoDependencyBehavior();
                    xincoDependencyTypeCollectionNewXincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
                    xincoDependencyTypeCollectionNewXincoDependencyType = em.merge(xincoDependencyTypeCollectionNewXincoDependencyType);
                    if (oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionNewXincoDependencyType != null && !oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionNewXincoDependencyType.equals(xincoDependencyBehavior)) {
                        oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionNewXincoDependencyType.getXincoDependencyTypeCollection().remove(xincoDependencyTypeCollectionNewXincoDependencyType);
                        oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionNewXincoDependencyType = em.merge(oldXincoDependencyBehaviorOfXincoDependencyTypeCollectionNewXincoDependencyType);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoDependencyBehavior.getId();
                if (findXincoDependencyBehavior(id) == null) {
                    throw new NonexistentEntityException("The xincoDependencyBehavior with id " + id + " no longer exists.");
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
            XincoDependencyBehavior xincoDependencyBehavior;
            try {
                xincoDependencyBehavior = em.getReference(XincoDependencyBehavior.class, id);
                xincoDependencyBehavior.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoDependencyBehavior with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<XincoDependencyType> xincoDependencyTypeCollectionOrphanCheck = xincoDependencyBehavior.getXincoDependencyTypeCollection();
            for (XincoDependencyType xincoDependencyTypeCollectionOrphanCheckXincoDependencyType : xincoDependencyTypeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoDependencyBehavior (" + xincoDependencyBehavior + ") cannot be destroyed since the XincoDependencyType " + xincoDependencyTypeCollectionOrphanCheckXincoDependencyType + " in its xincoDependencyTypeCollection field has a non-nullable xincoDependencyBehavior field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xincoDependencyBehavior);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoDependencyBehavior> findXincoDependencyBehaviorEntities() {
        return findXincoDependencyBehaviorEntities(true, -1, -1);
    }

    public List<XincoDependencyBehavior> findXincoDependencyBehaviorEntities(int maxResults, int firstResult) {
        return findXincoDependencyBehaviorEntities(false, maxResults, firstResult);
    }

    private List<XincoDependencyBehavior> findXincoDependencyBehaviorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoDependencyBehavior.class));
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

    public XincoDependencyBehavior findXincoDependencyBehavior(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoDependencyBehavior.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoDependencyBehaviorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoDependencyBehavior> rt = cq.from(XincoDependencyBehavior.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
