/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence.controller;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributeT;
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
public class XincoCoreDataTypeAttributeTJpaController implements Serializable {

    public XincoCoreDataTypeAttributeTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XincoCoreDataTypeAttributeT xincoCoreDataTypeAttributeT) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(xincoCoreDataTypeAttributeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findXincoCoreDataTypeAttributeT(xincoCoreDataTypeAttributeT.getRecordId()) != null) {
                throw new PreexistingEntityException("XincoCoreDataTypeAttributeT " + xincoCoreDataTypeAttributeT + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XincoCoreDataTypeAttributeT xincoCoreDataTypeAttributeT) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            xincoCoreDataTypeAttributeT = em.merge(xincoCoreDataTypeAttributeT);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = xincoCoreDataTypeAttributeT.getRecordId();
                if (findXincoCoreDataTypeAttributeT(id) == null) {
                    throw new NonexistentEntityException("The xincoCoreDataTypeAttributeT with id " + id + " no longer exists.");
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
            XincoCoreDataTypeAttributeT xincoCoreDataTypeAttributeT;
            try {
                xincoCoreDataTypeAttributeT = em.getReference(XincoCoreDataTypeAttributeT.class, id);
                xincoCoreDataTypeAttributeT.getRecordId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xincoCoreDataTypeAttributeT with id " + id + " no longer exists.", enfe);
            }
            em.remove(xincoCoreDataTypeAttributeT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XincoCoreDataTypeAttributeT> findXincoCoreDataTypeAttributeTEntities() {
        return findXincoCoreDataTypeAttributeTEntities(true, -1, -1);
    }

    public List<XincoCoreDataTypeAttributeT> findXincoCoreDataTypeAttributeTEntities(int maxResults, int firstResult) {
        return findXincoCoreDataTypeAttributeTEntities(false, maxResults, firstResult);
    }

    private List<XincoCoreDataTypeAttributeT> findXincoCoreDataTypeAttributeTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XincoCoreDataTypeAttributeT.class));
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

    public XincoCoreDataTypeAttributeT findXincoCoreDataTypeAttributeT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XincoCoreDataTypeAttributeT.class, id);
        } finally {
            em.close();
        }
    }

    public int getXincoCoreDataTypeAttributeTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XincoCoreDataTypeAttributeT> rt = cq.from(XincoCoreDataTypeAttributeT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
