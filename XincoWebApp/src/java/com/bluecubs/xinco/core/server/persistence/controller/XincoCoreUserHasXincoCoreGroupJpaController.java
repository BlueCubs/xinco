/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupJpaController implements Serializable {

    public XincoCoreUserHasXincoCoreGroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreUserHasXincoCoreGroup xincoCoreUserHasXincoCoreGroup) throws PreexistingEntityException, Exception {
        if (xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK() == null) {
            xincoCoreUserHasXincoCoreGroup.setXincoCoreUserHasXincoCoreGroupPK(new XincoCoreUserHasXincoCoreGroupPK());
        }
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserHasXincoCoreGroup.getXincoCoreUser1().getId());
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup1().getId());
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
            XincoCoreGroup xincoCoreGroup1 = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup1();
            if (xincoCoreGroup1 != null) {
                xincoCoreGroup1 = em.getReference(xincoCoreGroup1.getClass(), xincoCoreGroup1.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup1(xincoCoreGroup1);
            }
            XincoCoreUser xincoCoreUser1 = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser1();
            if (xincoCoreUser1 != null) {
                xincoCoreUser1 = em.getReference(xincoCoreUser1.getClass(), xincoCoreUser1.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser1(xincoCoreUser1);
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
            if (xincoCoreGroup1 != null) {
                xincoCoreGroup1.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup1 = em.merge(xincoCoreGroup1);
            }
            if (xincoCoreUser1 != null) {
                xincoCoreUser1.getXincoCoreUserHasXincoCoreGroupList1().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser1 = em.merge(xincoCoreUser1);
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
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserHasXincoCoreGroup.getXincoCoreUser1().getId());
        xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoCoreUserHasXincoCoreGroup persistentXincoCoreUserHasXincoCoreGroup = em.find(XincoCoreUserHasXincoCoreGroup.class, xincoCoreUserHasXincoCoreGroup.getXincoCoreUserHasXincoCoreGroupPK());
            XincoCoreGroup xincoCoreGroupOld = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            XincoCoreGroup xincoCoreGroupNew = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup();
            XincoCoreUser xincoCoreUserOld = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            XincoCoreUser xincoCoreUserNew = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser();
            XincoCoreGroup xincoCoreGroup1Old = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreGroup1();
            XincoCoreGroup xincoCoreGroup1New = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup1();
            XincoCoreUser xincoCoreUser1Old = persistentXincoCoreUserHasXincoCoreGroup.getXincoCoreUser1();
            XincoCoreUser xincoCoreUser1New = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser1();
            if (xincoCoreGroupNew != null) {
                xincoCoreGroupNew = em.getReference(xincoCoreGroupNew.getClass(), xincoCoreGroupNew.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup(xincoCoreGroupNew);
            }
            if (xincoCoreUserNew != null) {
                xincoCoreUserNew = em.getReference(xincoCoreUserNew.getClass(), xincoCoreUserNew.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser(xincoCoreUserNew);
            }
            if (xincoCoreGroup1New != null) {
                xincoCoreGroup1New = em.getReference(xincoCoreGroup1New.getClass(), xincoCoreGroup1New.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreGroup1(xincoCoreGroup1New);
            }
            if (xincoCoreUser1New != null) {
                xincoCoreUser1New = em.getReference(xincoCoreUser1New.getClass(), xincoCoreUser1New.getId());
                xincoCoreUserHasXincoCoreGroup.setXincoCoreUser1(xincoCoreUser1New);
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
            if (xincoCoreGroup1Old != null && !xincoCoreGroup1Old.equals(xincoCoreGroup1New)) {
                xincoCoreGroup1Old.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup1Old = em.merge(xincoCoreGroup1Old);
            }
            if (xincoCoreGroup1New != null && !xincoCoreGroup1New.equals(xincoCoreGroup1Old)) {
                xincoCoreGroup1New.getXincoCoreUserHasXincoCoreGroupList().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup1New = em.merge(xincoCoreGroup1New);
            }
            if (xincoCoreUser1Old != null && !xincoCoreUser1Old.equals(xincoCoreUser1New)) {
                xincoCoreUser1Old.getXincoCoreUserHasXincoCoreGroupList1().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser1Old = em.merge(xincoCoreUser1Old);
            }
            if (xincoCoreUser1New != null && !xincoCoreUser1New.equals(xincoCoreUser1Old)) {
                xincoCoreUser1New.getXincoCoreUserHasXincoCoreGroupList1().add(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser1New = em.merge(xincoCoreUser1New);
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
            XincoCoreGroup xincoCoreGroup1 = xincoCoreUserHasXincoCoreGroup.getXincoCoreGroup1();
            if (xincoCoreGroup1 != null) {
                xincoCoreGroup1.getXincoCoreUserHasXincoCoreGroupList().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreGroup1 = em.merge(xincoCoreGroup1);
            }
            XincoCoreUser xincoCoreUser1 = xincoCoreUserHasXincoCoreGroup.getXincoCoreUser1();
            if (xincoCoreUser1 != null) {
                xincoCoreUser1.getXincoCoreUserHasXincoCoreGroupList1().remove(xincoCoreUserHasXincoCoreGroup);
                xincoCoreUser1 = em.merge(xincoCoreUser1);
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
