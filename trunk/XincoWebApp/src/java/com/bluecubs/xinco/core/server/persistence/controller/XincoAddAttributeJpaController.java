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
 * Name: XincoAddAttributeJpaController
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoAddAttributeJpaController implements Serializable {

    public XincoAddAttributeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoAddAttribute xincoAddAttribute) throws PreexistingEntityException, Exception {
        if (xincoAddAttribute.getXincoAddAttributePK() == null) {
            xincoAddAttribute.setXincoAddAttributePK(new XincoAddAttributePK());
        }
        xincoAddAttribute.getXincoAddAttributePK().setXincoCoreDataId(xincoAddAttribute.getXincoCoreData().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreData xincoCoreData = xincoAddAttribute.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData = em.getReference(xincoCoreData.getClass(), xincoCoreData.getId());
                xincoAddAttribute.setXincoCoreData(xincoCoreData);
            }
            em.persist(xincoAddAttribute);
            if (xincoCoreData != null) {
                xincoCoreData.getXincoAddAttributeList().add(xincoAddAttribute);
                xincoCoreData = em.merge(xincoCoreData);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoAddAttribute(xincoAddAttribute.getXincoAddAttributePK()) != null) {
                throw new PreexistingEntityException("XincoAddAttribute " + xincoAddAttribute + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoAddAttribute xincoAddAttribute) throws NonexistentEntityException, Exception {
        xincoAddAttribute.getXincoAddAttributePK().setXincoCoreDataId(xincoAddAttribute.getXincoCoreData().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoAddAttribute persistentXincoAddAttribute = em.find(XincoAddAttribute.class, xincoAddAttribute.getXincoAddAttributePK());
            XincoCoreData xincoCoreDataOld = persistentXincoAddAttribute.getXincoCoreData();
            XincoCoreData xincoCoreDataNew = xincoAddAttribute.getXincoCoreData();
            if (xincoCoreDataNew != null) {
                xincoCoreDataNew = em.getReference(xincoCoreDataNew.getClass(), xincoCoreDataNew.getId());
                xincoAddAttribute.setXincoCoreData(xincoCoreDataNew);
            }
            xincoAddAttribute = em.merge(xincoAddAttribute);
            if (xincoCoreDataOld != null && !xincoCoreDataOld.equals(xincoCoreDataNew)) {
                xincoCoreDataOld.getXincoAddAttributeList().remove(xincoAddAttribute);
                xincoCoreDataOld = em.merge(xincoCoreDataOld);
            }
            if (xincoCoreDataNew != null && !xincoCoreDataNew.equals(xincoCoreDataOld)) {
                xincoCoreDataNew.getXincoAddAttributeList().add(xincoAddAttribute);
                xincoCoreDataNew = em.merge(xincoCoreDataNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoAddAttributePK id = xincoAddAttribute.getXincoAddAttributePK();
                if (findXincoAddAttribute(id) == null) {
                    throw new NonexistentEntityException("The xincoAddAttribute with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoAddAttributePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoAddAttribute xincoAddAttribute;
            try {
                xincoAddAttribute = em.getReference(XincoAddAttribute.class, id);
                xincoAddAttribute.getXincoAddAttributePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoAddAttribute with id " + id + " no longer exists.", enfe);
            }
            XincoCoreData xincoCoreData = xincoAddAttribute.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData.getXincoAddAttributeList().remove(xincoAddAttribute);
                xincoCoreData = em.merge(xincoCoreData);
            }
            em.remove(xincoAddAttribute);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoAddAttribute> findXincoAddAttributeEntities() {
        return findXincoAddAttributeEntities(true, -1, -1);
    }

    public List<XincoAddAttribute> findXincoAddAttributeEntities(int maxResults, int firstResult) {
        return findXincoAddAttributeEntities(false, maxResults, firstResult);
    }

    private List<XincoAddAttribute> findXincoAddAttributeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoAddAttribute.class));
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

    public XincoAddAttribute findXincoAddAttribute(XincoAddAttributePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoAddAttribute.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoAddAttributeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoAddAttribute> rt = cq.from(XincoAddAttribute.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
