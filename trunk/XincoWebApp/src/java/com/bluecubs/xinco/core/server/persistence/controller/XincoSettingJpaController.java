/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoSetting;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
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
public class XincoSettingJpaController implements Serializable {

    public XincoSettingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoSetting xincoSetting) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoSetting);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoSetting xincoSetting) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoSetting = em.merge(xincoSetting);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoSetting.getId();
                if (findXincoSetting(id) == null) {
                    throw new NonexistentEntityException("The xincoSetting with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            XincoSetting xincoSetting;
            try {
                xincoSetting = em.getReference(XincoSetting.class, id);
                xincoSetting.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoSetting with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoSetting);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoSetting> findXincoSettingEntities() {
        return findXincoSettingEntities(true, -1, -1);
    }

    public List<XincoSetting> findXincoSettingEntities(int maxResults, int firstResult) {
        return findXincoSettingEntities(false, maxResults, firstResult);
    }

    private List<XincoSetting> findXincoSettingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoSetting.class));
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

    public XincoSetting findXincoSetting(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoSetting.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoSettingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoSetting> rt = cq.from(XincoSetting.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
