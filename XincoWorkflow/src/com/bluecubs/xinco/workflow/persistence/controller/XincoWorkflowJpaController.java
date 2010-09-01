/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowPK;
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
import com.bluecubs.xinco.workflow.persistence.UserLink;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowJpaController implements Serializable {

    public XincoWorkflowJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoWorkflow xincoWorkflow) throws PreexistingEntityException, Exception {
        if (xincoWorkflow.getXincoWorkflowPK() == null) {
            xincoWorkflow.setXincoWorkflowPK(new XincoWorkflowPK());
        }
        if (xincoWorkflow.getXincoWorkflowStateList() == null) {
            xincoWorkflow.setXincoWorkflowStateList(new ArrayList<XincoWorkflowState>());
        }
        xincoWorkflow.getXincoWorkflowPK().setUserLinkId(xincoWorkflow.getUserLink().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLink userLink = xincoWorkflow.getUserLink();
            if (userLink != null) {
                userLink = em.getReference(userLink.getClass(), userLink.getId());
                xincoWorkflow.setUserLink(userLink);
            }
            List<XincoWorkflowState> attachedXincoWorkflowStateList = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowStateToAttach : xincoWorkflow.getXincoWorkflowStateList()) {
                xincoWorkflowStateListXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateList.add(xincoWorkflowStateListXincoWorkflowStateToAttach);
            }
            xincoWorkflow.setXincoWorkflowStateList(attachedXincoWorkflowStateList);
            em.persist(xincoWorkflow);
            if (userLink != null) {
                userLink.getXincoWorkflowList().add(xincoWorkflow);
                userLink = em.merge(userLink);
            }
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowState : xincoWorkflow.getXincoWorkflowStateList()) {
                XincoWorkflow oldXincoWorkflowOfXincoWorkflowStateListXincoWorkflowState = xincoWorkflowStateListXincoWorkflowState.getXincoWorkflow();
                xincoWorkflowStateListXincoWorkflowState.setXincoWorkflow(xincoWorkflow);
                xincoWorkflowStateListXincoWorkflowState = em.merge(xincoWorkflowStateListXincoWorkflowState);
                if (oldXincoWorkflowOfXincoWorkflowStateListXincoWorkflowState != null) {
                    oldXincoWorkflowOfXincoWorkflowStateListXincoWorkflowState.getXincoWorkflowStateList().remove(xincoWorkflowStateListXincoWorkflowState);
                    oldXincoWorkflowOfXincoWorkflowStateListXincoWorkflowState = em.merge(oldXincoWorkflowOfXincoWorkflowStateListXincoWorkflowState);
                }
            }
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

    public void edit(XincoWorkflow xincoWorkflow) throws IllegalOrphanException, NonexistentEntityException, Exception {
        xincoWorkflow.getXincoWorkflowPK().setUserLinkId(xincoWorkflow.getUserLink().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflow persistentXincoWorkflow = em.find(XincoWorkflow.class, xincoWorkflow.getXincoWorkflowPK());
            UserLink userLinkOld = persistentXincoWorkflow.getUserLink();
            UserLink userLinkNew = xincoWorkflow.getUserLink();
            List<XincoWorkflowState> xincoWorkflowStateListOld = persistentXincoWorkflow.getXincoWorkflowStateList();
            List<XincoWorkflowState> xincoWorkflowStateListNew = xincoWorkflow.getXincoWorkflowStateList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkflowState xincoWorkflowStateListOldXincoWorkflowState : xincoWorkflowStateListOld) {
                if (!xincoWorkflowStateListNew.contains(xincoWorkflowStateListOldXincoWorkflowState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkflowState " + xincoWorkflowStateListOldXincoWorkflowState + " since its xincoWorkflow field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userLinkNew != null) {
                userLinkNew = em.getReference(userLinkNew.getClass(), userLinkNew.getId());
                xincoWorkflow.setUserLink(userLinkNew);
            }
            List<XincoWorkflowState> attachedXincoWorkflowStateListNew = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowStateToAttach : xincoWorkflowStateListNew) {
                xincoWorkflowStateListNewXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListNewXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListNewXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateListNew.add(xincoWorkflowStateListNewXincoWorkflowStateToAttach);
            }
            xincoWorkflowStateListNew = attachedXincoWorkflowStateListNew;
            xincoWorkflow.setXincoWorkflowStateList(xincoWorkflowStateListNew);
            xincoWorkflow = em.merge(xincoWorkflow);
            if (userLinkOld != null && !userLinkOld.equals(userLinkNew)) {
                userLinkOld.getXincoWorkflowList().remove(xincoWorkflow);
                userLinkOld = em.merge(userLinkOld);
            }
            if (userLinkNew != null && !userLinkNew.equals(userLinkOld)) {
                userLinkNew.getXincoWorkflowList().add(xincoWorkflow);
                userLinkNew = em.merge(userLinkNew);
            }
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowState : xincoWorkflowStateListNew) {
                if (!xincoWorkflowStateListOld.contains(xincoWorkflowStateListNewXincoWorkflowState)) {
                    XincoWorkflow oldXincoWorkflowOfXincoWorkflowStateListNewXincoWorkflowState = xincoWorkflowStateListNewXincoWorkflowState.getXincoWorkflow();
                    xincoWorkflowStateListNewXincoWorkflowState.setXincoWorkflow(xincoWorkflow);
                    xincoWorkflowStateListNewXincoWorkflowState = em.merge(xincoWorkflowStateListNewXincoWorkflowState);
                    if (oldXincoWorkflowOfXincoWorkflowStateListNewXincoWorkflowState != null && !oldXincoWorkflowOfXincoWorkflowStateListNewXincoWorkflowState.equals(xincoWorkflow)) {
                        oldXincoWorkflowOfXincoWorkflowStateListNewXincoWorkflowState.getXincoWorkflowStateList().remove(xincoWorkflowStateListNewXincoWorkflowState);
                        oldXincoWorkflowOfXincoWorkflowStateListNewXincoWorkflowState = em.merge(oldXincoWorkflowOfXincoWorkflowStateListNewXincoWorkflowState);
                    }
                }
            }
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

    public void destroy(XincoWorkflowPK id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<XincoWorkflowState> xincoWorkflowStateListOrphanCheck = xincoWorkflow.getXincoWorkflowStateList();
            for (XincoWorkflowState xincoWorkflowStateListOrphanCheckXincoWorkflowState : xincoWorkflowStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkflow (" + xincoWorkflow + ") cannot be destroyed since the XincoWorkflowState " + xincoWorkflowStateListOrphanCheckXincoWorkflowState + " in its xincoWorkflowStateList field has a non-nullable xincoWorkflow field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserLink userLink = xincoWorkflow.getUserLink();
            if (userLink != null) {
                userLink.getXincoWorkflowList().remove(xincoWorkflow);
                userLink = em.merge(userLink);
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
