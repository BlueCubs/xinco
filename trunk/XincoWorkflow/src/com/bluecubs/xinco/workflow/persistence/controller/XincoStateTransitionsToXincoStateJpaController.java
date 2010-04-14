/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState;
import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoStatePK;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.UserLink;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.workflow.persistence.XincoAction;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoStateTransitionsToXincoStateJpaController {

    public XincoStateTransitionsToXincoStateJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoWorkflowPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoStateTransitionsToXincoState xincoStateTransitionsToXincoState) throws PreexistingEntityException, Exception {
        if (xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK() == null) {
            xincoStateTransitionsToXincoState.setXincoStateTransitionsToXincoStatePK(new XincoStateTransitionsToXincoStatePK());
        }
        if (xincoStateTransitionsToXincoState.getUserLinkList() == null) {
            xincoStateTransitionsToXincoState.setUserLinkList(new ArrayList<UserLink>());
        }
        if (xincoStateTransitionsToXincoState.getXincoActionList() == null) {
            xincoStateTransitionsToXincoState.setXincoActionList(new ArrayList<XincoAction>());
        }
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setDestXincoStateXincoWorkflowId1(xincoStateTransitionsToXincoState.getDestXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setSourceXincoStateXincoWorkflowVersion(xincoStateTransitionsToXincoState.getSourceXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setSourceXincoStateXincoWorkflowId(xincoStateTransitionsToXincoState.getSourceXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setDestXincoStateXincoWorkflowVersion1(xincoStateTransitionsToXincoState.getDestXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflowState sourceXincoWorkflowState = xincoStateTransitionsToXincoState.getSourceXincoWorkflowState();
            if (sourceXincoWorkflowState != null) {
                sourceXincoWorkflowState = em.getReference(sourceXincoWorkflowState.getClass(), sourceXincoWorkflowState.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setSourceXincoWorkflowState(sourceXincoWorkflowState);
            }
            XincoWorkflowState destXincoWorkflowState = xincoStateTransitionsToXincoState.getDestXincoWorkflowState();
            if (destXincoWorkflowState != null) {
                destXincoWorkflowState = em.getReference(destXincoWorkflowState.getClass(), destXincoWorkflowState.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setDestXincoWorkflowState(destXincoWorkflowState);
            }
            List<UserLink> attachedUserLinkList = new ArrayList<UserLink>();
            for (UserLink userLinkListUserLinkToAttach : xincoStateTransitionsToXincoState.getUserLinkList()) {
                userLinkListUserLinkToAttach = em.getReference(userLinkListUserLinkToAttach.getClass(), userLinkListUserLinkToAttach.getId());
                attachedUserLinkList.add(userLinkListUserLinkToAttach);
            }
            xincoStateTransitionsToXincoState.setUserLinkList(attachedUserLinkList);
            List<XincoAction> attachedXincoActionList = new ArrayList<XincoAction>();
            for (XincoAction xincoActionListXincoActionToAttach : xincoStateTransitionsToXincoState.getXincoActionList()) {
                xincoActionListXincoActionToAttach = em.getReference(xincoActionListXincoActionToAttach.getClass(), xincoActionListXincoActionToAttach.getId());
                attachedXincoActionList.add(xincoActionListXincoActionToAttach);
            }
            xincoStateTransitionsToXincoState.setXincoActionList(attachedXincoActionList);
            em.persist(xincoStateTransitionsToXincoState);
            if (sourceXincoWorkflowState != null) {
                sourceXincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                sourceXincoWorkflowState = em.merge(sourceXincoWorkflowState);
            }
            if (destXincoWorkflowState != null) {
                destXincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                destXincoWorkflowState = em.merge(destXincoWorkflowState);
            }
            for (UserLink userLinkListUserLink : xincoStateTransitionsToXincoState.getUserLinkList()) {
                userLinkListUserLink.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                userLinkListUserLink = em.merge(userLinkListUserLink);
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
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setDestXincoStateXincoWorkflowId1(xincoStateTransitionsToXincoState.getDestXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setSourceXincoStateXincoWorkflowVersion(xincoStateTransitionsToXincoState.getSourceXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setSourceXincoStateXincoWorkflowId(xincoStateTransitionsToXincoState.getSourceXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK().setDestXincoStateXincoWorkflowVersion1(xincoStateTransitionsToXincoState.getDestXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoStateTransitionsToXincoState persistentXincoStateTransitionsToXincoState = em.find(XincoStateTransitionsToXincoState.class, xincoStateTransitionsToXincoState.getXincoStateTransitionsToXincoStatePK());
            XincoWorkflowState sourceXincoWorkflowStateOld = persistentXincoStateTransitionsToXincoState.getSourceXincoWorkflowState();
            XincoWorkflowState sourceXincoWorkflowStateNew = xincoStateTransitionsToXincoState.getSourceXincoWorkflowState();
            XincoWorkflowState destXincoWorkflowStateOld = persistentXincoStateTransitionsToXincoState.getDestXincoWorkflowState();
            XincoWorkflowState destXincoWorkflowStateNew = xincoStateTransitionsToXincoState.getDestXincoWorkflowState();
            List<UserLink> userLinkListOld = persistentXincoStateTransitionsToXincoState.getUserLinkList();
            List<UserLink> userLinkListNew = xincoStateTransitionsToXincoState.getUserLinkList();
            List<XincoAction> xincoActionListOld = persistentXincoStateTransitionsToXincoState.getXincoActionList();
            List<XincoAction> xincoActionListNew = xincoStateTransitionsToXincoState.getXincoActionList();
            if (sourceXincoWorkflowStateNew != null) {
                sourceXincoWorkflowStateNew = em.getReference(sourceXincoWorkflowStateNew.getClass(), sourceXincoWorkflowStateNew.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setSourceXincoWorkflowState(sourceXincoWorkflowStateNew);
            }
            if (destXincoWorkflowStateNew != null) {
                destXincoWorkflowStateNew = em.getReference(destXincoWorkflowStateNew.getClass(), destXincoWorkflowStateNew.getXincoWorkflowStatePK());
                xincoStateTransitionsToXincoState.setDestXincoWorkflowState(destXincoWorkflowStateNew);
            }
            List<UserLink> attachedUserLinkListNew = new ArrayList<UserLink>();
            for (UserLink userLinkListNewUserLinkToAttach : userLinkListNew) {
                userLinkListNewUserLinkToAttach = em.getReference(userLinkListNewUserLinkToAttach.getClass(), userLinkListNewUserLinkToAttach.getId());
                attachedUserLinkListNew.add(userLinkListNewUserLinkToAttach);
            }
            userLinkListNew = attachedUserLinkListNew;
            xincoStateTransitionsToXincoState.setUserLinkList(userLinkListNew);
            List<XincoAction> attachedXincoActionListNew = new ArrayList<XincoAction>();
            for (XincoAction xincoActionListNewXincoActionToAttach : xincoActionListNew) {
                xincoActionListNewXincoActionToAttach = em.getReference(xincoActionListNewXincoActionToAttach.getClass(), xincoActionListNewXincoActionToAttach.getId());
                attachedXincoActionListNew.add(xincoActionListNewXincoActionToAttach);
            }
            xincoActionListNew = attachedXincoActionListNew;
            xincoStateTransitionsToXincoState.setXincoActionList(xincoActionListNew);
            xincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoState);
            if (sourceXincoWorkflowStateOld != null && !sourceXincoWorkflowStateOld.equals(sourceXincoWorkflowStateNew)) {
                sourceXincoWorkflowStateOld.getSourceXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                sourceXincoWorkflowStateOld = em.merge(sourceXincoWorkflowStateOld);
            }
            if (sourceXincoWorkflowStateNew != null && !sourceXincoWorkflowStateNew.equals(sourceXincoWorkflowStateOld)) {
                sourceXincoWorkflowStateNew.getSourceXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                sourceXincoWorkflowStateNew = em.merge(sourceXincoWorkflowStateNew);
            }
            if (destXincoWorkflowStateOld != null && !destXincoWorkflowStateOld.equals(destXincoWorkflowStateNew)) {
                destXincoWorkflowStateOld.getSourceXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                destXincoWorkflowStateOld = em.merge(destXincoWorkflowStateOld);
            }
            if (destXincoWorkflowStateNew != null && !destXincoWorkflowStateNew.equals(destXincoWorkflowStateOld)) {
                destXincoWorkflowStateNew.getSourceXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                destXincoWorkflowStateNew = em.merge(destXincoWorkflowStateNew);
            }
            for (UserLink userLinkListOldUserLink : userLinkListOld) {
                if (!userLinkListNew.contains(userLinkListOldUserLink)) {
                    userLinkListOldUserLink.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                    userLinkListOldUserLink = em.merge(userLinkListOldUserLink);
                }
            }
            for (UserLink userLinkListNewUserLink : userLinkListNew) {
                if (!userLinkListOld.contains(userLinkListNewUserLink)) {
                    userLinkListNewUserLink.getXincoStateTransitionsToXincoStateList().add(xincoStateTransitionsToXincoState);
                    userLinkListNewUserLink = em.merge(userLinkListNewUserLink);
                }
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
            XincoWorkflowState sourceXincoWorkflowState = xincoStateTransitionsToXincoState.getSourceXincoWorkflowState();
            if (sourceXincoWorkflowState != null) {
                sourceXincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                sourceXincoWorkflowState = em.merge(sourceXincoWorkflowState);
            }
            XincoWorkflowState destXincoWorkflowState = xincoStateTransitionsToXincoState.getDestXincoWorkflowState();
            if (destXincoWorkflowState != null) {
                destXincoWorkflowState.getSourceXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                destXincoWorkflowState = em.merge(destXincoWorkflowState);
            }
            List<UserLink> userLinkList = xincoStateTransitionsToXincoState.getUserLinkList();
            for (UserLink userLinkListUserLink : userLinkList) {
                userLinkListUserLink.getXincoStateTransitionsToXincoStateList().remove(xincoStateTransitionsToXincoState);
                userLinkListUserLink = em.merge(userLinkListUserLink);
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
