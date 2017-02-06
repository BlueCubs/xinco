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
 * Name: XincoDependencyBehaviorJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoDependencyBehaviorJpaController implements Serializable {

    public XincoDependencyBehaviorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoDependencyBehavior xincoDependencyBehavior) {
        if (xincoDependencyBehavior.getXincoDependencyTypeList() == null) {
            xincoDependencyBehavior.setXincoDependencyTypeList(new ArrayList<XincoDependencyType>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoDependencyType> attachedXincoDependencyTypeList = new ArrayList<>();
            for (XincoDependencyType xincoDependencyTypeListXincoDependencyTypeToAttach : xincoDependencyBehavior.getXincoDependencyTypeList()) {
                xincoDependencyTypeListXincoDependencyTypeToAttach = em.getReference(xincoDependencyTypeListXincoDependencyTypeToAttach.getClass(), xincoDependencyTypeListXincoDependencyTypeToAttach.getId());
                attachedXincoDependencyTypeList.add(xincoDependencyTypeListXincoDependencyTypeToAttach);
            }
            xincoDependencyBehavior.setXincoDependencyTypeList(attachedXincoDependencyTypeList);
            em.persist(xincoDependencyBehavior);
            for (XincoDependencyType xincoDependencyTypeListXincoDependencyType : xincoDependencyBehavior.getXincoDependencyTypeList()) {
                XincoDependencyBehavior oldXincoDependencyBehaviorOfXincoDependencyTypeListXincoDependencyType = xincoDependencyTypeListXincoDependencyType.getXincoDependencyBehavior();
                xincoDependencyTypeListXincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
                xincoDependencyTypeListXincoDependencyType = em.merge(xincoDependencyTypeListXincoDependencyType);
                if (oldXincoDependencyBehaviorOfXincoDependencyTypeListXincoDependencyType != null) {
                    oldXincoDependencyBehaviorOfXincoDependencyTypeListXincoDependencyType.getXincoDependencyTypeList().remove(xincoDependencyTypeListXincoDependencyType);
                    oldXincoDependencyBehaviorOfXincoDependencyTypeListXincoDependencyType = em.merge(oldXincoDependencyBehaviorOfXincoDependencyTypeListXincoDependencyType);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoDependencyBehavior xincoDependencyBehavior) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoDependencyBehavior persistentXincoDependencyBehavior = em.find(XincoDependencyBehavior.class, xincoDependencyBehavior.getId());
            List<XincoDependencyType> xincoDependencyTypeListOld = persistentXincoDependencyBehavior.getXincoDependencyTypeList();
            List<XincoDependencyType> xincoDependencyTypeListNew = xincoDependencyBehavior.getXincoDependencyTypeList();
            List<String> illegalOrphanMessages = null;
            for (XincoDependencyType xincoDependencyTypeListOldXincoDependencyType : xincoDependencyTypeListOld) {
                if (!xincoDependencyTypeListNew.contains(xincoDependencyTypeListOldXincoDependencyType)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain XincoDependencyType " + xincoDependencyTypeListOldXincoDependencyType + " since its xincoDependencyBehavior field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoDependencyType> attachedXincoDependencyTypeListNew = new ArrayList<>();
            for (XincoDependencyType xincoDependencyTypeListNewXincoDependencyTypeToAttach : xincoDependencyTypeListNew) {
                xincoDependencyTypeListNewXincoDependencyTypeToAttach = em.getReference(xincoDependencyTypeListNewXincoDependencyTypeToAttach.getClass(), xincoDependencyTypeListNewXincoDependencyTypeToAttach.getId());
                attachedXincoDependencyTypeListNew.add(xincoDependencyTypeListNewXincoDependencyTypeToAttach);
            }
            xincoDependencyTypeListNew = attachedXincoDependencyTypeListNew;
            xincoDependencyBehavior.setXincoDependencyTypeList(xincoDependencyTypeListNew);
            xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
            for (XincoDependencyType xincoDependencyTypeListNewXincoDependencyType : xincoDependencyTypeListNew) {
                if (!xincoDependencyTypeListOld.contains(xincoDependencyTypeListNewXincoDependencyType)) {
                    XincoDependencyBehavior oldXincoDependencyBehaviorOfXincoDependencyTypeListNewXincoDependencyType = xincoDependencyTypeListNewXincoDependencyType.getXincoDependencyBehavior();
                    xincoDependencyTypeListNewXincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
                    xincoDependencyTypeListNewXincoDependencyType = em.merge(xincoDependencyTypeListNewXincoDependencyType);
                    if (oldXincoDependencyBehaviorOfXincoDependencyTypeListNewXincoDependencyType != null && !oldXincoDependencyBehaviorOfXincoDependencyTypeListNewXincoDependencyType.equals(xincoDependencyBehavior)) {
                        oldXincoDependencyBehaviorOfXincoDependencyTypeListNewXincoDependencyType.getXincoDependencyTypeList().remove(xincoDependencyTypeListNewXincoDependencyType);
                        oldXincoDependencyBehaviorOfXincoDependencyTypeListNewXincoDependencyType = em.merge(oldXincoDependencyBehaviorOfXincoDependencyTypeListNewXincoDependencyType);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoDependencyBehavior.getId();
                if (findXincoDependencyBehavior(id) == null) {
                    throw new NonexistentEntityException("The xincoDependencyBehavior with id " + id + " no longer exists.");
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
            XincoDependencyBehavior xincoDependencyBehavior;
            try {
                xincoDependencyBehavior = em.getReference(XincoDependencyBehavior.class, id);
                xincoDependencyBehavior.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoDependencyBehavior with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoDependencyType> xincoDependencyTypeListOrphanCheck = xincoDependencyBehavior.getXincoDependencyTypeList();
            for (XincoDependencyType xincoDependencyTypeListOrphanCheckXincoDependencyType : xincoDependencyTypeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This XincoDependencyBehavior (" + xincoDependencyBehavior + ") cannot be destroyed since the XincoDependencyType " + xincoDependencyTypeListOrphanCheckXincoDependencyType + " in its xincoDependencyTypeList field has a non-nullable xincoDependencyBehavior field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xincoDependencyBehavior);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoDependencyBehavior> findXincoDependencyBehaviorEntities() {
        return findXincoDependencyBehaviorEntities(true, -1, -1);
    }

    public List<XincoDependencyBehavior> findXincoDependencyBehaviorEntities(int maxResults, int firstResult) {
        return findXincoDependencyBehaviorEntities(false, maxResults, firstResult);
    }

    private List<XincoDependencyBehavior> findXincoDependencyBehaviorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoDependencyBehavior.class));
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

    public XincoDependencyBehavior findXincoDependencyBehavior(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoDependencyBehavior.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoDependencyBehaviorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoDependencyBehavior> rt = cq.from(XincoDependencyBehavior.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
