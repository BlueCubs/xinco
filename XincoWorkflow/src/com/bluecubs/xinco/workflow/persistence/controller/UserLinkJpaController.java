/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.UserLink;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class UserLinkJpaController implements Serializable {

    public UserLinkJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserLink userLink) {
        if (userLink.getXincoStateTransitionsToXincoStateList() == null) {
            userLink.setXincoStateTransitionsToXincoStateList(new ArrayList<XincoStateTransitionsToXincoState>());
        }
        if (userLink.getXincoWorkflowStateList() == null) {
            userLink.setXincoWorkflowStateList(new ArrayList<XincoWorkflowState>());
        }
        if (userLink.getXincoWorkflowList() == null) {
            userLink.setXincoWorkflowList(new ArrayList<XincoWorkflow>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoStateTransitionsToXincoState> attachedXincoStateTransitionsToXincoStateList = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach : userLink.getXincoStateTransitionsToXincoStateList()) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach = em.getReference(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getClass(), xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedXincoStateTransitionsToXincoStateList.add(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach);
            }
            userLink.setXincoStateTransitionsToXincoStateList(attachedXincoStateTransitionsToXincoStateList);
            List<XincoWorkflowState> attachedXincoWorkflowStateList = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowStateToAttach : userLink.getXincoWorkflowStateList()) {
                xincoWorkflowStateListXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateList.add(xincoWorkflowStateListXincoWorkflowStateToAttach);
            }
            userLink.setXincoWorkflowStateList(attachedXincoWorkflowStateList);
            List<XincoWorkflow> attachedXincoWorkflowList = new ArrayList<XincoWorkflow>();
            for (XincoWorkflow xincoWorkflowListXincoWorkflowToAttach : userLink.getXincoWorkflowList()) {
                xincoWorkflowListXincoWorkflowToAttach = em.getReference(xincoWorkflowListXincoWorkflowToAttach.getClass(), xincoWorkflowListXincoWorkflowToAttach.getXincoWorkflowPK());
                attachedXincoWorkflowList.add(xincoWorkflowListXincoWorkflowToAttach);
            }
            userLink.setXincoWorkflowList(attachedXincoWorkflowList);
            em.persist(userLink);
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : userLink.getXincoStateTransitionsToXincoStateList()) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getUserLinkList().add(userLink);
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
            }
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowState : userLink.getXincoWorkflowStateList()) {
                xincoWorkflowStateListXincoWorkflowState.getUserLinkList().add(userLink);
                xincoWorkflowStateListXincoWorkflowState = em.merge(xincoWorkflowStateListXincoWorkflowState);
            }
            for (XincoWorkflow xincoWorkflowListXincoWorkflow : userLink.getXincoWorkflowList()) {
                UserLink oldUserLinkOfXincoWorkflowListXincoWorkflow = xincoWorkflowListXincoWorkflow.getUserLink();
                xincoWorkflowListXincoWorkflow.setUserLink(userLink);
                xincoWorkflowListXincoWorkflow = em.merge(xincoWorkflowListXincoWorkflow);
                if (oldUserLinkOfXincoWorkflowListXincoWorkflow != null) {
                    oldUserLinkOfXincoWorkflowListXincoWorkflow.getXincoWorkflowList().remove(xincoWorkflowListXincoWorkflow);
                    oldUserLinkOfXincoWorkflowListXincoWorkflow = em.merge(oldUserLinkOfXincoWorkflowListXincoWorkflow);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserLink userLink) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLink persistentUserLink = em.find(UserLink.class, userLink.getId());
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListOld = persistentUserLink.getXincoStateTransitionsToXincoStateList();
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListNew = userLink.getXincoStateTransitionsToXincoStateList();
            List<XincoWorkflowState> xincoWorkflowStateListOld = persistentUserLink.getXincoWorkflowStateList();
            List<XincoWorkflowState> xincoWorkflowStateListNew = userLink.getXincoWorkflowStateList();
            List<XincoWorkflow> xincoWorkflowListOld = persistentUserLink.getXincoWorkflowList();
            List<XincoWorkflow> xincoWorkflowListNew = userLink.getXincoWorkflowList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkflow xincoWorkflowListOldXincoWorkflow : xincoWorkflowListOld) {
                if (!xincoWorkflowListNew.contains(xincoWorkflowListOldXincoWorkflow)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkflow " + xincoWorkflowListOldXincoWorkflow + " since its userLink field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoStateTransitionsToXincoState> attachedXincoStateTransitionsToXincoStateListNew = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach : xincoStateTransitionsToXincoStateListNew) {
                xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach = em.getReference(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getClass(), xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedXincoStateTransitionsToXincoStateListNew.add(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoStateToAttach);
            }
            xincoStateTransitionsToXincoStateListNew = attachedXincoStateTransitionsToXincoStateListNew;
            userLink.setXincoStateTransitionsToXincoStateList(xincoStateTransitionsToXincoStateListNew);
            List<XincoWorkflowState> attachedXincoWorkflowStateListNew = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowStateToAttach : xincoWorkflowStateListNew) {
                xincoWorkflowStateListNewXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListNewXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListNewXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateListNew.add(xincoWorkflowStateListNewXincoWorkflowStateToAttach);
            }
            xincoWorkflowStateListNew = attachedXincoWorkflowStateListNew;
            userLink.setXincoWorkflowStateList(xincoWorkflowStateListNew);
            List<XincoWorkflow> attachedXincoWorkflowListNew = new ArrayList<XincoWorkflow>();
            for (XincoWorkflow xincoWorkflowListNewXincoWorkflowToAttach : xincoWorkflowListNew) {
                xincoWorkflowListNewXincoWorkflowToAttach = em.getReference(xincoWorkflowListNewXincoWorkflowToAttach.getClass(), xincoWorkflowListNewXincoWorkflowToAttach.getXincoWorkflowPK());
                attachedXincoWorkflowListNew.add(xincoWorkflowListNewXincoWorkflowToAttach);
            }
            xincoWorkflowListNew = attachedXincoWorkflowListNew;
            userLink.setXincoWorkflowList(xincoWorkflowListNew);
            userLink = em.merge(userLink);
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListOld) {
                if (!xincoStateTransitionsToXincoStateListNew.contains(xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState)) {
                    xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState.getUserLinkList().remove(userLink);
                    xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState);
                }
            }
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListNew) {
                if (!xincoStateTransitionsToXincoStateListOld.contains(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState)) {
                    xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getUserLinkList().add(userLink);
                    xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                }
            }
            for (XincoWorkflowState xincoWorkflowStateListOldXincoWorkflowState : xincoWorkflowStateListOld) {
                if (!xincoWorkflowStateListNew.contains(xincoWorkflowStateListOldXincoWorkflowState)) {
                    xincoWorkflowStateListOldXincoWorkflowState.getUserLinkList().remove(userLink);
                    xincoWorkflowStateListOldXincoWorkflowState = em.merge(xincoWorkflowStateListOldXincoWorkflowState);
                }
            }
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowState : xincoWorkflowStateListNew) {
                if (!xincoWorkflowStateListOld.contains(xincoWorkflowStateListNewXincoWorkflowState)) {
                    xincoWorkflowStateListNewXincoWorkflowState.getUserLinkList().add(userLink);
                    xincoWorkflowStateListNewXincoWorkflowState = em.merge(xincoWorkflowStateListNewXincoWorkflowState);
                }
            }
            for (XincoWorkflow xincoWorkflowListNewXincoWorkflow : xincoWorkflowListNew) {
                if (!xincoWorkflowListOld.contains(xincoWorkflowListNewXincoWorkflow)) {
                    UserLink oldUserLinkOfXincoWorkflowListNewXincoWorkflow = xincoWorkflowListNewXincoWorkflow.getUserLink();
                    xincoWorkflowListNewXincoWorkflow.setUserLink(userLink);
                    xincoWorkflowListNewXincoWorkflow = em.merge(xincoWorkflowListNewXincoWorkflow);
                    if (oldUserLinkOfXincoWorkflowListNewXincoWorkflow != null && !oldUserLinkOfXincoWorkflowListNewXincoWorkflow.equals(userLink)) {
                        oldUserLinkOfXincoWorkflowListNewXincoWorkflow.getXincoWorkflowList().remove(xincoWorkflowListNewXincoWorkflow);
                        oldUserLinkOfXincoWorkflowListNewXincoWorkflow = em.merge(oldUserLinkOfXincoWorkflowListNewXincoWorkflow);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userLink.getId();
                if (findUserLink(id) == null) {
                    throw new NonexistentEntityException("The userLink with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLink userLink;
            try {
                userLink = em.getReference(UserLink.class, id);
                userLink.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userLink with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoWorkflow> xincoWorkflowListOrphanCheck = userLink.getXincoWorkflowList();
            for (XincoWorkflow xincoWorkflowListOrphanCheckXincoWorkflow : xincoWorkflowListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLink (" + userLink + ") cannot be destroyed since the XincoWorkflow " + xincoWorkflowListOrphanCheckXincoWorkflow + " in its xincoWorkflowList field has a non-nullable userLink field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList = userLink.getXincoStateTransitionsToXincoStateList();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateList) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getUserLinkList().remove(userLink);
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
            }
            List<XincoWorkflowState> xincoWorkflowStateList = userLink.getXincoWorkflowStateList();
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowState : xincoWorkflowStateList) {
                xincoWorkflowStateListXincoWorkflowState.getUserLinkList().remove(userLink);
                xincoWorkflowStateListXincoWorkflowState = em.merge(xincoWorkflowStateListXincoWorkflowState);
            }
            em.remove(userLink);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserLink> findUserLinkEntities() {
        return findUserLinkEntities(true, -1, -1);
    }

    public List<UserLink> findUserLinkEntities(int maxResults, int firstResult) {
        return findUserLinkEntities(false, maxResults, firstResult);
    }

    private List<UserLink> findUserLinkEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserLink.class));
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

    public UserLink findUserLink(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserLink.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserLinkCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserLink> rt = cq.from(UserLink.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
