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
 * Name: XincoDependencyTypeJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
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
public class XincoDependencyTypeJpaController implements Serializable {

    public XincoDependencyTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyType xincoDependencyType) {
        if (xincoDependencyType.getXincoCoreDataHasDependencyList() == null) {
            xincoDependencyType.setXincoCoreDataHasDependencyList(new ArrayList<XincoCoreDataHasDependency>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyBehavior xincoDependencyBehavior = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior = em.getReference(xincoDependencyBehavior.getClass(), xincoDependencyBehavior.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
            }
            List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyList = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach : xincoDependencyType.getXincoCoreDataHasDependencyList()) {
                xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyList.add(xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach);
            }
            xincoDependencyType.setXincoCoreDataHasDependencyList(attachedXincoCoreDataHasDependencyList);
            em.persist(xincoDependencyType);
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior.getXincoDependencyTypeList().add(xincoDependencyType);
                xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListXincoCoreDataHasDependency : xincoDependencyType.getXincoCoreDataHasDependencyList()) {
                XincoDependencyType oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency = xincoCoreDataHasDependencyListXincoCoreDataHasDependency.getXincoDependencyType();
                xincoCoreDataHasDependencyListXincoCoreDataHasDependency.setXincoDependencyType(xincoDependencyType);
                xincoCoreDataHasDependencyListXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyListXincoCoreDataHasDependency);
                if (oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency != null) {
                    oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependencyListXincoCoreDataHasDependency);
                    oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency = em.merge(oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoDependencyType xincoDependencyType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyType persistentXincoDependencyType = em.find(XincoDependencyType.class, xincoDependencyType.getId());
            XincoDependencyBehavior xincoDependencyBehaviorOld = persistentXincoDependencyType.getXincoDependencyBehavior();
            XincoDependencyBehavior xincoDependencyBehaviorNew = xincoDependencyType.getXincoDependencyBehavior();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOld = persistentXincoDependencyType.getXincoCoreDataHasDependencyList();
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListNew = xincoDependencyType.getXincoCoreDataHasDependencyList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency : xincoCoreDataHasDependencyListOld) {
                if (!xincoCoreDataHasDependencyListNew.contains(xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreDataHasDependency " + xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency + " since its xincoDependencyType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoDependencyBehaviorNew != null) {
                xincoDependencyBehaviorNew = em.getReference(xincoDependencyBehaviorNew.getClass(), xincoDependencyBehaviorNew.getId());
                xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehaviorNew);
            }
            List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyListNew = new ArrayList<XincoCoreDataHasDependency>();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach : xincoCoreDataHasDependencyListNew) {
                xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach = em.getReference(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach.getClass(), xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach.getXincoCoreDataHasDependencyPK());
                attachedXincoCoreDataHasDependencyListNew.add(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach);
            }
            xincoCoreDataHasDependencyListNew = attachedXincoCoreDataHasDependencyListNew;
            xincoDependencyType.setXincoCoreDataHasDependencyList(xincoCoreDataHasDependencyListNew);
            xincoDependencyType = em.merge(xincoDependencyType);
            if (xincoDependencyBehaviorOld != null && !xincoDependencyBehaviorOld.equals(xincoDependencyBehaviorNew)) {
                xincoDependencyBehaviorOld.getXincoDependencyTypeList().remove(xincoDependencyType);
                xincoDependencyBehaviorOld = em.merge(xincoDependencyBehaviorOld);
            }
            if (xincoDependencyBehaviorNew != null && !xincoDependencyBehaviorNew.equals(xincoDependencyBehaviorOld)) {
                xincoDependencyBehaviorNew.getXincoDependencyTypeList().add(xincoDependencyType);
                xincoDependencyBehaviorNew = em.merge(xincoDependencyBehaviorNew);
            }
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency : xincoCoreDataHasDependencyListNew) {
                if (!xincoCoreDataHasDependencyListOld.contains(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency)) {
                    XincoDependencyType oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency = xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.getXincoDependencyType();
                    xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.setXincoDependencyType(xincoDependencyType);
                    xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency = em.merge(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
                    if (oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency != null && !oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.equals(xincoDependencyType)) {
                        oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.getXincoCoreDataHasDependencyList().remove(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
                        oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency = em.merge(oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoDependencyType.getId();
                if (findXincoDependencyType(id) == null) {
                    throw new NonexistentEntityException("The xincoDependencyType with id " + id + " no longer exists.");
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
            XincoDependencyType xincoDependencyType;
            try {
                xincoDependencyType = em.getReference(XincoDependencyType.class, id);
                xincoDependencyType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoDependencyType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOrphanCheck = xincoDependencyType.getXincoCoreDataHasDependencyList();
            for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListOrphanCheckXincoCoreDataHasDependency : xincoCoreDataHasDependencyListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoDependencyType (" + xincoDependencyType + ") cannot be destroyed since the XincoCoreDataHasDependency " + xincoCoreDataHasDependencyListOrphanCheckXincoCoreDataHasDependency + " in its xincoCoreDataHasDependencyList field has a non-nullable xincoDependencyType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoDependencyBehavior xincoDependencyBehavior = xincoDependencyType.getXincoDependencyBehavior();
            if (xincoDependencyBehavior != null) {
                xincoDependencyBehavior.getXincoDependencyTypeList().remove(xincoDependencyType);
                xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            }
            em.remove(xincoDependencyType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoDependencyType> findXincoDependencyTypeEntities() {
        return findXincoDependencyTypeEntities(true, -1, -1);
    }

    public List<XincoDependencyType> findXincoDependencyTypeEntities(int maxResults, int firstResult) {
        return findXincoDependencyTypeEntities(false, maxResults, firstResult);
    }

    private List<XincoDependencyType> findXincoDependencyTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoDependencyType.class));
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

    public XincoDependencyType findXincoDependencyType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoDependencyType.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoDependencyTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoDependencyType> rt = cq.from(XincoDependencyType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
