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
 * Name: XincoCoreDataTypeJpaController
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
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
                XincoCoreDataType oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData = xincoCoreDataListXincoCoreData.getXincoCoreDataType();
                xincoCoreDataListXincoCoreData.setXincoCoreDataType(xincoCoreDataType);
                xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
                if (oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData != null) {
                    oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData.getXincoCoreDataList().remove(xincoCoreDataListXincoCoreData);
                    oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData = em.merge(oldXincoCoreDataTypeOfXincoCoreDataListXincoCoreData);
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
                    illegalOrphanMessages.add("You must retain XincoCoreData " + xincoCoreDataListOldXincoCoreData + " since its xincoCoreDataType field is not nullable.");
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
