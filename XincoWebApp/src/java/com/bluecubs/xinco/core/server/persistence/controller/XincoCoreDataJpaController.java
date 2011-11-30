/*
 * Copyright 2011 blueCubs.com.
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
 * Name: XincoCoreDataJpaController
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.*;
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
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
        if (xincoCoreData.getXincoCoreDataHasDependencyList() == null) {
            xincoCoreData.setXincoCoreDataHasDependencyList(new ArrayList<XincoCoreDataHasDependency>());
        }
        if (xincoCoreData.getXincoCoreDataHasDependencyList1() == null) {
            xincoCoreData.setXincoCoreDataHasDependencyList1(new ArrayList<XincoCoreDataHasDependency>());
        }
        if (xincoCoreData.getXincoCoreAceList() == null) {
            xincoCoreData.setXincoCoreAceList(new ArrayList<XincoCoreAce>());
        }
        if (xincoCoreData.getXincoCoreLogList() == null) {
            xincoCoreData.setXincoCoreLogList(new ArrayList<XincoCoreLog>());
        }
        if (xincoCoreData.getXincoAddAttributeList() == null) {
            xincoCoreData.setXincoAddAttributeList(new ArrayList<XincoAddAttribute>());
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
            List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyList = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach : xincoCoreData.getXincoCoreDataHasDependencyList()) {
                xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyList.add(xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreData.setXincoCoreDataHasDependencyList(attachedXincoCoreDataHasDependencyList);
            List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyList1 = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyList1XincoCoreDataHasDependencyToAttach : xincoCoreData.getXincoCoreDataHasDependencyList1()) {
                xincoCoreDataHasDependencyList1XincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyList1XincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyList1XincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyList1.add(xincoCoreDataHasDependencyList1XincoCoreDataHasDependencyToAttach);
            }
            xincoCoreData.setXincoCoreDataHasDependencyList1(attachedXincoCoreDataHasDependencyList1);
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
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListXincoCoreDataHasDependency : xincoCoreData.getXincoCoreDataHasDependencyList()) {
                XincoCoreData oldXincoCoreDataOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency = xincoCoreDataHasDependencyListXincoCoreDataHasDependency.getXincoCoreData();
                xincoCoreDataHasDependencyListXincoCoreDataHasDependency.setXincoCoreData(xincoCoreData);
                xincoCoreDataHasDependencyListXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyListXincoCoreDataHasDependency);
                if (oldXincoCoreDataOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency != null) {
                    oldXincoCoreDataOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependencyListXincoCoreDataHasDependency);
                    oldXincoCoreDataOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency = em.merge(oldXincoCoreDataOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency);
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyList1XincoCoreDataHasDependency : xincoCoreData.getXincoCoreDataHasDependencyList1()) {
                XincoCoreData oldXincoCoreData1OfXincoCoreDataHasDependencyList1XincoCoreDataHasDependency = xincoCoreDataHasDependencyList1XincoCoreDataHasDependency.getXincoCoreData1();
                xincoCoreDataHasDependencyList1XincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData);
                xincoCoreDataHasDependencyList1XincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyList1XincoCoreDataHasDependency);
                if (oldXincoCoreData1OfXincoCoreDataHasDependencyList1XincoCoreDataHasDependency != null) {
                    oldXincoCoreData1OfXincoCoreDataHasDependencyList1XincoCoreDataHasDependency.getXincoCoreDataHasDependencyList1().remove(xincoCoreDataHasDependencyList1XincoCoreDataHasDependency);
                    oldXincoCoreData1OfXincoCoreDataHasDependencyList1XincoCoreDataHasDependency = em.merge(oldXincoCoreData1OfXincoCoreDataHasDependencyList1XincoCoreDataHasDependency);
                }
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
            XincoCoreDataType xincoCoreDataTypeOld = persistentXincoCoreData.getXincoCoreDataType();
            XincoCoreDataType xincoCoreDataTypeNew = xincoCoreData.getXincoCoreDataType();
            XincoCoreLanguage xincoCoreLanguageOld = persistentXincoCoreData.getXincoCoreLanguage();
            XincoCoreLanguage xincoCoreLanguageNew = xincoCoreData.getXincoCoreLanguage();
            XincoCoreNode xincoCoreNodeOld = persistentXincoCoreData.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeNew = xincoCoreData.getXincoCoreNode();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOld = persistentXincoCoreData.getXincoCoreDataHasDependencyList();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListNew = xincoCoreData.getXincoCoreDataHasDependencyList();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1Old = persistentXincoCoreData.getXincoCoreDataHasDependencyList1();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1New = xincoCoreData.getXincoCoreDataHasDependencyList1();
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreData.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreData.getXincoCoreAceList();
            List<XincoCoreLog> xincoCoreLogListOld = persistentXincoCoreData.getXincoCoreLogList();
            List<XincoCoreLog> xincoCoreLogListNew = xincoCoreData.getXincoCoreLogList();
            List<XincoAddAttribute> xincoAddAttributeListOld = persistentXincoCoreData.getXincoAddAttributeList();
            List<XincoAddAttribute> xincoAddAttributeListNew = xincoCoreData.getXincoAddAttributeList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency : xincoCoreDataHasDependencyListOld) {
                if (!xincoCoreDataHasDependencyListNew.contains(xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataHasDependency " + xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency + " since its xincoCoreData field is not nullable.");
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyList1OldXincoCoreDataHasDependency : xincoCoreDataHasDependencyList1Old) {
                if (!xincoCoreDataHasDependencyList1New.contains(xincoCoreDataHasDependencyList1OldXincoCoreDataHasDependency)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataHasDependency " + xincoCoreDataHasDependencyList1OldXincoCoreDataHasDependency + " since its xincoCoreData1 field is not nullable.");
                }
            }
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
            List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyListNew = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyListNew) {
                xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyListNew.add(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyListNew = attachedXincoCoreDataHasDependencyListNew;
            xincoCoreData.setXincoCoreDataHasDependencyList(xincoCoreDataHasDependencyListNew);
            List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyList1New = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyList1New) {
                xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyList1New.add(xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyList1New = attachedXincoCoreDataHasDependencyList1New;
            xincoCoreData.setXincoCoreDataHasDependencyList1(xincoCoreDataHasDependencyList1New);
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
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency : xincoCoreDataHasDependencyListNew) {
                if (!xincoCoreDataHasDependencyListOld.contains(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency)) {
                    XincoCoreData oldXincoCoreDataOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency = xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.getXincoCoreData();
                    xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.setXincoCoreData(xincoCoreData);
                    xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
                    if (oldXincoCoreDataOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency != null && !oldXincoCoreDataOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.equals(xincoCoreData)) {
                        oldXincoCoreDataOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
                        oldXincoCoreDataOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency = em.merge(oldXincoCoreDataOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
                    }
                }
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency : xincoCoreDataHasDependencyList1New) {
                if (!xincoCoreDataHasDependencyList1Old.contains(xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency)) {
                    XincoCoreData oldXincoCoreData1OfXincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency = xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency.getXincoCoreData1();
                    xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData);
                    xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency);
                    if (oldXincoCoreData1OfXincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency != null && !oldXincoCoreData1OfXincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency.equals(xincoCoreData)) {
                        oldXincoCoreData1OfXincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency.getXincoCoreDataHasDependencyList1().remove(xincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency);
                        oldXincoCoreData1OfXincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency = em.merge(oldXincoCoreData1OfXincoCoreDataHasDependencyList1NewXincoCoreDataHasDependency);
                    }
                }
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
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOrphanCheck = xincoCoreData.getXincoCoreDataHasDependencyList();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListOrphanCheckXincoCoreDataHasDependency : xincoCoreDataHasDependencyListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoCoreDataHasDependency " + xincoCoreDataHasDependencyListOrphanCheckXincoCoreDataHasDependency + " in its xincoCoreDataHasDependencyList field has a non-nullable xincoCoreData field.");
            }
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1OrphanCheck = xincoCoreData.getXincoCoreDataHasDependencyList1();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyList1OrphanCheckXincoCoreDataHasDependency : xincoCoreDataHasDependencyList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreData (" + xincoCoreData + ") cannot be destroyed since the XincoCoreDataHasDependency " + xincoCoreDataHasDependencyList1OrphanCheckXincoCoreDataHasDependency + " in its xincoCoreDataHasDependencyList1 field has a non-nullable xincoCoreData1 field.");
            }
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
