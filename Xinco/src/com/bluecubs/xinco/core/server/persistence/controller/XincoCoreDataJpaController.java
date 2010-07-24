/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import java.util.ArrayList;
import java.util.List;

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

    public void create(XincoCoreData xincoCoreData) {
        if (xincoCoreData.getXincoCoreDataHasDependencyList() == null) {
            xincoCoreData.setXincoCoreDataHasDependencyList(new ArrayList<XincoCoreDataHasDependency>());
        }
        if (xincoCoreData.getXincoCoreDataHasDependencyList1() == null) {
            xincoCoreData.setXincoCoreDataHasDependencyList1(new ArrayList<XincoCoreDataHasDependency>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLanguage xincoCoreLanguage = xincoCoreData.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage = em.getReference(xincoCoreLanguage.getClass(), xincoCoreLanguage.getId());
                xincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
            }
            XincoCoreDataType xincoCoreDataType = xincoCoreData.getXincoCoreDataType();
            if (xincoCoreDataType != null) {
                xincoCoreDataType = em.getReference(xincoCoreDataType.getClass(), xincoCoreDataType.getId());
                xincoCoreData.setXincoCoreDataType(xincoCoreDataType);
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
            em.persist(xincoCoreData);
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            if (xincoCoreDataType != null) {
                xincoCoreDataType.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreDataType = em.merge(xincoCoreDataType);
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
            XincoCoreLanguage xincoCoreLanguageOld = persistentXincoCoreData.getXincoCoreLanguage();
            XincoCoreLanguage xincoCoreLanguageNew = xincoCoreData.getXincoCoreLanguage();
            XincoCoreDataType xincoCoreDataTypeOld = persistentXincoCoreData.getXincoCoreDataType();
            XincoCoreDataType xincoCoreDataTypeNew = xincoCoreData.getXincoCoreDataType();
            XincoCoreNode xincoCoreNodeOld = persistentXincoCoreData.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeNew = xincoCoreData.getXincoCoreNode();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOld = persistentXincoCoreData.getXincoCoreDataHasDependencyList();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListNew = xincoCoreData.getXincoCoreDataHasDependencyList();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1Old = persistentXincoCoreData.getXincoCoreDataHasDependencyList1();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1New = xincoCoreData.getXincoCoreDataHasDependencyList1();
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
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoCoreLanguageNew != null) {
                xincoCoreLanguageNew = em.getReference(xincoCoreLanguageNew.getClass(), xincoCoreLanguageNew.getId());
                xincoCoreData.setXincoCoreLanguage(xincoCoreLanguageNew);
            }
            if (xincoCoreDataTypeNew != null) {
                xincoCoreDataTypeNew = em.getReference(xincoCoreDataTypeNew.getClass(), xincoCoreDataTypeNew.getId());
                xincoCoreData.setXincoCoreDataType(xincoCoreDataTypeNew);
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
            xincoCoreData = em.merge(xincoCoreData);
            if (xincoCoreLanguageOld != null && !xincoCoreLanguageOld.equals(xincoCoreLanguageNew)) {
                xincoCoreLanguageOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreLanguageOld = em.merge(xincoCoreLanguageOld);
            }
            if (xincoCoreLanguageNew != null && !xincoCoreLanguageNew.equals(xincoCoreLanguageOld)) {
                xincoCoreLanguageNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreLanguageNew = em.merge(xincoCoreLanguageNew);
            }
            if (xincoCoreDataTypeOld != null && !xincoCoreDataTypeOld.equals(xincoCoreDataTypeNew)) {
                xincoCoreDataTypeOld.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreDataTypeOld = em.merge(xincoCoreDataTypeOld);
            }
            if (xincoCoreDataTypeNew != null && !xincoCoreDataTypeNew.equals(xincoCoreDataTypeOld)) {
                xincoCoreDataTypeNew.getXincoCoreDataList().add(xincoCoreData);
                xincoCoreDataTypeNew = em.merge(xincoCoreDataTypeNew);
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
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoCoreLanguage xincoCoreLanguage = xincoCoreData.getXincoCoreLanguage();
            if (xincoCoreLanguage != null) {
                xincoCoreLanguage.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreLanguage = em.merge(xincoCoreLanguage);
            }
            XincoCoreDataType xincoCoreDataType = xincoCoreData.getXincoCoreDataType();
            if (xincoCoreDataType != null) {
                xincoCoreDataType.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreDataType = em.merge(xincoCoreDataType);
            }
            XincoCoreNode xincoCoreNode = xincoCoreData.getXincoCoreNode();
            if (xincoCoreNode != null) {
                xincoCoreNode.getXincoCoreDataList().remove(xincoCoreData);
                xincoCoreNode = em.merge(xincoCoreNode);
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
