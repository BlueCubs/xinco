/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db.controller;

import com.bluecubs.xinco.workflow.db.XincoWorkflowState;
import com.bluecubs.xinco.workflow.db.XincoWorkflowStatePK;
import com.bluecubs.xinco.workflow.db.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.db.XincoWorkflow;
import com.bluecubs.xinco.workflow.db.XincoStateType;
import com.bluecubs.xinco.workflow.db.XincoAction;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.workflow.db.XincoWorkItemHasXincoState;
import com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoState;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowStateJpaController {

    public XincoWorkflowStateJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoWorkflowState xincoWorkflowState) throws PreexistingEntityException, Exception {
        if (xincoWorkflowState.getXincoWorkflowStatePK() == null) {
            xincoWorkflowState.setXincoWorkflowStatePK(new XincoWorkflowStatePK());
        }
        if (xincoWorkflowState.getXincoActionList() == null) {
            xincoWorkflowState.setXincoActionList(new ArrayList<XincoAction>());
        }
        if (xincoWorkflowState.getXincoWorkItemHasXincoStateList() == null) {
            xincoWorkflowState.setXincoWorkItemHasXincoStateList(new ArrayList<XincoWorkItemHasXincoState>());
        }
        if (xincoWorkflowState.getXincoStateTransitionsToXincoStateList() == null) {
            xincoWorkflowState.setXincoStateTransitionsToXincoStateList(new ArrayList<XincoStateTransitionsToXincoState>());
        }
        xincoWorkflowState.getXincoWorkflowStatePK().setXincoWorkflowId(xincoWorkflowState.getXincoWorkflow().getXincoWorkflowPK().getId());
        xincoWorkflowState.getXincoWorkflowStatePK().setXincoWorkflowVersion(xincoWorkflowState.getXincoWorkflow().getXincoWorkflowPK().getVersion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflow xincoWorkflow = xincoWorkflowState.getXincoWorkflow();
            if (xincoWorkflow != null) {
                xincoWorkflow = em.getReference(xincoWorkflow.getClass(), xincoWorkflow.getXincoWorkflowPK());
                xincoWorkflowState.setXincoWorkflow(xincoWorkflow);
            }
            XincoStateType xincoStateTypeId = xincoWorkflowState.getXincoStateTypeId();
            if (xincoStateTypeId != null) {
                xincoStateTypeId = em.getReference(xincoStateTypeId.getClass(), xincoStateTypeId.getId());
                xincoWorkflowState.setXincoStateTypeId(xincoStateTypeId);
            }
            List<XincoAction> attachedXincoActionList = new ArrayList<XincoAction>();
            for (XincoAction xincoActionListXincoActionToAttach : xincoWorkflowState.getXincoActionList()) {
                xincoActionListXincoActionToAttach = em.getReference(xincoActionListXincoActionToAttach.getClass(), xincoActionListXincoActionToAttach.getId());
                attachedXincoActionList.add(xincoActionListXincoActionToAttach);
            }
            xincoWorkflowState.setXincoActionList(attachedXincoActionList);
            List<XincoWorkItemHasXincoState> attachedXincoWorkItemHasXincoStateList = new ArrayList<XincoWorkItemHasXincoState>();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach : xincoWorkflowState.getXincoWorkItemHasXincoStateList()) {
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach = em.getReference(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach.getClass(), xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach.getXincoWorkItemHasXincoStatePK());
                attachedXincoWorkItemHasXincoStateList.add(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach);
            }
            xincoWorkflowState.setXincoWorkItemHasXincoStateList(attachedXincoWorkItemHasXincoStateList);
            List<XincoStateTransitionsToXincoState> attachedXincoStateTransitionsToXincoStateList = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach : xincoWorkflowState.getXincoStateTransitionsToXincoStateList()) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach = em.getReference(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getClass(), xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedXincoStateTransitionsToXincoStateList.add(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach);
            }
            xincoWorkflowState.setXincoStateTransitionsToXincoStateList(attachedXincoStateTransitionsToXincoStateList);
            em.persist(xincoWorkflowState);
            if (xincoWorkflow != null) {
                xincoWorkflow.getXincoWorkflowStateList().add(xincoWorkflowState);
                xincoWorkflow = em.merge(xincoWorkflow);
            }
            if (xincoStateTypeId != null) {
                xincoStateTypeId.getXincoWorkflowStateList().add(xincoWorkflowState);
                xincoStateTypeId = em.merge(xincoStateTypeId);
            }
            for (XincoAction xincoActionListXincoAction : xincoWorkflowState.getXincoActionList()) {
                xincoActionListXincoAction.getXincoWorkflowStateList().add(xincoWorkflowState);
                xincoActionListXincoAction = em.merge(xincoActionListXincoAction);
            }
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState : xincoWorkflowState.getXincoWorkItemHasXincoStateList()) {
                XincoWorkflowState oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState = xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState.getXincoWorkflowState();
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState.setXincoWorkflowState(xincoWorkflowState);
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState);
                if (oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState != null) {
                    oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState);
                    oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState = em.merge(oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState);
                }
            }
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : xincoWorkflowState.getXincoStateTransitionsToXincoStateList()) {
                XincoWorkflowState oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getXincoWorkflowState();
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.setXincoWorkflowState(xincoWorkflowState);
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                if (oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState != null) {
                    oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                    oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoWorkflowState(xincoWorkflowState.getXincoWorkflowStatePK()) != null) {
                throw new PreexistingEntityException("XincoWorkflowState " + xincoWorkflowState + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoWorkflowState xincoWorkflowState) throws IllegalOrphanException, NonexistentEntityException, Exception {
        xincoWorkflowState.getXincoWorkflowStatePK().setXincoWorkflowId(xincoWorkflowState.getXincoWorkflow().getXincoWorkflowPK().getId());
        xincoWorkflowState.getXincoWorkflowStatePK().setXincoWorkflowVersion(xincoWorkflowState.getXincoWorkflow().getXincoWorkflowPK().getVersion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflowState persistentXincoWorkflowState = em.find(XincoWorkflowState.class, xincoWorkflowState.getXincoWorkflowStatePK());
            XincoWorkflow xincoWorkflowOld = persistentXincoWorkflowState.getXincoWorkflow();
            XincoWorkflow xincoWorkflowNew = xincoWorkflowState.getXincoWorkflow();
            XincoStateType xincoStateTypeIdOld = persistentXincoWorkflowState.getXincoStateTypeId();
            XincoStateType xincoStateTypeIdNew = xincoWorkflowState.getXincoStateTypeId();
            List<XincoAction> xincoActionListOld = persistentXincoWorkflowState.getXincoActionList();
            List<XincoAction> xincoActionListNew = xincoWorkflowState.getXincoActionList();
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListOld = persistentXincoWorkflowState.getXincoWorkItemHasXincoStateList();
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListNew = xincoWorkflowState.getXincoWorkItemHasXincoStateList();
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListOld = persistentXincoWorkflowState.getXincoStateTransitionsToXincoStateList();
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListNew = xincoWorkflowState.getXincoStateTransitionsToXincoStateList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListOld) {
                if (!xincoWorkItemHasXincoStateListNew.contains(xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkItemHasXincoState " + xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState + " since its xincoWorkflowState field is not nullable.");
                }
            }
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListOld) {
                if (!xincoStateTransitionsToXincoStateListNew.contains(xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoStateTransitionsToXincoState " + xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState + " since its xincoWorkflowState field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoWorkflowNew != null) {
                xincoWorkflowNew = em.getReference(xincoWorkflowNew.getClass(), xincoWorkflowNew.getXincoWorkflowPK());
                xincoWorkflowState.setXincoWorkflow(xincoWorkflowNew);
            }
            if (xincoStateTypeIdNew != null) {
                xincoStateTypeIdNew = em.getReference(xincoStateTypeIdNew.getClass(), xincoStateTypeIdNew.getId());
                xincoWorkflowState.setXincoStateTypeId(xincoStateTypeIdNew);
            }
            List<XincoAction> attachedXincoActionListNew = new ArrayList<XincoAction>();
            for (XincoAction xincoActionListNewXincoActionToAttach : xincoActionListNew) {
                xincoActionListNewXincoActionToAttach = em.getReference(xincoActionListNewXincoActionToAttach.getClass(), xincoActionListNewXincoActionToAttach.getId());
                attachedXincoActionListNew.add(xincoActionListNewXincoActionToAttach);
            }
            xincoActionListNew = attachedXincoActionListNew;
            xincoWorkflowState.setXincoActionList(xincoActionListNew);
            List<XincoWorkItemHasXincoState> attachedXincoWorkItemHasXincoStateListNew = new ArrayList<XincoWorkItemHasXincoState>();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach : xincoWorkItemHasXincoStateListNew) {
                xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach = em.getReference(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach.getClass(), xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach.getXincoWorkItemHasXincoStatePK());
                attachedXincoWorkItemHasXincoStateListNew.add(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach);
            }
            xincoWorkItemHasXincoStateListNew = attachedXincoWorkItemHasXincoStateListNew;
            xincoWorkflowState.setXincoWorkItemHasXincoStateList(xincoWorkItemHasXincoStateListNew);
            List<XincoStateTransitionsToXincoState> attachedXincoStateTransitionsToXincoStateListNew = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach : xincoStateTransitionsToXincoStateListNew) {
                xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach = em.getReference(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getClass(), xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedXincoStateTransitionsToXincoStateListNew.add(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach);
            }
            xincoStateTransitionsToXincoStateListNew = attachedXincoStateTransitionsToXincoStateListNew;
            xincoWorkflowState.setXincoStateTransitionsToXincoStateList(xincoStateTransitionsToXincoStateListNew);
            xincoWorkflowState = em.merge(xincoWorkflowState);
            if (xincoWorkflowOld != null && !xincoWorkflowOld.equals(xincoWorkflowNew)) {
                xincoWorkflowOld.getXincoWorkflowStateList().remove(xincoWorkflowState);
                xincoWorkflowOld = em.merge(xincoWorkflowOld);
            }
            if (xincoWorkflowNew != null && !xincoWorkflowNew.equals(xincoWorkflowOld)) {
                xincoWorkflowNew.getXincoWorkflowStateList().add(xincoWorkflowState);
                xincoWorkflowNew = em.merge(xincoWorkflowNew);
            }
            if (xincoStateTypeIdOld != null && !xincoStateTypeIdOld.equals(xincoStateTypeIdNew)) {
                xincoStateTypeIdOld.getXincoWorkflowStateList().remove(xincoWorkflowState);
                xincoStateTypeIdOld = em.merge(xincoStateTypeIdOld);
            }
            if (xincoStateTypeIdNew != null && !xincoStateTypeIdNew.equals(xincoStateTypeIdOld)) {
                xincoStateTypeIdNew.getXincoWorkflowStateList().add(xincoWorkflowState);
                xincoStateTypeIdNew = em.merge(xincoStateTypeIdNew);
            }
            for (XincoAction xincoActionListOldXincoAction : xincoActionListOld) {
                if (!xincoActionListNew.contains(xincoActionListOldXincoAction)) {
                    xincoActionListOldXincoAction.getXincoWorkflowStateList().remove(xincoWorkflowState);
                    xincoActionListOldXincoAction = em.merge(xincoActionListOldXincoAction);
                }
            }
            for (XincoAction xincoActionListNewXincoAction : xincoActionListNew) {
                if (!xincoActionListOld.contains(xincoActionListNewXincoAction)) {
                    xincoActionListNewXincoAction.getXincoWorkflowStateList().add(xincoWorkflowState);
                    xincoActionListNewXincoAction = em.merge(xincoActionListNewXincoAction);
                }
            }
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListNew) {
                if (!xincoWorkItemHasXincoStateListOld.contains(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState)) {
                    XincoWorkflowState oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState = xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.getXincoWorkflowState();
                    xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.setXincoWorkflowState(xincoWorkflowState);
                    xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState);
                    if (oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState != null && !oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.equals(xincoWorkflowState)) {
                        oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState);
                        oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState = em.merge(oldXincoWorkflowStateOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState);
                    }
                }
            }
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListNew) {
                if (!xincoStateTransitionsToXincoStateListOld.contains(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState)) {
                    XincoWorkflowState oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getXincoWorkflowState();
                    xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.setXincoWorkflowState(xincoWorkflowState);
                    xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                    if (oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState != null && !oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.equals(xincoWorkflowState)) {
                        oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                        oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(oldXincoWorkflowStateOfXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoWorkflowStatePK id = xincoWorkflowState.getXincoWorkflowStatePK();
                if (findXincoWorkflowState(id) == null) {
                    throw new NonexistentEntityException("The xincoWorkflowState with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoWorkflowStatePK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflowState xincoWorkflowState;
            try {
                xincoWorkflowState = em.getReference(XincoWorkflowState.class, id);
                xincoWorkflowState.getXincoWorkflowStatePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoWorkflowState with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListOrphanCheck = xincoWorkflowState.getXincoWorkItemHasXincoStateList();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListOrphanCheckXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkflowState (" + xincoWorkflowState + ") cannot be destroyed since the XincoWorkItemHasXincoState " + xincoWorkItemHasXincoStateListOrphanCheckXincoWorkItemHasXincoState + " in its xincoWorkItemHasXincoStateList field has a non-nullable xincoWorkflowState field.");
            }
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListOrphanCheck = xincoWorkflowState.getXincoStateTransitionsToXincoStateList();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListOrphanCheckXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkflowState (" + xincoWorkflowState + ") cannot be destroyed since the XincoStateTransitionsToXincoState " + xincoStateTransitionsToXincoStateListOrphanCheckXincoStateTransitionsToXincoState + " in its xincoStateTransitionsToXincoStateList field has a non-nullable xincoWorkflowState field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoWorkflow xincoWorkflow = xincoWorkflowState.getXincoWorkflow();
            if (xincoWorkflow != null) {
                xincoWorkflow.getXincoWorkflowStateList().remove(xincoWorkflowState);
                xincoWorkflow = em.merge(xincoWorkflow);
            }
            XincoStateType xincoStateTypeId = xincoWorkflowState.getXincoStateTypeId();
            if (xincoStateTypeId != null) {
                xincoStateTypeId.getXincoWorkflowStateList().remove(xincoWorkflowState);
                xincoStateTypeId = em.merge(xincoStateTypeId);
            }
            List<XincoAction> xincoActionList = xincoWorkflowState.getXincoActionList();
            for (XincoAction xincoActionListXincoAction : xincoActionList) {
                xincoActionListXincoAction.getXincoWorkflowStateList().remove(xincoWorkflowState);
                xincoActionListXincoAction = em.merge(xincoActionListXincoAction);
            }
            em.remove(xincoWorkflowState);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoWorkflowState> findXincoWorkflowStateEntities() {
        return findXincoWorkflowStateEntities(true, -1, -1);
    }

    public List<XincoWorkflowState> findXincoWorkflowStateEntities(int maxResults, int firstResult) {
        return findXincoWorkflowStateEntities(false, maxResults, firstResult);
    }

    private List<XincoWorkflowState> findXincoWorkflowStateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoWorkflowState.class));
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

    public XincoWorkflowState findXincoWorkflowState(XincoWorkflowStatePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoWorkflowState.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoWorkflowStateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoWorkflowState> rt = cq.from(XincoWorkflowState.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
