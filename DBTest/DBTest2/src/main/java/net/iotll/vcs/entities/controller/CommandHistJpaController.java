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
import net.iotll.vcs.entities.CommandHist;
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.Scs;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class CommandHistJpaController implements Serializable {

    public CommandHistJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommandHist commandHist) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpcs lpc = commandHist.getLpc();
            if (lpc != null) {
                lpc = em.getReference(lpc.getClass(), lpc.getIdLPC());
                commandHist.setLpc(lpc);
            }
            Scs sc = commandHist.getSc();
            if (sc != null) {
                sc = em.getReference(sc.getClass(), sc.getIdSegmentController());
                commandHist.setSc(sc);
            }
            em.persist(commandHist);
            if (lpc != null) {
                lpc.getCommandHistList().add(commandHist);
                lpc = em.merge(lpc);
            }
            if (sc != null) {
                sc.getCommandHistList().add(commandHist);
                sc = em.merge(sc);
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

    public void edit(CommandHist commandHist) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CommandHist persistentCommandHist = em.find(CommandHist.class, commandHist.getIdCommandHist());
            Lpcs lpcOld = persistentCommandHist.getLpc();
            Lpcs lpcNew = commandHist.getLpc();
            Scs scOld = persistentCommandHist.getSc();
            Scs scNew = commandHist.getSc();
            if (lpcNew != null) {
                lpcNew = em.getReference(lpcNew.getClass(), lpcNew.getIdLPC());
                commandHist.setLpc(lpcNew);
            }
            if (scNew != null) {
                scNew = em.getReference(scNew.getClass(), scNew.getIdSegmentController());
                commandHist.setSc(scNew);
            }
            commandHist = em.merge(commandHist);
            if (lpcOld != null && !lpcOld.equals(lpcNew)) {
                lpcOld.getCommandHistList().remove(commandHist);
                lpcOld = em.merge(lpcOld);
            }
            if (lpcNew != null && !lpcNew.equals(lpcOld)) {
                lpcNew.getCommandHistList().add(commandHist);
                lpcNew = em.merge(lpcNew);
            }
            if (scOld != null && !scOld.equals(scNew)) {
                scOld.getCommandHistList().remove(commandHist);
                scOld = em.merge(scOld);
            }
            if (scNew != null && !scNew.equals(scOld)) {
                scNew.getCommandHistList().add(commandHist);
                scNew = em.merge(scNew);
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
                Integer id = commandHist.getIdCommandHist();
                if (findCommandHist(id) == null) {
                    throw new NonexistentEntityException("The commandHist with id " + id + " no longer exists.");
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
            CommandHist commandHist;
            try {
                commandHist = em.getReference(CommandHist.class, id);
                commandHist.getIdCommandHist();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commandHist with id " + id + " no longer exists.", enfe);
            }
            Lpcs lpc = commandHist.getLpc();
            if (lpc != null) {
                lpc.getCommandHistList().remove(commandHist);
                lpc = em.merge(lpc);
            }
            Scs sc = commandHist.getSc();
            if (sc != null) {
                sc.getCommandHistList().remove(commandHist);
                sc = em.merge(sc);
            }
            em.remove(commandHist);
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

    public List<CommandHist> findCommandHistEntities() {
        return findCommandHistEntities(true, -1, -1);
    }

    public List<CommandHist> findCommandHistEntities(int maxResults, int firstResult) {
        return findCommandHistEntities(false, maxResults, firstResult);
    }

    private List<CommandHist> findCommandHistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommandHist.class));
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

    public CommandHist findCommandHist(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommandHist.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandHistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommandHist> rt = cq.from(CommandHist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
