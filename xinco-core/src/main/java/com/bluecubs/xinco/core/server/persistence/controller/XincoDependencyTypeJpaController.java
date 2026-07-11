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
 * Name: XincoDependencyTypeJpaController
 *
 * Description: JPA Controller
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
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
      xincoDependencyType.setXincoCoreDataHasDependencyList(new ArrayList<>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      XincoDependencyBehavior xincoDependencyBehavior =
          xincoDependencyType.getXincoDependencyBehavior();
      if (xincoDependencyBehavior != null) {
        xincoDependencyBehavior =
            em.getReference(
                org.hibernate.Hibernate.getClass(xincoDependencyBehavior),
                xincoDependencyBehavior.getId());
        xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehavior);
      }
      List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyList = new ArrayList<>();
      for (XincoCoreDataHasDependency
          xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach :
              xincoDependencyType.getXincoCoreDataHasDependencyList()) {
        xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach =
            em.getReference(
                org.hibernate.Hibernate.getClass(
                    xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach),
                xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach
                    .getXincoCoreDataHasDependencyPK());
        attachedXincoCoreDataHasDependencyList.add(
            xincoCoreDataHasDependencyListXincoCoreDataHasDependencyToAttach);
      }
      xincoDependencyType.setXincoCoreDataHasDependencyList(attachedXincoCoreDataHasDependencyList);
      em.persist(xincoDependencyType);
      if (xincoDependencyBehavior != null) {
        xincoDependencyBehavior.getXincoDependencyTypeList().add(xincoDependencyType);
        xincoDependencyBehavior = em.merge(xincoDependencyBehavior);
      }
      for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListXincoCoreDataHasDependency :
          xincoDependencyType.getXincoCoreDataHasDependencyList()) {
        XincoDependencyType
            oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency =
                xincoCoreDataHasDependencyListXincoCoreDataHasDependency.getXincoDependencyType();
        xincoCoreDataHasDependencyListXincoCoreDataHasDependency.setXincoDependencyType(
            xincoDependencyType);
        xincoCoreDataHasDependencyListXincoCoreDataHasDependency =
            em.merge(xincoCoreDataHasDependencyListXincoCoreDataHasDependency);
        if (oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency
            != null) {
          oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency
              .getXincoCoreDataHasDependencyList()
              .remove(xincoCoreDataHasDependencyListXincoCoreDataHasDependency);
          oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency =
              em.merge(
                  oldXincoDependencyTypeOfXincoCoreDataHasDependencyListXincoCoreDataHasDependency);
        }
      }
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(XincoDependencyType xincoDependencyType)
      throws IllegalOrphanException, NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      XincoDependencyType persistentXincoDependencyType =
          em.find(XincoDependencyType.class, xincoDependencyType.getId());
      XincoDependencyBehavior xincoDependencyBehaviorOld =
          persistentXincoDependencyType.getXincoDependencyBehavior();
      XincoDependencyBehavior xincoDependencyBehaviorNew =
          xincoDependencyType.getXincoDependencyBehavior();
      List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOld =
          persistentXincoDependencyType.getXincoCoreDataHasDependencyList();
      List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListNew =
          xincoDependencyType.getXincoCoreDataHasDependencyList();
      boolean xincoCoreDataHasDependencyListNewInit =
          org.hibernate.Hibernate.isInitialized(xincoCoreDataHasDependencyListNew);
      List<String> illegalOrphanMessages = null;
      for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency :
          xincoCoreDataHasDependencyListOld) {
        if (xincoCoreDataHasDependencyListNewInit
            && !xincoCoreDataHasDependencyListNew.contains(
                xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<>();
          }
          illegalOrphanMessages.add(
              "You must retain XincoCoreDataHasDependency "
                  + xincoCoreDataHasDependencyListOldXincoCoreDataHasDependency
                  + " since its xincoDependencyType field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      if (xincoDependencyBehaviorNew != null) {
        xincoDependencyBehaviorNew =
            em.getReference(
                org.hibernate.Hibernate.getClass(xincoDependencyBehaviorNew),
                xincoDependencyBehaviorNew.getId());
        xincoDependencyType.setXincoDependencyBehavior(xincoDependencyBehaviorNew);
      }
      List<XincoCoreDataHasDependency> attachedXincoCoreDataHasDependencyListNew =
          new ArrayList<>();
      if (xincoCoreDataHasDependencyListNewInit) {
        for (XincoCoreDataHasDependency
            xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach :
                xincoCoreDataHasDependencyListNew) {
          xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach =
              em.getReference(
                  org.hibernate.Hibernate.getClass(
                      xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach),
                  xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach
                      .getXincoCoreDataHasDependencyPK());
          attachedXincoCoreDataHasDependencyListNew.add(
              xincoCoreDataHasDependencyListNewXincoCoreDataHasDependencyToAttach);
        }
      }
      xincoCoreDataHasDependencyListNew = attachedXincoCoreDataHasDependencyListNew;
      xincoDependencyType.setXincoCoreDataHasDependencyList(xincoCoreDataHasDependencyListNew);
      xincoDependencyType = em.merge(xincoDependencyType);
      if (xincoDependencyBehaviorOld != null
          && !xincoDependencyBehaviorOld.equals(xincoDependencyBehaviorNew)) {
        xincoDependencyBehaviorOld.getXincoDependencyTypeList().remove(xincoDependencyType);
        xincoDependencyBehaviorOld = em.merge(xincoDependencyBehaviorOld);
      }
      if (xincoDependencyBehaviorNew != null
          && !xincoDependencyBehaviorNew.equals(xincoDependencyBehaviorOld)) {
        xincoDependencyBehaviorNew.getXincoDependencyTypeList().add(xincoDependencyType);
        xincoDependencyBehaviorNew = em.merge(xincoDependencyBehaviorNew);
      }
      for (XincoCoreDataHasDependency xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency :
          xincoCoreDataHasDependencyListNew) {
        if (!xincoCoreDataHasDependencyListOld.contains(
            xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency)) {
          XincoDependencyType
              oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency =
                  xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency
                      .getXincoDependencyType();
          xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency.setXincoDependencyType(
              xincoDependencyType);
          xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency =
              em.merge(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
          if (oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency
                  != null
              && !oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency
                  .equals(xincoDependencyType)) {
            oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency
                .getXincoCoreDataHasDependencyList()
                .remove(xincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
            oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency =
                em.merge(
                    oldXincoDependencyTypeOfXincoCoreDataHasDependencyListNewXincoCoreDataHasDependency);
          }
        }
      }
      em.getTransaction().commit();
    } catch (IllegalOrphanException ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.isEmpty()) {
        Integer id = xincoDependencyType.getId();
        if (findXincoDependencyType(id) == null) {
          throw new NonexistentEntityException(
              "The xincoDependencyType with id " + id + " no longer exists.");
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
      XincoDependencyType xincoDependencyType = em.find(XincoDependencyType.class, id);

      if (xincoDependencyType == null) {
        throw new NonexistentEntityException(
            "The xincoDependencyType with id " + id + " no longer exists.");
      }
      List<String> illegalOrphanMessages = null;
      List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyListOrphanCheck =
          xincoDependencyType.getXincoCoreDataHasDependencyList();
      for (XincoCoreDataHasDependency
          xincoCoreDataHasDependencyListOrphanCheckXincoCoreDataHasDependency :
              xincoCoreDataHasDependencyListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<>();
        }
        illegalOrphanMessages.add(
            "This XincoDependencyType ("
                + xincoDependencyType
                + ") cannot be destroyed since the XincoCoreDataHasDependency "
                + xincoCoreDataHasDependencyListOrphanCheckXincoCoreDataHasDependency
                + " in its xincoCoreDataHasDependencyList field has a non-nullable"
                + " xincoDependencyType field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      XincoDependencyBehavior xincoDependencyBehavior =
          xincoDependencyType.getXincoDependencyBehavior();
      if (xincoDependencyBehavior != null) {
        xincoDependencyBehavior.getXincoDependencyTypeList().remove(xincoDependencyType);
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

  public List<XincoDependencyType> findXincoDependencyTypeEntities(
      int maxResults, int firstResult) {
    return findXincoDependencyTypeEntities(false, maxResults, firstResult);
  }

  private List<XincoDependencyType> findXincoDependencyTypeEntities(
      boolean all, int maxResults, int firstResult) {
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
