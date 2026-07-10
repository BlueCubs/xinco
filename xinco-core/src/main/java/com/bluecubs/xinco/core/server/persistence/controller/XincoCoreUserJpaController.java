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
 * Name: XincoCoreUserJpaController
 *
 * Description: JPA Controller
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
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
public class XincoCoreUserJpaController implements Serializable {

  public XincoCoreUserJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(XincoCoreUser xincoCoreUser) throws PreexistingEntityException, Exception {
    if (xincoCoreUser.getXincoCoreAceList() == null) {
      xincoCoreUser.setXincoCoreAceList(new ArrayList<>());
    }
    if (xincoCoreUser.getXincoCoreLogList() == null) {
      xincoCoreUser.setXincoCoreLogList(new ArrayList<>());
    }
    if (xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList() == null) {
      xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      List<XincoCoreAce> attachedXincoCoreAceList = new ArrayList<>();
      for (XincoCoreAce xincoCoreAceListXincoCoreAceToAttach :
          xincoCoreUser.getXincoCoreAceList()) {
        xincoCoreAceListXincoCoreAceToAttach =
            em.getReference(
                org.hibernate.Hibernate.getClass(xincoCoreAceListXincoCoreAceToAttach),
                xincoCoreAceListXincoCoreAceToAttach.getId());
        attachedXincoCoreAceList.add(xincoCoreAceListXincoCoreAceToAttach);
      }
      xincoCoreUser.setXincoCoreAceList(attachedXincoCoreAceList);
      List<XincoCoreLog> attachedXincoCoreLogList = new ArrayList<>();
      for (XincoCoreLog xincoCoreLogListXincoCoreLogToAttach :
          xincoCoreUser.getXincoCoreLogList()) {
        xincoCoreLogListXincoCoreLogToAttach =
            em.getReference(
                org.hibernate.Hibernate.getClass(xincoCoreLogListXincoCoreLogToAttach),
                xincoCoreLogListXincoCoreLogToAttach.getId());
        attachedXincoCoreLogList.add(xincoCoreLogListXincoCoreLogToAttach);
      }
      xincoCoreUser.setXincoCoreLogList(attachedXincoCoreLogList);
      List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupList =
          new ArrayList<>();
      for (XincoCoreUserHasXincoCoreGroup
          xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach :
              xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList()) {
        xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach =
            em.getReference(
                org.hibernate.Hibernate.getClass(
                    xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach),
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach
                    .getXincoCoreUserHasXincoCoreGroupPK());
        attachedXincoCoreUserHasXincoCoreGroupList.add(
            xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach);
      }
      xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList(
          attachedXincoCoreUserHasXincoCoreGroupList);
      em.persist(xincoCoreUser);
      for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreUser.getXincoCoreAceList()) {
        XincoCoreUser oldXincoCoreUserOfXincoCoreAceListXincoCoreAce =
            xincoCoreAceListXincoCoreAce.getXincoCoreUser();
        xincoCoreAceListXincoCoreAce.setXincoCoreUser(xincoCoreUser);
        xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
        if (oldXincoCoreUserOfXincoCoreAceListXincoCoreAce != null) {
          oldXincoCoreUserOfXincoCoreAceListXincoCoreAce
              .getXincoCoreAceList()
              .remove(xincoCoreAceListXincoCoreAce);
          oldXincoCoreUserOfXincoCoreAceListXincoCoreAce =
              em.merge(oldXincoCoreUserOfXincoCoreAceListXincoCoreAce);
        }
      }
      for (XincoCoreLog xincoCoreLogListXincoCoreLog : xincoCoreUser.getXincoCoreLogList()) {
        XincoCoreUser oldXincoCoreUserOfXincoCoreLogListXincoCoreLog =
            xincoCoreLogListXincoCoreLog.getXincoCoreUser();
        xincoCoreLogListXincoCoreLog.setXincoCoreUser(xincoCoreUser);
        xincoCoreLogListXincoCoreLog = em.merge(xincoCoreLogListXincoCoreLog);
        if (oldXincoCoreUserOfXincoCoreLogListXincoCoreLog != null) {
          oldXincoCoreUserOfXincoCoreLogListXincoCoreLog
              .getXincoCoreLogList()
              .remove(xincoCoreLogListXincoCoreLog);
          oldXincoCoreUserOfXincoCoreLogListXincoCoreLog =
              em.merge(oldXincoCoreUserOfXincoCoreLogListXincoCoreLog);
        }
      }
      for (XincoCoreUserHasXincoCoreGroup
          xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup :
              xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList()) {
        XincoCoreUser
            oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup =
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
        xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.setXincoCoreUser(
            xincoCoreUser);
        xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup =
            em.merge(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
        if (oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup
            != null) {
          oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup
              .getXincoCoreUserHasXincoCoreGroupList()
              .remove(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
          oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup =
              em.merge(
                  oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
        }
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (findXincoCoreUser(xincoCoreUser.getId()) != null) {
        throw new PreexistingEntityException(
            "XincoCoreUser " + xincoCoreUser + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(XincoCoreUser xincoCoreUser)
      throws IllegalOrphanException, NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      XincoCoreUser persistentXincoCoreUser = em.find(XincoCoreUser.class, xincoCoreUser.getId());
      List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreUser.getXincoCoreAceList();
      List<XincoCoreAce> xincoCoreAceListNew = xincoCoreUser.getXincoCoreAceList();
      boolean xincoCoreAceListNewInit = org.hibernate.Hibernate.isInitialized(xincoCoreAceListNew);
      List<XincoCoreLog> xincoCoreLogListOld = persistentXincoCoreUser.getXincoCoreLogList();
      List<XincoCoreLog> xincoCoreLogListNew = xincoCoreUser.getXincoCoreLogList();
      boolean xincoCoreLogListNewInit = org.hibernate.Hibernate.isInitialized(xincoCoreLogListNew);
      List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListOld =
          persistentXincoCoreUser.getXincoCoreUserHasXincoCoreGroupList();
      List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListNew =
          xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList();
      boolean xincoCoreUserHasXincoCoreGroupListNewInit =
          org.hibernate.Hibernate.isInitialized(xincoCoreUserHasXincoCoreGroupListNew);
      List<String> illegalOrphanMessages = null;
      for (XincoCoreLog xincoCoreLogListOldXincoCoreLog : xincoCoreLogListOld) {
        if (xincoCoreLogListNewInit
            && !xincoCoreLogListNew.contains(xincoCoreLogListOldXincoCoreLog)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<>();
          }
          illegalOrphanMessages.add(
              "You must retain XincoCoreLog "
                  + xincoCoreLogListOldXincoCoreLog
                  + " since its xincoCoreUser field is not nullable.");
        }
      }
      for (XincoCoreUserHasXincoCoreGroup
          xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup :
              xincoCoreUserHasXincoCoreGroupListOld) {
        if (xincoCoreUserHasXincoCoreGroupListNewInit
            && !xincoCoreUserHasXincoCoreGroupListNew.contains(
                xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<>();
          }
          illegalOrphanMessages.add(
              "You must retain XincoCoreUserHasXincoCoreGroup "
                  + xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup
                  + " since its xincoCoreUser field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      List<XincoCoreAce> attachedXincoCoreAceListNew = new ArrayList<>();
      if (xincoCoreAceListNewInit) {
        for (XincoCoreAce xincoCoreAceListNewXincoCoreAceToAttach : xincoCoreAceListNew) {
          xincoCoreAceListNewXincoCoreAceToAttach =
              em.getReference(
                  org.hibernate.Hibernate.getClass(xincoCoreAceListNewXincoCoreAceToAttach),
                  xincoCoreAceListNewXincoCoreAceToAttach.getId());
          attachedXincoCoreAceListNew.add(xincoCoreAceListNewXincoCoreAceToAttach);
        }
      }
      xincoCoreAceListNew = attachedXincoCoreAceListNew;
      xincoCoreUser.setXincoCoreAceList(xincoCoreAceListNew);
      List<XincoCoreLog> attachedXincoCoreLogListNew = new ArrayList<>();
      if (xincoCoreLogListNewInit) {
        for (XincoCoreLog xincoCoreLogListNewXincoCoreLogToAttach : xincoCoreLogListNew) {
          xincoCoreLogListNewXincoCoreLogToAttach =
              em.getReference(
                  org.hibernate.Hibernate.getClass(xincoCoreLogListNewXincoCoreLogToAttach),
                  xincoCoreLogListNewXincoCoreLogToAttach.getId());
          attachedXincoCoreLogListNew.add(xincoCoreLogListNewXincoCoreLogToAttach);
        }
      }
      xincoCoreLogListNew = attachedXincoCoreLogListNew;
      xincoCoreUser.setXincoCoreLogList(xincoCoreLogListNew);
      List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupListNew =
          new ArrayList<>();
      if (xincoCoreUserHasXincoCoreGroupListNewInit) {
        for (XincoCoreUserHasXincoCoreGroup
            xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach :
                xincoCoreUserHasXincoCoreGroupListNew) {
          xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach =
              em.getReference(
                  org.hibernate.Hibernate.getClass(
                      xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach),
                  xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach
                      .getXincoCoreUserHasXincoCoreGroupPK());
          attachedXincoCoreUserHasXincoCoreGroupListNew.add(
              xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach);
        }
      }
      xincoCoreUserHasXincoCoreGroupListNew = attachedXincoCoreUserHasXincoCoreGroupListNew;
      xincoCoreUser.setXincoCoreUserHasXincoCoreGroupList(xincoCoreUserHasXincoCoreGroupListNew);
      xincoCoreUser = em.merge(xincoCoreUser);
      for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
        if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
          xincoCoreAceListOldXincoCoreAce.setXincoCoreUser(null);
          xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
        }
      }
      for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
        if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
          XincoCoreUser oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce =
              xincoCoreAceListNewXincoCoreAce.getXincoCoreUser();
          xincoCoreAceListNewXincoCoreAce.setXincoCoreUser(xincoCoreUser);
          xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
          if (oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce != null
              && !oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreUser)) {
            oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce
                .getXincoCoreAceList()
                .remove(xincoCoreAceListNewXincoCoreAce);
            oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce =
                em.merge(oldXincoCoreUserOfXincoCoreAceListNewXincoCoreAce);
          }
        }
      }
      for (XincoCoreLog xincoCoreLogListNewXincoCoreLog : xincoCoreLogListNew) {
        if (!xincoCoreLogListOld.contains(xincoCoreLogListNewXincoCoreLog)) {
          XincoCoreUser oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog =
              xincoCoreLogListNewXincoCoreLog.getXincoCoreUser();
          xincoCoreLogListNewXincoCoreLog.setXincoCoreUser(xincoCoreUser);
          xincoCoreLogListNewXincoCoreLog = em.merge(xincoCoreLogListNewXincoCoreLog);
          if (oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog != null
              && !oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog.equals(xincoCoreUser)) {
            oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog
                .getXincoCoreLogList()
                .remove(xincoCoreLogListNewXincoCoreLog);
            oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog =
                em.merge(oldXincoCoreUserOfXincoCoreLogListNewXincoCoreLog);
          }
        }
      }
      for (XincoCoreUserHasXincoCoreGroup
          xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup :
              xincoCoreUserHasXincoCoreGroupListNew) {
        if (!xincoCoreUserHasXincoCoreGroupListOld.contains(
            xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup)) {
          XincoCoreUser
              oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup =
                  xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup
                      .getXincoCoreUser();
          xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.setXincoCoreUser(
              xincoCoreUser);
          xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup =
              em.merge(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
          if (oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup
                  != null
              && !oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup
                  .equals(xincoCoreUser)) {
            oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup
                .getXincoCoreUserHasXincoCoreGroupList()
                .remove(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
            oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup =
                em.merge(
                    oldXincoCoreUserOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
          }
        }
      }
      em.getTransaction().commit();
    } catch (IllegalOrphanException ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.isEmpty()) {
        Integer id = xincoCoreUser.getId();
        if (findXincoCoreUser(id) == null) {
          throw new NonexistentEntityException(
              "The xincoCoreUser with id " + id + " no longer exists.");
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
      XincoCoreUser xincoCoreUser = em.find(XincoCoreUser.class, id);

      if (xincoCoreUser == null) {
        throw new NonexistentEntityException(
            "The xincoCoreUser with id " + id + " no longer exists.");
      }
      List<String> illegalOrphanMessages = null;
      List<XincoCoreLog> xincoCoreLogListOrphanCheck = xincoCoreUser.getXincoCoreLogList();
      for (XincoCoreLog xincoCoreLogListOrphanCheckXincoCoreLog : xincoCoreLogListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<>();
        }
        illegalOrphanMessages.add(
            "This XincoCoreUser ("
                + xincoCoreUser
                + ") cannot be destroyed since the XincoCoreLog "
                + xincoCoreLogListOrphanCheckXincoCoreLog
                + " in its xincoCoreLogList field has a non-nullable xincoCoreUser field.");
      }
      List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListOrphanCheck =
          xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList();
      for (XincoCoreUserHasXincoCoreGroup
          xincoCoreUserHasXincoCoreGroupListOrphanCheckXincoCoreUserHasXincoCoreGroup :
              xincoCoreUserHasXincoCoreGroupListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<>();
        }
        illegalOrphanMessages.add(
            "This XincoCoreUser ("
                + xincoCoreUser
                + ") cannot be destroyed since the XincoCoreUserHasXincoCoreGroup "
                + xincoCoreUserHasXincoCoreGroupListOrphanCheckXincoCoreUserHasXincoCoreGroup
                + " in its xincoCoreUserHasXincoCoreGroupList field has a non-nullable"
                + " xincoCoreUser field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      List<XincoCoreAce> xincoCoreAceList = xincoCoreUser.getXincoCoreAceList();
      for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
        xincoCoreAceListXincoCoreAce.setXincoCoreUser(null);
      }
      em.remove(xincoCoreUser);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<XincoCoreUser> findXincoCoreUserEntities() {
    return findXincoCoreUserEntities(true, -1, -1);
  }

  public List<XincoCoreUser> findXincoCoreUserEntities(int maxResults, int firstResult) {
    return findXincoCoreUserEntities(false, maxResults, firstResult);
  }

  private List<XincoCoreUser> findXincoCoreUserEntities(
      boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(XincoCoreUser.class));
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

  public XincoCoreUser findXincoCoreUser(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(XincoCoreUser.class, id);
    } finally {
      em.close();
    }
  }

  public int getXincoCoreUserCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<XincoCoreUser> rt = cq.from(XincoCoreUser.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
}
