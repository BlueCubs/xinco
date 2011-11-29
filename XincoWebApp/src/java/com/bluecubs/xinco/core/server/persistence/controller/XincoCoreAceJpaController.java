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
 * Name: XincoCoreAceJpaController
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.*;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreAceJpaController implements Serializable {

    public XincoCoreAceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreAce xincoCoreAce) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUser xincoCoreUser = xincoCoreAce.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreAce.setXincoCoreUser(xincoCoreUser);
            }
            XincoCoreData xincoCoreData = xincoCoreAce.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData = em.getReference(xincoCoreData.getClass(), xincoCoreData.getId());
                xincoCoreAce.setXincoCoreData(xincoCoreData);
            }
            XincoCoreNode xincoCoreNode = xincoCoreAce.getXincoCoreNode();
            if (xincoCoreNode != null) {
                xincoCoreNode = em.getReference(xincoCoreNode.getClass(), xincoCoreNode.getId());
                xincoCoreAce.setXincoCoreNode(xincoCoreNode);
            }
            XincoCoreGroup xincoCoreGroup = xincoCoreAce.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup = em.getReference(xincoCoreGroup.getClass(), xincoCoreGroup.getId());
                xincoCoreAce.setXincoCoreGroup(xincoCoreGroup);
            }
            em.persist(xincoCoreAce);
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreData = em.merge(xincoCoreData);
            }
            if (xincoCoreNode != null) {
                xincoCoreNode.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreNode = em.merge(xincoCoreNode);
            }
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreAce(xincoCoreAce.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreAce " + xincoCoreAce + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreAce xincoCoreAce) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreAce persistentXincoCoreAce = em.find(XincoCoreAce.class, xincoCoreAce.getId());
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreAce.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreAce.getXincoCoreUser();
            XincoCoreData xincoCoreDataOld = persistentXincoCoreAce.getXincoCoreData();
            XincoCoreData xincoCoreDataNew = xincoCoreAce.getXincoCoreData();
            XincoCoreNode xincoCoreNodeOld = persistentXincoCoreAce.getXincoCoreNode();
            XincoCoreNode xincoCoreNodeNew = xincoCoreAce.getXincoCoreNode();
            XincoCoreGroup xincoCoreGroupOld = persistentXincoCoreAce.getXincoCoreGroup();
            XincoCoreGroup xincoCoreGroupNew = xincoCoreAce.getXincoCoreGroup();
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreAce.setXincoCoreUser(xincoCoreUserNew);
            }
            if (xincoCoreDataNew != null) {
                xincoCoreDataNew = em.getReference(xincoCoreDataNew.getClass(), xincoCoreDataNew.getId());
                xincoCoreAce.setXincoCoreData(xincoCoreDataNew);
            }
            if (xincoCoreNodeNew != null) {
                xincoCoreNodeNew = em.getReference(xincoCoreNodeNew.getClass(), xincoCoreNodeNew.getId());
                xincoCoreAce.setXincoCoreNode(xincoCoreNodeNew);
            }
            if (xincoCoreGroupNew != null) {
                xincoCoreGroupNew = em.getReference(xincoCoreGroupNew.getClass(), xincoCoreGroupNew.getId());
                xincoCoreAce.setXincoCoreGroup(xincoCoreGroupNew);
            }
            xincoCoreAce = em.merge(xincoCoreAce);
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
            }
            if (xincoCoreDataOld != null && !xincoCoreDataOld.equals(xincoCoreDataNew)) {
                xincoCoreDataOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreDataOld = em.merge(xincoCoreDataOld);
            }
            if (xincoCoreDataNew != null && !xincoCoreDataNew.equals(xincoCoreDataOld)) {
                xincoCoreDataNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreDataNew = em.merge(xincoCoreDataNew);
            }
            if (xincoCoreNodeOld != null && !xincoCoreNodeOld.equals(xincoCoreNodeNew)) {
                xincoCoreNodeOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreNodeOld = em.merge(xincoCoreNodeOld);
            }
            if (xincoCoreNodeNew != null && !xincoCoreNodeNew.equals(xincoCoreNodeOld)) {
                xincoCoreNodeNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreNodeNew = em.merge(xincoCoreNodeNew);
            }
            if (xincoCoreGroupOld != null && !xincoCoreGroupOld.equals(xincoCoreGroupNew)) {
                xincoCoreGroupOld.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreGroupOld = em.merge(xincoCoreGroupOld);
            }
            if (xincoCoreGroupNew != null && !xincoCoreGroupNew.equals(xincoCoreGroupOld)) {
                xincoCoreGroupNew.getXincoCoreAceList().add(xincoCoreAce);
                xincoCoreGroupNew = em.merge(xincoCoreGroupNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreAce.getId();
                if (findXincoCoreAce(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreAce with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreAce xincoCoreAce;
            try {
                xincoCoreAce = em.getReference(XincoCoreAce.class, id);
                xincoCoreAce.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreAce with id " + id + " no longer exists.", enfe);
            }
            XincoCoreUser xincoCoreUser = xincoCoreAce.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            XincoCoreData xincoCoreData = xincoCoreAce.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreData = em.merge(xincoCoreData);
            }
            XincoCoreNode xincoCoreNode = xincoCoreAce.getXincoCoreNode();
            if (xincoCoreNode != null) {
                xincoCoreNode.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreNode = em.merge(xincoCoreNode);
            }
            XincoCoreGroup xincoCoreGroup = xincoCoreAce.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreAceList().remove(xincoCoreAce);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            em.remove(xincoCoreAce);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreAce> findXincoCoreAceEntities() {
        return findXincoCoreAceEntities(true, -1, -1);
    }

    public List<XincoCoreAce> findXincoCoreAceEntities(int maxResults, int firstResult) {
        return findXincoCoreAceEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreAce> findXincoCoreAceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreAce.class));
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

    public XincoCoreAce findXincoCoreAce(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreAce.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreAceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreAce> rt = cq.from(XincoCoreAce.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
