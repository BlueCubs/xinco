/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
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

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataJpaController {

    public XincoCoreDataJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
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
