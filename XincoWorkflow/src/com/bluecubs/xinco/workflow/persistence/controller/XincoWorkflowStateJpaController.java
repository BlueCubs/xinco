/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowStatePK;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;
import com.bluecubs.xinco.workflow.persistence.XincoStateType;
import com.bluecubs.xinco.workflow.persistence.XincoAction;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.workflow.persistence.UserLink;
import com.bluecubs.xinco.workflow.persistence.XincoWorkItemHasXincoState;
import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowStateJpaController implements Serializable {

    public XincoWorkflowStateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
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
        if (xincoWorkflowState.getUserLinkList() == null) {
            xincoWorkflowState.setUserLinkList(new ArrayList<UserLink>());
        }
        if (xincoWorkflowState.getXincoWorkItemHasXincoStateList() == null) {
            xincoWorkflowState.setXincoWorkItemHasXincoStateList(new ArrayList<XincoWorkItemHasXincoState>());
        }
        if (xincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList() == null) {
            xincoWorkflowState.setSourceXincoStateTransitionsToXincoStateList(new ArrayList<XincoStateTransitionsToXincoState>());
        }
        if (xincoWorkflowState.getDestXincoStateTransitionsToXincoStateList() == null) {
            xincoWorkflowState.setDestXincoStateTransitionsToXincoStateList(new ArrayList<XincoStateTransitionsToXincoState>());
        }
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
            List<UserLink> attachedUserLinkList = new ArrayList<UserLink>();
            for (UserLink userLinkListUserLinkToAttach : xincoWorkflowState.getUserLinkList()) {
                userLinkListUserLinkToAttach = em.getReference(userLinkListUserLinkToAttach.getClass(), userLinkListUserLinkToAttach.getId());
                attachedUserLinkList.add(userLinkListUserLinkToAttach);
            }
            xincoWorkflowState.setUserLinkList(attachedUserLinkList);
            List<XincoWorkItemHasXincoState> attachedXincoWorkItemHasXincoStateList = new ArrayList<XincoWorkItemHasXincoState>();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach : xincoWorkflowState.getXincoWorkItemHasXincoStateList()) {
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach = em.getReference(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach.getClass(), xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach.getXincoWorkItemHasXincoStatePK());
                attachedXincoWorkItemHasXincoStateList.add(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach);
            }
            xincoWorkflowState.setXincoWorkItemHasXincoStateList(attachedXincoWorkItemHasXincoStateList);
            List<XincoStateTransitionsToXincoState> attachedSourceXincoStateTransitionsToXincoStateList = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach : xincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList()) {
                sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach = em.getReference(sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getClass(), sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedSourceXincoStateTransitionsToXincoStateList.add(sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach);
            }
            xincoWorkflowState.setSourceXincoStateTransitionsToXincoStateList(attachedSourceXincoStateTransitionsToXincoStateList);
            List<XincoStateTransitionsToXincoState> attachedDestXincoStateTransitionsToXincoStateList = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach : xincoWorkflowState.getDestXincoStateTransitionsToXincoStateList()) {
                destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach = em.getReference(destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getClass(), destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedDestXincoStateTransitionsToXincoStateList.add(destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach);
            }
            xincoWorkflowState.setDestXincoStateTransitionsToXincoStateList(attachedDestXincoStateTransitionsToXincoStateList);
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
            for (UserLink userLinkListUserLink : xincoWorkflowState.getUserLinkList()) {
                userLinkListUserLink.getXincoWorkflowStateList().add(xincoWorkflowState);
                userLinkListUserLink = em.merge(userLinkListUserLink);
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
            for (XincoStateTransitionsToXincoState sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : xincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList()) {
                XincoWorkflowState oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getSourceXincoWorkflowState();
                sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.setSourceXincoWorkflowState(xincoWorkflowState);
                sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                if (oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState != null) {
                    oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getSourceXincoStateTransitionsToXincoStateList().remove(sourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                    oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                }
            }
            for (XincoStateTransitionsToXincoState destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : xincoWorkflowState.getDestXincoStateTransitionsToXincoStateList()) {
                XincoWorkflowState oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getDestXincoWorkflowState();
                destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.setDestXincoWorkflowState(xincoWorkflowState);
                destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                if (oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState != null) {
                    oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getDestXincoStateTransitionsToXincoStateList().remove(destXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
                    oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
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
            List<UserLink> userLinkListOld = persistentXincoWorkflowState.getUserLinkList();
            List<UserLink> userLinkListNew = xincoWorkflowState.getUserLinkList();
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListOld = persistentXincoWorkflowState.getXincoWorkItemHasXincoStateList();
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListNew = xincoWorkflowState.getXincoWorkItemHasXincoStateList();
            List<XincoStateTransitionsToXincoState> sourceXincoStateTransitionsToXincoStateListOld = persistentXincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList();
            List<XincoStateTransitionsToXincoState> sourceXincoStateTransitionsToXincoStateListNew = xincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList();
            List<XincoStateTransitionsToXincoState> destXincoStateTransitionsToXincoStateListOld = persistentXincoWorkflowState.getDestXincoStateTransitionsToXincoStateList();
            List<XincoStateTransitionsToXincoState> destXincoStateTransitionsToXincoStateListNew = xincoWorkflowState.getDestXincoStateTransitionsToXincoStateList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListOld) {
                if (!xincoWorkItemHasXincoStateListNew.contains(xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkItemHasXincoState " + xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState + " since its xincoWorkflowState field is not nullable.");
                }
            }
            for (XincoStateTransitionsToXincoState sourceXincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState : sourceXincoStateTransitionsToXincoStateListOld) {
                if (!sourceXincoStateTransitionsToXincoStateListNew.contains(sourceXincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoStateTransitionsToXincoState " + sourceXincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState + " since its sourceXincoWorkflowState field is not nullable.");
                }
            }
            for (XincoStateTransitionsToXincoState destXincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState : destXincoStateTransitionsToXincoStateListOld) {
                if (!destXincoStateTransitionsToXincoStateListNew.contains(destXincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoStateTransitionsToXincoState " + destXincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState + " since its destXincoWorkflowState field is not nullable.");
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
            List<UserLink> attachedUserLinkListNew = new ArrayList<UserLink>();
            for (UserLink userLinkListNewUserLinkToAttach : userLinkListNew) {
                userLinkListNewUserLinkToAttach = em.getReference(userLinkListNewUserLinkToAttach.getClass(), userLinkListNewUserLinkToAttach.getId());
                attachedUserLinkListNew.add(userLinkListNewUserLinkToAttach);
            }
            userLinkListNew = attachedUserLinkListNew;
            xincoWorkflowState.setUserLinkList(userLinkListNew);
            List<XincoWorkItemHasXincoState> attachedXincoWorkItemHasXincoStateListNew = new ArrayList<XincoWorkItemHasXincoState>();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach : xincoWorkItemHasXincoStateListNew) {
                xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach = em.getReference(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach.getClass(), xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach.getXincoWorkItemHasXincoStatePK());
                attachedXincoWorkItemHasXincoStateListNew.add(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach);
            }
            xincoWorkItemHasXincoStateListNew = attachedXincoWorkItemHasXincoStateListNew;
            xincoWorkflowState.setXincoWorkItemHasXincoStateList(xincoWorkItemHasXincoStateListNew);
            List<XincoStateTransitionsToXincoState> attachedSourceXincoStateTransitionsToXincoStateListNew = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach : sourceXincoStateTransitionsToXincoStateListNew) {
                sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach = em.getReference(sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getClass(), sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedSourceXincoStateTransitionsToXincoStateListNew.add(sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach);
            }
            sourceXincoStateTransitionsToXincoStateListNew = attachedSourceXincoStateTransitionsToXincoStateListNew;
            xincoWorkflowState.setSourceXincoStateTransitionsToXincoStateList(sourceXincoStateTransitionsToXincoStateListNew);
            List<XincoStateTransitionsToXincoState> attachedDestXincoStateTransitionsToXincoStateListNew = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach : destXincoStateTransitionsToXincoStateListNew) {
                destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach = em.getReference(destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getClass(), destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedDestXincoStateTransitionsToXincoStateListNew.add(destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach);
            }
            destXincoStateTransitionsToXincoStateListNew = attachedDestXincoStateTransitionsToXincoStateListNew;
            xincoWorkflowState.setDestXincoStateTransitionsToXincoStateList(destXincoStateTransitionsToXincoStateListNew);
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
            for (UserLink userLinkListOldUserLink : userLinkListOld) {
                if (!userLinkListNew.contains(userLinkListOldUserLink)) {
                    userLinkListOldUserLink.getXincoWorkflowStateList().remove(xincoWorkflowState);
                    userLinkListOldUserLink = em.merge(userLinkListOldUserLink);
                }
            }
            for (UserLink userLinkListNewUserLink : userLinkListNew) {
                if (!userLinkListOld.contains(userLinkListNewUserLink)) {
                    userLinkListNewUserLink.getXincoWorkflowStateList().add(xincoWorkflowState);
                    userLinkListNewUserLink = em.merge(userLinkListNewUserLink);
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
            for (XincoStateTransitionsToXincoState sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState : sourceXincoStateTransitionsToXincoStateListNew) {
                if (!sourceXincoStateTransitionsToXincoStateListOld.contains(sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState)) {
                    XincoWorkflowState oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getSourceXincoWorkflowState();
                    sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.setSourceXincoWorkflowState(xincoWorkflowState);
                    sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                    if (oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState != null && !oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.equals(xincoWorkflowState)) {
                        oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getSourceXincoStateTransitionsToXincoStateList().remove(sourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                        oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(oldSourceXincoWorkflowStateOfSourceXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                    }
                }
            }
            for (XincoStateTransitionsToXincoState destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState : destXincoStateTransitionsToXincoStateListNew) {
                if (!destXincoStateTransitionsToXincoStateListOld.contains(destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState)) {
                    XincoWorkflowState oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getDestXincoWorkflowState();
                    destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.setDestXincoWorkflowState(xincoWorkflowState);
                    destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                    if (oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState != null && !oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.equals(xincoWorkflowState)) {
                        oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getDestXincoStateTransitionsToXincoStateList().remove(destXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                        oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(oldDestXincoWorkflowStateOfDestXincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
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
            List<XincoStateTransitionsToXincoState> sourceXincoStateTransitionsToXincoStateListOrphanCheck = xincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList();
            for (XincoStateTransitionsToXincoState sourceXincoStateTransitionsToXincoStateListOrphanCheckXincoStateTransitionsToXincoState : sourceXincoStateTransitionsToXincoStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkflowState (" + xincoWorkflowState + ") cannot be destroyed since the XincoStateTransitionsToXincoState " + sourceXincoStateTransitionsToXincoStateListOrphanCheckXincoStateTransitionsToXincoState + " in its sourceXincoStateTransitionsToXincoStateList field has a non-nullable sourceXincoWorkflowState field.");
            }
            List<XincoStateTransitionsToXincoState> destXincoStateTransitionsToXincoStateListOrphanCheck = xincoWorkflowState.getDestXincoStateTransitionsToXincoStateList();
            for (XincoStateTransitionsToXincoState destXincoStateTransitionsToXincoStateListOrphanCheckXincoStateTransitionsToXincoState : destXincoStateTransitionsToXincoStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkflowState (" + xincoWorkflowState + ") cannot be destroyed since the XincoStateTransitionsToXincoState " + destXincoStateTransitionsToXincoStateListOrphanCheckXincoStateTransitionsToXincoState + " in its destXincoStateTransitionsToXincoStateList field has a non-nullable destXincoWorkflowState field.");
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
            List<UserLink> userLinkList = xincoWorkflowState.getUserLinkList();
            for (UserLink userLinkListUserLink : userLinkList) {
                userLinkListUserLink.getXincoWorkflowStateList().remove(xincoWorkflowState);
                userLinkListUserLink = em.merge(userLinkListUserLink);
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
