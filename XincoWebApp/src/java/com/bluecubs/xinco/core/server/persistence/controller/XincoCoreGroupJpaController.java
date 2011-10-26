/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoCoreGroupJpaController implements Serializable {

    public XincoCoreGroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreGroup xincoCoreGroup) {
        if (xincoCoreGroup.getXincoCoreAceList() == null) {
            xincoCoreGroup.setXincoCoreAceList(new ArrayList<XincoCoreAce>());
        }
        if (xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList() == null) {
            xincoCoreGroup.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<XincoCoreUserHasXincoCoreGroup>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<XincoCoreAce> attachedXincoCoreAceList = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListXincoCoreAceToAttach : xincoCoreGroup.getXincoCoreAceList()) {
                xincoCoreAceListXincoCoreAceToAttach = em.getReference(xincoCoreAceListXincoCoreAceToAttach.getClass(), xincoCoreAceListXincoCoreAceToAttach.getId());
                attachedXincoCoreAceList.add(xincoCoreAceListXincoCoreAceToAttach);
            }
            xincoCoreGroup.setXincoCoreAceList(attachedXincoCoreAceList);
            List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupList = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach : xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList()) {
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach = em.getReference(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach.getClass(), xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach.getXincoCoreUserHasXincoCoreGroupPK());
                attachedXincoCoreUserHasXincoCoreGroupList.add(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroupToAttach);
            }
            xincoCoreGroup.setXincoCoreUserHasXincoCoreGroupList(attachedXincoCoreUserHasXincoCoreGroupList);
            em.persist(xincoCoreGroup);
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreGroup.getXincoCoreAceList()) {
                XincoCoreGroup oldXincoCoreGroupOfXincoCoreAceListXincoCoreAce = xincoCoreAceListXincoCoreAce.getXincoCoreGroup();
                xincoCoreAceListXincoCoreAce.setXincoCoreGroup(xincoCoreGroup);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
                if (oldXincoCoreGroupOfXincoCoreAceListXincoCoreAce != null) {
                    oldXincoCoreGroupOfXincoCoreAceListXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListXincoCoreAce);
                    oldXincoCoreGroupOfXincoCoreAceListXincoCoreAce = em.merge(oldXincoCoreGroupOfXincoCoreAceListXincoCoreAce);
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup : xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList()) {
                XincoCoreGroup oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup = xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroup);
                xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
                if (oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup != null) {
                    oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
                    oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup = em.merge(oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListXincoCoreUserHasXincoCoreGroup);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreGroup xincoCoreGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreGroup persistentXincoCoreGroup = em.find(XincoCoreGroup.class, xincoCoreGroup.getId());
            List<XincoCoreAce> xincoCoreAceListOld = persistentXincoCoreGroup.getXincoCoreAceList();
            List<XincoCoreAce> xincoCoreAceListNew = xincoCoreGroup.getXincoCoreAceList();
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListOld = persistentXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList();
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListNew = xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList();
            List<String> illegalOrphanMessages = null;
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupListOld) {
                if (!xincoCoreUserHasXincoCoreGroupListNew.contains(xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroupListOldXincoCoreUserHasXincoCoreGroup + " since its xincoCoreGroup field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreAce> attachedXincoCoreAceListNew = new ArrayList<XincoCoreAce>();
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAceToAttach : xincoCoreAceListNew) {
                xincoCoreAceListNewXincoCoreAceToAttach = em.getReference(xincoCoreAceListNewXincoCoreAceToAttach.getClass(), xincoCoreAceListNewXincoCoreAceToAttach.getId());
                attachedXincoCoreAceListNew.add(xincoCoreAceListNewXincoCoreAceToAttach);
            }
            xincoCoreAceListNew = attachedXincoCoreAceListNew;
            xincoCoreGroup.setXincoCoreAceList(xincoCoreAceListNew);
            List<XincoCoreUserHasXincoCoreGroup> attachedXincoCoreUserHasXincoCoreGroupListNew = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach : xincoCoreUserHasXincoCoreGroupListNew) {
                xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach = em.getReference(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach.getClass(), xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach.getXincoCoreUserHasXincoCoreGroupPK());
                attachedXincoCoreUserHasXincoCoreGroupListNew.add(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroupToAttach);
            }
            xincoCoreUserHasXincoCoreGroupListNew = attachedXincoCoreUserHasXincoCoreGroupListNew;
            xincoCoreGroup.setXincoCoreUserHasXincoCoreGroupList(xincoCoreUserHasXincoCoreGroupListNew);
            xincoCoreGroup = em.merge(xincoCoreGroup);
            for (XincoCoreAce xincoCoreAceListOldXincoCoreAce : xincoCoreAceListOld) {
                if (!xincoCoreAceListNew.contains(xincoCoreAceListOldXincoCoreAce)) {
                    xincoCoreAceListOldXincoCoreAce.setXincoCoreGroup(null);
                    xincoCoreAceListOldXincoCoreAce = em.merge(xincoCoreAceListOldXincoCoreAce);
                }
            }
            for (XincoCoreAce xincoCoreAceListNewXincoCoreAce : xincoCoreAceListNew) {
                if (!xincoCoreAceListOld.contains(xincoCoreAceListNewXincoCoreAce)) {
                    XincoCoreGroup oldXincoCoreGroupOfXincoCoreAceListNewXincoCoreAce = xincoCoreAceListNewXincoCoreAce.getXincoCoreGroup();
                    xincoCoreAceListNewXincoCoreAce.setXincoCoreGroup(xincoCoreGroup);
                    xincoCoreAceListNewXincoCoreAce = em.merge(xincoCoreAceListNewXincoCoreAce);
                    if (oldXincoCoreGroupOfXincoCoreAceListNewXincoCoreAce != null && !oldXincoCoreGroupOfXincoCoreAceListNewXincoCoreAce.equals(xincoCoreGroup)) {
                        oldXincoCoreGroupOfXincoCoreAceListNewXincoCoreAce.getXincoCoreAceList().remove(xincoCoreAceListNewXincoCoreAce);
                        oldXincoCoreGroupOfXincoCoreAceListNewXincoCoreAce = em.merge(oldXincoCoreGroupOfXincoCoreAceListNewXincoCoreAce);
                    }
                }
            }
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupListNew) {
                if (!xincoCoreUserHasXincoCoreGroupListOld.contains(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup)) {
                    XincoCoreGroup oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup = xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
                    xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroup);
                    xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
                    if (oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup != null && !oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.equals(xincoCoreGroup)) {
                        oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
                        oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup = em.merge(oldXincoCoreGroupOfXincoCoreUserHasXincoCoreGroupListNewXincoCoreUserHasXincoCoreGroup);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreGroup.getId();
                if (findXincoCoreGroup(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreGroup with id " + id + " no longer exists.");
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
            XincoCoreGroup xincoCoreGroup;
            try {
                xincoCoreGroup = em.getReference(XincoCoreGroup.class, id);
                xincoCoreGroup.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreGroup with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupListOrphanCheck = xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList();
            for (XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroupListOrphanCheckXincoCoreUserHasXincoCoreGroup : xincoCoreUserHasXincoCoreGroupListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XincoCoreGroup (" + xincoCoreGroup + ") cannot be destroyed since the XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroupListOrphanCheckXincoCoreUserHasXincoCoreGroup + " in its xincoCoreUserHasXincoCoreGroupList field has a non-nullable xincoCoreGroup field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<XincoCoreAce> xincoCoreAceList = xincoCoreGroup.getXincoCoreAceList();
            for (XincoCoreAce xincoCoreAceListXincoCoreAce : xincoCoreAceList) {
                xincoCoreAceListXincoCoreAce.setXincoCoreGroup(null);
                xincoCoreAceListXincoCoreAce = em.merge(xincoCoreAceListXincoCoreAce);
            }
            em.remove(xincoCoreGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreGroup> findXincoCoreGroupEntities() {
        return findXincoCoreGroupEntities(true, -1, -1);
    }

    public List<XincoCoreGroup> findXincoCoreGroupEntities(int maxResults, int firstResult) {
        return findXincoCoreGroupEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreGroup> findXincoCoreGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreGroup.class));
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

    public XincoCoreGroup findXincoCoreGroup(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreGroup> rt = cq.from(XincoCoreGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
