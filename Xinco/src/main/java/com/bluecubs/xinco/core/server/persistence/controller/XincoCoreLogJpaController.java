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
 * Name: XincoCoreLogJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
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
public class XincoCoreLogJpaController implements Serializable {

    public XincoCoreLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreLog xincoCoreLog) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUser xincoCoreUser = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreLog.setXincoCoreUser(xincoCoreUser);
            }
            XincoCoreData xincoCoreData = xincoCoreLog.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData = em.getReference(xincoCoreData.getClass(), xincoCoreData.getId());
                xincoCoreLog.setXincoCoreData(xincoCoreData);
            }
            em.persist(xincoCoreLog);
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreData = em.merge(xincoCoreData);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreLog(xincoCoreLog.getId()) != null) {
                throw new PreexistingEntityException("XincoCoreLog " + xincoCoreLog + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreLog xincoCoreLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreLog persistentXincoCoreLog = em.find(XincoCoreLog.class, xincoCoreLog.getId());
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreLog.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreLog.getXincoCoreUser();
            XincoCoreData xincoCoreDataOld = persistentXincoCoreLog.getXincoCoreData();
            XincoCoreData xincoCoreDataNew = xincoCoreLog.getXincoCoreData();
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreLog.setXincoCoreUser(xincoCoreUserNew);
            }
            if (xincoCoreDataNew != null) {
                xincoCoreDataNew = em.getReference(xincoCoreDataNew.getClass(), xincoCoreDataNew.getId());
                xincoCoreLog.setXincoCoreData(xincoCoreDataNew);
            }
            xincoCoreLog = em.merge(xincoCoreLog);
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
            }
            if (xincoCoreDataOld != null && !xincoCoreDataOld.equals(xincoCoreDataNew)) {
                xincoCoreDataOld.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreDataOld = em.merge(xincoCoreDataOld);
            }
            if (xincoCoreDataNew != null && !xincoCoreDataNew.equals(xincoCoreDataOld)) {
                xincoCoreDataNew.getXincoCoreLogList().add(xincoCoreLog);
                xincoCoreDataNew = em.merge(xincoCoreDataNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreLog.getId();
                if (findXincoCoreLog(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreLog with id " + id + " no longer exists.");
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
            XincoCoreLog xincoCoreLog;
            try {
                xincoCoreLog = em.getReference(XincoCoreLog.class, id);
                xincoCoreLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreLog with id " + id + " no longer exists.", enfe);
            }
            XincoCoreUser xincoCoreUser = xincoCoreLog.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            XincoCoreData xincoCoreData = xincoCoreLog.getXincoCoreData();
            if (xincoCoreData != null) {
                xincoCoreData.getXincoCoreLogList().remove(xincoCoreLog);
                xincoCoreData = em.merge(xincoCoreData);
            }
            em.remove(xincoCoreLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreLog> findXincoCoreLogEntities() {
        return findXincoCoreLogEntities(true, -1, -1);
    }

    public List<XincoCoreLog> findXincoCoreLogEntities(int maxResults, int firstResult) {
        return findXincoCoreLogEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreLog> findXincoCoreLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreLog.class));
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

    public XincoCoreLog findXincoCoreLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreLog> rt = cq.from(XincoCoreLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
