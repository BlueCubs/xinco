/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguageT;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLanguageTJpaController {

    public XincoCoreLanguageTJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreLanguageT xincoCoreLanguageT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreLanguageT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreLanguageT(xincoCoreLanguageT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreLanguageT " + xincoCoreLanguageT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreLanguageT xincoCoreLanguageT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreLanguageT = em.merge(xincoCoreLanguageT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreLanguageT.getRecordId();
                if (findXincoCoreLanguageT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreLanguageT with id " + id + " no longer exists.");
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
            XincoCoreLanguageT xincoCoreLanguageT;
            try {
                xincoCoreLanguageT = em.getReference(XincoCoreLanguageT.class, id);
                xincoCoreLanguageT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreLanguageT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreLanguageT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreLanguageT> findXincoCoreLanguageTEntities() {
        return findXincoCoreLanguageTEntities(true, -1, -1);
    }

    public List<XincoCoreLanguageT> findXincoCoreLanguageTEntities(int maxResults, int firstResult) {
        return findXincoCoreLanguageTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreLanguageT> findXincoCoreLanguageTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreLanguageT.class));
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

    public XincoCoreLanguageT findXincoCoreLanguageT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreLanguageT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreLanguageTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreLanguageT> rt = cq.from(XincoCoreLanguageT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
