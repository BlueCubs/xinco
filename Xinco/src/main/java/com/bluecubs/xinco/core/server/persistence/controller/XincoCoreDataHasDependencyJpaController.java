/*
 * Copyright 2012 blueCubs.com.
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
 * Name: XincoCoreDataHasDependencyJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
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
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoCoreDataHasDependencyJpaController implements Serializable {

    public XincoCoreDataHasDependencyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataHasDependency xincoCoreDataHasDependency) throws PreexistingEntityException, Exception {
        if (xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK() == null) {
            xincoCoreDataHasDependency.setXincoCoreDataHasDependencyPK(new XincoCoreDataHasDependencyPK());
        }
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataChildrenId(xincoCoreDataHasDependency.getXincoCoreData().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataParentId(xincoCoreDataHasDependency.getXincoCoreData1().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setDependencyTypeId(xincoCoreDataHasDependency.getXincoDependencyType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyType xincoDependencyType = xincoCoreDataHasDependency.getXincoDependencyType();
            if (xincoDependencyType != null) {
                xincoDependencyType = em.getReference(xincoDependencyType.getClass(), xincoDependencyType.getId());
                xincoCoreDataHasDependency.setXincoDependencyType(xincoDependencyType);
            }
            XincoCoreData xincoCoreData = xincoCoreDataHasDependency.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData = em.getReference(xincoCoreData.getClass(), xincoCoreData.getId());
                xincoCoreDataHasDependency.setXincoCoreData(xincoCoreData);
            }
            XincoCoreData xincoCoreData1 = xincoCoreDataHasDependency.getXincoCoreData1();
            if (xincoCoreData1 != null) {
                xincoCoreData1 = em.getReference(xincoCoreData1.getClass(), xincoCoreData1.getId());
                xincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData1);
            }
            em.persist(xincoCoreDataHasDependency);
            if (xincoDependencyType != null) {
                xincoDependencyType.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoDependencyType = em.merge(xincoDependencyType);
            }
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreData = em.merge(xincoCoreData);
            }
            if (xincoCoreData1 != null) {
                xincoCoreData1.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreData1 = em.merge(xincoCoreData1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataHasDependency(xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK()) != null) {
                throw new PreexistingEntityException("XincoCoreDataHasDependency " + xincoCoreDataHasDependency + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataHasDependency xincoCoreDataHasDependency) throws NonexistentEntityException, Exception {
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataChildrenId(xincoCoreDataHasDependency.getXincoCoreData().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setXincoCoreDataParentId(xincoCoreDataHasDependency.getXincoCoreData1().getId());
        xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK().setDependencyTypeId(xincoCoreDataHasDependency.getXincoDependencyType().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataHasDependency persistentXincoCoreDataHasDependency = em.find(XincoCoreDataHasDependency.class, xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK());
            XincoDependencyType xincoDependencyTypeOld = persistentXincoCoreDataHasDependency.getXincoDependencyType();
            XincoDependencyType xincoDependencyTypeNew = xincoCoreDataHasDependency.getXincoDependencyType();
            XincoCoreData xincoCoreDataOld = persistentXincoCoreDataHasDependency.getXincoCoreData();
            XincoCoreData xincoCoreDataNew = xincoCoreDataHasDependency.getXincoCoreData();
            XincoCoreData xincoCoreData1Old = persistentXincoCoreDataHasDependency.getXincoCoreData1();
            XincoCoreData xincoCoreData1New = xincoCoreDataHasDependency.getXincoCoreData1();
            if (xincoDependencyTypeNew != null) {
                xincoDependencyTypeNew = em.getReference(xincoDependencyTypeNew.getClass(), xincoDependencyTypeNew.getId());
                xincoCoreDataHasDependency.setXincoDependencyType(xincoDependencyTypeNew);
            }
            if (xincoCoreDataNew != null) {
                xincoCoreDataNew = em.getReference(xincoCoreDataNew.getClass(), xincoCoreDataNew.getId());
                xincoCoreDataHasDependency.setXincoCoreData(xincoCoreDataNew);
            }
            if (xincoCoreData1New != null) {
                xincoCoreData1New = em.getReference(xincoCoreData1New.getClass(), xincoCoreData1New.getId());
                xincoCoreDataHasDependency.setXincoCoreData1(xincoCoreData1New);
            }
            xincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependency);
            if (xincoDependencyTypeOld != null && !xincoDependencyTypeOld.equals(xincoDependencyTypeNew)) {
                xincoDependencyTypeOld.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoDependencyTypeOld = em.merge(xincoDependencyTypeOld);
            }
            if (xincoDependencyTypeNew != null && !xincoDependencyTypeNew.equals(xincoDependencyTypeOld)) {
                xincoDependencyTypeNew.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoDependencyTypeNew = em.merge(xincoDependencyTypeNew);
            }
            if (xincoCoreDataOld != null && !xincoCoreDataOld.equals(xincoCoreDataNew)) {
                xincoCoreDataOld.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreDataOld = em.merge(xincoCoreDataOld);
            }
            if (xincoCoreDataNew != null && !xincoCoreDataNew.equals(xincoCoreDataOld)) {
                xincoCoreDataNew.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreDataNew = em.merge(xincoCoreDataNew);
            }
            if (xincoCoreData1Old != null && !xincoCoreData1Old.equals(xincoCoreData1New)) {
                xincoCoreData1Old.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreData1Old = em.merge(xincoCoreData1Old);
            }
            if (xincoCoreData1New != null && !xincoCoreData1New.equals(xincoCoreData1Old)) {
                xincoCoreData1New.getXincoCoreDataHasDependencyList().add(xincoCoreDataHasDependency);
                xincoCoreData1New = em.merge(xincoCoreData1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoCoreDataHasDependencyPK id = xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK();
                if (findXincoCoreDataHasDependency(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataHasDependency with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoCoreDataHasDependencyPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreDataHasDependency xincoCoreDataHasDependency;
            try {
                xincoCoreDataHasDependency = em.getReference(XincoCoreDataHasDependency.class, id);
                xincoCoreDataHasDependency.getXincoCoreDataHasDependencyPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataHasDependency with id " + id + " no longer exists.", enfe);
            }
            XincoDependencyType xincoDependencyType = xincoCoreDataHasDependency.getXincoDependencyType();
            if (xincoDependencyType != null) {
                xincoDependencyType.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoDependencyType = em.merge(xincoDependencyType);
            }
            XincoCoreData xincoCoreData = xincoCoreDataHasDependency.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreData = em.merge(xincoCoreData);
            }
            XincoCoreData xincoCoreData1 = xincoCoreDataHasDependency.getXincoCoreData1();
            if (xincoCoreData1 != null) {
                xincoCoreData1.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependency);
                xincoCoreData1 = em.merge(xincoCoreData1);
            }
            em.remove(xincoCoreDataHasDependency);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataHasDependency> findXincoCoreDataHasDependencyEntities() {
        return findXincoCoreDataHasDependencyEntities(true, -1, -1);
    }

    public List<XincoCoreDataHasDependency> findXincoCoreDataHasDependencyEntities(int maxResults, int firstResult) {
        return findXincoCoreDataHasDependencyEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataHasDependency> findXincoCoreDataHasDependencyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataHasDependency.class));
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

    public XincoCoreDataHasDependency findXincoCoreDataHasDependency(XincoCoreDataHasDependencyPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataHasDependency.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataHasDependencyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataHasDependency> rt = cq.from(XincoCoreDataHasDependency.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
