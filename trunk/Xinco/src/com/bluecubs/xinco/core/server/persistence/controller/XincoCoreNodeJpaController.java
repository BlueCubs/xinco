/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeJpaController {

    public XincoCoreNodeJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreNode xincoCoreNode) {
        if (xincoCoreNode.getXincoCoreNodeList() == null) {
            xincoCoreNode.setXincoCoreNodeList(new ArrayList<XincoCoreNode>());
        }
        if (xincoCoreNode.getXincoCoreDataList() == null) {
            xincoCoreNode.setXincoCoreDataList(new ArrayList<XincoCoreData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreNode xincoCoreNodeRel = xincoCoreNode.getXincoCoreNode();
            if (xincoCoreNodeRel != null) {
                xincoCoreNodeRel = em.getReference(xincoCoreNode.getClass(), xincoCoreNodeRel.getId());
                xincoCoreNode.setXincoCoreNode(xincoCoreNodeRel);
            }
            XincoCoreLanguage xincoCoreLanguage = xincoCoreNode.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage = em.getReference(xincoCoreLanguage.getClass(), xincoCoreLanguage.getId());
                xincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
            }
            List<XincoCoreNode> attachedXincoCoreNodeList = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNodeToAttach : xincoCoreNode.getXincoCoreNodeList()) {
                xincoCoreNodeListXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListXincoCoreNodeToAttach.getClass(), xincoCoreNodeListXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeList.add(xincoCoreNodeListXincoCoreNodeToAttach);
            }
            xincoCoreNode.setXincoCoreNodeList(attachedXincoCoreNodeList);
            List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach : xincoCoreNode.getXincoCoreDataList()) {
                xincoCoreDataListXincoCoreDataToAttach = em.getReference(xincoCoreDataListXincoCoreDataToAttach.getClass(), xincoCoreDataListXincoCoreDataToAttach.getId());
                attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
            }
            xincoCoreNode.setXincoCoreDataList(attachedXincoCoreDataList);
            em.persist(xincoCoreNode);
            if (xincoCoreNodeRel != null) {
                xincoCoreNodeRel.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreNodeRel = em.merge(xincoCoreNodeRel);
            }
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            for (XincoCoreNode xincoCoreNodeListXincoCoreNode : xincoCoreNode.getXincoCoreNodeList()) {
                XincoCoreNode oldXincoCoreNodeOfXincoCoreNodeListXincoCoreNode = xincoCoreNodeListXincoCoreNode.getXincoCoreNode();
                xincoCoreNodeListXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
                if (oldXincoCoreNodeOfXincoCoreNodeListXincoCoreNode != null) {
                    oldXincoCoreNodeOfXincoCoreNodeListXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListXincoCoreNode);
                    oldXincoCoreNodeOfXincoCoreNodeListXincoCoreNode = em.merge(oldXincoCoreNodeOfXincoCoreNodeListXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreNode.getXincoCoreDataList()) {
                XincoCoreNode oldXincoCoreNodeOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreNode();
                xincoCoreDataListXincoCoreData.setXincoCoreNode(xincoCoreNode);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreNodeOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreNodeOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreNodeOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreNodeOfXincoCoreDataListXincoCoreData);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreNode xincoCoreNode) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreNode persistentXincoCoreNode = em.find(XincoCoreNode.class, xincoCoreNode.getId());
            XincoCoreNode xincoCoreNodeRelOld = persistentXincoCoreNode.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeRelNew = xincoCoreNode.getXincoCoreNode();
            XincoCoreLanguage xincoCoreLanguageOld = persistentXincoCoreNode.getXincoCoreLanguage();
            XincoCoreLanguage xincoCoreLanguageNew = xincoCoreNode.getXincoCoreLanguage();
            List<XincoCoreNode> xincoCoreNodeListOld = persistentXincoCoreNode.getXincoCoreNodeList();
            List<XincoCoreNode> xincoCoreNodeListNew = xincoCoreNode.getXincoCoreNodeList();
            List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreNode.getXincoCoreDataList();
            List<XincoCoreData> xincoCoreDataListNew = xincoCoreNode.getXincoCoreDataList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreNode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoCoreNodeRelNew != null) {
                xincoCoreNodeRelNew = em.getReference(xincoCoreNodeRelNew.getClass(), xincoCoreNodeRelNew.getId());
                xincoCoreNode.setXincoCoreNode(xincoCoreNodeRelNew);
            }
            if (xincoCoreLanguageNew != null) {
                xincoCoreLanguageNew = em.getReference(xincoCoreLanguageNew.getClass(), xincoCoreLanguageNew.getId());
                xincoCoreNode.setXincoCoreLanguage(xincoCoreLanguageNew);
            }
            List<XincoCoreNode> attachedXincoCoreNodeListNew = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNodeToAttach : xincoCoreNodeListNew) {
                xincoCoreNodeListNewXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListNewXincoCoreNodeToAttach.getClass(), xincoCoreNodeListNewXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeListNew.add(xincoCoreNodeListNewXincoCoreNodeToAttach);
            }
            xincoCoreNodeListNew = attachedXincoCoreNodeListNew;
            xincoCoreNode.setXincoCoreNodeList(xincoCoreNodeListNew);
            List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
                xincoCoreDataListNewXincoCoreDataToAttach = em.getReference(xincoCoreDataListNewXincoCoreDataToAttach.getClass(), xincoCoreDataListNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
            }
            xincoCoreDataListNew = attachedXincoCoreDataListNew;
            xincoCoreNode.setXincoCoreDataList(xincoCoreDataListNew);
            xincoCoreNode = em.merge(xincoCoreNode);
            if (xincoCoreNodeRelOld != null && !xincoCoreNodeRelOld.equals(xincoCoreNodeRelNew)) {
                xincoCoreNodeRelOld.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeRelOld = em.merge(xincoCoreNodeRelOld);
            }
            if (xincoCoreNodeRelNew != null && !xincoCoreNodeRelNew.equals(xincoCoreNodeRelOld)) {
                xincoCoreNodeRelNew.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreNodeRelNew = em.merge(xincoCoreNodeRelNew);
            }
            if (xincoCoreLanguageOld != null && !xincoCoreLanguageOld.equals(xincoCoreLanguageNew)) {
                xincoCoreLanguageOld.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguageOld = em.merge(xincoCoreLanguageOld);
            }
            if (xincoCoreLanguageNew != null && !xincoCoreLanguageNew.equals(xincoCoreLanguageOld)) {
                xincoCoreLanguageNew.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreLanguageNew = em.merge(xincoCoreLanguageNew);
            }
            for (XincoCoreNode xincoCoreNodeListOldXincoCoreNode : xincoCoreNodeListOld) {
                if (!xincoCoreNodeListNew.contains(xincoCoreNodeListOldXincoCoreNode)) {
                    xincoCoreNodeListOldXincoCoreNode.setXincoCoreNode(null);
                    xincoCoreNodeListOldXincoCoreNode = em.merge(xincoCoreNodeListOldXincoCoreNode);
                }
            }
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNode : xincoCoreNodeListNew) {
                if (!xincoCoreNodeListOld.contains(xincoCoreNodeListNewXincoCoreNode)) {
                    XincoCoreNode oldXincoCoreNodeOfXincoCoreNodeListNewXincoCoreNode = xincoCoreNodeListNewXincoCoreNode.getXincoCoreNode();
                    xincoCoreNodeListNewXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                    xincoCoreNodeListNewXincoCoreNode = em.merge(xincoCoreNodeListNewXincoCoreNode);
                    if (oldXincoCoreNodeOfXincoCoreNodeListNewXincoCoreNode != null && !oldXincoCoreNodeOfXincoCoreNodeListNewXincoCoreNode.equals(xincoCoreNode)) {
                        oldXincoCoreNodeOfXincoCoreNodeListNewXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListNewXincoCoreNode);
                        oldXincoCoreNodeOfXincoCoreNodeListNewXincoCoreNode = em.merge(oldXincoCoreNodeOfXincoCoreNodeListNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreNode oldXincoCoreNodeOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreNode();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreNode(xincoCoreNode);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreNodeOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreNodeOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreNode)) {
                        oldXincoCoreNodeOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreNodeOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreNodeOfXincoCoreDataListNewXincoCoreData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreNode.getId();
                if (findXincoCoreNode(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreNode with id " + id + " no longer exists.");
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
            XincoCoreNode xincoCoreNode;
            try {
                xincoCoreNode = em.getReference(XincoCoreNode.class, id);
                xincoCoreNode.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreNode with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreData> xincoCoreDataListOrphanCheck = xincoCoreNode.getXincoCoreDataList();
            for (XincoCoreData xincoCoreDataListOrphanCheckXincoCoreData : xincoCoreDataListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreNode (" + xincoCoreNode + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreNode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoCoreNode xincoCoreNodeRel = xincoCoreNode.getXincoCoreNode();
            if (xincoCoreNodeRel != null) {
                xincoCoreNodeRel.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeRel = em.merge(xincoCoreNodeRel);
            }
            XincoCoreLanguage xincoCoreLanguage = xincoCoreNode.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            List<XincoCoreNode> xincoCoreNodeList = xincoCoreNode.getXincoCoreNodeList();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNode : xincoCoreNodeList) {
                xincoCoreNodeListXincoCoreNode.setXincoCoreNode(null);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
            }
            em.remove(xincoCoreNode);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreNode> findXincoCoreNodeEntities() {
        return findXincoCoreNodeEntities(true, -1, -1);
    }

    public List<XincoCoreNode> findXincoCoreNodeEntities(int maxResults, int firstResult) {
        return findXincoCoreNodeEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreNode> findXincoCoreNodeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreNode.class));
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

    public XincoCoreNode findXincoCoreNode(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreNode.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreNodeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreNode> rt = cq.from(XincoCoreNode.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
