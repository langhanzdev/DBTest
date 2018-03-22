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
import net.iotll.vcs.entities.ReceivedMessageHistory;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class ReceivedMessageHistoryJpaController implements Serializable {

    public ReceivedMessageHistoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReceivedMessageHistory receivedMessageHistory) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(receivedMessageHistory);
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

    public void edit(ReceivedMessageHistory receivedMessageHistory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            receivedMessageHistory = em.merge(receivedMessageHistory);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = receivedMessageHistory.getIdReceivedMessageHistory();
                if (findReceivedMessageHistory(id) == null) {
                    throw new NonexistentEntityException("The receivedMessageHistory with id " + id + " no longer exists.");
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
            ReceivedMessageHistory receivedMessageHistory;
            try {
                receivedMessageHistory = em.getReference(ReceivedMessageHistory.class, id);
                receivedMessageHistory.getIdReceivedMessageHistory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The receivedMessageHistory with id " + id + " no longer exists.", enfe);
            }
            em.remove(receivedMessageHistory);
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

    public List<ReceivedMessageHistory> findReceivedMessageHistoryEntities() {
        return findReceivedMessageHistoryEntities(true, -1, -1);
    }

    public List<ReceivedMessageHistory> findReceivedMessageHistoryEntities(int maxResults, int firstResult) {
        return findReceivedMessageHistoryEntities(false, maxResults, firstResult);
    }

    private List<ReceivedMessageHistory> findReceivedMessageHistoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReceivedMessageHistory.class));
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

    public ReceivedMessageHistory findReceivedMessageHistory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReceivedMessageHistory.class, id);
        } finally {
            em.close();
        }
    }

    public int getReceivedMessageHistoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReceivedMessageHistory> rt = cq.from(ReceivedMessageHistory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
