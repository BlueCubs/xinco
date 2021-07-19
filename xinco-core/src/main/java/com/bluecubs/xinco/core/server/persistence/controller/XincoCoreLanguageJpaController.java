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
 * Name: XincoCoreLanguageJpaController
 *
 * Description: JPA Controller
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoCoreLanguageJpaController implements Serializable {

  public XincoCoreLanguageJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(XincoCoreLanguage xincoCoreLanguage)
      throws PreexistingEntityException, Exception {
    if (xincoCoreLanguage.getXincoCoreNodeList() == null) {
      xincoCoreLanguage.setXincoCoreNodeList(new ArrayList<>());
    }
    if (xincoCoreLanguage.getXincoCoreDataList() == null) {
      xincoCoreLanguage.setXincoCoreDataList(new ArrayList<>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      List<XincoCoreNode> attachedXincoCoreNodeList = new ArrayList<>();
      for (XincoCoreNode xincoCoreNodeListXincoCoreNodeToAttach :
          xincoCoreLanguage.getXincoCoreNodeList()) {
        xincoCoreNodeListXincoCoreNodeToAttach =
            em.getReference(
                xincoCoreNodeListXincoCoreNodeToAttach.getClass(),
                xincoCoreNodeListXincoCoreNodeToAttach.getId());
        attachedXincoCoreNodeList.add(xincoCoreNodeListXincoCoreNodeToAttach);
      }
      xincoCoreLanguage.setXincoCoreNodeList(attachedXincoCoreNodeList);
      List<XincoCoreData> attachedXincoCoreDataList = new ArrayList<>();
      for (XincoCoreData xincoCoreDataListXincoCoreDataToAttach :
          xincoCoreLanguage.getXincoCoreDataList()) {
        xincoCoreDataListXincoCoreDataToAttach =
            em.getReference(
                xincoCoreDataListXincoCoreDataToAttach.getClass(),
                xincoCoreDataListXincoCoreDataToAttach.getId());
        attachedXincoCoreDataList.add(xincoCoreDataListXincoCoreDataToAttach);
      }
      xincoCoreLanguage.setXincoCoreDataList(attachedXincoCoreDataList);
      em.persist(xincoCoreLanguage);
      for (XincoCoreNode xincoCoreNodeListXincoCoreNode :
          xincoCoreLanguage.getXincoCoreNodeList()) {
        XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode =
            xincoCoreNodeListXincoCoreNode.getXincoCoreLanguage();
        xincoCoreNodeListXincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
        xincoCoreNodeListXincoCoreNode = em.merge(xincoCoreNodeListXincoCoreNode);
        if (oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode != null) {
          oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode
              .getXincoCoreNodeList()
              .remove(xincoCoreNodeListXincoCoreNode);
          oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode =
              em.merge(oldXincoCoreLanguageOfXincoCoreNodeListXincoCoreNode);
        }
      }
      for (XincoCoreData xincoCoreDataListXincoCoreData :
          xincoCoreLanguage.getXincoCoreDataList()) {
        XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData =
            xincoCoreDataListXincoCoreData.getXincoCoreLanguage();
        xincoCoreDataListXincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
        xincoCoreDataListXincoCoreData = em.merge(xincoCoreDataListXincoCoreData);
        if (oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData != null) {
          oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData
              .getXincoCoreDataList()
              .remove(xincoCoreDataListXincoCoreData);
          oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData =
              em.merge(oldXincoCoreLanguageOfXincoCoreDataListXincoCoreData);
        }
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (findXincoCoreLanguage(xincoCoreLanguage.getId()) != null) {
        throw new PreexistingEntityException(
            "XincoCoreLanguage " + xincoCoreLanguage + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(XincoCoreLanguage xincoCoreLanguage)
      throws IllegalOrphanException, NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      XincoCoreLanguage persistentXincoCoreLanguage =
          em.find(XincoCoreLanguage.class, xincoCoreLanguage.getId());
      List<XincoCoreNode> xincoCoreNodeListOld = persistentXincoCoreLanguage.getXincoCoreNodeList();
      List<XincoCoreNode> xincoCoreNodeListNew = xincoCoreLanguage.getXincoCoreNodeList();
      List<XincoCoreData> xincoCoreDataListOld = persistentXincoCoreLanguage.getXincoCoreDataList();
      List<XincoCoreData> xincoCoreDataListNew = xincoCoreLanguage.getXincoCoreDataList();
      List<String> illegalOrphanMessages = null;
      for (XincoCoreNode xincoCoreNodeListOldXincoCoreNode : xincoCoreNodeListOld) {
        if (!xincoCoreNodeListNew.contains(xincoCoreNodeListOldXincoCoreNode)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<>();
          }
          illegalOrphanMessages.add(
              "You must retain XincoCoreNode "
                  + xincoCoreNodeListOldXincoCoreNode
                  + " since its xincoCoreLanguage field is not nullable.");
        }
      }
      for (XincoCoreData xincoCoreDataListOldXincoCoreData : xincoCoreDataListOld) {
        if (!xincoCoreDataListNew.contains(xincoCoreDataListOldXincoCoreData)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<>();
          }
          illegalOrphanMessages.add(
              "You must retain XincoCoreData "
                  + xincoCoreDataListOldXincoCoreData
                  + " since its xincoCoreLanguage field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      List<XincoCoreNode> attachedXincoCoreNodeListNew = new ArrayList<>();
      for (XincoCoreNode xincoCoreNodeListNewXincoCoreNodeToAttach : xincoCoreNodeListNew) {
        xincoCoreNodeListNewXincoCoreNodeToAttach =
            em.getReference(
                xincoCoreNodeListNewXincoCoreNodeToAttach.getClass(),
                xincoCoreNodeListNewXincoCoreNodeToAttach.getId());
        attachedXincoCoreNodeListNew.add(xincoCoreNodeListNewXincoCoreNodeToAttach);
      }
      xincoCoreNodeListNew = attachedXincoCoreNodeListNew;
      xincoCoreLanguage.setXincoCoreNodeList(xincoCoreNodeListNew);
      List<XincoCoreData> attachedXincoCoreDataListNew = new ArrayList<>();
      for (XincoCoreData xincoCoreDataListNewXincoCoreDataToAttach : xincoCoreDataListNew) {
        xincoCoreDataListNewXincoCoreDataToAttach =
            em.getReference(
                xincoCoreDataListNewXincoCoreDataToAttach.getClass(),
                xincoCoreDataListNewXincoCoreDataToAttach.getId());
        attachedXincoCoreDataListNew.add(xincoCoreDataListNewXincoCoreDataToAttach);
      }
      xincoCoreDataListNew = attachedXincoCoreDataListNew;
      xincoCoreLanguage.setXincoCoreDataList(xincoCoreDataListNew);
      xincoCoreLanguage = em.merge(xincoCoreLanguage);
      for (XincoCoreNode xincoCoreNodeListNewXincoCoreNode : xincoCoreNodeListNew) {
        if (!xincoCoreNodeListOld.contains(xincoCoreNodeListNewXincoCoreNode)) {
          XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode =
              xincoCoreNodeListNewXincoCoreNode.getXincoCoreLanguage();
          xincoCoreNodeListNewXincoCoreNode.setXincoCoreLanguage(xincoCoreLanguage);
          xincoCoreNodeListNewXincoCoreNode = em.merge(xincoCoreNodeListNewXincoCoreNode);
          if (oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode != null
              && !oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode.equals(
                  xincoCoreLanguage)) {
            oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode
                .getXincoCoreNodeList()
                .remove(xincoCoreNodeListNewXincoCoreNode);
            oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode =
                em.merge(oldXincoCoreLanguageOfXincoCoreNodeListNewXincoCoreNode);
          }
        }
      }
      for (XincoCoreData xincoCoreDataListNewXincoCoreData : xincoCoreDataListNew) {
        if (!xincoCoreDataListOld.contains(xincoCoreDataListNewXincoCoreData)) {
          XincoCoreLanguage oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData =
              xincoCoreDataListNewXincoCoreData.getXincoCoreLanguage();
          xincoCoreDataListNewXincoCoreData.setXincoCoreLanguage(xincoCoreLanguage);
          xincoCoreDataListNewXincoCoreData = em.merge(xincoCoreDataListNewXincoCoreData);
          if (oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData != null
              && !oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData.equals(
                  xincoCoreLanguage)) {
            oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData
                .getXincoCoreDataList()
                .remove(xincoCoreDataListNewXincoCoreData);
            oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData =
                em.merge(oldXincoCoreLanguageOfXincoCoreDataListNewXincoCoreData);
          }
        }
      }
      em.getTransaction().commit();
    } catch (IllegalOrphanException ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = xincoCoreLanguage.getId();
        if (findXincoCoreLanguage(id) == null) {
          throw new NonexistentEntityException(
              "The xincoCoreLanguage with id " + id + " no longer exists.");
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
      XincoCoreLanguage xincoCoreLanguage;
      try {
        xincoCoreLanguage = em.getReference(XincoCoreLanguage.class, id);
        xincoCoreLanguage.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The xincoCoreLanguage with id " + id + " no longer exists.", enfe);
      }
      List<String> illegalOrphanMessages = null;
      List<XincoCoreNode> xincoCoreNodeListOrphanCheck = xincoCoreLanguage.getXincoCoreNodeList();
      for (XincoCoreNode xincoCoreNodeListOrphanCheckXincoCoreNode : xincoCoreNodeListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<>();
        }
        illegalOrphanMessages.add(
            "This XincoCoreLanguage ("
                + xincoCoreLanguage
                + ") cannot be destroyed since the XincoCoreNode "
                + xincoCoreNodeListOrphanCheckXincoCoreNode
                + " in its xincoCoreNodeList field has a non-nullable xincoCoreLanguage field.");
      }
      List<XincoCoreData> xincoCoreDataListOrphanCheck = xincoCoreLanguage.getXincoCoreDataList();
      for (XincoCoreData xincoCoreDataListOrphanCheckXincoCoreData : xincoCoreDataListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<>();
        }
        illegalOrphanMessages.add(
            "This XincoCoreLanguage ("
                + xincoCoreLanguage
                + ") cannot be destroyed since the XincoCoreData "
                + xincoCoreDataListOrphanCheckXincoCoreData
                + " in its xincoCoreDataList field has a non-nullable xincoCoreLanguage field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(xincoCoreLanguage);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<XincoCoreLanguage> findXincoCoreLanguageEntities() {
    return findXincoCoreLanguageEntities(true, -1, -1);
  }

  public List<XincoCoreLanguage> findXincoCoreLanguageEntities(int maxResults, int firstResult) {
    return findXincoCoreLanguageEntities(false, maxResults, firstResult);
  }

  private List<XincoCoreLanguage> findXincoCoreLanguageEntities(
      boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(XincoCoreLanguage.class));
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

  public XincoCoreLanguage findXincoCoreLanguage(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(XincoCoreLanguage.class, id);
    } finally {
      em.close();
    }
  }

  public int getXincoCoreLanguageCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<XincoCoreLanguage> rt = cq.from(XincoCoreLanguage.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
}
