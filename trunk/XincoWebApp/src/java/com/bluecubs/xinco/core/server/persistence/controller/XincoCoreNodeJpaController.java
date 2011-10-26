/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeJpaController implements Serializable {

    public XincoCoreNodeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreNode xincoCoreNode) {
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
            XincoCoreLanguage xincoCoreLanguage = xincoCoreNode.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage = em.getReference(xincoCoreLanguage.getClass(), xincoCoreLanguage.getId());
                xincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
            }
            XincoCoreNode xincoCoreNodeRel = xincoCoreNode.getXincoCoreNode();
            if (xincoCoreNodeRel != null) {
                xincoCoreNodeRel = em.getReference(xincoCoreNodeRel.getClass(), xincoCoreNodeRel.getId());
                xincoCoreNode.setXincoCoreNode(xincoCoreNodeRel);
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
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            if (xincoCoreNodeRel != null) {
                xincoCoreNodeRel.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreNodeRel = em.merge(xincoCoreNodeRel);
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
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreNode.getXincoCoreAceList()) {
                XincoCoreNode oldXincoCoreNodeOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreNode();
                xincoCoreAceListXincoCoreAce.setXincoCoreNode(xincoCoreNode);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreNodeOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreNodeOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreNodeOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreNodeOfXincoCoreAceListXincoCoreAce);
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
            for (XincoCoreNode xincoCoreNodeCollectionXincoCoreNode : xincoCoreNode.getXincoCoreNodeCollection()) {
                XincoCoreNode oldXincoCoreNodeOfXincoCoreNodeCollectionXincoCoreNode = xincoCoreNodeCollectionXincoCoreNode.getXincoCoreNode();
                xincoCoreNodeCollectionXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                xincoCoreNodeCollectionXincoCoreNode = em.merge(xincoCoreNodeCollectionXincoCoreNode);
                if (oldXincoCoreNodeOfXincoCoreNodeCollectionXincoCoreNode != null) {
                    oldXincoCoreNodeOfXincoCoreNodeCollectionXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionXincoCoreNode);
                    oldXincoCoreNodeOfXincoCoreNodeCollectionXincoCoreNode = em.merge(oldXincoCoreNodeOfXincoCoreNodeCollectionXincoCoreNode);
                }
            }
            for (XincoCoreData xincoCoreDataCollectionXincoCoreData : xincoCoreNode.getXincoCoreDataCollection()) {
                XincoCoreNode oldXincoCoreNodeOfXincoCoreDataCollectionXincoCoreData = xincoCoreDataCollectionXincoCoreData.getXincoCoreNode();
                xincoCoreDataCollectionXincoCoreData.setXincoCoreNode(xincoCoreNode);
                xincoCoreDataCollectionXincoCoreData = em.merge(xincoCoreDataCollectionXincoCoreData);
                if (oldXincoCoreNodeOfXincoCoreDataCollectionXincoCoreData != null) {
                    oldXincoCoreNodeOfXincoCoreDataCollectionXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionXincoCoreData);
                    oldXincoCoreNodeOfXincoCoreDataCollectionXincoCoreData = em.merge(oldXincoCoreNodeOfXincoCoreDataCollectionXincoCoreData);
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
            XincoCoreLanguage xincoCoreLanguageOld = persistentXincoCoreNode.getXincoCoreLanguage();
            XincoCoreLanguage xincoCoreLanguageNew = xincoCoreNode.getXincoCoreLanguage();
            XincoCoreNode xincoCoreNodeRelOld = persistentXincoCoreNode.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeRelNew = xincoCoreNode.getXincoCoreNode();
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
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreNode field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataCollectionOldXincoCoreData : xincoCoreDataCollectionOld) {
                if (!xincoCoreDataCollectionNew.contains(xincoCoreDataCollectionOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataCollectionOldXincoCoreData + " since its xincoCoreNode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoCoreLanguageNew != null) {
                xincoCoreLanguageNew = em.getReference(xincoCoreLanguageNew.getClass(), xincoCoreLanguageNew.getId());
                xincoCoreNode.setXincoCoreLanguage(xincoCoreLanguageNew);
            }
            if (xincoCoreNodeRelNew != null) {
                xincoCoreNodeRelNew = em.getReference(xincoCoreNodeRelNew.getClass(), xincoCoreNodeRelNew.getId());
                xincoCoreNode.setXincoCoreNode(xincoCoreNodeRelNew);
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
            if (xincoCoreLanguageOld != null && !xincoCoreLanguageOld.equals(xincoCoreLanguageNew)) {
                xincoCoreLanguageOld.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguageOld = em.merge(xincoCoreLanguageOld);
            }
            if (xincoCoreLanguageNew != null && !xincoCoreLanguageNew.equals(xincoCoreLanguageOld)) {
                xincoCoreLanguageNew.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreLanguageNew = em.merge(xincoCoreLanguageNew);
            }
            if (xincoCoreNodeRelOld != null && !xincoCoreNodeRelOld.equals(xincoCoreNodeRelNew)) {
                xincoCoreNodeRelOld.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeRelOld = em.merge(xincoCoreNodeRelOld);
            }
            if (xincoCoreNodeRelNew != null && !xincoCoreNodeRelNew.equals(xincoCoreNodeRelOld)) {
                xincoCoreNodeRelNew.getXincoCoreNodeList().add(xincoCoreNode);
                xincoCoreNodeRelNew = em.merge(xincoCoreNodeRelNew);
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
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreNode(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreNode oldXincoCoreNodeOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreNode();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreNode(xincoCoreNode);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreNodeOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreNodeOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreNode)) {
                        oldXincoCoreNodeOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreNodeOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreNodeOfXincoCoreAceListNewXincoCoreAce);
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
            for (XincoCoreNode xincoCoreNodeCollectionOldXincoCoreNode : xincoCoreNodeCollectionOld) {
                if (!xincoCoreNodeCollectionNew.contains(xincoCoreNodeCollectionOldXincoCoreNode)) {
                    xincoCoreNodeCollectionOldXincoCoreNode.setXincoCoreNode(null);
                    xincoCoreNodeCollectionOldXincoCoreNode = em.merge(xincoCoreNodeCollectionOldXincoCoreNode);
                }
            }
            for (XincoCoreNode xincoCoreNodeCollectionNewXincoCoreNode : xincoCoreNodeCollectionNew) {
                if (!xincoCoreNodeCollectionOld.contains(xincoCoreNodeCollectionNewXincoCoreNode)) {
                    XincoCoreNode oldXincoCoreNodeOfXincoCoreNodeCollectionNewXincoCoreNode = xincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreNode();
                    xincoCoreNodeCollectionNewXincoCoreNode.setXincoCoreNode(xincoCoreNode);
                    xincoCoreNodeCollectionNewXincoCoreNode = em.merge(xincoCoreNodeCollectionNewXincoCoreNode);
                    if (oldXincoCoreNodeOfXincoCoreNodeCollectionNewXincoCoreNode != null && !oldXincoCoreNodeOfXincoCoreNodeCollectionNewXincoCoreNode.equals(xincoCoreNode)) {
                        oldXincoCoreNodeOfXincoCoreNodeCollectionNewXincoCoreNode.getXincoCoreNodeCollection().remove(xincoCoreNodeCollectionNewXincoCoreNode);
                        oldXincoCoreNodeOfXincoCoreNodeCollectionNewXincoCoreNode = em.merge(oldXincoCoreNodeOfXincoCoreNodeCollectionNewXincoCoreNode);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreData : xincoCoreDataCollectionNew) {
                if (!xincoCoreDataCollectionOld.contains(xincoCoreDataCollectionNewXincoCoreData)) {
                    XincoCoreNode oldXincoCoreNodeOfXincoCoreDataCollectionNewXincoCoreData = xincoCoreDataCollectionNewXincoCoreData.getXincoCoreNode();
                    xincoCoreDataCollectionNewXincoCoreData.setXincoCoreNode(xincoCoreNode);
                    xincoCoreDataCollectionNewXincoCoreData = em.merge(xincoCoreDataCollectionNewXincoCoreData);
                    if (oldXincoCoreNodeOfXincoCoreDataCollectionNewXincoCoreData != null && !oldXincoCoreNodeOfXincoCoreDataCollectionNewXincoCoreData.equals(xincoCoreNode)) {
                        oldXincoCoreNodeOfXincoCoreDataCollectionNewXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionNewXincoCoreData);
                        oldXincoCoreNodeOfXincoCoreDataCollectionNewXincoCoreData = em.merge(oldXincoCoreNodeOfXincoCoreDataCollectionNewXincoCoreData);
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
            Collection<XincoCoreData> xincoCoreDataCollectionOrphanCheck = xincoCoreNode.getXincoCoreDataCollection();
            for (XincoCoreData xincoCoreDataCollectionOrphanCheckXincoCoreData : xincoCoreDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreNode (" + xincoCoreNode + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataCollectionOrphanCheckXincoCoreData + " in its xincoCoreDataCollection field has a non-nullable xincoCoreNode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoCoreLanguage xincoCoreLanguage = xincoCoreNode.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            XincoCoreNode xincoCoreNodeRel = xincoCoreNode.getXincoCoreNode();
            if (xincoCoreNodeRel != null) {
                xincoCoreNodeRel.getXincoCoreNodeList().remove(xincoCoreNode);
                xincoCoreNodeRel = em.merge(xincoCoreNodeRel);
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
