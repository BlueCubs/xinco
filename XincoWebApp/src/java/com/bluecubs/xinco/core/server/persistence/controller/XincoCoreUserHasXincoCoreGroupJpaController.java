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
 * Name: XincoCoreUserHasXincoCoreGroupJpaController
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK;
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupJpaController implements Serializable {

    public XincoCoreUserHasXincoCoreGroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup) throws PreexistingEntityException, Exception {
        if (xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK() == null) {
            xincoCoreUserHasXincoCoreGroup.setXincoCoreUserHasXincoCoreGroupPK(new XincoCoreUserHasXincoCoreGroupPK());
        }
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup().getId());
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserHasXincoCoreGroup.getXincoCoreUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUser xincoCoreUser = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUser);
            }
            XincoCoreGroup xincoCoreGroup = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup = em.getReference(xincoCoreGroup.getClass(), xincoCoreGroup.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroup);
            }
            em.persist(xincoCoreUserHasXincoCoreGroup);
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreUserHasXincoCoreGroup(xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK()) != null) {
                throw new PreexistingEntityException("XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroup + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup) throws NonexistentEntityException, Exception {
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup().getId());
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserHasXincoCoreGroup.getXincoCoreUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserHasXincoCoreGroup persistentXincoCoreUserHasXincoCoreGroup = em.find(XincoCoreUserHasXincoCoreGroup.class, xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK());
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            XincoCoreGroup xincoCoreGroupOld = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            XincoCoreGroup xincoCoreGroupNew = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUserNew);
            }
            if (xincoCoreGroupNew != null) {
                xincoCoreGroupNew = em.getReference(xincoCoreGroupNew.getClass(), xincoCoreGroupNew.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroupNew);
            }
            xincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroup);
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
            }
            if (xincoCoreGroupOld != null && !xincoCoreGroupOld.equals(xincoCoreGroupNew)) {
                xincoCoreGroupOld.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroupOld = em.merge(xincoCoreGroupOld);
            }
            if (xincoCoreGroupNew != null && !xincoCoreGroupNew.equals(xincoCoreGroupOld)) {
                xincoCoreGroupNew.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroupNew = em.merge(xincoCoreGroupNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoCoreUserHasXincoCoreGroupPK id = xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK();
                if (findXincoCoreUserHasXincoCoreGroup(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreUserHasXincoCoreGroup with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoCoreUserHasXincoCoreGroupPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup;
            try {
                xincoCoreUserHasXincoCoreGroup = em.getReference(XincoCoreUserHasXincoCoreGroup.class, id);
                xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreUserHasXincoCoreGroup with id " + id + " no longer exists.", enfe);
            }
            XincoCoreUser xincoCoreUser = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            XincoCoreGroup xincoCoreGroup = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            em.remove(xincoCoreUserHasXincoCoreGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreUserHasXincoCoreGroup> findXincoCoreUserHasXincoCoreGroupEntities() {
        return findXincoCoreUserHasXincoCoreGroupEntities(true, -1, -1);
    }

    public List<XincoCoreUserHasXincoCoreGroup> findXincoCoreUserHasXincoCoreGroupEntities(int maxResults, int firstResult) {
        return findXincoCoreUserHasXincoCoreGroupEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreUserHasXincoCoreGroup> findXincoCoreUserHasXincoCoreGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreUserHasXincoCoreGroup.class));
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

    public XincoCoreUserHasXincoCoreGroup findXincoCoreUserHasXincoCoreGroup(XincoCoreUserHasXincoCoreGroupPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreUserHasXincoCoreGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreUserHasXincoCoreGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreUserHasXincoCoreGroup> rt = cq.from(XincoCoreUserHasXincoCoreGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
