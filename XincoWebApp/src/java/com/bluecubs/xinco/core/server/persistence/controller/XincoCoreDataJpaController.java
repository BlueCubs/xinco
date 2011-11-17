/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.*;
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
public class XincoCoreDataJpaController implements Serializable {

    public XincoCoreDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreData xincoCoreData) {
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
            XincoCoreDataType xincoCoreDataType = xincoCoreData.getXincoCoreDataType();
            if (xincoCoreDataType != null) {
                xincoCoreDataType = em.getReference(xincoCoreDataType.getClass(), xincoCoreDataType.getId());
                xincoCoreData.setXincoCoreDataType(xincoCoreDataType);
            }
            XincoCoreLanguage xincoCoreLanguage = xincoCoreData.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage = em.getReference(xincoCoreLanguage.getClass(), xincoCoreLanguage.getId());
                xincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
            }
            XincoCoreNode xincoCoreNode = xincoCoreData.getXincoCoreNode();
            if (xincoCoreNode != null) {
                xincoCoreNode = em.getReference(xincoCoreNode.getClass(), xincoCoreNode.getId());
                xincoCoreData.setXincoCoreNode(xincoCoreNode);
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
            if (xincoCoreDataType != null) {
                xincoCoreDataType.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreDataType = em.merge(xincoCoreDataType);
            }
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            if (xincoCoreNode != null) {
                xincoCoreNode.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreNode = em.merge(xincoCoreNode);
            }
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreData.getXincoCoreAceList()) {
                XincoCoreData oldXincoCoreDataOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreData();
                xincoCoreAceListXincoCoreAce.setXincoCoreData(xincoCoreData);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreDataOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreDataOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreDataOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreDataOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreLog xincoCoreLogListXincoCoreLog : xincoCoreData.getXincoCoreLogList()) {
                XincoCoreData oldXincoCoreDataOfXincoCoreLogListXincoCoreLog = xincoCoreLogListXincoCoreLog.getXincoCoreData();
                xincoCoreLogListXincoCoreLog.setXincoCoreData(xincoCoreData);
                xincoCoreLogListXincoCoreLog = em.merge(xincoCoreLogListXincoCoreLog);
                if (oldXincoCoreDataOfXincoCoreLogListXincoCoreLog != null) {
                    oldXincoCoreDataOfXincoCoreLogListXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListXincoCoreLog);
                    oldXincoCoreDataOfXincoCoreLogListXincoCoreLog = em.merge(oldXincoCoreDataOfXincoCoreLogListXincoCoreLog);
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
            XincoCoreDataType xincoCoreDataTypeOld = persistentXincoCoreData.getXincoCoreDataType();
            XincoCoreDataType xincoCoreDataTypeNew = xincoCoreData.getXincoCoreDataType();
            XincoCoreLanguage xincoCoreLanguageOld = persistentXincoCoreData.getXincoCoreLanguage();
            XincoCoreLanguage xincoCoreLanguageNew = xincoCoreData.getXincoCoreLanguage();
            XincoCoreNode xincoCoreNodeOld = persistentXincoCoreData.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeNew = xincoCoreData.getXincoCoreNode();
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
                    illegalOrphanMessages.add("You must retain XincoCoreLog " + xincoCoreLogListOldXincoCoreLog + " since its xincoCoreData field is not nullable.");
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
            if (xincoCoreDataTypeNew != null) {
                xincoCoreDataTypeNew = em.getReference(xincoCoreDataTypeNew.getClass(), xincoCoreDataTypeNew.getId());
                xincoCoreData.setXincoCoreDataType(xincoCoreDataTypeNew);
            }
            if (xincoCoreLanguageNew != null) {
                xincoCoreLanguageNew = em.getReference(xincoCoreLanguageNew.getClass(), xincoCoreLanguageNew.getId());
                xincoCoreData.setXincoCoreLanguage(xincoCoreLanguageNew);
            }
            if (xincoCoreNodeNew != null) {
                xincoCoreNodeNew = em.getReference(xincoCoreNodeNew.getClass(), xincoCoreNodeNew.getId());
                xincoCoreData.setXincoCoreNode(xincoCoreNodeNew);
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
            if (xincoCoreDataTypeOld != null && !xincoCoreDataTypeOld.equals(xincoCoreDataTypeNew)) {
                xincoCoreDataTypeOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreDataTypeOld = em.merge(xincoCoreDataTypeOld);
            }
            if (xincoCoreDataTypeNew != null && !xincoCoreDataTypeNew.equals(xincoCoreDataTypeOld)) {
                xincoCoreDataTypeNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreDataTypeNew = em.merge(xincoCoreDataTypeNew);
            }
            if (xincoCoreLanguageOld != null && !xincoCoreLanguageOld.equals(xincoCoreLanguageNew)) {
                xincoCoreLanguageOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreLanguageOld = em.merge(xincoCoreLanguageOld);
            }
            if (xincoCoreLanguageNew != null && !xincoCoreLanguageNew.equals(xincoCoreLanguageOld)) {
                xincoCoreLanguageNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreLanguageNew = em.merge(xincoCoreLanguageNew);
            }
            if (xincoCoreNodeOld != null && !xincoCoreNodeOld.equals(xincoCoreNodeNew)) {
                xincoCoreNodeOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreNodeOld = em.merge(xincoCoreNodeOld);
            }
            if (xincoCoreNodeNew != null && !xincoCoreNodeNew.equals(xincoCoreNodeOld)) {
                xincoCoreNodeNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreNodeNew = em.merge(xincoCoreNodeNew);
            }
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreData(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreData oldXincoCoreDataOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreData();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreData(xincoCoreData);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreDataOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreDataOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreData)) {
                        oldXincoCoreDataOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreDataOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreDataOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreLog xincoCoreLogListNewXincoCoreLog : xincoCoreLogListNew) {
                if (!xincoCoreLogListOld.contains(xincoCoreLogListNewXincoCoreLog)) {
                    XincoCoreData oldXincoCoreDataOfXincoCoreLogListNewXincoCoreLog = xincoCoreLogListNewXincoCoreLog.getXincoCoreData();
                    xincoCoreLogListNewXincoCoreLog.setXincoCoreData(xincoCoreData);
                    xincoCoreLogListNewXincoCoreLog = em.merge(xincoCoreLogListNewXincoCoreLog);
                    if (oldXincoCoreDataOfXincoCoreLogListNewXincoCoreLog != null && !oldXincoCoreDataOfXincoCoreLogListNewXincoCoreLog.equals(xincoCoreData)) {
                        oldXincoCoreDataOfXincoCoreLogListNewXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListNewXincoCoreLog);
                        oldXincoCoreDataOfXincoCoreLogListNewXincoCoreLog = em.merge(oldXincoCoreDataOfXincoCoreLogListNewXincoCoreLog);
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
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoCoreLog " + xincoCoreLogListOrphanCheckXincoCoreLog + " in its xincoCoreLogList field has a non-nullable xincoCoreData field.");
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
            XincoCoreDataType xincoCoreDataType = xincoCoreData.getXincoCoreDataType();
            if (xincoCoreDataType != null) {
                xincoCoreDataType.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreDataType = em.merge(xincoCoreDataType);
            }
            XincoCoreLanguage xincoCoreLanguage = xincoCoreData.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            XincoCoreNode xincoCoreNode = xincoCoreData.getXincoCoreNode();
            if (xincoCoreNode != null) {
                xincoCoreNode.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreNode = em.merge(xincoCoreNode);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreData.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreData(null);
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
