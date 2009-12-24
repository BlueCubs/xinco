/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db.controller;

import com.bluecubs.xinco.workflow.db.XincoWorkflow;
import com.bluecubs.xinco.workflow.db.XincoWorkflowPK;
import com.bluecubs.xinco.workflow.db.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowJpaController {

    public XincoWorkflowJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoWorkflow xincoWorkflow) throws PreexistingEntityException, Exception {
        if (xincoWorkflow.getXincoWorkflowPK() == null) {
            xincoWorkflow.setXincoWorkflowPK(new XincoWorkflowPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoWorkflow);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoWorkflow(xincoWorkflow.getXincoWorkflowPK()) != null) {
                throw new PreexistingEntityException("XincoWorkflow " + xincoWorkflow + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoWorkflow xincoWorkflow) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoWorkflow = em.merge(xincoWorkflow);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoWorkflowPK id = xincoWorkflow.getXincoWorkflowPK();
                if (findXincoWorkflow(id) == null) {
                    throw new NonexistentEntityException("The xincoWorkflow with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoWorkflowPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflow xincoWorkflow;
            try {
                xincoWorkflow = em.getReference(XincoWorkflow.class, id);
                xincoWorkflow.getXincoWorkflowPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoWorkflow with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoWorkflow);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoWorkflow> findXincoWorkflowEntities() {
        return findXincoWorkflowEntities(true, -1, -1);
    }

    public List<XincoWorkflow> findXincoWorkflowEntities(int maxResults, int firstResult) {
        return findXincoWorkflowEntities(false, maxResults, firstResult);
    }

    private List<XincoWorkflow> findXincoWorkflowEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoWorkflow.class));
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

    public XincoWorkflow findXincoWorkflow(XincoWorkflowPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoWorkflow.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoWorkflowCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoWorkflow> rt = cq.from(XincoWorkflow.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
