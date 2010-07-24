/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeJpaController {

    public XincoCoreDataTypeJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataType xincoCoreDataType) {
        if (xincoCoreDataType.getXincoCoreDataList() == null) {
            xincoCoreDataType.setXincoCoreDataList(new ArrayList<XincoCoreData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach : xincoCoreDataType.getXincoCoreDataList()) {
                xincoCoreDataListXincoCoreDataToAttach = em.getReference(xincoCoreDataListXincoCoreDataToAttach.getClass(), xincoCoreDataListXincoCoreDataToAttach.getId());
                attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
            }
            xincoCoreDataType.setXincoCoreDataList(attachedXincoCoreDataList);
            em.persist(xincoCoreDataType);
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreDataType.getXincoCoreDataList()) {
                XincoCoreDataType oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreDataType();
                xincoCoreDataListXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataType xincoCoreDataType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataType persistentXincoCoreDataType = em.find(XincoCoreDataType.class, xincoCoreDataType.getId());
            List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreDataType.getXincoCoreDataList();
            List<XincoCoreData> xincoCoreDataListNew = xincoCoreDataType.getXincoCoreDataList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreDataType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
                xincoCoreDataListNewXincoCoreDataToAttach = em.getReference(xincoCoreDataListNewXincoCoreDataToAttach.getClass(), xincoCoreDataListNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
            }
            xincoCoreDataListNew = attachedXincoCoreDataListNew;
            xincoCoreDataType.setXincoCoreDataList(xincoCoreDataListNew);
            xincoCoreDataType = em.merge(xincoCoreDataType);
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreDataType oldXincoCoreDataTypeOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreDataType();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreDataTypeOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreDataTypeOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreDataType)) {
                        oldXincoCoreDataTypeOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreDataTypeOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreDataTypeOfXincoCoreDataListNewXincoCoreData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreDataType.getId();
                if (findXincoCoreDataType(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataType with id " + id + " no longer exists.");
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
            XincoCoreDataType xincoCoreDataType;
            try {
                xincoCoreDataType = em.getReference(XincoCoreDataType.class, id);
                xincoCoreDataType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreData> xincoCoreDataListOrphanCheck = xincoCoreDataType.getXincoCoreDataList();
            for (XincoCoreData xincoCoreDataListOrphanCheckXincoCoreData : xincoCoreDataListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreDataType (" + xincoCoreDataType + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreDataType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xincoCoreDataType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataType> findXincoCoreDataTypeEntities() {
        return findXincoCoreDataTypeEntities(true, -1, -1);
    }

    public List<XincoCoreDataType> findXincoCoreDataTypeEntities(int maxResults, int firstResult) {
        return findXincoCoreDataTypeEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataType> findXincoCoreDataTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataType.class));
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

    public XincoCoreDataType findXincoCoreDataType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataType.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataType> rt = cq.from(XincoCoreDataType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
