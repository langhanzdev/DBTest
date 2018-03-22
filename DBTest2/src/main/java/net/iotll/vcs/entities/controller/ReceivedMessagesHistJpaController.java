/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.ReceivedMessagesHist;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class ReceivedMessagesHistJpaController implements Serializable {

    public ReceivedMessagesHistJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReceivedMessagesHist receivedMessagesHist) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(receivedMessagesHist);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReceivedMessagesHist receivedMessagesHist) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            receivedMessagesHist = em.merge(receivedMessagesHist);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = receivedMessagesHist.getIdMessage();
                if (findReceivedMessagesHist(id) == null) {
                    throw new NonexistentEntityException("The receivedMessagesHist with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ReceivedMessagesHist receivedMessagesHist;
            try {
                receivedMessagesHist = em.getReference(ReceivedMessagesHist.class, id);
                receivedMessagesHist.getIdMessage();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The receivedMessagesHist with id " + id + " no longer exists.", enfe);
            }
            em.remove(receivedMessagesHist);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReceivedMessagesHist> findReceivedMessagesHistEntities() {
        return findReceivedMessagesHistEntities(true, -1, -1);
    }

    public List<ReceivedMessagesHist> findReceivedMessagesHistEntities(int maxResults, int firstResult) {
        return findReceivedMessagesHistEntities(false, maxResults, firstResult);
    }

    private List<ReceivedMessagesHist> findReceivedMessagesHistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReceivedMessagesHist.class));
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

    public ReceivedMessagesHist findReceivedMessagesHist(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReceivedMessagesHist.class, id);
        } finally {
            em.close();
        }
    }

    public int getReceivedMessagesHistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReceivedMessagesHist> rt = cq.from(ReceivedMessagesHist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
