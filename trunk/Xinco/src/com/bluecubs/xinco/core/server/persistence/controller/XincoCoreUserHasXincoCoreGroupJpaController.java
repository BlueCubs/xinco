/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence.controller;

import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupJpaController {

    public XincoCoreUserHasXincoCoreGroupJpaController() {
        emf = Persistence.createEntityManagerFactory("XincoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup) throws PreexistingEntityException, Exception {
        if (xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK() == null) {
            xincoCoreUserHasXincoCoreGroup.setXincoCoreUserHasXincoCoreGroupPK(new XincoCoreUserHasXincoCoreGroupPK());
        }
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup().getId());
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserHasXincoCoreGroup.getXincoCoreUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreGroup xincoCoreGroup = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup = em.getReference(xincoCoreGroup.getClass(), xincoCoreGroup.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroup);
            }
            XincoCoreUser xincoCoreUser = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser = em.getReference(xincoCoreUser.getClass(), xincoCoreUser.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUser);
            }
            em.persist(xincoCoreUserHasXincoCoreGroup);
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreUserHasXincoCoreGroup(xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK()) != null) {
                throw new PreexistingEntityException("XincoCoreUserHasXincoCoreGroup " + xincoCoreUserHasXincoCoreGroup + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup) throws NonexistentEntityException, Exception {
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup().getId());
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserHasXincoCoreGroup.getXincoCoreUser().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserHasXincoCoreGroup persistentXincoCoreUserHasXincoCoreGroup = em.find(XincoCoreUserHasXincoCoreGroup.class, xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK());
            XincoCoreGroup xincoCoreGroupOld = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            XincoCoreGroup xincoCoreGroupNew = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            if (xincoCoreGroupNew != null) {
                xincoCoreGroupNew = em.getReference(xincoCoreGroupNew.getClass(), xincoCoreGroupNew.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroupNew);
            }
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUserNew);
            }
            xincoCoreUserHasXincoCoreGroup = em.merge(xincoCoreUserHasXincoCoreGroup);
            if (xincoCoreGroupOld != null && !xincoCoreGroupOld.equals(xincoCoreGroupNew)) {
                xincoCoreGroupOld.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroupOld = em.merge(xincoCoreGroupOld);
            }
            if (xincoCoreGroupNew != null && !xincoCoreGroupNew.equals(xincoCoreGroupOld)) {
                xincoCoreGroupNew.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroupNew = em.merge(xincoCoreGroupNew);
            }
            if (xincoCoreUserOld != null && !xincoCoreUserOld.equals(xincoCoreUserNew)) {
                xincoCoreUserOld.getXincoCoreUserHasXincoCoreGroupList1().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUserOld = em.merge(xincoCoreUserOld);
            }
            if (xincoCoreUserNew != null && !xincoCoreUserNew.equals(xincoCoreUserOld)) {
                xincoCoreUserNew.getXincoCoreUserHasXincoCoreGroupList1().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUserNew = em.merge(xincoCoreUserNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                XincoCoreUserHasXincoCoreGroupPK id = xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK();
                if (findXincoCoreUserHasXincoCoreGroup(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreUserHasXincoCoreGroup with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XincoCoreUserHasXincoCoreGroupPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup;
            try {
                xincoCoreUserHasXincoCoreGroup = em.getReference(XincoCoreUserHasXincoCoreGroup.class, id);
                xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreUserHasXincoCoreGroup with id " + id + " no longer exists.", enfe);
            }
            XincoCoreGroup xincoCoreGroup = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            if (xincoCoreGroup != null) {
                xincoCoreGroup.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup = em.merge(xincoCoreGroup);
            }
            XincoCoreUser xincoCoreUser = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            if (xincoCoreUser != null) {
                xincoCoreUser.getXincoCoreUserHasXincoCoreGroupList1().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser = em.merge(xincoCoreUser);
            }
            em.remove(xincoCoreUserHasXincoCoreGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreUserHasXincoCoreGroup> findXincoCoreUserHasXincoCoreGroupEntities() {
        return findXincoCoreUserHasXincoCoreGroupEntities(true, -1, -1);
    }

    public List<XincoCoreUserHasXincoCoreGroup> findXincoCoreUserHasXincoCoreGroupEntities(int maxResults, int firstResult) {
        return findXincoCoreUserHasXincoCoreGroupEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreUserHasXincoCoreGroup> findXincoCoreUserHasXincoCoreGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreUserHasXincoCoreGroup.class));
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

    public XincoCoreUserHasXincoCoreGroup findXincoCoreUserHasXincoCoreGroup(XincoCoreUserHasXincoCoreGroupPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreUserHasXincoCoreGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreUserHasXincoCoreGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreUserHasXincoCoreGroup> rt = cq.from(XincoCoreUserHasXincoCoreGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
