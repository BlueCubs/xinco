/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
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
public class XincoCoreLanguageJpaController implements Serializable {

    public XincoCoreLanguageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreLanguage xincoCoreLanguage) {
        if (xincoCoreLanguage.getXincoCoreNodeList() == null) {
            xincoCoreLanguage.setXincoCoreNodeList(new ArrayList<XincoCoreNode>());
        }
        if (xincoCoreLanguage.getXincoCoreDataList() == null) {
            xincoCoreLanguage.setXincoCoreDataList(new ArrayList<XincoCoreData>());
        }
        if (xincoCoreLanguage.getXincoCoreNodeCollection() == null) {
            xincoCoreLanguage.setXincoCoreNodeCollection(new ArrayList<XincoCoreNode>());
        }
        if (xincoCoreLanguage.getXincoCoreDataCollection() == null) {
            xincoCoreLanguage.setXincoCoreDataCollection(new ArrayList<XincoCoreData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoCoreNode> attachedXincoCoreNodeList = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNodeToAttach : xincoCoreLanguage.getXincoCoreNodeList()) {
                xincoCoreNodeListXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListXincoCoreNodeToAttach.getClass(), xincoCoreNodeListXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeList.add(xincoCoreNodeListXincoCoreNodeToAttach);
            }
            xincoCoreLanguage.setXincoCoreNodeList(attachedXincoCoreNodeList);
            List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach : xincoCoreLanguage.getXincoCoreDataList()) {
                xincoCoreDataListXincoCoreDataToAttach = em.getReference(xincoCoreDataListXincoCoreDataToAttach.getClass(), xincoCoreDataListXincoCoreDataToAttach.getId());
                attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
            }
            xincoCoreLanguage.setXincoCoreDataList(attachedXincoCoreDataList);
            Collection<XincoCoreNode> attachedXincoCoreNodeCollection = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNodeToAttach : xincoCoreLanguage.getXincoCoreNodeCollection()) {
                xincoCoreNodeCollectionXincoCoreNodeToAttach = em.getReference(xincoCoreNodeCollectionXincoCoreNodeToAttach.getClass(), xincoCoreNodeCollectionXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeCollection.add(xincoCoreNodeCollectionXincoCoreNodeToAttach);
            }
            xincoCoreLanguage.setXincoCoreNodeCollection(attachedXincoCoreNodeCollection);
            Collection<XincoCoreData> attachedXincoCoreDataCollection = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataCollectionXincoCoreDataToAttach : xincoCoreLanguage.getXincoCoreDataCollection()) {
                xincoCoreDataCollectionXincoCoreDataToAttach = em.getReference(xincoCoreDataCollectionXincoCoreDataToAttach.getClass(), xincoCoreDataCollectionXincoCoreDataToAttach.getId());
                attachedXincoCoreDataCollection.add(xincoCoreDataCollectionXincoCoreDataToAttach);
            }
            xincoCoreLanguage.setXincoCoreDataCollection(attachedXincoCoreDataCollection);
            em.persist(xincoCoreLanguage);
            for (XincoCoreNode xincoCoreNodeListXincoCoreNode : xincoCoreLanguage.getXincoCoreNodeList()) {
                XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode = xincoCoreNodeListXincoCoreNode.getXincoCoreLanguage();
                xincoCoreNodeListXincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
                if (oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode != null) {
                    oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListXincoCoreNode);
                    oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode = em.merge(oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreLanguage.getXincoCoreDataList()) {
                XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreLanguage();
                xincoCoreDataListXincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData);
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNode : xincoCoreLanguage.getXincoCoreNodeCollection()) {
                XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreNodeCollectionXincoCoreNode = xincoCoreNodeCollectionXincoCoreNode.getXincoCoreLanguage();
                xincoCoreNodeCollectionXincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
                xincoCoreNodeCollectionXincoCoreNode = em.merge(xincoCoreNodeCollectionXincoCoreNode);
                if (oldXincoCoreLanguageOfXincoCoreNodeCollectionXincoCoreNode != null) {
                    oldXincoCoreLanguageOfXincoCoreNodeCollectionXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionXincoCoreNode);
                    oldXincoCoreLanguageOfXincoCoreNodeCollectionXincoCoreNode = em.merge(oldXincoCoreLanguageOfXincoCoreNodeCollectionXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataCollectionXincoCoreData : xincoCoreLanguage.getXincoCoreDataCollection()) {
                XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreDataCollectionXincoCoreData = xincoCoreDataCollectionXincoCoreData.getXincoCoreLanguage();
                xincoCoreDataCollectionXincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
                xincoCoreDataCollectionXincoCoreData = em.merge(xincoCoreDataCollectionXincoCoreData);
                if (oldXincoCoreLanguageOfXincoCoreDataCollectionXincoCoreData != null) {
                    oldXincoCoreLanguageOfXincoCoreDataCollectionXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionXincoCoreData);
                    oldXincoCoreLanguageOfXincoCoreDataCollectionXincoCoreData = em.merge(oldXincoCoreLanguageOfXincoCoreDataCollectionXincoCoreData);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreLanguage xincoCoreLanguage) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLanguage persistentXincoCoreLanguage = em.find(XincoCoreLanguage.class, xincoCoreLanguage.getId());
            List<XincoCoreNode> xincoCoreNodeListOld = persistentXincoCoreLanguage.getXincoCoreNodeList();
            List<XincoCoreNode> xincoCoreNodeListNew = xincoCoreLanguage.getXincoCoreNodeList();
            List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreLanguage.getXincoCoreDataList();
            List<XincoCoreData> xincoCoreDataListNew = xincoCoreLanguage.getXincoCoreDataList();
            Collection<XincoCoreNode> xincoCoreNodeCollectionOld = persistentXincoCoreLanguage.getXincoCoreNodeCollection();
            Collection<XincoCoreNode> xincoCoreNodeCollectionNew = xincoCoreLanguage.getXincoCoreNodeCollection();
            Collection<XincoCoreData> xincoCoreDataCollectionOld = persistentXincoCoreLanguage.getXincoCoreDataCollection();
            Collection<XincoCoreData> xincoCoreDataCollectionNew = xincoCoreLanguage.getXincoCoreDataCollection();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreNode xincoCoreNodeListOldXincoCoreNode : xincoCoreNodeListOld) {
                if (!xincoCoreNodeListNew.contains(xincoCoreNodeListOldXincoCoreNode)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreNode " + xincoCoreNodeListOldXincoCoreNode + " since its xincoCoreLanguage field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreLanguage field is not nullable.");
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionOldXincoCoreNode : xincoCoreNodeCollectionOld) {
                if (!xincoCoreNodeCollectionNew.contains(xincoCoreNodeCollectionOldXincoCoreNode)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreNode " + xincoCoreNodeCollectionOldXincoCoreNode + " since its xincoCoreLanguage field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataCollectionOldXincoCoreData : xincoCoreDataCollectionOld) {
                if (!xincoCoreDataCollectionNew.contains(xincoCoreDataCollectionOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataCollectionOldXincoCoreData + " since its xincoCoreLanguage field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreNode> attachedXincoCoreNodeListNew = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNodeToAttach : xincoCoreNodeListNew) {
                xincoCoreNodeListNewXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListNewXincoCoreNodeToAttach.getClass(), xincoCoreNodeListNewXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeListNew.add(xincoCoreNodeListNewXincoCoreNodeToAttach);
            }
            xincoCoreNodeListNew = attachedXincoCoreNodeListNew;
            xincoCoreLanguage.setXincoCoreNodeList(xincoCoreNodeListNew);
            List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
                xincoCoreDataListNewXincoCoreDataToAttach = em.getReference(xincoCoreDataListNewXincoCoreDataToAttach.getClass(), xincoCoreDataListNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
            }
            xincoCoreDataListNew = attachedXincoCoreDataListNew;
            xincoCoreLanguage.setXincoCoreDataList(xincoCoreDataListNew);
            Collection<XincoCoreNode> attachedXincoCoreNodeCollectionNew = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeCollectionNewXincoCoreNodeToAttach : xincoCoreNodeCollectionNew) {
                xincoCoreNodeCollectionNewXincoCoreNodeToAttach = em.getReference(xincoCoreNodeCollectionNewXincoCoreNodeToAttach.getClass(), xincoCoreNodeCollectionNewXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeCollectionNew.add(xincoCoreNodeCollectionNewXincoCoreNodeToAttach);
            }
            xincoCoreNodeCollectionNew = attachedXincoCoreNodeCollectionNew;
            xincoCoreLanguage.setXincoCoreNodeCollection(xincoCoreNodeCollectionNew);
            Collection<XincoCoreData> attachedXincoCoreDataCollectionNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreDataToAttach : xincoCoreDataCollectionNew) {
                xincoCoreDataCollectionNewXincoCoreDataToAttach = em.getReference(xincoCoreDataCollectionNewXincoCoreDataToAttach.getClass(), xincoCoreDataCollectionNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataCollectionNew.add(xincoCoreDataCollectionNewXincoCoreDataToAttach);
            }
            xincoCoreDataCollectionNew = attachedXincoCoreDataCollectionNew;
            xincoCoreLanguage.setXincoCoreDataCollection(xincoCoreDataCollectionNew);
            xincoCoreLanguage = em.merge(xincoCoreLanguage);
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNode : xincoCoreNodeListNew) {
                if (!xincoCoreNodeListOld.contains(xincoCoreNodeListNewXincoCoreNode)) {
                    XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode = xincoCoreNodeListNewXincoCoreNode.getXincoCoreLanguage();
                    xincoCoreNodeListNewXincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
                    xincoCoreNodeListNewXincoCoreNode = em.merge(xincoCoreNodeListNewXincoCoreNode);
                    if (oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode != null && !oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListNewXincoCoreNode);
                        oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode = em.merge(oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreLanguage();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData);
                    }
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionNewXincoCoreNode : xincoCoreNodeCollectionNew) {
                if (!xincoCoreNodeCollectionOld.contains(xincoCoreNodeCollectionNewXincoCoreNode)) {
                    XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreNodeCollectionNewXincoCoreNode = xincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreLanguage();
                    xincoCoreNodeCollectionNewXincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
                    xincoCoreNodeCollectionNewXincoCoreNode = em.merge(xincoCoreNodeCollectionNewXincoCoreNode);
                    if (oldXincoCoreLanguageOfXincoCoreNodeCollectionNewXincoCoreNode != null && !oldXincoCoreLanguageOfXincoCoreNodeCollectionNewXincoCoreNode.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageOfXincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionNewXincoCoreNode);
                        oldXincoCoreLanguageOfXincoCoreNodeCollectionNewXincoCoreNode = em.merge(oldXincoCoreLanguageOfXincoCoreNodeCollectionNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreData : xincoCoreDataCollectionNew) {
                if (!xincoCoreDataCollectionOld.contains(xincoCoreDataCollectionNewXincoCoreData)) {
                    XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreDataCollectionNewXincoCoreData = xincoCoreDataCollectionNewXincoCoreData.getXincoCoreLanguage();
                    xincoCoreDataCollectionNewXincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
                    xincoCoreDataCollectionNewXincoCoreData = em.merge(xincoCoreDataCollectionNewXincoCoreData);
                    if (oldXincoCoreLanguageOfXincoCoreDataCollectionNewXincoCoreData != null && !oldXincoCoreLanguageOfXincoCoreDataCollectionNewXincoCoreData.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageOfXincoCoreDataCollectionNewXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionNewXincoCoreData);
                        oldXincoCoreLanguageOfXincoCoreDataCollectionNewXincoCoreData = em.merge(oldXincoCoreLanguageOfXincoCoreDataCollectionNewXincoCoreData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreLanguage.getId();
                if (findXincoCoreLanguage(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreLanguage with id " + id + " no longer exists.");
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
            XincoCoreLanguage xincoCoreLanguage;
            try {
                xincoCoreLanguage = em.getReference(XincoCoreLanguage.class, id);
                xincoCoreLanguage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreLanguage with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreNode> xincoCoreNodeListOrphanCheck = xincoCoreLanguage.getXincoCoreNodeList();
            for (XincoCoreNode xincoCoreNodeListOrphanCheckXincoCoreNode : xincoCoreNodeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreNode " + xincoCoreNodeListOrphanCheckXincoCoreNode + " in its xincoCoreNodeList field has a non-nullable xincoCoreLanguage field.");
            }
            List<XincoCoreData> xincoCoreDataListOrphanCheck = xincoCoreLanguage.getXincoCoreDataList();
            for (XincoCoreData xincoCoreDataListOrphanCheckXincoCoreData : xincoCoreDataListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreLanguage field.");
            }
            Collection<XincoCoreNode> xincoCoreNodeCollectionOrphanCheck = xincoCoreLanguage.getXincoCoreNodeCollection();
            for (XincoCoreNode xincoCoreNodeCollectionOrphanCheckXincoCoreNode : xincoCoreNodeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreNode " + xincoCoreNodeCollectionOrphanCheckXincoCoreNode + " in its xincoCoreNodeCollection field has a non-nullable xincoCoreLanguage field.");
            }
            Collection<XincoCoreData> xincoCoreDataCollectionOrphanCheck = xincoCoreLanguage.getXincoCoreDataCollection();
            for (XincoCoreData xincoCoreDataCollectionOrphanCheckXincoCoreData : xincoCoreDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataCollectionOrphanCheckXincoCoreData + " in its xincoCoreDataCollection field has a non-nullable xincoCoreLanguage field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xincoCoreLanguage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreLanguage> findXincoCoreLanguageEntities() {
        return findXincoCoreLanguageEntities(true, -1, -1);
    }

    public List<XincoCoreLanguage> findXincoCoreLanguageEntities(int maxResults, int firstResult) {
        return findXincoCoreLanguageEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreLanguage> findXincoCoreLanguageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreLanguage.class));
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

    public XincoCoreLanguage findXincoCoreLanguage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreLanguage.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreLanguageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreLanguage> rt = cq.from(XincoCoreLanguage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
