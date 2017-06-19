/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoCoreNodeJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
            List<XincoCoreNode> attachedXincoCoreNodeList = new ArrayList<>();
            for (XincoCoreNode xincoCoreNodeListXincoCoreNodeToAttach : xincoCoreNode.getXincoCoreNodeList()) {
                xincoCoreNodeListXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListXincoCoreNodeToAttach.getClass(), xincoCoreNodeListXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeList.add(xincoCoreNodeListXincoCoreNodeToAttach);
            }
            xincoCoreNode.setXincoCoreNodeList(attachedXincoCoreNodeList);
            List<XincoCoreAce> attachedXincoCoreAceList = new ArrayList<>();
            for (XincoCoreAce xincoCoreAceListXincoCoreAceToAttach : xincoCoreNode.getXincoCoreAceList()) {
                xincoCoreAceListXincoCoreAceToAttach = em.getReference(xincoCoreAceListXincoCoreAceToAttach.getClass(), xincoCoreAceListXincoCoreAceToAttach.getId());
                attachedXincoCoreAceList.add(xincoCoreAceListXincoCoreAceToAttach);
            }
            xincoCoreNode.setXincoCoreAceList(attachedXincoCoreAceList);
            List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<>();
            for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach : xincoCoreNode.getXincoCoreDataList()) {
                xincoCoreDataListXincoCoreDataToAttach = em.getReference(xincoCoreDataListXincoCoreDataToAttach.getClass(), xincoCoreDataListXincoCoreDataToAttach.getId());
                attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
            }
            xincoCoreNode.setXincoCoreDataList(attachedXincoCoreDataList);
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
            List<String> illegalOrphanMessages = null;
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreNode field is not nullable.");
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
            List<XincoCoreNode> attachedXincoCoreNodeListNew = new ArrayList<>();
            for (XincoCoreNode xincoCoreNodeListNewXincoCoreNodeToAttach : xincoCoreNodeListNew) {
                xincoCoreNodeListNewXincoCoreNodeToAttach = em.getReference(xincoCoreNodeListNewXincoCoreNodeToAttach.getClass(), xincoCoreNodeListNewXincoCoreNodeToAttach.getId());
                attachedXincoCoreNodeListNew.add(xincoCoreNodeListNewXincoCoreNodeToAttach);
            }
            xincoCoreNodeListNew = attachedXincoCoreNodeListNew;
            xincoCoreNode.setXincoCoreNodeList(xincoCoreNodeListNew);
            List<XincoCoreAce> attachedXincoCoreAceListNew = new ArrayList<>();
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAceToAttach : xincoCoreAceListNew) {
                xincoCoreAceListNewXincoCoreAceToAttach = em.getReference(xincoCoreAceListNewXincoCoreAceToAttach.getClass(), xincoCoreAceListNewXincoCoreAceToAttach.getId());
                attachedXincoCoreAceListNew.add(xincoCoreAceListNewXincoCoreAceToAttach);
            }
            xincoCoreAceListNew = attachedXincoCoreAceListNew;
            xincoCoreNode.setXincoCoreAceList(xincoCoreAceListNew);
            List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<>();
            for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
                xincoCoreDataListNewXincoCoreDataToAttach = em.getReference(xincoCoreDataListNewXincoCoreDataToAttach.getClass(), xincoCoreDataListNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
            }
            xincoCoreDataListNew = attachedXincoCoreDataListNew;
            xincoCoreNode.setXincoCoreDataList(xincoCoreDataListNew);
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
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This XincoCoreNode (" + xincoCoreNode + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreNode field.");
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
