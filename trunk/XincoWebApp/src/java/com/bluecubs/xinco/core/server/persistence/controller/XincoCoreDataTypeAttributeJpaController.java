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
 * Name: XincoCoreDataTypeAttributeJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
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
public class XincoCoreDataTypeAttributeJpaController implements Serializable {

    public XincoCoreDataTypeAttributeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataTypeAttribute xincoCoreDataTypeAttribute) throws PreexistingEntityException, Exception {
        if (xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK() == null) {
            xincoCoreDataTypeAttribute.setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK());
        }
        xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(xincoCoreDataTypeAttribute.getXincoCoreDataType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataType xincoCoreDataType = xincoCoreDataTypeAttribute.getXincoCoreDataType();
            if (xincoCoreDataType != null) {
                xincoCoreDataType = em.getReference(xincoCoreDataType.getClass(), xincoCoreDataType.getId());
                xincoCoreDataTypeAttribute.setXincoCoreDataType(xincoCoreDataType);
            }
            em.persist(xincoCoreDataTypeAttribute);
            if (xincoCoreDataType != null) {
                xincoCoreDataType.getXincoCoreDataTypeAttributeList().add(xincoCoreDataTypeAttribute);
                xincoCoreDataType = em.merge(xincoCoreDataType);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataTypeAttribute(xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK()) != null) {
                throw new PreexistingEntityException("XincoCoreDataTypeAttribute " + xincoCoreDataTypeAttribute + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataTypeAttribute xincoCoreDataTypeAttribute) throws NonexistentEntityException, Exception {
        xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(xincoCoreDataTypeAttribute.getXincoCoreDataType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataTypeAttribute persistentXincoCoreDataTypeAttribute = em.find(XincoCoreDataTypeAttribute.class, xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK());
            XincoCoreDataType xincoCoreDataTypeOld = persistentXincoCoreDataTypeAttribute.getXincoCoreDataType();
            XincoCoreDataType xincoCoreDataTypeNew = xincoCoreDataTypeAttribute.getXincoCoreDataType();
            if (xincoCoreDataTypeNew != null) {
                xincoCoreDataTypeNew = em.getReference(xincoCoreDataTypeNew.getClass(), xincoCoreDataTypeNew.getId());
                xincoCoreDataTypeAttribute.setXincoCoreDataType(xincoCoreDataTypeNew);
            }
            xincoCoreDataTypeAttribute = em.merge(xincoCoreDataTypeAttribute);
            if (xincoCoreDataTypeOld != null && !xincoCoreDataTypeOld.equals(xincoCoreDataTypeNew)) {
                xincoCoreDataTypeOld.getXincoCoreDataTypeAttributeList().remove(xincoCoreDataTypeAttribute);
                xincoCoreDataTypeOld = em.merge(xincoCoreDataTypeOld);
            }
            if (xincoCoreDataTypeNew != null && !xincoCoreDataTypeNew.equals(xincoCoreDataTypeOld)) {
                xincoCoreDataTypeNew.getXincoCoreDataTypeAttributeList().add(xincoCoreDataTypeAttribute);
                xincoCoreDataTypeNew = em.merge(xincoCoreDataTypeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoCoreDataTypeAttributePK id = xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK();
                if (findXincoCoreDataTypeAttribute(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataTypeAttribute with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoCoreDataTypeAttributePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataTypeAttribute xincoCoreDataTypeAttribute;
            try {
                xincoCoreDataTypeAttribute = em.getReference(XincoCoreDataTypeAttribute.class, id);
                xincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataTypeAttribute with id " + id + " no longer exists.", enfe);
            }
            XincoCoreDataType xincoCoreDataType = xincoCoreDataTypeAttribute.getXincoCoreDataType();
            if (xincoCoreDataType != null) {
                xincoCoreDataType.getXincoCoreDataTypeAttributeList().remove(xincoCoreDataTypeAttribute);
                xincoCoreDataType = em.merge(xincoCoreDataType);
            }
            em.remove(xincoCoreDataTypeAttribute);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataTypeAttribute> findXincoCoreDataTypeAttributeEntities() {
        return findXincoCoreDataTypeAttributeEntities(true, -1, -1);
    }

    public List<XincoCoreDataTypeAttribute> findXincoCoreDataTypeAttributeEntities(int maxResults, int firstResult) {
        return findXincoCoreDataTypeAttributeEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataTypeAttribute> findXincoCoreDataTypeAttributeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataTypeAttribute.class));
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

    public XincoCoreDataTypeAttribute findXincoCoreDataTypeAttribute(XincoCoreDataTypeAttributePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataTypeAttribute.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataTypeAttributeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataTypeAttribute> rt = cq.from(XincoCoreDataTypeAttribute.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
