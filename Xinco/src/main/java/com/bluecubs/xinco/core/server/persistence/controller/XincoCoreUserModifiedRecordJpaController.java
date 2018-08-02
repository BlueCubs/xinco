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
 * Name: XincoCoreUserModifiedRecordJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultron javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecordPK;
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
public class XincoCoreUserModifiedRecordJpaController implements Serializable {

    public XincoCoreUserModifiedRecordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUserModifiedRecord xincoCoreUserModifiedRecord) throws PreexistingEntityException, Exception {
        if (xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK() == null) {
            xincoCoreUserModifiedRecord.setXincoCoreUserModifiedRecordPK(new XincoCoreUserModifiedRecordPK());
        }
        xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK().setId(xincoCoreUserModifiedRecord.getXincoCoreUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUser xincoCoreUser = xincoCoreUserModifiedRecord.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreUserModifiedRecord.setXincoCoreUser(xincoCoreUser);
            }
            em.persist(xincoCoreUserModifiedRecord);
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreUserModifiedRecordList().add(xincoCoreUserModifiedRecord);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreUserModifiedRecord(xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK()) != null) {
                throw new PreexistingEntityException("XincoCoreUserModifiedRecord " + xincoCoreUserModifiedRecord + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreUserModifiedRecord xincoCoreUserModifiedRecord) throws NonexistentEntityException, Exception {
        xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK().setId(xincoCoreUserModifiedRecord.getXincoCoreUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserModifiedRecord persistentXincoCoreUserModifiedRecord = em.find(XincoCoreUserModifiedRecord.class, xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK());
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreUserModifiedRecord.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreUserModifiedRecord.getXincoCoreUser();
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreUserModifiedRecord.setXincoCoreUser(xincoCoreUserNew);
            }
            xincoCoreUserModifiedRecord = em.merge(xincoCoreUserModifiedRecord);
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreUserModifiedRecordList().remove(xincoCoreUserModifiedRecord);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreUserModifiedRecordList().add(xincoCoreUserModifiedRecord);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoCoreUserModifiedRecordPK id = xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK();
                if (findXincoCoreUserModifiedRecord(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreUserModifiedRecord with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoCoreUserModifiedRecordPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserModifiedRecord xincoCoreUserModifiedRecord;
            try {
                xincoCoreUserModifiedRecord = em.getReference(XincoCoreUserModifiedRecord.class, id);
                xincoCoreUserModifiedRecord.getXincoCoreUserModifiedRecordPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreUserModifiedRecord with id " + id + " no longer exists.", enfe);
            }
            XincoCoreUser xincoCoreUser = xincoCoreUserModifiedRecord.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreUserModifiedRecordList().remove(xincoCoreUserModifiedRecord);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.remove(xincoCoreUserModifiedRecord);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreUserModifiedRecord> findXincoCoreUserModifiedRecordEntities() {
        return findXincoCoreUserModifiedRecordEntities(true, -1, -1);
    }

    public List<XincoCoreUserModifiedRecord> findXincoCoreUserModifiedRecordEntities(int maxResults, int firstResult) {
        return findXincoCoreUserModifiedRecordEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreUserModifiedRecord> findXincoCoreUserModifiedRecordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreUserModifiedRecord.class));
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

    public XincoCoreUserModifiedRecord findXincoCoreUserModifiedRecord(XincoCoreUserModifiedRecordPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreUserModifiedRecord.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreUserModifiedRecordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreUserModifiedRecord> rt = cq.from(XincoCoreUserModifiedRecord.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
