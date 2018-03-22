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
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.LpcsHist;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class LpcsHistJpaController implements Serializable {

    public LpcsHistJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LpcsHist lpcsHist) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpcs lpc = lpcsHist.getLpc();
            if (lpc != null) {
                lpc = em.getReference(lpc.getClass(), lpc.getIdLPC());
                lpcsHist.setLpc(lpc);
            }
            em.persist(lpcsHist);
            if (lpc != null) {
                lpc.getLpcsHistList().add(lpcsHist);
                lpc = em.merge(lpc);
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

    public void edit(LpcsHist lpcsHist) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            LpcsHist persistentLpcsHist = em.find(LpcsHist.class, lpcsHist.getIdLpcHist());
            Lpcs lpcOld = persistentLpcsHist.getLpc();
            Lpcs lpcNew = lpcsHist.getLpc();
            if (lpcNew != null) {
                lpcNew = em.getReference(lpcNew.getClass(), lpcNew.getIdLPC());
                lpcsHist.setLpc(lpcNew);
            }
            lpcsHist = em.merge(lpcsHist);
            if (lpcOld != null && !lpcOld.equals(lpcNew)) {
                lpcOld.getLpcsHistList().remove(lpcsHist);
                lpcOld = em.merge(lpcOld);
            }
            if (lpcNew != null && !lpcNew.equals(lpcOld)) {
                lpcNew.getLpcsHistList().add(lpcsHist);
                lpcNew = em.merge(lpcNew);
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
                Integer id = lpcsHist.getIdLpcHist();
                if (findLpcsHist(id) == null) {
                    throw new NonexistentEntityException("The lpcsHist with id " + id + " no longer exists.");
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
            LpcsHist lpcsHist;
            try {
                lpcsHist = em.getReference(LpcsHist.class, id);
                lpcsHist.getIdLpcHist();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lpcsHist with id " + id + " no longer exists.", enfe);
            }
            Lpcs lpc = lpcsHist.getLpc();
            if (lpc != null) {
                lpc.getLpcsHistList().remove(lpcsHist);
                lpc = em.merge(lpc);
            }
            em.remove(lpcsHist);
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

    public List<LpcsHist> findLpcsHistEntities() {
        return findLpcsHistEntities(true, -1, -1);
    }

    public List<LpcsHist> findLpcsHistEntities(int maxResults, int firstResult) {
        return findLpcsHistEntities(false, maxResults, firstResult);
    }

    private List<LpcsHist> findLpcsHistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LpcsHist.class));
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

    public LpcsHist findLpcsHist(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LpcsHist.class, id);
        } finally {
            em.close();
        }
    }

    public int getLpcsHistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LpcsHist> rt = cq.from(LpcsHist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
