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
 * Name: XincoCoreUserJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserJpaController implements Serializable {

    public XincoCoreUserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUser xincoCoreUser) throws PreexistingEntityException, Exception {
        if (xincoCoreUser.getXincoCoreUserModifiedRecordList() == null) {
            xincoCoreUser.setXincoCoreUserModifiedRecordList(new ArrayList<XincoCoreUserModifiedRecord>());
        }
        if (xincoCoreUser.getXincoCoreAceList() == null) {
            xincoCoreUser.setXincoCoreAceList(new ArrayList<XincoCoreAce>());
        }
        if (xincoCoreUser.getXincoCoreLogList() == null) {
            xincoCoreUser.setXincoCoreLogList(new ArrayList<XincoCoreLog>());
        }
        if (xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList() == null) {
            xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<XincoCoreUserHasXincoCoreGroup>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoCoreUserModifiedRecord> attachedXincoCoreUserModifiedRecordList = new ArrayList<XincoCoreUserModifiedRecord>();
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach : xincoCoreUser.getXincoCoreUserModifiedRecordList()) {
                xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach = em.getReference(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach.getClass(), xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach.getXincoCoreUserModifiedRecordPK());
                attachedXincoCoreUserModifiedRecordList.add(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach);
            }
            xincoCoreUser.setXincoCoreUserModifiedRecordList(attachedXincoCoreUserModifiedRecordList);
            List<XincoCoreAce> attachedXincoCoreAceList = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListXincoCoreAceToAttach : xincoCoreUser.getXincoCoreAceList()) {
                xincoCoreAceListXincoCoreAceToAttach = em.getReference(xincoCoreAceListXincoCoreAceToAttach.getClass(), xincoCoreAceListXincoCoreAceToAttach.getId());
                attachedXincoCoreAceList.add(xincoCoreAceListXincoCoreAceToAttach);
            }
            xincoCoreUser.setXincoCoreAceList(attachedXincoCoreAceList);
            List<XincoCoreLog> attachedXincoCoreLogList = new ArrayList<XincoCoreLog>();
            for (XincoCoreLog xincoCoreLogListXincoCoreLogToAttach : xincoCoreUser.getXincoCoreLogList()) {
                xincoCoreLogListXincoCoreLogToAttach = em.getReference(xincoCoreLogListXincoCoreLogToAttach.getClass(), xincoCoreLogListXincoCoreLogToAttach.getId());
                attachedXincoCoreLogList.add(xincoCoreLogListXincoCoreLogToAttach);
            }
            xincoCoreUser.setXincoCoreLogList(attachedXincoCoreLogList);
            List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupList = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach : xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList()) {
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach = em.getReference(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach.getClass(), xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach.getXincoCoreUserHasXincoCoreGroupPK());
                attachedXincoCoreUserHasXincoCoreGroupList.add(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach);
            }
            xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList(attachedXincoCoreUserHasXincoCoreGroupList);
            em.persist(xincoCoreUser);
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord : xincoCoreUser.getXincoCoreUserModifiedRecordList()) {
                XincoCoreUser oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord = xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord.getXincoCoreUser();
                xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord.setXincoCoreUser(xincoCoreUser);
                xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord = em.merge(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord);
                if (oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord != null) {
                    oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordList().remove(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord);
                    oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord = em.merge(oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord);
                }
            }
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreUser.getXincoCoreAceList()) {
                XincoCoreUser oldXincoCoreUserOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreUser();
                xincoCoreAceListXincoCoreAce.setXincoCoreUser(xincoCoreUser);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreUserOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreUserOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreUserOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreUserOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreLog xincoCoreLogListXincoCoreLog : xincoCoreUser.getXincoCoreLogList()) {
                XincoCoreUser oldXincoCoreUserOfXincoCoreLogListXincoCoreLog = xincoCoreLogListXincoCoreLog.getXincoCoreUser();
                xincoCoreLogListXincoCoreLog.setXincoCoreUser(xincoCoreUser);
                xincoCoreLogListXincoCoreLog = em.merge(xincoCoreLogListXincoCoreLog);
                if (oldXincoCoreUserOfXincoCoreLogListXincoCoreLog != null) {
                    oldXincoCoreUserOfXincoCoreLogListXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListXincoCoreLog);
                    oldXincoCoreUserOfXincoCoreLogListXincoCoreLog = em.merge(oldXincoCoreUserOfXincoCoreLogListXincoCoreLog);
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup : xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList()) {
                XincoCoreUser oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup = xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUser);
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
                if (oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup != null) {
                    oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
                    oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup = em.merge(oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreUser(xincoCoreUser.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreUser " + xincoCoreUser + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreUser xincoCoreUser) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUser persistentXincoCoreUser = em.find(XincoCoreUser.class, xincoCoreUser.getId());
            List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordListOld = persistentXincoCoreUser.getXincoCoreUserModifiedRecordList();
            List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordListNew = xincoCoreUser.getXincoCoreUserModifiedRecordList();
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreUser.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreUser.getXincoCoreAceList();
            List<XincoCoreLog> xincoCoreLogListOld = persistentXincoCoreUser.getXincoCoreLogList();
            List<XincoCoreLog> xincoCoreLogListNew = xincoCoreUser.getXincoCoreLogList();
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListOld = persistentXincoCoreUser.getXincoCoreUserHasXincoCoreGroupList();
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListNew = xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListOldXincoCoreUserModifiedRecord : xincoCoreUserModifiedRecordListOld) {
                if (!xincoCoreUserModifiedRecordListNew.contains(xincoCoreUserModifiedRecordListOldXincoCoreUserModifiedRecord)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreUserModifiedRecord " + xincoCoreUserModifiedRecordListOldXincoCoreUserModifiedRecord + " since its xincoCoreUser field is not nullable.");
                }
            }
            for (XincoCoreLog xincoCoreLogListOldXincoCoreLog : xincoCoreLogListOld) {
                if (!xincoCoreLogListNew.contains(xincoCoreLogListOldXincoCoreLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreLog " + xincoCoreLogListOldXincoCoreLog + " since its xincoCoreUser field is not nullable.");
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupListOld) {
                if (!xincoCoreUserHasXincoCoreGroupListNew.contains(xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup + " since its xincoCoreUser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreUserModifiedRecord> attachedXincoCoreUserModifiedRecordListNew = new ArrayList<XincoCoreUserModifiedRecord>();
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach : xincoCoreUserModifiedRecordListNew) {
                xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach = em.getReference(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach.getClass(), xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach.getXincoCoreUserModifiedRecordPK());
                attachedXincoCoreUserModifiedRecordListNew.add(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach);
            }
            xincoCoreUserModifiedRecordListNew = attachedXincoCoreUserModifiedRecordListNew;
            xincoCoreUser.setXincoCoreUserModifiedRecordList(xincoCoreUserModifiedRecordListNew);
            List<XincoCoreAce> attachedXincoCoreAceListNew = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAceToAttach : xincoCoreAceListNew) {
                xincoCoreAceListNewXincoCoreAceToAttach = em.getReference(xincoCoreAceListNewXincoCoreAceToAttach.getClass(), xincoCoreAceListNewXincoCoreAceToAttach.getId());
                attachedXincoCoreAceListNew.add(xincoCoreAceListNewXincoCoreAceToAttach);
            }
            xincoCoreAceListNew = attachedXincoCoreAceListNew;
            xincoCoreUser.setXincoCoreAceList(xincoCoreAceListNew);
            List<XincoCoreLog> attachedXincoCoreLogListNew = new ArrayList<XincoCoreLog>();
            for (XincoCoreLog xincoCoreLogListNewXincoCoreLogToAttach : xincoCoreLogListNew) {
                xincoCoreLogListNewXincoCoreLogToAttach = em.getReference(xincoCoreLogListNewXincoCoreLogToAttach.getClass(), xincoCoreLogListNewXincoCoreLogToAttach.getId());
                attachedXincoCoreLogListNew.add(xincoCoreLogListNewXincoCoreLogToAttach);
            }
            xincoCoreLogListNew = attachedXincoCoreLogListNew;
            xincoCoreUser.setXincoCoreLogList(xincoCoreLogListNew);
            List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupListNew = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach : xincoCoreUserHasXincoCoreGroupListNew) {
                xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach = em.getReference(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach.getClass(), xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach.getXincoCoreUserHasXincoCoreGroupPK());
                attachedXincoCoreUserHasXincoCoreGroupListNew.add(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach);
            }
            xincoCoreUserHasXincoCoreGroupListNew = attachedXincoCoreUserHasXincoCoreGroupListNew;
            xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList(xincoCoreUserHasXincoCoreGroupListNew);
            xincoCoreUser = em.merge(xincoCoreUser);
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord : xincoCoreUserModifiedRecordListNew) {
                if (!xincoCoreUserModifiedRecordListOld.contains(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord)) {
                    XincoCoreUser oldXincoCoreUserOfXincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord = xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord.getXincoCoreUser();
                    xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord.setXincoCoreUser(xincoCoreUser);
                    xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord = em.merge(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord);
                    if (oldXincoCoreUserOfXincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord != null && !oldXincoCoreUserOfXincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord.equals(xincoCoreUser)) {
                        oldXincoCoreUserOfXincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordList().remove(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord);
                        oldXincoCoreUserOfXincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord = em.merge(oldXincoCoreUserOfXincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecord);
                    }
                }
            }
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreUser(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreUser oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreUser();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreUser(xincoCoreUser);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreUser)) {
                        oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreLog xincoCoreLogListNewXincoCoreLog : xincoCoreLogListNew) {
                if (!xincoCoreLogListOld.contains(xincoCoreLogListNewXincoCoreLog)) {
                    XincoCoreUser oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog = xincoCoreLogListNewXincoCoreLog.getXincoCoreUser();
                    xincoCoreLogListNewXincoCoreLog.setXincoCoreUser(xincoCoreUser);
                    xincoCoreLogListNewXincoCoreLog = em.merge(xincoCoreLogListNewXincoCoreLog);
                    if (oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog != null && !oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog.equals(xincoCoreUser)) {
                        oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListNewXincoCoreLog);
                        oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog = em.merge(oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog);
                    }
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupListNew) {
                if (!xincoCoreUserHasXincoCoreGroupListOld.contains(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup)) {
                    XincoCoreUser oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup = xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
                    xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUser);
                    xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
                    if (oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup != null && !oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.equals(xincoCoreUser)) {
                        oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
                        oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup = em.merge(oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreUser.getId();
                if (findXincoCoreUser(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreUser with id " + id + " no longer exists.");
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
            XincoCoreUser xincoCoreUser;
            try {
                xincoCoreUser = em.getReference(XincoCoreUser.class, id);
                xincoCoreUser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreUser with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordListOrphanCheck = xincoCoreUser.getXincoCoreUserModifiedRecordList();
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListOrphanCheckXincoCoreUserModifiedRecord : xincoCoreUserModifiedRecordListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreUser (" + xincoCoreUser + ") cannot be destroyed since the XincoCoreUserModifiedRecord " + xincoCoreUserModifiedRecordListOrphanCheckXincoCoreUserModifiedRecord + " in its xincoCoreUserModifiedRecordList field has a non-nullable xincoCoreUser field.");
            }
            List<XincoCoreLog> xincoCoreLogListOrphanCheck = xincoCoreUser.getXincoCoreLogList();
            for (XincoCoreLog xincoCoreLogListOrphanCheckXincoCoreLog : xincoCoreLogListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreUser (" + xincoCoreUser + ") cannot be destroyed since the XincoCoreLog " + xincoCoreLogListOrphanCheckXincoCoreLog + " in its xincoCoreLogList field has a non-nullable xincoCoreUser field.");
            }
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListOrphanCheck = xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListOrphanCheckXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreUser (" + xincoCoreUser + ") cannot be destroyed since the XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroupListOrphanCheckXincoCoreUserHasXincoCoreGroup + " in its xincoCoreUserHasXincoCoreGroupList field has a non-nullable xincoCoreUser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreUser.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreUser(null);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
            }
            em.remove(xincoCoreUser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreUser> findXincoCoreUserEntities() {
        return findXincoCoreUserEntities(true, -1, -1);
    }

    public List<XincoCoreUser> findXincoCoreUserEntities(int maxResults, int firstResult) {
        return findXincoCoreUserEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreUser> findXincoCoreUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreUser.class));
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

    public XincoCoreUser findXincoCoreUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreUser.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreUser> rt = cq.from(XincoCoreUser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
