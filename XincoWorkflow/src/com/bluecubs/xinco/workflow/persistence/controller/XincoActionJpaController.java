/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoAction;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState;
import java.util.ArrayList;
import java.util.List;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.XincoParameter;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoActionJpaController {

    public XincoActionJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoWorkflowPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoAction xincoAction) {
        if (xincoAction.getXincoStateTransitionsToXincoStateList() == null) {
            xincoAction.setXincoStateTransitionsToXincoStateList(new ArrayList<XincoStateTransitionsToXincoState>());
        }
        if (xincoAction.getXincoWorkflowStateList() == null) {
            xincoAction.setXincoWorkflowStateList(new ArrayList<XincoWorkflowState>());
        }
        if (xincoAction.getXincoParameterList() == null) {
            xincoAction.setXincoParameterList(new ArrayList<XincoParameter>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoStateTransitionsToXincoState> attachedXincoStateTransitionsToXincoStateList = new ArrayList<XincoStateTransitionsToXincoState>();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach : xincoAction.getXincoStateTransitionsToXincoStateList()) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach = em.getReference(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getClass(), xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach.getXincoStateTransitionsToXincoStatePK());
                attachedXincoStateTransitionsToXincoStateList.add(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoStateToAttach);
            }
            xincoAction.setXincoStateTransitionsToXincoStateList(attachedXincoStateTransitionsToXincoStateList);
            List<XincoWorkflowState> attachedXincoWorkflowStateList = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowStateToAttach : xincoAction.getXincoWorkflowStateList()) {
                xincoWorkflowStateListXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateList.add(xincoWorkflowStateListXincoWorkflowStateToAttach);
            }
            xincoAction.setXincoWorkflowStateList(attachedXincoWorkflowStateList);
            List<XincoParameter> attachedXincoParameterList = new ArrayList<XincoParameter>();
            for (XincoParameter xincoParameterListXincoParameterToAttach : xincoAction.getXincoParameterList()) {
                xincoParameterListXincoParameterToAttach = em.getReference(xincoParameterListXincoParameterToAttach.getClass(), xincoParameterListXincoParameterToAttach.getXincoParameterPK());
                attachedXincoParameterList.add(xincoParameterListXincoParameterToAttach);
            }
            xincoAction.setXincoParameterList(attachedXincoParameterList);
            em.persist(xincoAction);
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : xincoAction.getXincoStateTransitionsToXincoStateList()) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getXincoActionList().add(xincoAction);
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
            }
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowState : xincoAction.getXincoWorkflowStateList()) {
                xincoWorkflowStateListXincoWorkflowState.getXincoActionList().add(xincoAction);
                xincoWorkflowStateListXincoWorkflowState = em.merge(xincoWorkflowStateListXincoWorkflowState);
            }
            for (XincoParameter xincoParameterListXincoParameter : xincoAction.getXincoParameterList()) {
                XincoAction oldXincoActionOfXincoParameterListXincoParameter = xincoParameterListXincoParameter.getXincoAction();
                xincoParameterListXincoParameter.setXincoAction(xincoAction);
                xincoParameterListXincoParameter = em.merge(xincoParameterListXincoParameter);
                if (oldXincoActionOfXincoParameterListXincoParameter != null) {
                    oldXincoActionOfXincoParameterListXincoParameter.getXincoParameterList().remove(xincoParameterListXincoParameter);
                    oldXincoActionOfXincoParameterListXincoParameter = em.merge(oldXincoActionOfXincoParameterListXincoParameter);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoAction xincoAction) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoAction persistentXincoAction = em.find(XincoAction.class, xincoAction.getId());
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListOld = persistentXincoAction.getXincoStateTransitionsToXincoStateList();
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateListNew = xincoAction.getXincoStateTransitionsToXincoStateList();
            List<XincoWorkflowState> xincoWorkflowStateListOld = persistentXincoAction.getXincoWorkflowStateList();
            List<XincoWorkflowState> xincoWorkflowStateListNew = xincoAction.getXincoWorkflowStateList();
            List<XincoParameter> xincoParameterListOld = persistentXincoAction.getXincoParameterList();
            List<XincoParameter> xincoParameterListNew = xincoAction.getXincoParameterList();
            List<String> illegalOrphanMessages = null;
            for (XincoParameter xincoParameterListOldXincoParameter : xincoParameterListOld) {
                if (!xincoParameterListNew.contains(xincoParameterListOldXincoParameter)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoParameter " + xincoParameterListOldXincoParameter + " since its xincoAction field is not nullable.");
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
            xincoAction.setXincoStateTransitionsToXincoStateList(xincoStateTransitionsToXincoStateListNew);
            List<XincoWorkflowState> attachedXincoWorkflowStateListNew = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowStateToAttach : xincoWorkflowStateListNew) {
                xincoWorkflowStateListNewXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListNewXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListNewXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateListNew.add(xincoWorkflowStateListNewXincoWorkflowStateToAttach);
            }
            xincoWorkflowStateListNew = attachedXincoWorkflowStateListNew;
            xincoAction.setXincoWorkflowStateList(xincoWorkflowStateListNew);
            List<XincoParameter> attachedXincoParameterListNew = new ArrayList<XincoParameter>();
            for (XincoParameter xincoParameterListNewXincoParameterToAttach : xincoParameterListNew) {
                xincoParameterListNewXincoParameterToAttach = em.getReference(xincoParameterListNewXincoParameterToAttach.getClass(), xincoParameterListNewXincoParameterToAttach.getXincoParameterPK());
                attachedXincoParameterListNew.add(xincoParameterListNewXincoParameterToAttach);
            }
            xincoParameterListNew = attachedXincoParameterListNew;
            xincoAction.setXincoParameterList(xincoParameterListNew);
            xincoAction = em.merge(xincoAction);
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListOld) {
                if (!xincoStateTransitionsToXincoStateListNew.contains(xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState)) {
                    xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState.getXincoActionList().remove(xincoAction);
                    xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListOldXincoStateTransitionsToXincoState);
                }
            }
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateListNew) {
                if (!xincoStateTransitionsToXincoStateListOld.contains(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState)) {
                    xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState.getXincoActionList().add(xincoAction);
                    xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListNewXincoStateTransitionsToXincoState);
                }
            }
            for (XincoWorkflowState xincoWorkflowStateListOldXincoWorkflowState : xincoWorkflowStateListOld) {
                if (!xincoWorkflowStateListNew.contains(xincoWorkflowStateListOldXincoWorkflowState)) {
                    xincoWorkflowStateListOldXincoWorkflowState.getXincoActionList().remove(xincoAction);
                    xincoWorkflowStateListOldXincoWorkflowState = em.merge(xincoWorkflowStateListOldXincoWorkflowState);
                }
            }
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowState : xincoWorkflowStateListNew) {
                if (!xincoWorkflowStateListOld.contains(xincoWorkflowStateListNewXincoWorkflowState)) {
                    xincoWorkflowStateListNewXincoWorkflowState.getXincoActionList().add(xincoAction);
                    xincoWorkflowStateListNewXincoWorkflowState = em.merge(xincoWorkflowStateListNewXincoWorkflowState);
                }
            }
            for (XincoParameter xincoParameterListNewXincoParameter : xincoParameterListNew) {
                if (!xincoParameterListOld.contains(xincoParameterListNewXincoParameter)) {
                    XincoAction oldXincoActionOfXincoParameterListNewXincoParameter = xincoParameterListNewXincoParameter.getXincoAction();
                    xincoParameterListNewXincoParameter.setXincoAction(xincoAction);
                    xincoParameterListNewXincoParameter = em.merge(xincoParameterListNewXincoParameter);
                    if (oldXincoActionOfXincoParameterListNewXincoParameter != null && !oldXincoActionOfXincoParameterListNewXincoParameter.equals(xincoAction)) {
                        oldXincoActionOfXincoParameterListNewXincoParameter.getXincoParameterList().remove(xincoParameterListNewXincoParameter);
                        oldXincoActionOfXincoParameterListNewXincoParameter = em.merge(oldXincoActionOfXincoParameterListNewXincoParameter);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoAction.getId();
                if (findXincoAction(id) == null) {
                    throw new NonexistentEntityException("The xincoAction with id " + id + " no longer exists.");
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
            XincoAction xincoAction;
            try {
                xincoAction = em.getReference(XincoAction.class, id);
                xincoAction.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoAction with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoParameter> xincoParameterListOrphanCheck = xincoAction.getXincoParameterList();
            for (XincoParameter xincoParameterListOrphanCheckXincoParameter : xincoParameterListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoAction (" + xincoAction + ") cannot be destroyed since the XincoParameter " + xincoParameterListOrphanCheckXincoParameter + " in its xincoParameterList field has a non-nullable xincoAction field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList = xincoAction.getXincoStateTransitionsToXincoStateList();
            for (XincoStateTransitionsToXincoState xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState : xincoStateTransitionsToXincoStateList) {
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState.getXincoActionList().remove(xincoAction);
                xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState = em.merge(xincoStateTransitionsToXincoStateListXincoStateTransitionsToXincoState);
            }
            List<XincoWorkflowState> xincoWorkflowStateList = xincoAction.getXincoWorkflowStateList();
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowState : xincoWorkflowStateList) {
                xincoWorkflowStateListXincoWorkflowState.getXincoActionList().remove(xincoAction);
                xincoWorkflowStateListXincoWorkflowState = em.merge(xincoWorkflowStateListXincoWorkflowState);
            }
            em.remove(xincoAction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoAction> findXincoActionEntities() {
        return findXincoActionEntities(true, -1, -1);
    }

    public List<XincoAction> findXincoActionEntities(int maxResults, int firstResult) {
        return findXincoActionEntities(false, maxResults, firstResult);
    }

    private List<XincoAction> findXincoActionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoAction.class));
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

    public XincoAction findXincoAction(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoAction.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoActionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoAction> rt = cq.from(XincoAction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
