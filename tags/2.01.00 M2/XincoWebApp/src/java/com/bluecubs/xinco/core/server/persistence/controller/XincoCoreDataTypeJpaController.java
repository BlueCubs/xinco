/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
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
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import java.util.Collection;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeJpaController implements Serializable {

    public XincoCoreDataTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataType xincoCoreDataType) throws PreexistingEntityException, Exception {
        if (xincoCoreDataType.getXincoCoreDataTypeAttributeList() == null) {
            xincoCoreDataType.setXincoCoreDataTypeAttributeList(new ArrayList<XincoCoreDataTypeAttribute>());
        }
        if (xincoCoreDataType.getXincoCoreDataList() == null) {
            xincoCoreDataType.setXincoCoreDataList(new ArrayList<XincoCoreData>());
        }
        if (xincoCoreDataType.getXincoCoreDataCollection() == null) {
            xincoCoreDataType.setXincoCoreDataCollection(new ArrayList<XincoCoreData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoCoreDataTypeAttribute> attachedXincoCoreDataTypeAttributeList = new ArrayList<XincoCoreDataTypeAttribute>();
            for (XincoCoreDataTypeAttribute xincoCoreDataTypeAttributeListXincoCoreDataTypeAttributeToAttach : xincoCoreDataType.getXincoCoreDataTypeAttributeList()) {
                xincoCoreDataTypeAttributeListXincoCoreDataTypeAttributeToAttach = em.getReference(xincoCoreDataTypeAttributeListXincoCoreDataTypeAttributeToAttach.getClass(), xincoCoreDataTypeAttributeListXincoCoreDataTypeAttributeToAttach.getXincoCoreDataTypeAttributePK());
                attachedXincoCoreDataTypeAttributeList.add(xincoCoreDataTypeAttributeListXincoCoreDataTypeAttributeToAttach);
            }
            xincoCoreDataType.setXincoCoreDataTypeAttributeList(attachedXincoCoreDataTypeAttributeList);
            List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach : xincoCoreDataType.getXincoCoreDataList()) {
                xincoCoreDataListXincoCoreDataToAttach = em.getReference(xincoCoreDataListXincoCoreDataToAttach.getClass(), xincoCoreDataListXincoCoreDataToAttach.getId());
                attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
            }
            xincoCoreDataType.setXincoCoreDataList(attachedXincoCoreDataList);
            Collection<XincoCoreData> attachedXincoCoreDataCollection = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataCollectionXincoCoreDataToAttach : xincoCoreDataType.getXincoCoreDataCollection()) {
                xincoCoreDataCollectionXincoCoreDataToAttach = em.getReference(xincoCoreDataCollectionXincoCoreDataToAttach.getClass(), xincoCoreDataCollectionXincoCoreDataToAttach.getId());
                attachedXincoCoreDataCollection.add(xincoCoreDataCollectionXincoCoreDataToAttach);
            }
            xincoCoreDataType.setXincoCoreDataCollection(attachedXincoCoreDataCollection);
            em.persist(xincoCoreDataType);
            for (XincoCoreDataTypeAttribute xincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute : xincoCoreDataType.getXincoCoreDataTypeAttributeList()) {
                XincoCoreDataType oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute = xincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute.getXincoCoreDataType();
                xincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute.setXincoCoreDataType(xincoCoreDataType);
                xincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute = em.merge(xincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute);
                if (oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute != null) {
                    oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributeList().remove(xincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute);
                    oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute = em.merge(oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListXincoCoreDataTypeAttribute);
                }
            }
            for (XincoCoreData xincoCoreDataListXincoCoreData : xincoCoreDataType.getXincoCoreDataList()) {
                XincoCoreDataType oldXincoCoreDataTypeIdOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreDataType();
                xincoCoreDataListXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreDataTypeIdOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreDataTypeIdOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreDataTypeIdOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreDataTypeIdOfXincoCoreDataListXincoCoreData);
                }
            }
            for (XincoCoreData xincoCoreDataCollectionXincoCoreData : xincoCoreDataType.getXincoCoreDataCollection()) {
                XincoCoreDataType oldXincoCoreDataTypeIdOfXincoCoreDataCollectionXincoCoreData = xincoCoreDataCollectionXincoCoreData.getXincoCoreDataType();
                xincoCoreDataCollectionXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                xincoCoreDataCollectionXincoCoreData = em.merge(xincoCoreDataCollectionXincoCoreData);
                if (oldXincoCoreDataTypeIdOfXincoCoreDataCollectionXincoCoreData != null) {
                    oldXincoCoreDataTypeIdOfXincoCoreDataCollectionXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionXincoCoreData);
                    oldXincoCoreDataTypeIdOfXincoCoreDataCollectionXincoCoreData = em.merge(oldXincoCoreDataTypeIdOfXincoCoreDataCollectionXincoCoreData);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataType(xincoCoreDataType.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreDataType " + xincoCoreDataType + " already exists.", ex);
            }
            throw ex;
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
            List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeListOld = persistentXincoCoreDataType.getXincoCoreDataTypeAttributeList();
            List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeListNew = xincoCoreDataType.getXincoCoreDataTypeAttributeList();
            List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreDataType.getXincoCoreDataList();
            List<XincoCoreData> xincoCoreDataListNew = xincoCoreDataType.getXincoCoreDataList();
            Collection<XincoCoreData> xincoCoreDataCollectionOld = persistentXincoCoreDataType.getXincoCoreDataCollection();
            Collection<XincoCoreData> xincoCoreDataCollectionNew = xincoCoreDataType.getXincoCoreDataCollection();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreDataTypeAttribute xincoCoreDataTypeAttributeListOldXincoCoreDataTypeAttribute : xincoCoreDataTypeAttributeListOld) {
                if (!xincoCoreDataTypeAttributeListNew.contains(xincoCoreDataTypeAttributeListOldXincoCoreDataTypeAttribute)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataTypeAttribute " + xincoCoreDataTypeAttributeListOldXincoCoreDataTypeAttribute + " since its xincoCoreDataType field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
                if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreDataTypeId field is not nullable.");
                }
            }
            for (XincoCoreData xincoCoreDataCollectionOldXincoCoreData : xincoCoreDataCollectionOld) {
                if (!xincoCoreDataCollectionNew.contains(xincoCoreDataCollectionOldXincoCoreData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataCollectionOldXincoCoreData + " since its xincoCoreDataTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreDataTypeAttribute> attachedXincoCoreDataTypeAttributeListNew = new ArrayList<XincoCoreDataTypeAttribute>();
            for (XincoCoreDataTypeAttribute xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttributeToAttach : xincoCoreDataTypeAttributeListNew) {
                xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttributeToAttach = em.getReference(xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttributeToAttach.getClass(), xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttributeToAttach.getXincoCoreDataTypeAttributePK());
                attachedXincoCoreDataTypeAttributeListNew.add(xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttributeToAttach);
            }
            xincoCoreDataTypeAttributeListNew = attachedXincoCoreDataTypeAttributeListNew;
            xincoCoreDataType.setXincoCoreDataTypeAttributeList(xincoCoreDataTypeAttributeListNew);
            List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
                xincoCoreDataListNewXincoCoreDataToAttach = em.getReference(xincoCoreDataListNewXincoCoreDataToAttach.getClass(), xincoCoreDataListNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
            }
            xincoCoreDataListNew = attachedXincoCoreDataListNew;
            xincoCoreDataType.setXincoCoreDataList(xincoCoreDataListNew);
            Collection<XincoCoreData> attachedXincoCoreDataCollectionNew = new ArrayList<XincoCoreData>();
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreDataToAttach : xincoCoreDataCollectionNew) {
                xincoCoreDataCollectionNewXincoCoreDataToAttach = em.getReference(xincoCoreDataCollectionNewXincoCoreDataToAttach.getClass(), xincoCoreDataCollectionNewXincoCoreDataToAttach.getId());
                attachedXincoCoreDataCollectionNew.add(xincoCoreDataCollectionNewXincoCoreDataToAttach);
            }
            xincoCoreDataCollectionNew = attachedXincoCoreDataCollectionNew;
            xincoCoreDataType.setXincoCoreDataCollection(xincoCoreDataCollectionNew);
            xincoCoreDataType = em.merge(xincoCoreDataType);
            for (XincoCoreDataTypeAttribute xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute : xincoCoreDataTypeAttributeListNew) {
                if (!xincoCoreDataTypeAttributeListOld.contains(xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute)) {
                    XincoCoreDataType oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute = xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute.getXincoCoreDataType();
                    xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute.setXincoCoreDataType(xincoCoreDataType);
                    xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute = em.merge(xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute);
                    if (oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute != null && !oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute.equals(xincoCoreDataType)) {
                        oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributeList().remove(xincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute);
                        oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute = em.merge(oldXincoCoreDataTypeOfXincoCoreDataTypeAttributeListNewXincoCoreDataTypeAttribute);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
                if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
                    XincoCoreDataType oldXincoCoreDataTypeIdOfXincoCoreDataListNewXincoCoreData = xincoCoreDataListNewXincoCoreData.getXincoCoreDataType();
                    xincoCoreDataListNewXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                    xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
                    if (oldXincoCoreDataTypeIdOfXincoCoreDataListNewXincoCoreData != null && !oldXincoCoreDataTypeIdOfXincoCoreDataListNewXincoCoreData.equals(xincoCoreDataType)) {
                        oldXincoCoreDataTypeIdOfXincoCoreDataListNewXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListNewXincoCoreData);
                        oldXincoCoreDataTypeIdOfXincoCoreDataListNewXincoCoreData = em.merge(oldXincoCoreDataTypeIdOfXincoCoreDataListNewXincoCoreData);
                    }
                }
            }
            for (XincoCoreData xincoCoreDataCollectionNewXincoCoreData : xincoCoreDataCollectionNew) {
                if (!xincoCoreDataCollectionOld.contains(xincoCoreDataCollectionNewXincoCoreData)) {
                    XincoCoreDataType oldXincoCoreDataTypeIdOfXincoCoreDataCollectionNewXincoCoreData = xincoCoreDataCollectionNewXincoCoreData.getXincoCoreDataType();
                    xincoCoreDataCollectionNewXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                    xincoCoreDataCollectionNewXincoCoreData = em.merge(xincoCoreDataCollectionNewXincoCoreData);
                    if (oldXincoCoreDataTypeIdOfXincoCoreDataCollectionNewXincoCoreData != null && !oldXincoCoreDataTypeIdOfXincoCoreDataCollectionNewXincoCoreData.equals(xincoCoreDataType)) {
                        oldXincoCoreDataTypeIdOfXincoCoreDataCollectionNewXincoCoreData.getXincoCoreDataCollection().remove(xincoCoreDataCollectionNewXincoCoreData);
                        oldXincoCoreDataTypeIdOfXincoCoreDataCollectionNewXincoCoreData = em.merge(oldXincoCoreDataTypeIdOfXincoCoreDataCollectionNewXincoCoreData);
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
            List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeListOrphanCheck = xincoCoreDataType.getXincoCoreDataTypeAttributeList();
            for (XincoCoreDataTypeAttribute xincoCoreDataTypeAttributeListOrphanCheckXincoCoreDataTypeAttribute : xincoCoreDataTypeAttributeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreDataType (" + xincoCoreDataType + ") cannot be destroyed since the XincoCoreDataTypeAttribute " + xincoCoreDataTypeAttributeListOrphanCheckXincoCoreDataTypeAttribute + " in its xincoCoreDataTypeAttributeList field has a non-nullable xincoCoreDataType field.");
            }
            List<XincoCoreData> xincoCoreDataListOrphanCheck = xincoCoreDataType.getXincoCoreDataList();
            for (XincoCoreData xincoCoreDataListOrphanCheckXincoCoreData : xincoCoreDataListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreDataType (" + xincoCoreDataType + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataListOrphanCheckXincoCoreData + " in its xincoCoreDataList field has a non-nullable xincoCoreDataTypeId field.");
            }
            Collection<XincoCoreData> xincoCoreDataCollectionOrphanCheck = xincoCoreDataType.getXincoCoreDataCollection();
            for (XincoCoreData xincoCoreDataCollectionOrphanCheckXincoCoreData : xincoCoreDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreDataType (" + xincoCoreDataType + ") cannot be destroyed since the XincoCoreData " + xincoCoreDataCollectionOrphanCheckXincoCoreData + " in its xincoCoreDataCollection field has a non-nullable xincoCoreDataTypeId field.");
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