/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import java.util.Collection;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLanguageJpaController implements Serializable {

    public XincoCoreLanguageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreLanguage xincoCoreLanguage) throws PreexistingEntityException, Exception {
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
                XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreNodeListXincoCoreNode = xincoCoreNodeListXincoCoreNode.getXincoCoreLanguageId();
                xincoCoreNodeListXincoCoreNode.setXincoCoreLanguageId(xincoCoreLanguage);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
                if (oldXincoCoreLanguageIdOfXincoCoreNodeListXincoCoreNode != null) {
                    oldXincoCoreLanguageIdOfXincoCoreNodeListXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListXincoCoreNode);
                    oldXincoCoreLanguageIdOfXincoCoreNodeListXincoCoreNode = em.merge(oldXincoCoreLanguageIdOfXincoCoreNodeListXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreLanguage.getXincoCoreDataList()) {
                XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreLanguageId();
                xincoCoreDataListXincoCoreData.setXincoCoreLanguageId(xincoCoreLanguage);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreLanguageIdOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreLanguageIdOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreLanguageIdOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreLanguageIdOfXincoCoreDataListXincoCoreData);
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNode : xincoCoreLanguage.getXincoCoreNodeCollection()) {
                XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreNodeCollectionXincoCoreNode = xincoCoreNodeCollectionXincoCoreNode.getXincoCoreLanguageId();
                xincoCoreNodeCollectionXincoCoreNode.setXincoCoreLanguageId(xincoCoreLanguage);
                xincoCoreNodeCollectionXincoCoreNode = em.merge(xincoCoreNodeCollectionXincoCoreNode);
                if (oldXincoCoreLanguageIdOfXincoCoreNodeCollectionXincoCoreNode != null) {
                    oldXincoCoreLanguageIdOfXincoCoreNodeCollectionXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionXincoCoreNode);
                    oldXincoCoreLanguageIdOfXincoCoreNodeCollectionXincoCoreNode = em.merge(oldXincoCoreLanguageIdOfXincoCoreNodeCollectionXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataCollectionXincoCoreData : xincoCoreLanguage.getXincoCoreDataCollection()) {
                XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreDataCollectionXincoCoreData = xincoCoreDataCollectionXincoCoreData.getXincoCoreLanguageId();
                xincoCoreDataCollectionXincoCoreData.setXincoCoreLanguageId(xincoCoreLanguage);
                xincoCoreDataCollectionXincoCoreData = em.merge(xincoCoreDataCollectionXincoCoreData);
                if (oldXincoCoreLanguageIdOfXincoCoreDataCollectionXincoCoreData != null) {
                    oldXincoCoreLanguageIdOfXincoCoreDataCollectionXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionXincoCoreData);
                    oldXincoCoreLanguageIdOfXincoCoreDataCollectionXincoCoreData = em.merge(oldXincoCoreLanguageIdOfXincoCoreDataCollectionXincoCoreData);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreLanguage(xincoCoreLanguage.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreLanguage " + xincoCoreLanguage + " already exists.", ex);
            }
            throw ex;
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
                    illegalOrphanMessages.add("You must retain XincoCoreNode " + xincoCoreNodeListOldXincoCoreNode + " since its xincoCoreLanguageId field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreLanguageId field is not nullable.");
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionOldXincoCoreNode : xincoCoreNodeCollectionOld) {
                if (!xincoCoreNodeCollectionNew.contains(xincoCoreNodeCollectionOldXincoCoreNode)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreNode " + xincoCoreNodeCollectionOldXincoCoreNode + " since its xincoCoreLanguageId field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataCollectionOldXincoCoreData : xincoCoreDataCollectionOld) {
                if (!xincoCoreDataCollectionNew.contains(xincoCoreDataCollectionOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataCollectionOldXincoCoreData + " since its xincoCoreLanguageId field is not nullable.");
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
                    XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreNodeListNewXincoCoreNode = xincoCoreNodeListNewXincoCoreNode.getXincoCoreLanguageId();
                    xincoCoreNodeListNewXincoCoreNode.setXincoCoreLanguageId(xincoCoreLanguage);
                    xincoCoreNodeListNewXincoCoreNode = em.merge(xincoCoreNodeListNewXincoCoreNode);
                    if (oldXincoCoreLanguageIdOfXincoCoreNodeListNewXincoCoreNode != null && !oldXincoCoreLanguageIdOfXincoCoreNodeListNewXincoCoreNode.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageIdOfXincoCoreNodeListNewXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListNewXincoCoreNode);
                        oldXincoCoreLanguageIdOfXincoCoreNodeListNewXincoCoreNode = em.merge(oldXincoCoreLanguageIdOfXincoCoreNodeListNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreLanguageId();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreLanguageId(xincoCoreLanguage);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreLanguageIdOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreLanguageIdOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageIdOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreLanguageIdOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreLanguageIdOfXincoCoreDataListNewXincoCoreData);
                    }
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionNewXincoCoreNode : xincoCoreNodeCollectionNew) {
                if (!xincoCoreNodeCollectionOld.contains(xincoCoreNodeCollectionNewXincoCoreNode)) {
                    XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreNodeCollectionNewXincoCoreNode = xincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreLanguageId();
                    xincoCoreNodeCollectionNewXincoCoreNode.setXincoCoreLanguageId(xincoCoreLanguage);
                    xincoCoreNodeCollectionNewXincoCoreNode = em.merge(xincoCoreNodeCollectionNewXincoCoreNode);
                    if (oldXincoCoreLanguageIdOfXincoCoreNodeCollectionNewXincoCoreNode != null && !oldXincoCoreLanguageIdOfXincoCoreNodeCollectionNewXincoCoreNode.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageIdOfXincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionNewXincoCoreNode);
                        oldXincoCoreLanguageIdOfXincoCoreNodeCollectionNewXincoCoreNode = em.merge(oldXincoCoreLanguageIdOfXincoCoreNodeCollectionNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreData : xincoCoreDataCollectionNew) {
                if (!xincoCoreDataCollectionOld.contains(xincoCoreDataCollectionNewXincoCoreData)) {
                    XincoCoreLanguage oldXincoCoreLanguageIdOfXincoCoreDataCollectionNewXincoCoreData = xincoCoreDataCollectionNewXincoCoreData.getXincoCoreLanguageId();
                    xincoCoreDataCollectionNewXincoCoreData.setXincoCoreLanguageId(xincoCoreLanguage);
                    xincoCoreDataCollectionNewXincoCoreData = em.merge(xincoCoreDataCollectionNewXincoCoreData);
                    if (oldXincoCoreLanguageIdOfXincoCoreDataCollectionNewXincoCoreData != null && !oldXincoCoreLanguageIdOfXincoCoreDataCollectionNewXincoCoreData.equals(xincoCoreLanguage)) {
                        oldXincoCoreLanguageIdOfXincoCoreDataCollectionNewXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionNewXincoCoreData);
                        oldXincoCoreLanguageIdOfXincoCoreDataCollectionNewXincoCoreData = em.merge(oldXincoCoreLanguageIdOfXincoCoreDataCollectionNewXincoCoreData);
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
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreNode " + xincoCoreNodeListOrphanCheckXincoCoreNode + " in its xincoCoreNodeList field has a non-nullable xincoCoreLanguageId field.");
            }
            List<XincoCoreData> xincoCoreDataListOrphanCheck = xincoCoreLanguage.getXincoCoreDataList();
            for (XincoCoreData xincoCoreDataListOrphanCheckXincoCoreData : xincoCoreDataListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreLanguageId field.");
            }
            Collection<XincoCoreNode> xincoCoreNodeCollectionOrphanCheck = xincoCoreLanguage.getXincoCoreNodeCollection();
            for (XincoCoreNode xincoCoreNodeCollectionOrphanCheckXincoCoreNode : xincoCoreNodeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreNode " + xincoCoreNodeCollectionOrphanCheckXincoCoreNode + " in its xincoCoreNodeCollection field has a non-nullable xincoCoreLanguageId field.");
            }
            Collection<XincoCoreData> xincoCoreDataCollectionOrphanCheck = xincoCoreLanguage.getXincoCoreDataCollection();
            for (XincoCoreData xincoCoreDataCollectionOrphanCheckXincoCoreData : xincoCoreDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreLanguage (" + xincoCoreLanguage + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataCollectionOrphanCheckXincoCoreData + " in its xincoCoreDataCollection field has a non-nullable xincoCoreLanguageId field.");
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
