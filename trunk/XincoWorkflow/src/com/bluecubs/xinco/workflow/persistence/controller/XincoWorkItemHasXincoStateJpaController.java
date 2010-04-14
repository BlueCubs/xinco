/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoWorkItemHasXincoState;
import com.bluecubs.xinco.workflow.persistence.XincoWorkItemHasXincoStatePK;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowItem;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.XincoWorkItemParameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkItemHasXincoStateJpaController {

    public XincoWorkItemHasXincoStateJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoWorkflowPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoWorkItemHasXincoState xincoWorkItemHasXincoState) throws PreexistingEntityException, Exception {
        if (xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK() == null) {
            xincoWorkItemHasXincoState.setXincoWorkItemHasXincoStatePK(new XincoWorkItemHasXincoStatePK());
        }
        if (xincoWorkItemHasXincoState.getXincoWorkItemParameterList() == null) {
            xincoWorkItemHasXincoState.setXincoWorkItemParameterList(new ArrayList<XincoWorkItemParameter>());
        }
        xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK().setXincoStateXincoWorkflowVersion(xincoWorkItemHasXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK().setXincoWorkItemId(xincoWorkItemHasXincoState.getXincoWorkflowItem().getId());
        xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK().setXincoStateXincoWorkflowId(xincoWorkItemHasXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflowItem xincoWorkflowItem = xincoWorkItemHasXincoState.getXincoWorkflowItem();
            if (xincoWorkflowItem != null) {
                xincoWorkflowItem = em.getReference(xincoWorkflowItem.getClass(), xincoWorkflowItem.getId());
                xincoWorkItemHasXincoState.setXincoWorkflowItem(xincoWorkflowItem);
            }
            XincoWorkflowState xincoWorkflowState = xincoWorkItemHasXincoState.getXincoWorkflowState();
            if (xincoWorkflowState != null) {
                xincoWorkflowState = em.getReference(xincoWorkflowState.getClass(), xincoWorkflowState.getXincoWorkflowStatePK());
                xincoWorkItemHasXincoState.setXincoWorkflowState(xincoWorkflowState);
            }
            List<XincoWorkItemParameter> attachedXincoWorkItemParameterList = new ArrayList<XincoWorkItemParameter>();
            for (XincoWorkItemParameter xincoWorkItemParameterListXincoWorkItemParameterToAttach : xincoWorkItemHasXincoState.getXincoWorkItemParameterList()) {
                xincoWorkItemParameterListXincoWorkItemParameterToAttach = em.getReference(xincoWorkItemParameterListXincoWorkItemParameterToAttach.getClass(), xincoWorkItemParameterListXincoWorkItemParameterToAttach.getXincoWorkItemParameterPK());
                attachedXincoWorkItemParameterList.add(xincoWorkItemParameterListXincoWorkItemParameterToAttach);
            }
            xincoWorkItemHasXincoState.setXincoWorkItemParameterList(attachedXincoWorkItemParameterList);
            em.persist(xincoWorkItemHasXincoState);
            if (xincoWorkflowItem != null) {
                xincoWorkflowItem.getXincoWorkItemHasXincoStateList().add(xincoWorkItemHasXincoState);
                xincoWorkflowItem = em.merge(xincoWorkflowItem);
            }
            if (xincoWorkflowState != null) {
                xincoWorkflowState.getXincoWorkItemHasXincoStateList().add(xincoWorkItemHasXincoState);
                xincoWorkflowState = em.merge(xincoWorkflowState);
            }
            for (XincoWorkItemParameter xincoWorkItemParameterListXincoWorkItemParameter : xincoWorkItemHasXincoState.getXincoWorkItemParameterList()) {
                XincoWorkItemHasXincoState oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListXincoWorkItemParameter = xincoWorkItemParameterListXincoWorkItemParameter.getXincoWorkItemHasXincoState();
                xincoWorkItemParameterListXincoWorkItemParameter.setXincoWorkItemHasXincoState(xincoWorkItemHasXincoState);
                xincoWorkItemParameterListXincoWorkItemParameter = em.merge(xincoWorkItemParameterListXincoWorkItemParameter);
                if (oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListXincoWorkItemParameter != null) {
                    oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListXincoWorkItemParameter.getXincoWorkItemParameterList().remove(xincoWorkItemParameterListXincoWorkItemParameter);
                    oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListXincoWorkItemParameter = em.merge(oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListXincoWorkItemParameter);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoWorkItemHasXincoState(xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK()) != null) {
                throw new PreexistingEntityException("XincoWorkItemHasXincoState " + xincoWorkItemHasXincoState + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoWorkItemHasXincoState xincoWorkItemHasXincoState) throws IllegalOrphanException, NonexistentEntityException, Exception {
        xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK().setXincoStateXincoWorkflowVersion(xincoWorkItemHasXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowVersion());
        xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK().setXincoWorkItemId(xincoWorkItemHasXincoState.getXincoWorkflowItem().getId());
        xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK().setXincoStateXincoWorkflowId(xincoWorkItemHasXincoState.getXincoWorkflowState().getXincoWorkflowStatePK().getXincoWorkflowId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkItemHasXincoState persistentXincoWorkItemHasXincoState = em.find(XincoWorkItemHasXincoState.class, xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK());
            XincoWorkflowItem xincoWorkflowItemOld = persistentXincoWorkItemHasXincoState.getXincoWorkflowItem();
            XincoWorkflowItem xincoWorkflowItemNew = xincoWorkItemHasXincoState.getXincoWorkflowItem();
            XincoWorkflowState xincoWorkflowStateOld = persistentXincoWorkItemHasXincoState.getXincoWorkflowState();
            XincoWorkflowState xincoWorkflowStateNew = xincoWorkItemHasXincoState.getXincoWorkflowState();
            List<XincoWorkItemParameter> xincoWorkItemParameterListOld = persistentXincoWorkItemHasXincoState.getXincoWorkItemParameterList();
            List<XincoWorkItemParameter> xincoWorkItemParameterListNew = xincoWorkItemHasXincoState.getXincoWorkItemParameterList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkItemParameter xincoWorkItemParameterListOldXincoWorkItemParameter : xincoWorkItemParameterListOld) {
                if (!xincoWorkItemParameterListNew.contains(xincoWorkItemParameterListOldXincoWorkItemParameter)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkItemParameter " + xincoWorkItemParameterListOldXincoWorkItemParameter + " since its xincoWorkItemHasXincoState field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (xincoWorkflowItemNew != null) {
                xincoWorkflowItemNew = em.getReference(xincoWorkflowItemNew.getClass(), xincoWorkflowItemNew.getId());
                xincoWorkItemHasXincoState.setXincoWorkflowItem(xincoWorkflowItemNew);
            }
            if (xincoWorkflowStateNew != null) {
                xincoWorkflowStateNew = em.getReference(xincoWorkflowStateNew.getClass(), xincoWorkflowStateNew.getXincoWorkflowStatePK());
                xincoWorkItemHasXincoState.setXincoWorkflowState(xincoWorkflowStateNew);
            }
            List<XincoWorkItemParameter> attachedXincoWorkItemParameterListNew = new ArrayList<XincoWorkItemParameter>();
            for (XincoWorkItemParameter xincoWorkItemParameterListNewXincoWorkItemParameterToAttach : xincoWorkItemParameterListNew) {
                xincoWorkItemParameterListNewXincoWorkItemParameterToAttach = em.getReference(xincoWorkItemParameterListNewXincoWorkItemParameterToAttach.getClass(), xincoWorkItemParameterListNewXincoWorkItemParameterToAttach.getXincoWorkItemParameterPK());
                attachedXincoWorkItemParameterListNew.add(xincoWorkItemParameterListNewXincoWorkItemParameterToAttach);
            }
            xincoWorkItemParameterListNew = attachedXincoWorkItemParameterListNew;
            xincoWorkItemHasXincoState.setXincoWorkItemParameterList(xincoWorkItemParameterListNew);
            xincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoState);
            if (xincoWorkflowItemOld != null && !xincoWorkflowItemOld.equals(xincoWorkflowItemNew)) {
                xincoWorkflowItemOld.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoState);
                xincoWorkflowItemOld = em.merge(xincoWorkflowItemOld);
            }
            if (xincoWorkflowItemNew != null && !xincoWorkflowItemNew.equals(xincoWorkflowItemOld)) {
                xincoWorkflowItemNew.getXincoWorkItemHasXincoStateList().add(xincoWorkItemHasXincoState);
                xincoWorkflowItemNew = em.merge(xincoWorkflowItemNew);
            }
            if (xincoWorkflowStateOld != null && !xincoWorkflowStateOld.equals(xincoWorkflowStateNew)) {
                xincoWorkflowStateOld.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoState);
                xincoWorkflowStateOld = em.merge(xincoWorkflowStateOld);
            }
            if (xincoWorkflowStateNew != null && !xincoWorkflowStateNew.equals(xincoWorkflowStateOld)) {
                xincoWorkflowStateNew.getXincoWorkItemHasXincoStateList().add(xincoWorkItemHasXincoState);
                xincoWorkflowStateNew = em.merge(xincoWorkflowStateNew);
            }
            for (XincoWorkItemParameter xincoWorkItemParameterListNewXincoWorkItemParameter : xincoWorkItemParameterListNew) {
                if (!xincoWorkItemParameterListOld.contains(xincoWorkItemParameterListNewXincoWorkItemParameter)) {
                    XincoWorkItemHasXincoState oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListNewXincoWorkItemParameter = xincoWorkItemParameterListNewXincoWorkItemParameter.getXincoWorkItemHasXincoState();
                    xincoWorkItemParameterListNewXincoWorkItemParameter.setXincoWorkItemHasXincoState(xincoWorkItemHasXincoState);
                    xincoWorkItemParameterListNewXincoWorkItemParameter = em.merge(xincoWorkItemParameterListNewXincoWorkItemParameter);
                    if (oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListNewXincoWorkItemParameter != null && !oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListNewXincoWorkItemParameter.equals(xincoWorkItemHasXincoState)) {
                        oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListNewXincoWorkItemParameter.getXincoWorkItemParameterList().remove(xincoWorkItemParameterListNewXincoWorkItemParameter);
                        oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListNewXincoWorkItemParameter = em.merge(oldXincoWorkItemHasXincoStateOfXincoWorkItemParameterListNewXincoWorkItemParameter);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoWorkItemHasXincoStatePK id = xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK();
                if (findXincoWorkItemHasXincoState(id) == null) {
                    throw new NonexistentEntityException("The xincoWorkItemHasXincoState with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoWorkItemHasXincoStatePK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkItemHasXincoState xincoWorkItemHasXincoState;
            try {
                xincoWorkItemHasXincoState = em.getReference(XincoWorkItemHasXincoState.class, id);
                xincoWorkItemHasXincoState.getXincoWorkItemHasXincoStatePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoWorkItemHasXincoState with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoWorkItemParameter> xincoWorkItemParameterListOrphanCheck = xincoWorkItemHasXincoState.getXincoWorkItemParameterList();
            for (XincoWorkItemParameter xincoWorkItemParameterListOrphanCheckXincoWorkItemParameter : xincoWorkItemParameterListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkItemHasXincoState (" + xincoWorkItemHasXincoState + ") cannot be destroyed since the XincoWorkItemParameter " + xincoWorkItemParameterListOrphanCheckXincoWorkItemParameter + " in its xincoWorkItemParameterList field has a non-nullable xincoWorkItemHasXincoState field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            XincoWorkflowItem xincoWorkflowItem = xincoWorkItemHasXincoState.getXincoWorkflowItem();
            if (xincoWorkflowItem != null) {
                xincoWorkflowItem.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoState);
                xincoWorkflowItem = em.merge(xincoWorkflowItem);
            }
            XincoWorkflowState xincoWorkflowState = xincoWorkItemHasXincoState.getXincoWorkflowState();
            if (xincoWorkflowState != null) {
                xincoWorkflowState.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoState);
                xincoWorkflowState = em.merge(xincoWorkflowState);
            }
            em.remove(xincoWorkItemHasXincoState);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoWorkItemHasXincoState> findXincoWorkItemHasXincoStateEntities() {
        return findXincoWorkItemHasXincoStateEntities(true, -1, -1);
    }

    public List<XincoWorkItemHasXincoState> findXincoWorkItemHasXincoStateEntities(int maxResults, int firstResult) {
        return findXincoWorkItemHasXincoStateEntities(false, maxResults, firstResult);
    }

    private List<XincoWorkItemHasXincoState> findXincoWorkItemHasXincoStateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoWorkItemHasXincoState.class));
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

    public XincoWorkItemHasXincoState findXincoWorkItemHasXincoState(XincoWorkItemHasXincoStatePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoWorkItemHasXincoState.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoWorkItemHasXincoStateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoWorkItemHasXincoState> rt = cq.from(XincoWorkItemHasXincoState.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
