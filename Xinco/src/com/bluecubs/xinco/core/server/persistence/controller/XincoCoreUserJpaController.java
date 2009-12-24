/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecord;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserJpaController {

    public XincoCoreUserJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUser xincoCoreUser) throws PreexistingEntityException, Exception {
        if (xincoCoreUser.getXincoCoreAceList() == null) {
            xincoCoreUser.setXincoCoreAceList(new ArrayList<XincoCoreAce>());
        }
        if (xincoCoreUser.getXincoCoreLogList() == null) {
            xincoCoreUser.setXincoCoreLogList(new ArrayList<XincoCoreLog>());
        }
        if (xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1() == null) {
            xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList1(new ArrayList<XincoCoreUserHasXincoCoreGroup>());
        }
        if (xincoCoreUser.getXincoCoreUserModifiedRecordList() == null) {
            xincoCoreUser.setXincoCoreUserModifiedRecordList(new ArrayList<XincoCoreUserModifiedRecord>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
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
            List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupList1 = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroupToAttach : xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1()) {
                xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroupToAttach = em.getReference(xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroupToAttach.getClass(), xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroupToAttach.getXincoCoreUserHasXincoCoreGroupPK());
                attachedXincoCoreUserHasXincoCoreGroupList1.add(xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroupToAttach);
            }
            xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList1(attachedXincoCoreUserHasXincoCoreGroupList1);
            List<XincoCoreUserModifiedRecord> attachedXincoCoreUserModifiedRecordList = new ArrayList<XincoCoreUserModifiedRecord>();
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach : xincoCoreUser.getXincoCoreUserModifiedRecordList()) {
                xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach = em.getReference(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach.getClass(), xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach.getXincoCoreUserModifiedRecordPK());
                attachedXincoCoreUserModifiedRecordList.add(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecordToAttach);
            }
            xincoCoreUser.setXincoCoreUserModifiedRecordList(attachedXincoCoreUserModifiedRecordList);
            em.persist(xincoCoreUser);
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreUser.getXincoCoreAceList()) {
                XincoCoreUser oldXincoCoreUserIdOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreUserId();
                xincoCoreAceListXincoCoreAce.setXincoCoreUserId(xincoCoreUser);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreUserIdOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreUserIdOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreUserIdOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreUserIdOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreLog xincoCoreLogListXincoCoreLog : xincoCoreUser.getXincoCoreLogList()) {
                XincoCoreUser oldXincoCoreUserIdOfXincoCoreLogListXincoCoreLog = xincoCoreLogListXincoCoreLog.getXincoCoreUserId();
                xincoCoreLogListXincoCoreLog.setXincoCoreUserId(xincoCoreUser);
                xincoCoreLogListXincoCoreLog = em.merge(xincoCoreLogListXincoCoreLog);
                if (oldXincoCoreUserIdOfXincoCoreLogListXincoCoreLog != null) {
                    oldXincoCoreUserIdOfXincoCoreLogListXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListXincoCoreLog);
                    oldXincoCoreUserIdOfXincoCoreLogListXincoCoreLog = em.merge(oldXincoCoreUserIdOfXincoCoreLogListXincoCoreLog);
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup : xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1()) {
                XincoCoreUser oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup = xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
                xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUser);
                xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup);
                if (oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup != null) {
                    oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList1().remove(xincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup);
                    oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup = em.merge(oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1XincoCoreUserHasXincoCoreGroup);
                }
            }
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord : xincoCoreUser.getXincoCoreUserModifiedRecordList()) {
                XincoCoreUser oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord = xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord.getXincoCoreUser();
                xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord.setXincoCoreUser(xincoCoreUser);
                xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord = em.merge(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord);
                if (oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord != null) {
                    oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordList().remove(xincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord);
                    oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord = em.merge(oldXincoCoreUserOfXincoCoreUserModifiedRecordListXincoCoreUserModifiedRecord);
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
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreUser.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreUser.getXincoCoreAceList();
            List<XincoCoreLog> xincoCoreLogListOld = persistentXincoCoreUser.getXincoCoreLogList();
            List<XincoCoreLog> xincoCoreLogListNew = xincoCoreUser.getXincoCoreLogList();
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList1Old = persistentXincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1();
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList1New = xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1();
            List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordListOld = persistentXincoCoreUser.getXincoCoreUserModifiedRecordList();
            List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordListNew = xincoCoreUser.getXincoCoreUserModifiedRecordList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreLog xincoCoreLogListOldXincoCoreLog : xincoCoreLogListOld) {
                if (!xincoCoreLogListNew.contains(xincoCoreLogListOldXincoCoreLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreLog " + xincoCoreLogListOldXincoCoreLog + " since its xincoCoreUserId field is not nullable.");
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupList1OldXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupList1Old) {
                if (!xincoCoreUserHasXincoCoreGroupList1New.contains(xincoCoreUserHasXincoCoreGroupList1OldXincoCoreUserHasXincoCoreGroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroupList1OldXincoCoreUserHasXincoCoreGroup + " since its xincoCoreUser field is not nullable.");
                }
            }
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListOldXincoCoreUserModifiedRecord : xincoCoreUserModifiedRecordListOld) {
                if (!xincoCoreUserModifiedRecordListNew.contains(xincoCoreUserModifiedRecordListOldXincoCoreUserModifiedRecord)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreUserModifiedRecord " + xincoCoreUserModifiedRecordListOldXincoCoreUserModifiedRecord + " since its xincoCoreUser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
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
            List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupList1New = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroupToAttach : xincoCoreUserHasXincoCoreGroupList1New) {
                xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroupToAttach = em.getReference(xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroupToAttach.getClass(), xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroupToAttach.getXincoCoreUserHasXincoCoreGroupPK());
                attachedXincoCoreUserHasXincoCoreGroupList1New.add(xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroupToAttach);
            }
            xincoCoreUserHasXincoCoreGroupList1New = attachedXincoCoreUserHasXincoCoreGroupList1New;
            xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList1(xincoCoreUserHasXincoCoreGroupList1New);
            List<XincoCoreUserModifiedRecord> attachedXincoCoreUserModifiedRecordListNew = new ArrayList<XincoCoreUserModifiedRecord>();
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach : xincoCoreUserModifiedRecordListNew) {
                xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach = em.getReference(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach.getClass(), xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach.getXincoCoreUserModifiedRecordPK());
                attachedXincoCoreUserModifiedRecordListNew.add(xincoCoreUserModifiedRecordListNewXincoCoreUserModifiedRecordToAttach);
            }
            xincoCoreUserModifiedRecordListNew = attachedXincoCoreUserModifiedRecordListNew;
            xincoCoreUser.setXincoCoreUserModifiedRecordList(xincoCoreUserModifiedRecordListNew);
            xincoCoreUser = em.merge(xincoCoreUser);
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreUserId(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreUser oldXincoCoreUserIdOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreUserId();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreUserId(xincoCoreUser);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreUserIdOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreUserIdOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreUser)) {
                        oldXincoCoreUserIdOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreUserIdOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreUserIdOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreLog xincoCoreLogListNewXincoCoreLog : xincoCoreLogListNew) {
                if (!xincoCoreLogListOld.contains(xincoCoreLogListNewXincoCoreLog)) {
                    XincoCoreUser oldXincoCoreUserIdOfXincoCoreLogListNewXincoCoreLog = xincoCoreLogListNewXincoCoreLog.getXincoCoreUserId();
                    xincoCoreLogListNewXincoCoreLog.setXincoCoreUserId(xincoCoreUser);
                    xincoCoreLogListNewXincoCoreLog = em.merge(xincoCoreLogListNewXincoCoreLog);
                    if (oldXincoCoreUserIdOfXincoCoreLogListNewXincoCoreLog != null && !oldXincoCoreUserIdOfXincoCoreLogListNewXincoCoreLog.equals(xincoCoreUser)) {
                        oldXincoCoreUserIdOfXincoCoreLogListNewXincoCoreLog.getXincoCoreLogList().remove(xincoCoreLogListNewXincoCoreLog);
                        oldXincoCoreUserIdOfXincoCoreLogListNewXincoCoreLog = em.merge(oldXincoCoreUserIdOfXincoCoreLogListNewXincoCoreLog);
                    }
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupList1New) {
                if (!xincoCoreUserHasXincoCoreGroupList1Old.contains(xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup)) {
                    XincoCoreUser oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup = xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
                    xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUser);
                    xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup);
                    if (oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup != null && !oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup.equals(xincoCoreUser)) {
                        oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList1().remove(xincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup);
                        oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup = em.merge(oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupList1NewXincoCoreUserHasXincoCoreGroup);
                    }
                }
            }
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
            List<XincoCoreLog> xincoCoreLogListOrphanCheck = xincoCoreUser.getXincoCoreLogList();
            for (XincoCoreLog xincoCoreLogListOrphanCheckXincoCoreLog : xincoCoreLogListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreUser (" + xincoCoreUser + ") cannot be destroyed since the XincoCoreLog " + xincoCoreLogListOrphanCheckXincoCoreLog + " in its xincoCoreLogList field has a non-nullable xincoCoreUserId field.");
            }
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList1OrphanCheck = xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupList1OrphanCheckXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreUser (" + xincoCoreUser + ") cannot be destroyed since the XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroupList1OrphanCheckXincoCoreUserHasXincoCoreGroup + " in its xincoCoreUserHasXincoCoreGroupList1 field has a non-nullable xincoCoreUser field.");
            }
            List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordListOrphanCheck = xincoCoreUser.getXincoCoreUserModifiedRecordList();
            for (XincoCoreUserModifiedRecord xincoCoreUserModifiedRecordListOrphanCheckXincoCoreUserModifiedRecord : xincoCoreUserModifiedRecordListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreUser (" + xincoCoreUser + ") cannot be destroyed since the XincoCoreUserModifiedRecord " + xincoCoreUserModifiedRecordListOrphanCheckXincoCoreUserModifiedRecord + " in its xincoCoreUserModifiedRecordList field has a non-nullable xincoCoreUser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreUser.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreUserId(null);
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
