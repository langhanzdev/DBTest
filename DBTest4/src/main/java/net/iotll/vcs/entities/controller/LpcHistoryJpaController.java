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
import net.iotll.vcs.entities.Lpc;
import net.iotll.vcs.entities.LpcHistory;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class LpcHistoryJpaController implements Serializable {

    public LpcHistoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LpcHistory lpcHistory) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpc idLpc = lpcHistory.getIdLpc();
            if (idLpc != null) {
                idLpc = em.getReference(idLpc.getClass(), idLpc.getIdLpc());
                lpcHistory.setIdLpc(idLpc);
            }
            em.persist(lpcHistory);
            if (idLpc != null) {
                idLpc.getLpcHistoryList().add(lpcHistory);
                idLpc = em.merge(idLpc);
            }
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

    public void edit(LpcHistory lpcHistory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            LpcHistory persistentLpcHistory = em.find(LpcHistory.class, lpcHistory.getIdLpcHistory());
            Lpc idLpcOld = persistentLpcHistory.getIdLpc();
            Lpc idLpcNew = lpcHistory.getIdLpc();
            if (idLpcNew != null) {
                idLpcNew = em.getReference(idLpcNew.getClass(), idLpcNew.getIdLpc());
                lpcHistory.setIdLpc(idLpcNew);
            }
            lpcHistory = em.merge(lpcHistory);
            if (idLpcOld != null && !idLpcOld.equals(idLpcNew)) {
                idLpcOld.getLpcHistoryList().remove(lpcHistory);
                idLpcOld = em.merge(idLpcOld);
            }
            if (idLpcNew != null && !idLpcNew.equals(idLpcOld)) {
                idLpcNew.getLpcHistoryList().add(lpcHistory);
                idLpcNew = em.merge(idLpcNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lpcHistory.getIdLpcHistory();
                if (findLpcHistory(id) == null) {
                    throw new NonexistentEntityException("The lpcHistory with id " + id + " no longer exists.");
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
            LpcHistory lpcHistory;
            try {
                lpcHistory = em.getReference(LpcHistory.class, id);
                lpcHistory.getIdLpcHistory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lpcHistory with id " + id + " no longer exists.", enfe);
            }
            Lpc idLpc = lpcHistory.getIdLpc();
            if (idLpc != null) {
                idLpc.getLpcHistoryList().remove(lpcHistory);
                idLpc = em.merge(idLpc);
            }
            em.remove(lpcHistory);
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

    public List<LpcHistory> findLpcHistoryEntities() {
        return findLpcHistoryEntities(true, -1, -1);
    }

    public List<LpcHistory> findLpcHistoryEntities(int maxResults, int firstResult) {
        return findLpcHistoryEntities(false, maxResults, firstResult);
    }

    private List<LpcHistory> findLpcHistoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LpcHistory.class));
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

    public LpcHistory findLpcHistory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LpcHistory.class, id);
        } finally {
            em.close();
        }
    }

    public int getLpcHistoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LpcHistory> rt = cq.from(LpcHistory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
