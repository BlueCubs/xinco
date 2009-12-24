/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db.controller;

import com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoState;
import com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoStatePK;
import com.bluecubs.xinco.workflow.db.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.db.XincoWorkflowState;
import com.bluecubs.xinco.workflow.db.XincoAction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoStateTransitionsToXincoStateJpaController {

    public XincoStateTransitionsToXincoStateJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoStateTransitionsToXincoState xincoStateTransitionsToXincoState) throws PreexistingEntityException, Exception {
        if (xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK() == null) {
            xincoStateTransitionsToXincoState.setXincoStateTransitionsToXincoStatePK(new XincoStateTransitionsToXincoStatePK());
        }
        if (xincoStateTransitionsToXincoState.getXincoActionList() == null) {
            xincoStateTransitionsToXincoState.setXincoActionList(new ArrayList<XincoAction>());
        }
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setSourceStateId(xincoStateTransitionsToXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setDestinationStateId(xincoStateTransitionsToXincoState.getXincoWorkflowState1().getXincoWorkflowStatePK().getId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowId(xincoStateTransitionsToXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowVersion1(xincoStateTransitionsToXincoState.getXincoWorkflowState1().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowId1(xincoStateTransitionsToXincoState.getXincoWorkflowState1().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowVersion(xincoStateTransitionsToXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflowState xincoWorkflowState = xincoStateTransitionsToXincoState.getXincoWorkflowState();
            if (xincoWorkflowState != null) {
                xincoWorkflowState = em.getReference(xincoWorkflowState.getClass(), xincoWorkflowState.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setXincoWorkflowState(xincoWorkflowState);
            }
            XincoWorkflowState xincoWorkflowState1 = xincoStateTransitionsToXincoState.getXincoWorkflowState1();
            if (xincoWorkflowState1 != null) {
                xincoWorkflowState1 = em.getReference(xincoWorkflowState1.getClass(), xincoWorkflowState1.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setXincoWorkflowState1(xincoWorkflowState1);
            }
            List<XincoAction> attachedXincoActionList = new ArrayList<XincoAction>();
            for (XincoAction xincoActionListXincoActionToAttach : xincoStateTransitionsToXincoState.getXincoActionList()) {
                xincoActionListXincoActionToAttach = em.getReference(xincoActionListXincoActionToAttach.getClass(), xincoActionListXincoActionToAttach.getId());
                attachedXincoActionList.add(xincoActionListXincoActionToAttach);
            }
            xincoStateTransitionsToXincoState.setXincoActionList(attachedXincoActionList);
            em.persist(xincoStateTransitionsToXincoState);
            if (xincoWorkflowState != null) {
                xincoWorkflowState.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                xincoWorkflowState = em.merge(xincoWorkflowState);
            }
            if (xincoWorkflowState1 != null) {
                xincoWorkflowState1.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                xincoWorkflowState1 = em.merge(xincoWorkflowState1);
            }
            for (XincoAction xincoActionListXincoAction : xincoStateTransitionsToXincoState.getXincoActionList()) {
                xincoActionListXincoAction.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                xincoActionListXincoAction = em.merge(xincoActionListXincoAction);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoStateTransitionsToXincoState(xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK()) != null) {
                throw new PreexistingEntityException("XincoStateTransitionsToXincoState " + xincoStateTransitionsToXincoState + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoStateTransitionsToXincoState xincoStateTransitionsToXincoState) throws NonexistentEntityException, Exception {
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setSourceStateId(xincoStateTransitionsToXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setDestinationStateId(xincoStateTransitionsToXincoState.getXincoWorkflowState1().getXincoWorkflowStatePK().getId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowId(xincoStateTransitionsToXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowVersion1(xincoStateTransitionsToXincoState.getXincoWorkflowState1().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowId1(xincoStateTransitionsToXincoState.getXincoWorkflowState1().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setXincoStateXincoWorkflowVersion(xincoStateTransitionsToXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoStateTransitionsToXincoState persistentXincoStateTransitionsToXincoState = em.find(XincoStateTransitionsToXincoState.class, xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK());
            XincoWorkflowState xincoWorkflowStateOld = persistentXincoStateTransitionsToXincoState.getXincoWorkflowState();
            XincoWorkflowState xincoWorkflowStateNew = xincoStateTransitionsToXincoState.getXincoWorkflowState();
            XincoWorkflowState xincoWorkflowState1Old = persistentXincoStateTransitionsToXincoState.getXincoWorkflowState1();
            XincoWorkflowState xincoWorkflowState1New = xincoStateTransitionsToXincoState.getXincoWorkflowState1();
            List<XincoAction> xincoActionListOld = persistentXincoStateTransitionsToXincoState.getXincoActionList();
            List<XincoAction> xincoActionListNew = xincoStateTransitionsToXincoState.getXincoActionList();
            if (xincoWorkflowStateNew != null) {
                xincoWorkflowStateNew = em.getReference(xincoWorkflowStateNew.getClass(), xincoWorkflowStateNew.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setXincoWorkflowState(xincoWorkflowStateNew);
            }
            if (xincoWorkflowState1New != null) {
                xincoWorkflowState1New = em.getReference(xincoWorkflowState1New.getClass(), xincoWorkflowState1New.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setXincoWorkflowState1(xincoWorkflowState1New);
            }
            List<XincoAction> attachedXincoActionListNew = new ArrayList<XincoAction>();
            for (XincoAction xincoActionListNewXincoActionToAttach : xincoActionListNew) {
                xincoActionListNewXincoActionToAttach = em.getReference(xincoActionListNewXincoActionToAttach.getClass(), xincoActionListNewXincoActionToAttach.getId());
                attachedXincoActionListNew.add(xincoActionListNewXincoActionToAttach);
            }
            xincoActionListNew = attachedXincoActionListNew;
            xincoStateTransitionsToXincoState.setXincoActionList(xincoActionListNew);
            xincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoState);
            if (xincoWorkflowStateOld != null && !xincoWorkflowStateOld.equals(xincoWorkflowStateNew)) {
                xincoWorkflowStateOld.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                xincoWorkflowStateOld = em.merge(xincoWorkflowStateOld);
            }
            if (xincoWorkflowStateNew != null && !xincoWorkflowStateNew.equals(xincoWorkflowStateOld)) {
                xincoWorkflowStateNew.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                xincoWorkflowStateNew = em.merge(xincoWorkflowStateNew);
            }
            if (xincoWorkflowState1Old != null && !xincoWorkflowState1Old.equals(xincoWorkflowState1New)) {
                xincoWorkflowState1Old.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                xincoWorkflowState1Old = em.merge(xincoWorkflowState1Old);
            }
            if (xincoWorkflowState1New != null && !xincoWorkflowState1New.equals(xincoWorkflowState1Old)) {
                xincoWorkflowState1New.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                xincoWorkflowState1New = em.merge(xincoWorkflowState1New);
            }
            for (XincoAction xincoActionListOldXincoAction : xincoActionListOld) {
                if (!xincoActionListNew.contains(xincoActionListOldXincoAction)) {
                    xincoActionListOldXincoAction.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                    xincoActionListOldXincoAction = em.merge(xincoActionListOldXincoAction);
                }
            }
            for (XincoAction xincoActionListNewXincoAction : xincoActionListNew) {
                if (!xincoActionListOld.contains(xincoActionListNewXincoAction)) {
                    xincoActionListNewXincoAction.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                    xincoActionListNewXincoAction = em.merge(xincoActionListNewXincoAction);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoStateTransitionsToXincoStatePK id = xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK();
                if (findXincoStateTransitionsToXincoState(id) == null) {
                    throw new NonexistentEntityException("The xincoStateTransitionsToXincoState with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoStateTransitionsToXincoStatePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoStateTransitionsToXincoState xincoStateTransitionsToXincoState;
            try {
                xincoStateTransitionsToXincoState = em.getReference(XincoStateTransitionsToXincoState.class, id);
                xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoStateTransitionsToXincoState with id " + id + " no longer exists.", enfe);
            }
            XincoWorkflowState xincoWorkflowState = xincoStateTransitionsToXincoState.getXincoWorkflowState();
            if (xincoWorkflowState != null) {
                xincoWorkflowState.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                xincoWorkflowState = em.merge(xincoWorkflowState);
            }
            XincoWorkflowState xincoWorkflowState1 = xincoStateTransitionsToXincoState.getXincoWorkflowState1();
            if (xincoWorkflowState1 != null) {
                xincoWorkflowState1.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                xincoWorkflowState1 = em.merge(xincoWorkflowState1);
            }
            List<XincoAction> xincoActionList = xincoStateTransitionsToXincoState.getXincoActionList();
            for (XincoAction xincoActionListXincoAction : xincoActionList) {
                xincoActionListXincoAction.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                xincoActionListXincoAction = em.merge(xincoActionListXincoAction);
            }
            em.remove(xincoStateTransitionsToXincoState);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoStateTransitionsToXincoState> findXincoStateTransitionsToXincoStateEntities() {
        return findXincoStateTransitionsToXincoStateEntities(true, -1, -1);
    }

    public List<XincoStateTransitionsToXincoState> findXincoStateTransitionsToXincoStateEntities(int maxResults, int firstResult) {
        return findXincoStateTransitionsToXincoStateEntities(false, maxResults, firstResult);
    }

    private List<XincoStateTransitionsToXincoState> findXincoStateTransitionsToXincoStateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoStateTransitionsToXincoState.class));
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

    public XincoStateTransitionsToXincoState findXincoStateTransitionsToXincoState(XincoStateTransitionsToXincoStatePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoStateTransitionsToXincoState.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoStateTransitionsToXincoStateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoStateTransitionsToXincoState> rt = cq.from(XincoStateTransitionsToXincoState.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
