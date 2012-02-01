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
 * Name: XincoCoreUserTJpaController
 * 
 * Description: JPA Controller
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreUserT;
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
public class XincoCoreUserTJpaController implements Serializable {

    public XincoCoreUserTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUserT xincoCoreUserT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreUserT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreUserT(xincoCoreUserT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreUserT " + xincoCoreUserT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreUserT xincoCoreUserT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreUserT = em.merge(xincoCoreUserT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreUserT.getRecordId();
                if (findXincoCoreUserT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreUserT with id " + id + " no longer exists.");
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
            XincoCoreUserT xincoCoreUserT;
            try {
                xincoCoreUserT = em.getReference(XincoCoreUserT.class, id);
                xincoCoreUserT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreUserT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreUserT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreUserT> findXincoCoreUserTEntities() {
        return findXincoCoreUserTEntities(true, -1, -1);
    }

    public List<XincoCoreUserT> findXincoCoreUserTEntities(int maxResults, int firstResult) {
        return findXincoCoreUserTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreUserT> findXincoCoreUserTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreUserT.class));
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

    public XincoCoreUserT findXincoCoreUserT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreUserT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreUserTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreUserT> rt = cq.from(XincoCoreUserT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
