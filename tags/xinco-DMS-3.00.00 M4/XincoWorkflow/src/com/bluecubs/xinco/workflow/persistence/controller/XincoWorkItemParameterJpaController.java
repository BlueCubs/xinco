/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoWorkItemParameter;
import com.bluecubs.xinco.workflow.persistence.XincoWorkItemParameterPK;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoWorkItemHasXincoState;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoWorkItemParameterJpaController implements Serializable {

    public XincoWorkItemParameterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoWorkItemParameter xincoWorkItemParameter) throws PreexistingEntityException, Exception {
        if (xincoWorkItemParameter.getXincoWorkItemParameterPK() == null) {
            xincoWorkItemParameter.setXincoWorkItemParameterPK(new XincoWorkItemParameterPK());
        }
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoStateId(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoStateId());
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoWorkflowVersion(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoStateXincoWorkflowVersion());
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoWorkItemId(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoWorkItemId());
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoWorkflowId(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoStateXincoWorkflowId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkItemHasXincoState xincoWorkItemHasXincoState = xincoWorkItemParameter.getXincoWorkItemHasXincoState();
            if (xincoWorkItemHasXincoState != null) {
                xincoWorkItemHasXincoState = em.getReference(xincoWorkItemHasXincoState.getClass(), xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK());
                xincoWorkItemParameter.setXincoWorkItemHasXincoState(xincoWorkItemHasXincoState);
            }
            em.persist(xincoWorkItemParameter);
            if (xincoWorkItemHasXincoState != null) {
                xincoWorkItemHasXincoState.getXincoWorkItemParameterList().add(xincoWorkItemParameter);
                xincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoState);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoWorkItemParameter(xincoWorkItemParameter.getXincoWorkItemParameterPK()) != null) {
                throw new PreexistingEntityException("XincoWorkItemParameter " + xincoWorkItemParameter + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoWorkItemParameter xincoWorkItemParameter) throws NonexistentEntityException, Exception {
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoStateId(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoStateId());
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoWorkflowVersion(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoStateXincoWorkflowVersion());
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoWorkItemId(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoWorkItemId());
        xincoWorkItemParameter.getXincoWorkItemParameterPK().setXincoWorkflowId(xincoWorkItemParameter.getXincoWorkItemHasXincoState().getXincoWorkItemHasXincoStatePK().getXincoStateXincoWorkflowId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkItemParameter persistentXincoWorkItemParameter = em.find(XincoWorkItemParameter.class, xincoWorkItemParameter.getXincoWorkItemParameterPK());
            XincoWorkItemHasXincoState xincoWorkItemHasXincoStateOld = persistentXincoWorkItemParameter.getXincoWorkItemHasXincoState();
            XincoWorkItemHasXincoState xincoWorkItemHasXincoStateNew = xincoWorkItemParameter.getXincoWorkItemHasXincoState();
            if (xincoWorkItemHasXincoStateNew != null) {
                xincoWorkItemHasXincoStateNew = em.getReference(xincoWorkItemHasXincoStateNew.getClass(), xincoWorkItemHasXincoStateNew.getXincoWorkItemHasXincoStatePK());
                xincoWorkItemParameter.setXincoWorkItemHasXincoState(xincoWorkItemHasXincoStateNew);
            }
            xincoWorkItemParameter = em.merge(xincoWorkItemParameter);
            if (xincoWorkItemHasXincoStateOld != null && !xincoWorkItemHasXincoStateOld.equals(xincoWorkItemHasXincoStateNew)) {
                xincoWorkItemHasXincoStateOld.getXincoWorkItemParameterList().remove(xincoWorkItemParameter);
                xincoWorkItemHasXincoStateOld = em.merge(xincoWorkItemHasXincoStateOld);
            }
            if (xincoWorkItemHasXincoStateNew != null && !xincoWorkItemHasXincoStateNew.equals(xincoWorkItemHasXincoStateOld)) {
                xincoWorkItemHasXincoStateNew.getXincoWorkItemParameterList().add(xincoWorkItemParameter);
                xincoWorkItemHasXincoStateNew = em.merge(xincoWorkItemHasXincoStateNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoWorkItemParameterPK id = xincoWorkItemParameter.getXincoWorkItemParameterPK();
                if (findXincoWorkItemParameter(id) == null) {
                    throw new NonexistentEntityException("The xincoWorkItemParameter with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoWorkItemParameterPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkItemParameter xincoWorkItemParameter;
            try {
                xincoWorkItemParameter = em.getReference(XincoWorkItemParameter.class, id);
                xincoWorkItemParameter.getXincoWorkItemParameterPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoWorkItemParameter with id " + id + " no longer exists.", enfe);
            }
            XincoWorkItemHasXincoState xincoWorkItemHasXincoState = xincoWorkItemParameter.getXincoWorkItemHasXincoState();
            if (xincoWorkItemHasXincoState != null) {
                xincoWorkItemHasXincoState.getXincoWorkItemParameterList().remove(xincoWorkItemParameter);
                xincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoState);
            }
            em.remove(xincoWorkItemParameter);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoWorkItemParameter> findXincoWorkItemParameterEntities() {
        return findXincoWorkItemParameterEntities(true, -1, -1);
    }

    public List<XincoWorkItemParameter> findXincoWorkItemParameterEntities(int maxResults, int firstResult) {
        return findXincoWorkItemParameterEntities(false, maxResults, firstResult);
    }

    private List<XincoWorkItemParameter> findXincoWorkItemParameterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoWorkItemParameter.class));
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

    public XincoWorkItemParameter findXincoWorkItemParameter(XincoWorkItemParameterPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoWorkItemParameter.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoWorkItemParameterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoWorkItemParameter> rt = cq.from(XincoWorkItemParameter.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
