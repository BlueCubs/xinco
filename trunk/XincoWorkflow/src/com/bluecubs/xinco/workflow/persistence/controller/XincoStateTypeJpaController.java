/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence.controller;

import com.bluecubs.xinco.workflow.persistence.XincoStateType;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoStateTypeJpaController {

    public XincoStateTypeJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoWorkflowPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoStateType xincoStateType) {
        if (xincoStateType.getXincoWorkflowStateList() == null) {
            xincoStateType.setXincoWorkflowStateList(new ArrayList<XincoWorkflowState>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoWorkflowState> attachedXincoWorkflowStateList = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowStateToAttach : xincoStateType.getXincoWorkflowStateList()) {
                xincoWorkflowStateListXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateList.add(xincoWorkflowStateListXincoWorkflowStateToAttach);
            }
            xincoStateType.setXincoWorkflowStateList(attachedXincoWorkflowStateList);
            em.persist(xincoStateType);
            for (XincoWorkflowState xincoWorkflowStateListXincoWorkflowState : xincoStateType.getXincoWorkflowStateList()) {
                XincoStateType oldXincoStateTypeIdOfXincoWorkflowStateListXincoWorkflowState = xincoWorkflowStateListXincoWorkflowState.getXincoStateTypeId();
                xincoWorkflowStateListXincoWorkflowState.setXincoStateTypeId(xincoStateType);
                xincoWorkflowStateListXincoWorkflowState = em.merge(xincoWorkflowStateListXincoWorkflowState);
                if (oldXincoStateTypeIdOfXincoWorkflowStateListXincoWorkflowState != null) {
                    oldXincoStateTypeIdOfXincoWorkflowStateListXincoWorkflowState.getXincoWorkflowStateList().remove(xincoWorkflowStateListXincoWorkflowState);
                    oldXincoStateTypeIdOfXincoWorkflowStateListXincoWorkflowState = em.merge(oldXincoStateTypeIdOfXincoWorkflowStateListXincoWorkflowState);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoStateType xincoStateType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoStateType persistentXincoStateType = em.find(XincoStateType.class, xincoStateType.getId());
            List<XincoWorkflowState> xincoWorkflowStateListOld = persistentXincoStateType.getXincoWorkflowStateList();
            List<XincoWorkflowState> xincoWorkflowStateListNew = xincoStateType.getXincoWorkflowStateList();
            List<String> illegalOrphanMessages = null;
            for (XincoWorkflowState xincoWorkflowStateListOldXincoWorkflowState : xincoWorkflowStateListOld) {
                if (!xincoWorkflowStateListNew.contains(xincoWorkflowStateListOldXincoWorkflowState)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoWorkflowState " + xincoWorkflowStateListOldXincoWorkflowState + " since its xincoStateTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoWorkflowState> attachedXincoWorkflowStateListNew = new ArrayList<XincoWorkflowState>();
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowStateToAttach : xincoWorkflowStateListNew) {
                xincoWorkflowStateListNewXincoWorkflowStateToAttach = em.getReference(xincoWorkflowStateListNewXincoWorkflowStateToAttach.getClass(), xincoWorkflowStateListNewXincoWorkflowStateToAttach.getXincoWorkflowStatePK());
                attachedXincoWorkflowStateListNew.add(xincoWorkflowStateListNewXincoWorkflowStateToAttach);
            }
            xincoWorkflowStateListNew = attachedXincoWorkflowStateListNew;
            xincoStateType.setXincoWorkflowStateList(xincoWorkflowStateListNew);
            xincoStateType = em.merge(xincoStateType);
            for (XincoWorkflowState xincoWorkflowStateListNewXincoWorkflowState : xincoWorkflowStateListNew) {
                if (!xincoWorkflowStateListOld.contains(xincoWorkflowStateListNewXincoWorkflowState)) {
                    XincoStateType oldXincoStateTypeIdOfXincoWorkflowStateListNewXincoWorkflowState = xincoWorkflowStateListNewXincoWorkflowState.getXincoStateTypeId();
                    xincoWorkflowStateListNewXincoWorkflowState.setXincoStateTypeId(xincoStateType);
                    xincoWorkflowStateListNewXincoWorkflowState = em.merge(xincoWorkflowStateListNewXincoWorkflowState);
                    if (oldXincoStateTypeIdOfXincoWorkflowStateListNewXincoWorkflowState != null && !oldXincoStateTypeIdOfXincoWorkflowStateListNewXincoWorkflowState.equals(xincoStateType)) {
                        oldXincoStateTypeIdOfXincoWorkflowStateListNewXincoWorkflowState.getXincoWorkflowStateList().remove(xincoWorkflowStateListNewXincoWorkflowState);
                        oldXincoStateTypeIdOfXincoWorkflowStateListNewXincoWorkflowState = em.merge(oldXincoStateTypeIdOfXincoWorkflowStateListNewXincoWorkflowState);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoStateType.getId();
                if (findXincoStateType(id) == null) {
                    throw new NonexistentEntityException("The xincoStateType with id " + id + " no longer exists.");
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
            XincoStateType xincoStateType;
            try {
                xincoStateType = em.getReference(XincoStateType.class, id);
                xincoStateType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoStateType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoWorkflowState> xincoWorkflowStateListOrphanCheck = xincoStateType.getXincoWorkflowStateList();
            for (XincoWorkflowState xincoWorkflowStateListOrphanCheckXincoWorkflowState : xincoWorkflowStateListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoStateType (" + xincoStateType + ") cannot be destroyed since the XincoWorkflowState " + xincoWorkflowStateListOrphanCheckXincoWorkflowState + " in its xincoWorkflowStateList field has a non-nullable xincoStateTypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xincoStateType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoStateType> findXincoStateTypeEntities() {
        return findXincoStateTypeEntities(true, -1, -1);
    }

    public List<XincoStateType> findXincoStateTypeEntities(int maxResults, int firstResult) {
        return findXincoStateTypeEntities(false, maxResults, firstResult);
    }

    private List<XincoStateType> findXincoStateTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoStateType.class));
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

    public XincoStateType findXincoStateType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoStateType.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoStateTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoStateType> rt = cq.from(XincoStateType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
