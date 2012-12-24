/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoParameter;
import com.bluecubs.xinco.workflow.persistence.XincoParameterPK;
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
import com.bluecubs.xinco.workflow.persistence.XincoAction;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoParameterJpaController implements Serializable {

    public XincoParameterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoParameter xincoParameter) throws PreexistingEntityException, Exception {
        if (xincoParameter.getXincoParameterPK() == null) {
            xincoParameter.setXincoParameterPK(new XincoParameterPK());
        }
        xincoParameter.getXincoParameterPK().setXincoActionId(xincoParameter.getXincoAction().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoAction xincoAction = xincoParameter.getXincoAction();
            if (xincoAction != null) {
                xincoAction = em.getReference(xincoAction.getClass(), xincoAction.getId());
                xincoParameter.setXincoAction(xincoAction);
            }
            em.persist(xincoParameter);
            if (xincoAction != null) {
                xincoAction.getXincoParameterList().add(xincoParameter);
                xincoAction = em.merge(xincoAction);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoParameter(xincoParameter.getXincoParameterPK()) != null) {
                throw new PreexistingEntityException("XincoParameter " + xincoParameter + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoParameter xincoParameter) throws NonexistentEntityException, Exception {
        xincoParameter.getXincoParameterPK().setXincoActionId(xincoParameter.getXincoAction().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoParameter persistentXincoParameter = em.find(XincoParameter.class, xincoParameter.getXincoParameterPK());
            XincoAction xincoActionOld = persistentXincoParameter.getXincoAction();
            XincoAction xincoActionNew = xincoParameter.getXincoAction();
            if (xincoActionNew != null) {
                xincoActionNew = em.getReference(xincoActionNew.getClass(), xincoActionNew.getId());
                xincoParameter.setXincoAction(xincoActionNew);
            }
            xincoParameter = em.merge(xincoParameter);
            if (xincoActionOld != null && !xincoActionOld.equals(xincoActionNew)) {
                xincoActionOld.getXincoParameterList().remove(xincoParameter);
                xincoActionOld = em.merge(xincoActionOld);
            }
            if (xincoActionNew != null && !xincoActionNew.equals(xincoActionOld)) {
                xincoActionNew.getXincoParameterList().add(xincoParameter);
                xincoActionNew = em.merge(xincoActionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoParameterPK id = xincoParameter.getXincoParameterPK();
                if (findXincoParameter(id) == null) {
                    throw new NonexistentEntityException("The xincoParameter with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoParameterPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoParameter xincoParameter;
            try {
                xincoParameter = em.getReference(XincoParameter.class, id);
                xincoParameter.getXincoParameterPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoParameter with id " + id + " no longer exists.", enfe);
            }
            XincoAction xincoAction = xincoParameter.getXincoAction();
            if (xincoAction != null) {
                xincoAction.getXincoParameterList().remove(xincoParameter);
                xincoAction = em.merge(xincoAction);
            }
            em.remove(xincoParameter);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoParameter> findXincoParameterEntities() {
        return findXincoParameterEntities(true, -1, -1);
    }

    public List<XincoParameter> findXincoParameterEntities(int maxResults, int firstResult) {
        return findXincoParameterEntities(false, maxResults, firstResult);
    }

    private List<XincoParameter> findXincoParameterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoParameter.class));
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

    public XincoParameter findXincoParameter(XincoParameterPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoParameter.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoParameterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoParameter> rt = cq.from(XincoParameter.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
