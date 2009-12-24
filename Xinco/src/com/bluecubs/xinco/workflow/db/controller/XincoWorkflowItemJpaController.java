/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db.controller;

import com.bluecubs.xinco.workflow.db.XincoWorkflowItem;
import com.bluecubs.xinco.workflow.db.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.db.XincoWorkItemHasXincoState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowItemJpaController {

    public XincoWorkflowItemJpaController() {
        emf = com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoWorkflowItem xincoWorkflowItem) {
        if (xincoWorkflowItem.getXincoWorkItemHasXincoStateList() == null) {
            xincoWorkflowItem.setXincoWorkItemHasXincoStateList(new ArrayList<XincoWorkItemHasXincoState>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoWorkItemHasXincoState> attachedXincoWorkItemHasXincoStateList = new ArrayList<XincoWorkItemHasXincoState>();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach : xincoWorkflowItem.getXincoWorkItemHasXincoStateList()) {
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach = em.getReference(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach.getClass(), xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach.getXincoWorkItemHasXincoStatePK());
                attachedXincoWorkItemHasXincoStateList.add(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoStateToAttach);
            }
            xincoWorkflowItem.setXincoWorkItemHasXincoStateList(attachedXincoWorkItemHasXincoStateList);
            em.persist(xincoWorkflowItem);
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState : xincoWorkflowItem.getXincoWorkItemHasXincoStateList()) {
                XincoWorkflowItem oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState = xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState.getXincoWorkflowItem();
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState.setXincoWorkflowItem(xincoWorkflowItem);
                xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState);
                if (oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState != null) {
                    oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoStateListXincoWorkItemHasXincoState);
                    oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState = em.merge(oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListXincoWorkItemHasXincoState);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoWorkflowItem xincoWorkflowItem) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoWorkflowItem persistentXincoWorkflowItem = em.find(XincoWorkflowItem.class, xincoWorkflowItem.getId());
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListOld = persistentXincoWorkflowItem.getXincoWorkItemHasXincoStateList();
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListNew = xincoWorkflowItem.getXincoWorkItemHasXincoStateList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListOld) {
                if (!xincoWorkItemHasXincoStateListNew.contains(xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkItemHasXincoState " + xincoWorkItemHasXincoStateListOldXincoWorkItemHasXincoState + " since its xincoWorkflowItem field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoWorkItemHasXincoState> attachedXincoWorkItemHasXincoStateListNew = new ArrayList<XincoWorkItemHasXincoState>();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach : xincoWorkItemHasXincoStateListNew) {
                xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach = em.getReference(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach.getClass(), xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach.getXincoWorkItemHasXincoStatePK());
                attachedXincoWorkItemHasXincoStateListNew.add(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoStateToAttach);
            }
            xincoWorkItemHasXincoStateListNew = attachedXincoWorkItemHasXincoStateListNew;
            xincoWorkflowItem.setXincoWorkItemHasXincoStateList(xincoWorkItemHasXincoStateListNew);
            xincoWorkflowItem = em.merge(xincoWorkflowItem);
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListNew) {
                if (!xincoWorkItemHasXincoStateListOld.contains(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState)) {
                    XincoWorkflowItem oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState = xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.getXincoWorkflowItem();
                    xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.setXincoWorkflowItem(xincoWorkflowItem);
                    xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState = em.merge(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState);
                    if (oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState != null && !oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.equals(xincoWorkflowItem)) {
                        oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState.getXincoWorkItemHasXincoStateList().remove(xincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState);
                        oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState = em.merge(oldXincoWorkflowItemOfXincoWorkItemHasXincoStateListNewXincoWorkItemHasXincoState);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoWorkflowItem.getId();
                if (findXincoWorkflowItem(id) == null) {
                    throw new NonexistentEntityException("The xincoWorkflowItem with id " + id + " no longer exists.");
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
            XincoWorkflowItem xincoWorkflowItem;
            try {
                xincoWorkflowItem = em.getReference(XincoWorkflowItem.class, id);
                xincoWorkflowItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoWorkflowItem with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateListOrphanCheck = xincoWorkflowItem.getXincoWorkItemHasXincoStateList();
            for (XincoWorkItemHasXincoState xincoWorkItemHasXincoStateListOrphanCheckXincoWorkItemHasXincoState : xincoWorkItemHasXincoStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoWorkflowItem (" + xincoWorkflowItem + ") cannot be destroyed since the XincoWorkItemHasXincoState " + xincoWorkItemHasXincoStateListOrphanCheckXincoWorkItemHasXincoState + " in its xincoWorkItemHasXincoStateList field has a non-nullable xincoWorkflowItem field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xincoWorkflowItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoWorkflowItem> findXincoWorkflowItemEntities() {
        return findXincoWorkflowItemEntities(true, -1, -1);
    }

    public List<XincoWorkflowItem> findXincoWorkflowItemEntities(int maxResults, int firstResult) {
        return findXincoWorkflowItemEntities(false, maxResults, firstResult);
    }

    private List<XincoWorkflowItem> findXincoWorkflowItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoWorkflowItem.class));
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

    public XincoWorkflowItem findXincoWorkflowItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoWorkflowItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoWorkflowItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoWorkflowItem> rt = cq.from(XincoWorkflowItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
