/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
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
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import java.util.Collection;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataJpaController implements Serializable {

    public XincoCoreDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreData xincoCoreData) throws PreexistingEntityException, Exception {
        if (xincoCoreData.getXincoCoreAceList() == null) {
            xincoCoreData.setXincoCoreAceList(new ArrayList<XincoCoreAce>());
        }
        if (xincoCoreData.getXincoCoreLogList() == null) {
            xincoCoreData.setXincoCoreLogList(new ArrayList<XincoCoreLog>());
        }
        if (xincoCoreData.getXincoAddAttributeList() == null) {
            xincoCoreData.setXincoAddAttributeList(new ArrayList<XincoAddAttribute>());
        }
        if (xincoCoreData.getXincoCoreDataHasDependencyCollection() == null) {
            xincoCoreData.setXincoCoreDataHasDependencyCollection(new ArrayList<XincoCoreDataHasDependency>());
        }
        if (xincoCoreData.getXincoCoreDataHasDependencyCollection1() == null) {
            xincoCoreData.setXincoCoreDataHasDependencyCollection1(new ArrayList<XincoCoreDataHasDependency>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataType xincoCoreDataTypeId = xincoCoreData.getXincoCoreDataTypeId();
            if (xincoCoreDataTypeId != null) {
                xincoCoreDataTypeId = em.getReference(xincoCoreDataTypeId.getClass(), xincoCoreDataTypeId.getId());
                xincoCoreData.setXincoCoreDataTypeId(xincoCoreDataTypeId);
            }
            XincoCoreLanguage xincoCoreLanguageId = xincoCoreData.getXincoCoreLanguageId();
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId = em.getReference(xincoCoreLanguageId.getClass(), xincoCoreLanguageId.getId());
                xincoCoreData.setXincoCoreLanguageId(xincoCoreLanguageId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreData.getXincoCoreNodeId();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId = em.getReference(xincoCoreNodeId.getClass(), xincoCoreNodeId.getId());
                xincoCoreData.setXincoCoreNodeId(xincoCoreNodeId);
            }
            List<XincoCoreAce> attachedXincoCoreAceList = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListXincoCoreAceToAttach : xincoCoreData.getXincoCoreAceList()) {
                xincoCoreAceListXincoCoreAceToAttach = em.getReference(xincoCoreAceListXincoCoreAceToAttach.getClass(), xincoCoreAceListXincoCoreAceToAttach.getId());
                attachedXincoCoreAceList.add(xincoCoreAceListXincoCoreAceToAttach);
            }
            xincoCoreData.setXincoCoreAceList(attachedXincoCoreAceList);
            List<XincoCoreLog> attachedXincoCoreLogList = new ArrayList<XincoCoreLog>();
            for (XincoCoreLog xincoCoreLogListXincoCoreLogToAttach : xincoCoreData.getXincoCoreLogList()) {
                xincoCoreLogListXincoCoreLogToAttach = em.getReference(xincoCoreLogListXincoCoreLogToAttach.getClass(), xincoCoreLogListXincoCoreLogToAttach.getId());
                attachedXincoCoreLogList.add(xincoCoreLogListXincoCoreLogToAttach);
            }
            xincoCoreData.setXincoCoreLogList(attachedXincoCoreLogList);
            List<XincoAddAttribute> attachedXincoAddAttributeList = new ArrayList<XincoAddAttribute>();
            for (XincoAddAttribute xincoAddAttributeListXincoAddAttributeToAttach : xincoCoreData.getXincoAddAttributeList()) {
                xincoAddAttributeListXincoAddAttributeToAttach = em.getReference(xincoAddAttributeListXincoAddAttributeToAttach.getClass(), xincoAddAttributeListXincoAddAttributeToAttach.getXincoAddAttributePK());
                attachedXincoAddAttributeList.add(xincoAddAttributeListXincoAddAttributeToAttach);
            }
            xincoCoreData.setXincoAddAttributeList(attachedXincoAddAttributeList);
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollection = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach : xincoCoreData.getXincoCoreDataHasDependencyCollection()) {
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollection.add(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreData.setXincoCoreDataHasDependencyCollection(attachedXincoCoreDataHasDependencyCollection);
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollection1 = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependencyToAttach : xincoCoreData.getXincoCoreDataHasDependencyCollection1()) {
                xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollection1.add(xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependencyToAttach);
            }
            xincoCoreData.setXincoCoreDataHasDependencyCollection1(attachedXincoCoreDataHasDependencyCollection1);
            em.persist(xincoCoreData);
            if (xincoCoreDataTypeId != null) {
                xincoCoreDataTypeId.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreDataTypeId = em.merge(xincoCoreDataTypeId);
            }
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreLanguageId = em.merge(xincoCoreLanguageId);
            }
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreData.getXincoCoreAceList()) {
                XincoCoreData oldXincoCoreDataIdOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreDataId();
                xincoCoreAceListXincoCoreAce.setXincoCoreDataId(xincoCoreData);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreDataIdOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreDataIdOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreDataIdOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreDataIdOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreLog xincoCoreLogListXincoCoreLog : xincoCoreData.getXincoCoreLogList()) {
                XincoCoreData oldXincoCoreDataIdOfXincoCoreLogListXincoCoreLog = xincoCoreLogListXincoCoreLog.getXincoCoreDataId();
                xincoCoreLogListXincoCoreLog.setXincoCoreDataId(xincoCoreData);
                xincoCoreLogListXincoCoreLog = em.merge(xincoCoreLogListXincoCoreLog);
                if (oldXincoCoreDataIdOfXincoCoreLogListXincoCoreLog != null) {
                    oldXincoCoreDataIdOfXincoCoreLogListXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListXincoCoreLog);
                    oldXincoCoreDataIdOfXincoCoreLogListXincoCoreLog = em.merge(oldXincoCoreDataIdOfXincoCoreLogListXincoCoreLog);
                }
            }
            for (XincoAddAttribute xincoAddAttributeListXincoAddAttribute : xincoCoreData.getXincoAddAttributeList()) {
                XincoCoreData oldXincoCoreDataOfXincoAddAttributeListXincoAddAttribute = xincoAddAttributeListXincoAddAttribute.getXincoCoreData();
                xincoAddAttributeListXincoAddAttribute.setXincoCoreData(xincoCoreData);
                xincoAddAttributeListXincoAddAttribute = em.merge(xincoAddAttributeListXincoAddAttribute);
                if (oldXincoCoreDataOfXincoAddAttributeListXincoAddAttribute != null) {
                    oldXincoCoreDataOfXincoAddAttributeListXincoAddAttribute.getXincoAddAttributeList().remove(xincoAddAttributeListXincoAddAttribute);
                    oldXincoCoreDataOfXincoAddAttributeListXincoAddAttribute = em.merge(oldXincoCoreDataOfXincoAddAttributeListXincoAddAttribute);
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency : xincoCoreData.getXincoCoreDataHasDependencyCollection()) {
                XincoCoreData oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency = xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency.getXincoCoreData();
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency.setXincoCoreData(xincoCoreData);
                xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency);
                if (oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency != null) {
                    oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency.getXincoCoreDataHasDependencyCollection().remove(xincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency);
                    oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency = em.merge(oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionXincoCoreDataHasDependency);
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency : xincoCoreData.getXincoCoreDataHasDependencyCollection1()) {
                XincoCoreData oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency = xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency.getXincoCoreData1();
                xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData);
                xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency);
                if (oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency != null) {
                    oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency.getXincoCoreDataHasDependencyCollection1().remove(xincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency);
                    oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency = em.merge(oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1XincoCoreDataHasDependency);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreData(xincoCoreData.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreData " + xincoCoreData + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreData xincoCoreData) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreData persistentXincoCoreData = em.find(XincoCoreData.class, xincoCoreData.getId());
            XincoCoreDataType xincoCoreDataTypeIdOld = persistentXincoCoreData.getXincoCoreDataTypeId();
            XincoCoreDataType xincoCoreDataTypeIdNew = xincoCoreData.getXincoCoreDataTypeId();
            XincoCoreLanguage xincoCoreLanguageIdOld = persistentXincoCoreData.getXincoCoreLanguageId();
            XincoCoreLanguage xincoCoreLanguageIdNew = xincoCoreData.getXincoCoreLanguageId();
            XincoCoreNode xincoCoreNodeIdOld = persistentXincoCoreData.getXincoCoreNodeId();
            XincoCoreNode xincoCoreNodeIdNew = xincoCoreData.getXincoCoreNodeId();
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreData.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreData.getXincoCoreAceList();
            List<XincoCoreLog> xincoCoreLogListOld = persistentXincoCoreData.getXincoCoreLogList();
            List<XincoCoreLog> xincoCoreLogListNew = xincoCoreData.getXincoCoreLogList();
            List<XincoAddAttribute> xincoAddAttributeListOld = persistentXincoCoreData.getXincoAddAttributeList();
            List<XincoAddAttribute> xincoAddAttributeListNew = xincoCoreData.getXincoAddAttributeList();
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollectionOld = persistentXincoCoreData.getXincoCoreDataHasDependencyCollection();
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollectionNew = xincoCoreData.getXincoCoreDataHasDependencyCollection();
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollection1Old = persistentXincoCoreData.getXincoCoreDataHasDependencyCollection1();
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollection1New = xincoCoreData.getXincoCoreDataHasDependencyCollection1();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreLog xincoCoreLogListOldXincoCoreLog : xincoCoreLogListOld) {
                if (!xincoCoreLogListNew.contains(xincoCoreLogListOldXincoCoreLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreLog " + xincoCoreLogListOldXincoCoreLog + " since its xincoCoreDataId field is not nullable.");
                }
            }
            for (XincoAddAttribute xincoAddAttributeListOldXincoAddAttribute : xincoAddAttributeListOld) {
                if (!xincoAddAttributeListNew.contains(xincoAddAttributeListOldXincoAddAttribute)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoAddAttribute " + xincoAddAttributeListOldXincoAddAttribute + " since its xincoCoreData field is not nullable.");
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionOldXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollectionOld) {
                if (!xincoCoreDataHasDependencyCollectionNew.contains(xincoCoreDataHasDependencyCollectionOldXincoCoreDataHasDependency)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataHasDependency " + xincoCoreDataHasDependencyCollectionOldXincoCoreDataHasDependency + " since its xincoCoreData field is not nullable.");
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollection1OldXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollection1Old) {
                if (!xincoCoreDataHasDependencyCollection1New.contains(xincoCoreDataHasDependencyCollection1OldXincoCoreDataHasDependency)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataHasDependency " + xincoCoreDataHasDependencyCollection1OldXincoCoreDataHasDependency + " since its xincoCoreData1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoCoreDataTypeIdNew != null) {
                xincoCoreDataTypeIdNew = em.getReference(xincoCoreDataTypeIdNew.getClass(), xincoCoreDataTypeIdNew.getId());
                xincoCoreData.setXincoCoreDataTypeId(xincoCoreDataTypeIdNew);
            }
            if (xincoCoreLanguageIdNew != null) {
                xincoCoreLanguageIdNew = em.getReference(xincoCoreLanguageIdNew.getClass(), xincoCoreLanguageIdNew.getId());
                xincoCoreData.setXincoCoreLanguageId(xincoCoreLanguageIdNew);
            }
            if (xincoCoreNodeIdNew != null) {
                xincoCoreNodeIdNew = em.getReference(xincoCoreNodeIdNew.getClass(), xincoCoreNodeIdNew.getId());
                xincoCoreData.setXincoCoreNodeId(xincoCoreNodeIdNew);
            }
            List<XincoCoreAce> attachedXincoCoreAceListNew = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAceToAttach : xincoCoreAceListNew) {
                xincoCoreAceListNewXincoCoreAceToAttach = em.getReference(xincoCoreAceListNewXincoCoreAceToAttach.getClass(), xincoCoreAceListNewXincoCoreAceToAttach.getId());
                attachedXincoCoreAceListNew.add(xincoCoreAceListNewXincoCoreAceToAttach);
            }
            xincoCoreAceListNew = attachedXincoCoreAceListNew;
            xincoCoreData.setXincoCoreAceList(xincoCoreAceListNew);
            List<XincoCoreLog> attachedXincoCoreLogListNew = new ArrayList<XincoCoreLog>();
            for (XincoCoreLog xincoCoreLogListNewXincoCoreLogToAttach : xincoCoreLogListNew) {
                xincoCoreLogListNewXincoCoreLogToAttach = em.getReference(xincoCoreLogListNewXincoCoreLogToAttach.getClass(), xincoCoreLogListNewXincoCoreLogToAttach.getId());
                attachedXincoCoreLogListNew.add(xincoCoreLogListNewXincoCoreLogToAttach);
            }
            xincoCoreLogListNew = attachedXincoCoreLogListNew;
            xincoCoreData.setXincoCoreLogList(xincoCoreLogListNew);
            List<XincoAddAttribute> attachedXincoAddAttributeListNew = new ArrayList<XincoAddAttribute>();
            for (XincoAddAttribute xincoAddAttributeListNewXincoAddAttributeToAttach : xincoAddAttributeListNew) {
                xincoAddAttributeListNewXincoAddAttributeToAttach = em.getReference(xincoAddAttributeListNewXincoAddAttributeToAttach.getClass(), xincoAddAttributeListNewXincoAddAttributeToAttach.getXincoAddAttributePK());
                attachedXincoAddAttributeListNew.add(xincoAddAttributeListNewXincoAddAttributeToAttach);
            }
            xincoAddAttributeListNew = attachedXincoAddAttributeListNew;
            xincoCoreData.setXincoAddAttributeList(xincoAddAttributeListNew);
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollectionNew = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyCollectionNew) {
                xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollectionNew.add(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyCollectionNew = attachedXincoCoreDataHasDependencyCollectionNew;
            xincoCoreData.setXincoCoreDataHasDependencyCollection(xincoCoreDataHasDependencyCollectionNew);
            Collection<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyCollection1New = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyCollection1New) {
                xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyCollection1New.add(xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyCollection1New = attachedXincoCoreDataHasDependencyCollection1New;
            xincoCoreData.setXincoCoreDataHasDependencyCollection1(xincoCoreDataHasDependencyCollection1New);
            xincoCoreData = em.merge(xincoCoreData);
            if (xincoCoreDataTypeIdOld != null && !xincoCoreDataTypeIdOld.equals(xincoCoreDataTypeIdNew)) {
                xincoCoreDataTypeIdOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreDataTypeIdOld = em.merge(xincoCoreDataTypeIdOld);
            }
            if (xincoCoreDataTypeIdNew != null && !xincoCoreDataTypeIdNew.equals(xincoCoreDataTypeIdOld)) {
                xincoCoreDataTypeIdNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreDataTypeIdNew = em.merge(xincoCoreDataTypeIdNew);
            }
            if (xincoCoreLanguageIdOld != null && !xincoCoreLanguageIdOld.equals(xincoCoreLanguageIdNew)) {
                xincoCoreLanguageIdOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreLanguageIdOld = em.merge(xincoCoreLanguageIdOld);
            }
            if (xincoCoreLanguageIdNew != null && !xincoCoreLanguageIdNew.equals(xincoCoreLanguageIdOld)) {
                xincoCoreLanguageIdNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreLanguageIdNew = em.merge(xincoCoreLanguageIdNew);
            }
            if (xincoCoreNodeIdOld != null && !xincoCoreNodeIdOld.equals(xincoCoreNodeIdNew)) {
                xincoCoreNodeIdOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreNodeIdOld = em.merge(xincoCoreNodeIdOld);
            }
            if (xincoCoreNodeIdNew != null && !xincoCoreNodeIdNew.equals(xincoCoreNodeIdOld)) {
                xincoCoreNodeIdNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreNodeIdNew = em.merge(xincoCoreNodeIdNew);
            }
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreDataId(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreData oldXincoCoreDataIdOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreDataId();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreDataId(xincoCoreData);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreDataIdOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreDataIdOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreData)) {
                        oldXincoCoreDataIdOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreDataIdOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreDataIdOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreLog xincoCoreLogListNewXincoCoreLog : xincoCoreLogListNew) {
                if (!xincoCoreLogListOld.contains(xincoCoreLogListNewXincoCoreLog)) {
                    XincoCoreData oldXincoCoreDataIdOfXincoCoreLogListNewXincoCoreLog = xincoCoreLogListNewXincoCoreLog.getXincoCoreDataId();
                    xincoCoreLogListNewXincoCoreLog.setXincoCoreDataId(xincoCoreData);
                    xincoCoreLogListNewXincoCoreLog = em.merge(xincoCoreLogListNewXincoCoreLog);
                    if (oldXincoCoreDataIdOfXincoCoreLogListNewXincoCoreLog != null && !oldXincoCoreDataIdOfXincoCoreLogListNewXincoCoreLog.equals(xincoCoreData)) {
                        oldXincoCoreDataIdOfXincoCoreLogListNewXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListNewXincoCoreLog);
                        oldXincoCoreDataIdOfXincoCoreLogListNewXincoCoreLog = em.merge(oldXincoCoreDataIdOfXincoCoreLogListNewXincoCoreLog);
                    }
                }
            }
            for (XincoAddAttribute xincoAddAttributeListNewXincoAddAttribute : xincoAddAttributeListNew) {
                if (!xincoAddAttributeListOld.contains(xincoAddAttributeListNewXincoAddAttribute)) {
                    XincoCoreData oldXincoCoreDataOfXincoAddAttributeListNewXincoAddAttribute = xincoAddAttributeListNewXincoAddAttribute.getXincoCoreData();
                    xincoAddAttributeListNewXincoAddAttribute.setXincoCoreData(xincoCoreData);
                    xincoAddAttributeListNewXincoAddAttribute = em.merge(xincoAddAttributeListNewXincoAddAttribute);
                    if (oldXincoCoreDataOfXincoAddAttributeListNewXincoAddAttribute != null && !oldXincoCoreDataOfXincoAddAttributeListNewXincoAddAttribute.equals(xincoCoreData)) {
                        oldXincoCoreDataOfXincoAddAttributeListNewXincoAddAttribute.getXincoAddAttributeList().remove(xincoAddAttributeListNewXincoAddAttribute);
                        oldXincoCoreDataOfXincoAddAttributeListNewXincoAddAttribute = em.merge(oldXincoCoreDataOfXincoAddAttributeListNewXincoAddAttribute);
                    }
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollectionNew) {
                if (!xincoCoreDataHasDependencyCollectionOld.contains(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency)) {
                    XincoCoreData oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency = xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.getXincoCoreData();
                    xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.setXincoCoreData(xincoCoreData);
                    xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency);
                    if (oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency != null && !oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.equals(xincoCoreData)) {
                        oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency.getXincoCoreDataHasDependencyCollection().remove(xincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency);
                        oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency = em.merge(oldXincoCoreDataOfXincoCoreDataHasDependencyCollectionNewXincoCoreDataHasDependency);
                    }
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollection1New) {
                if (!xincoCoreDataHasDependencyCollection1Old.contains(xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency)) {
                    XincoCoreData oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency = xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency.getXincoCoreData1();
                    xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData);
                    xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency);
                    if (oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency != null && !oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency.equals(xincoCoreData)) {
                        oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency.getXincoCoreDataHasDependencyCollection1().remove(xincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency);
                        oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency = em.merge(oldXincoCoreData1OfXincoCoreDataHasDependencyCollection1NewXincoCoreDataHasDependency);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreData.getId();
                if (findXincoCoreData(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreData with id " + id + " no longer exists.");
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
            XincoCoreData xincoCoreData;
            try {
                xincoCoreData = em.getReference(XincoCoreData.class, id);
                xincoCoreData.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreData with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreLog> xincoCoreLogListOrphanCheck = xincoCoreData.getXincoCoreLogList();
            for (XincoCoreLog xincoCoreLogListOrphanCheckXincoCoreLog : xincoCoreLogListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoCoreLog " + xincoCoreLogListOrphanCheckXincoCoreLog + " in its xincoCoreLogList field has a non-nullable xincoCoreDataId field.");
            }
            List<XincoAddAttribute> xincoAddAttributeListOrphanCheck = xincoCoreData.getXincoAddAttributeList();
            for (XincoAddAttribute xincoAddAttributeListOrphanCheckXincoAddAttribute : xincoAddAttributeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoAddAttribute " + xincoAddAttributeListOrphanCheckXincoAddAttribute + " in its xincoAddAttributeList field has a non-nullable xincoCoreData field.");
            }
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollectionOrphanCheck = xincoCoreData.getXincoCoreDataHasDependencyCollection();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollectionOrphanCheckXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoCoreDataHasDependency " + xincoCoreDataHasDependencyCollectionOrphanCheckXincoCoreDataHasDependency + " in its xincoCoreDataHasDependencyCollection field has a non-nullable xincoCoreData field.");
            }
            Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollection1OrphanCheck = xincoCoreData.getXincoCoreDataHasDependencyCollection1();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyCollection1OrphanCheckXincoCoreDataHasDependency : xincoCoreDataHasDependencyCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoCoreDataHasDependency " + xincoCoreDataHasDependencyCollection1OrphanCheckXincoCoreDataHasDependency + " in its xincoCoreDataHasDependencyCollection1 field has a non-nullable xincoCoreData1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoCoreDataType xincoCoreDataTypeId = xincoCoreData.getXincoCoreDataTypeId();
            if (xincoCoreDataTypeId != null) {
                xincoCoreDataTypeId.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreDataTypeId = em.merge(xincoCoreDataTypeId);
            }
            XincoCoreLanguage xincoCoreLanguageId = xincoCoreData.getXincoCoreLanguageId();
            if (xincoCoreLanguageId != null) {
                xincoCoreLanguageId.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreLanguageId = em.merge(xincoCoreLanguageId);
            }
            XincoCoreNode xincoCoreNodeId = xincoCoreData.getXincoCoreNodeId();
            if (xincoCoreNodeId != null) {
                xincoCoreNodeId.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreNodeId = em.merge(xincoCoreNodeId);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreData.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreDataId(null);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
            }
            em.remove(xincoCoreData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreData> findXincoCoreDataEntities() {
        return findXincoCoreDataEntities(true, -1, -1);
    }

    public List<XincoCoreData> findXincoCoreDataEntities(int maxResults, int firstResult) {
        return findXincoCoreDataEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreData> findXincoCoreDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreData.class));
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

    public XincoCoreData findXincoCoreData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreData.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreData> rt = cq.from(XincoCoreData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
