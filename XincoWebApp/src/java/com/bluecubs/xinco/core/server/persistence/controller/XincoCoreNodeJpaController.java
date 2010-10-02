/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

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
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import java.util.Collection;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeJpaController implements Serializable {

    public XincoCoreNodeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
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
        if (xincoCoreNode.getXincoCoreNodeCollection() == null) {
            xincoCoreNode.setXincoCoreNodeCollection(new ArrayList<XincoCoreNode>());
        }
        if (xincoCoreNode.getXincoCoreDataCollection() == null) {
            xincoCoreNode.setXincoCoreDataCollection(new ArrayList<XincoCoreData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLanguage xincoCoreLanguageId = xincoCoreNode.getXincoCoreLanguage();
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId = em.getReference(xincoCoreLanguageId.getClass(), xincoCoreLanguageId.getId());
                xincoCoreNode.setXincoCoreLanguage(xincoCoreLanguageId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreNode.getXincoCoreNode();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId = em.getReference(xincoCoreNodeId.getClass(), xincoCoreNodeId.getId());
                xincoCoreNode.setXincoCoreNode(xincoCoreNodeId);
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
            Collection<XincoCoreNode> attachedXincoCoreNodeCollection = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNodeToAttach : xincoCoreNode.getXincoCoreNodeCollection()) {
                xincoCoreNodeCollectionXincoCoreNodeToAttach = em.getReference(xincoCoreNodeCollectionXincoCoreNodeToAttach.getClass(), xincoCoreNodeCollectionXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeCollection.add(xincoCoreNodeCollectionXincoCoreNodeToAttach);
            }
            xincoCoreNode.setXincoCoreNodeCollection(attachedXincoCoreNodeCollection);
            Collection<XincoCoreData> attachedXincoCoreDataCollection = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataCollectionXincoCoreDataToAttach : xincoCoreNode.getXincoCoreDataCollection()) {
                xincoCoreDataCollectionXincoCoreDataToAttach = em.getReference(xincoCoreDataCollectionXincoCoreDataToAttach.getClass(), xincoCoreDataCollectionXincoCoreDataToAttach.getId());
                attachedXincoCoreDataCollection.add(xincoCoreDataCollectionXincoCoreDataToAttach);
            }
            xincoCoreNode.setXincoCoreDataCollection(attachedXincoCoreDataCollection);
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
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode = xincoCoreNodeListXincoCoreNode.getXincoCoreNode();
                xincoCoreNodeListXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
                if (oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode != null) {
                    oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListXincoCoreNode);
                    oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode = em.merge(oldXincoCoreNodeIdOfXincoCoreNodeListXincoCoreNode);
                }
            }
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreNode.getXincoCoreAceList()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreNode();
                xincoCoreAceListXincoCoreAce.setXincoCoreNode(xincoCoreNode);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreNodeIdOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreNode.getXincoCoreDataList()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreNode();
                xincoCoreDataListXincoCoreData.setXincoCoreNode(xincoCoreNode);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreNodeIdOfXincoCoreDataListXincoCoreData);
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNode : xincoCoreNode.getXincoCoreNodeCollection()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreNodeCollectionXincoCoreNode = xincoCoreNodeCollectionXincoCoreNode.getXincoCoreNode();
                xincoCoreNodeCollectionXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                xincoCoreNodeCollectionXincoCoreNode = em.merge(xincoCoreNodeCollectionXincoCoreNode);
                if (oldXincoCoreNodeIdOfXincoCoreNodeCollectionXincoCoreNode != null) {
                    oldXincoCoreNodeIdOfXincoCoreNodeCollectionXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionXincoCoreNode);
                    oldXincoCoreNodeIdOfXincoCoreNodeCollectionXincoCoreNode = em.merge(oldXincoCoreNodeIdOfXincoCoreNodeCollectionXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataCollectionXincoCoreData : xincoCoreNode.getXincoCoreDataCollection()) {
                XincoCoreNode oldXincoCoreNodeIdOfXincoCoreDataCollectionXincoCoreData = xincoCoreDataCollectionXincoCoreData.getXincoCoreNode();
                xincoCoreDataCollectionXincoCoreData.setXincoCoreNode(xincoCoreNode);
                xincoCoreDataCollectionXincoCoreData = em.merge(xincoCoreDataCollectionXincoCoreData);
                if (oldXincoCoreNodeIdOfXincoCoreDataCollectionXincoCoreData != null) {
                    oldXincoCoreNodeIdOfXincoCoreDataCollectionXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionXincoCoreData);
                    oldXincoCoreNodeIdOfXincoCoreDataCollectionXincoCoreData = em.merge(oldXincoCoreNodeIdOfXincoCoreDataCollectionXincoCoreData);
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
            XincoCoreLanguage xincoCoreLanguageIdOld = persistentXincoCoreNode.getXincoCoreLanguage();
            XincoCoreLanguage xincoCoreLanguageIdNew = xincoCoreNode.getXincoCoreLanguage();
            XincoCoreNode xincoCoreNodeIdOld = persistentXincoCoreNode.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeIdNew = xincoCoreNode.getXincoCoreNode();
            List<XincoCoreNode> xincoCoreNodeListOld = persistentXincoCoreNode.getXincoCoreNodeList();
            List<XincoCoreNode> xincoCoreNodeListNew = xincoCoreNode.getXincoCoreNodeList();
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreNode.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreNode.getXincoCoreAceList();
            List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreNode.getXincoCoreDataList();
            List<XincoCoreData> xincoCoreDataListNew = xincoCoreNode.getXincoCoreDataList();
            Collection<XincoCoreNode> xincoCoreNodeCollectionOld = persistentXincoCoreNode.getXincoCoreNodeCollection();
            Collection<XincoCoreNode> xincoCoreNodeCollectionNew = xincoCoreNode.getXincoCoreNodeCollection();
            Collection<XincoCoreData> xincoCoreDataCollectionOld = persistentXincoCoreNode.getXincoCoreDataCollection();
            Collection<XincoCoreData> xincoCoreDataCollectionNew = xincoCoreNode.getXincoCoreDataCollection();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreNodeId field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataCollectionOldXincoCoreData : xincoCoreDataCollectionOld) {
                if (!xincoCoreDataCollectionNew.contains(xincoCoreDataCollectionOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataCollectionOldXincoCoreData + " since its xincoCoreNodeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoCoreLanguageIdNew != null) {
                xincoCoreLanguageIdNew = em.getReference(xincoCoreLanguageIdNew.getClass(), xincoCoreLanguageIdNew.getId());
                xincoCoreNode.setXincoCoreLanguage(xincoCoreLanguageIdNew);
            }
            if (xincoCoreNodeIdNew != null) {
                xincoCoreNodeIdNew = em.getReference(xincoCoreNodeIdNew.getClass(), xincoCoreNodeIdNew.getId());
                xincoCoreNode.setXincoCoreNode(xincoCoreNodeIdNew);
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
            Collection<XincoCoreNode> attachedXincoCoreNodeCollectionNew = new ArrayList<XincoCoreNode>();
            for (XincoCoreNode xincoCoreNodeCollectionNewXincoCoreNodeToAttach : xincoCoreNodeCollectionNew) {
                xincoCoreNodeCollectionNewXincoCoreNodeToAttach = em.getReference(xincoCoreNodeCollectionNewXincoCoreNodeToAttach.getClass(), xincoCoreNodeCollectionNewXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeCollectionNew.add(xincoCoreNodeCollectionNewXincoCoreNodeToAttach);
            }
            xincoCoreNodeCollectionNew = attachedXincoCoreNodeCollectionNew;
            xincoCoreNode.setXincoCoreNodeCollection(xincoCoreNodeCollectionNew);
            Collection<XincoCoreData> attachedXincoCoreDataCollectionNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreDataToAttach : xincoCoreDataCollectionNew) {
                xincoCoreDataCollectionNewXincoCoreDataToAttach = em.getReference(xincoCoreDataCollectionNewXincoCoreDataToAttach.getClass(), xincoCoreDataCollectionNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataCollectionNew.add(xincoCoreDataCollectionNewXincoCoreDataToAttach);
            }
            xincoCoreDataCollectionNew = attachedXincoCoreDataCollectionNew;
            xincoCoreNode.setXincoCoreDataCollection(xincoCoreDataCollectionNew);
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
                    xincoCoreNodeListOldXincoCoreNode.setXincoCoreNode(null);
                    xincoCoreNodeListOldXincoCoreNode = em.merge(xincoCoreNodeListOldXincoCoreNode);
                }
            }
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNode : xincoCoreNodeListNew) {
                if (!xincoCoreNodeListOld.contains(xincoCoreNodeListNewXincoCoreNode)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode = xincoCoreNodeListNewXincoCoreNode.getXincoCoreNode();
                    xincoCoreNodeListNewXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                    xincoCoreNodeListNewXincoCoreNode = em.merge(xincoCoreNodeListNewXincoCoreNode);
                    if (oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode != null && !oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode.getXincoCoreNodeList().remove(xincoCoreNodeListNewXincoCoreNode);
                        oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode = em.merge(oldXincoCoreNodeIdOfXincoCoreNodeListNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreNode(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreNode();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreNode(xincoCoreNode);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreNodeIdOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreNode();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreNode(xincoCoreNode);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreNodeIdOfXincoCoreDataListNewXincoCoreData);
                    }
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionOldXincoCoreNode : xincoCoreNodeCollectionOld) {
                if (!xincoCoreNodeCollectionNew.contains(xincoCoreNodeCollectionOldXincoCoreNode)) {
                    xincoCoreNodeCollectionOldXincoCoreNode.setXincoCoreNode(null);
                    xincoCoreNodeCollectionOldXincoCoreNode = em.merge(xincoCoreNodeCollectionOldXincoCoreNode);
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionNewXincoCoreNode : xincoCoreNodeCollectionNew) {
                if (!xincoCoreNodeCollectionOld.contains(xincoCoreNodeCollectionNewXincoCoreNode)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreNodeCollectionNewXincoCoreNode = xincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreNode();
                    xincoCoreNodeCollectionNewXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                    xincoCoreNodeCollectionNewXincoCoreNode = em.merge(xincoCoreNodeCollectionNewXincoCoreNode);
                    if (oldXincoCoreNodeIdOfXincoCoreNodeCollectionNewXincoCoreNode != null && !oldXincoCoreNodeIdOfXincoCoreNodeCollectionNewXincoCoreNode.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionNewXincoCoreNode);
                        oldXincoCoreNodeIdOfXincoCoreNodeCollectionNewXincoCoreNode = em.merge(oldXincoCoreNodeIdOfXincoCoreNodeCollectionNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreData : xincoCoreDataCollectionNew) {
                if (!xincoCoreDataCollectionOld.contains(xincoCoreDataCollectionNewXincoCoreData)) {
                    XincoCoreNode oldXincoCoreNodeIdOfXincoCoreDataCollectionNewXincoCoreData = xincoCoreDataCollectionNewXincoCoreData.getXincoCoreNode();
                    xincoCoreDataCollectionNewXincoCoreData.setXincoCoreNode(xincoCoreNode);
                    xincoCoreDataCollectionNewXincoCoreData = em.merge(xincoCoreDataCollectionNewXincoCoreData);
                    if (oldXincoCoreNodeIdOfXincoCoreDataCollectionNewXincoCoreData != null && !oldXincoCoreNodeIdOfXincoCoreDataCollectionNewXincoCoreData.equals(xincoCoreNode)) {
                        oldXincoCoreNodeIdOfXincoCoreDataCollectionNewXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionNewXincoCoreData);
                        oldXincoCoreNodeIdOfXincoCoreDataCollectionNewXincoCoreData = em.merge(oldXincoCoreNodeIdOfXincoCoreDataCollectionNewXincoCoreData);
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
            Collection<XincoCoreData> xincoCoreDataCollectionOrphanCheck = xincoCoreNode.getXincoCoreDataCollection();
            for (XincoCoreData xincoCoreDataCollectionOrphanCheckXincoCoreData : xincoCoreDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreNode (" + xincoCoreNode + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataCollectionOrphanCheckXincoCoreData + " in its xincoCoreDataCollection field has a non-nullable xincoCoreNodeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoCoreLanguage xincoCoreLanguageId = xincoCoreNode.getXincoCoreLanguage();
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguageId = em.merge(xincoCoreLanguageId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreNode.getXincoCoreNode();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            List<XincoCoreNode> xincoCoreNodeList = xincoCoreNode.getXincoCoreNodeList();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNode : xincoCoreNodeList) {
                xincoCoreNodeListXincoCoreNode.setXincoCoreNode(null);
                xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreNode.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreNode(null);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
            }
            Collection<XincoCoreNode> xincoCoreNodeCollection = xincoCoreNode.getXincoCoreNodeCollection();
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNode : xincoCoreNodeCollection) {
                xincoCoreNodeCollectionXincoCoreNode.setXincoCoreNode(null);
                xincoCoreNodeCollectionXincoCoreNode = em.merge(xincoCoreNodeCollectionXincoCoreNode);
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
