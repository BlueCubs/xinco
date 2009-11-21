/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeJpaController {

    public XincoCoreNodeJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreNode xincoCoreNode) throws PreexistingEntityException, Exception {
        if (xincoCoreNode.getXincoCoreNodeList() == null) {
            xincoCoreNode.setXincoCoreNodeList(new ArrayList<XincoCoreNode>());
        }
        if (xincoCoreNode.getXincoCoreAceList() == null) {
            xincoCoreNode.setXincoCoreAceList(new ArrayList<XincoCoreAce>());
        }
        if (xincoCoreNode.getXincoCoreDataList() == null) {
            xincoCoreNode.setXincoCoreDataList(new ArrayList<XincoCoreData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLanguage xincoCoreLanguageId = xincoCoreNode.getXincoCoreLanguageId();
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId = em.getReference(xincoCoreLanguageId.getClass(), xincoCoreLanguageId.getId());
                xincoCoreNode.setXincoCoreLanguageId(xincoCoreLanguageId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreNode.getXincoCoreNodeId();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId = em.getReference(xincoCoreNodeId.getClass(), xincoCoreNodeId.getId());
                xincoCoreNode.setXincoCoreNodeId(xincoCoreNodeId);
            }
            List<XincoCoreNode> attachedXincoCoreNodeList = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNodeToAttach : xincoCoreNode.getXincoCoreNodeList()) {
                xincoCoreNodeListXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListXincoCoreNodeToAttach.getClass(), xincoCoreNodeListXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeList.add(xincoCoreNodeListXincoCoreNodeToAttach);
            }
            xincoCoreNode.setXincoCoreNodeList(attachedXincoCoreNodeList);
            List<XincoCoreAce> attachedXincoCoreAceList = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListXincoCoreAceToAttach : xincoCoreNode.getXincoCoreAceList()) {
                xincoCoreAceListXincoCoreAceToAttach = em.getReference(xincoCoreAceListXincoCoreAceToAttach.getClass(), xincoCoreAceListXincoCoreAceToAttach.getId());
                attachedXincoCoreAceList.add(xincoCoreAceListXincoCoreAceToAttach);
            }
            xincoCoreNode.setXincoCoreAceList(attachedXincoCoreAceList);
            List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach : xincoCoreNode.getXincoCoreDataList()) {
                xincoCoreDataListXincoCoreDataToAttach = em.getReference(xincoCoreDataListXincoCoreDataToAttach.getClass(), xincoCoreDataListXincoCoreDataToAttach.getId());
                attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
            }
            xincoCoreNode.setXincoCoreDataList(attachedXincoCoreDataList);
            em.persist(xincoCoreNode);
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreLanguageId = em.merge(xincoCoreLanguageId);
            }
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            for (XincoCoreNode xincoCoreNodeListXincoCoreNode : xincoCoreNode.getXincoCoreNodeList()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode = xincoCoreNodeListXincoCoreNode.getXincoCoreNodeId();
                xincoCoreNodeListXincoCoreNode.setXincoCoreNodeId(xincoCoreNode);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
                if (oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode != null) {
                    oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListXincoCoreNode);
                    oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode = em.merge(oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode);
                }
            }
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreNode.getXincoCoreAceList()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreNodeId();
                xincoCoreAceListXincoCoreAce.setXincoCoreNodeId(xincoCoreNode);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreNode.getXincoCoreDataList()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreNodeId();
                xincoCoreDataListXincoCoreData.setXincoCoreNodeId(xincoCoreNode);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreNode(xincoCoreNode.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreNode " + xincoCoreNode + " already exists.", ex);
            }
            throw ex;
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
            XincoCoreLanguage xincoCoreLanguageIdOld = persistentXincoCoreNode.getXincoCoreLanguageId();
            XincoCoreLanguage xincoCoreLanguageIdNew = xincoCoreNode.getXincoCoreLanguageId();
            XincoCoreNode xincoCoreNodeIdOld = persistentXincoCoreNode.getXincoCoreNodeId();
            XincoCoreNode xincoCoreNodeIdNew = xincoCoreNode.getXincoCoreNodeId();
            List<XincoCoreNode> xincoCoreNodeListOld = persistentXincoCoreNode.getXincoCoreNodeList();
            List<XincoCoreNode> xincoCoreNodeListNew = xincoCoreNode.getXincoCoreNodeList();
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreNode.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreNode.getXincoCoreAceList();
            List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreNode.getXincoCoreDataList();
            List<XincoCoreData> xincoCoreDataListNew = xincoCoreNode.getXincoCoreDataList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreNodeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoCoreLanguageIdNew != null) {
                xincoCoreLanguageIdNew = em.getReference(xincoCoreLanguageIdNew.getClass(), xincoCoreLanguageIdNew.getId());
                xincoCoreNode.setXincoCoreLanguageId(xincoCoreLanguageIdNew);
            }
            if (xincoCoreNodeIdNew != null) {
                xincoCoreNodeIdNew = em.getReference(xincoCoreNodeIdNew.getClass(), xincoCoreNodeIdNew.getId());
                xincoCoreNode.setXincoCoreNodeId(xincoCoreNodeIdNew);
            }
            List<XincoCoreNode> attachedXincoCoreNodeListNew = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNodeToAttach : xincoCoreNodeListNew) {
                xincoCoreNodeListNewXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListNewXincoCoreNodeToAttach.getClass(), xincoCoreNodeListNewXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeListNew.add(xincoCoreNodeListNewXincoCoreNodeToAttach);
            }
            xincoCoreNodeListNew = attachedXincoCoreNodeListNew;
            xincoCoreNode.setXincoCoreNodeList(xincoCoreNodeListNew);
            List<XincoCoreAce> attachedXincoCoreAceListNew = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAceToAttach : xincoCoreAceListNew) {
                xincoCoreAceListNewXincoCoreAceToAttach = em.getReference(xincoCoreAceListNewXincoCoreAceToAttach.getClass(), xincoCoreAceListNewXincoCoreAceToAttach.getId());
                attachedXincoCoreAceListNew.add(xincoCoreAceListNewXincoCoreAceToAttach);
            }
            xincoCoreAceListNew = attachedXincoCoreAceListNew;
            xincoCoreNode.setXincoCoreAceList(xincoCoreAceListNew);
            List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
                xincoCoreDataListNewXincoCoreDataToAttach = em.getReference(xincoCoreDataListNewXincoCoreDataToAttach.getClass(), xincoCoreDataListNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
            }
            xincoCoreDataListNew = attachedXincoCoreDataListNew;
            xincoCoreNode.setXincoCoreDataList(xincoCoreDataListNew);
            xincoCoreNode = em.merge(xincoCoreNode);
            if (xincoCoreLanguageIdOld != null && !xincoCoreLanguageIdOld.equals(xincoCoreLanguageIdNew)) {
                xincoCoreLanguageIdOld.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguageIdOld = em.merge(xincoCoreLanguageIdOld);
            }
            if (xincoCoreLanguageIdNew != null && !xincoCoreLanguageIdNew.equals(xincoCoreLanguageIdOld)) {
                xincoCoreLanguageIdNew.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreLanguageIdNew = em.merge(xincoCoreLanguageIdNew);
            }
            if (xincoCoreNodeIdOld != null && !xincoCoreNodeIdOld.equals(xincoCoreNodeIdNew)) {
                xincoCoreNodeIdOld.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeIdOld = em.merge(xincoCoreNodeIdOld);
            }
            if (xincoCoreNodeIdNew != null && !xincoCoreNodeIdNew.equals(xincoCoreNodeIdOld)) {
                xincoCoreNodeIdNew.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreNodeIdNew = em.merge(xincoCoreNodeIdNew);
            }
            for (XincoCoreNode xincoCoreNodeListOldXincoCoreNode : xincoCoreNodeListOld) {
                if (!xincoCoreNodeListNew.contains(xincoCoreNodeListOldXincoCoreNode)) {
                    xincoCoreNodeListOldXincoCoreNode.setXincoCoreNodeId(null);
                    xincoCoreNodeListOldXincoCoreNode = em.merge(xincoCoreNodeListOldXincoCoreNode);
                }
            }
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNode : xincoCoreNodeListNew) {
                if (!xincoCoreNodeListOld.contains(xincoCoreNodeListNewXincoCoreNode)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode = xincoCoreNodeListNewXincoCoreNode.getXincoCoreNodeId();
                    xincoCoreNodeListNewXincoCoreNode.setXincoCoreNodeId(xincoCoreNode);
                    xincoCoreNodeListNewXincoCoreNode = em.merge(xincoCoreNodeListNewXincoCoreNode);
                    if (oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode != null && !oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListNewXincoCoreNode);
                        oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode = em.merge(oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreNodeId(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreNodeId();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreNodeId(xincoCoreNode);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreNodeId();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreNodeId(xincoCoreNode);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData);
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
                illegalOrphanMessages.add("This XincoCoreNode (" + xincoCoreNode + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreNodeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoCoreLanguage xincoCoreLanguageId = xincoCoreNode.getXincoCoreLanguageId();
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguageId = em.merge(xincoCoreLanguageId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreNode.getXincoCoreNodeId();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            List<XincoCoreNode> xincoCoreNodeList = xincoCoreNode.getXincoCoreNodeList();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNode : xincoCoreNodeList) {
                xincoCoreNodeListXincoCoreNode.setXincoCoreNodeId(null);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreNode.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreNodeId(null);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
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
