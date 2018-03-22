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
import net.iotll.vcs.entities.CommandHistory;
import net.iotll.vcs.entities.Lpc;
import net.iotll.vcs.entities.Sc;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class CommandHistoryJpaController implements Serializable {

    public CommandHistoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommandHistory commandHistory) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpc idLpc = commandHistory.getIdLpc();
            if (idLpc != null) {
                idLpc = em.getReference(idLpc.getClass(), idLpc.getIdLpc());
                commandHistory.setIdLpc(idLpc);
            }
            Sc idSc = commandHistory.getIdSc();
            if (idSc != null) {
                idSc = em.getReference(idSc.getClass(), idSc.getIdSc());
                commandHistory.setIdSc(idSc);
            }
            em.persist(commandHistory);
            if (idLpc != null) {
                idLpc.getCommandHistoryList().add(commandHistory);
                idLpc = em.merge(idLpc);
            }
            if (idSc != null) {
                idSc.getCommandHistoryList().add(commandHistory);
                idSc = em.merge(idSc);
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

    public void edit(CommandHistory commandHistory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CommandHistory persistentCommandHistory = em.find(CommandHistory.class, commandHistory.getIdCommandHistory());
            Lpc idLpcOld = persistentCommandHistory.getIdLpc();
            Lpc idLpcNew = commandHistory.getIdLpc();
            Sc idScOld = persistentCommandHistory.getIdSc();
            Sc idScNew = commandHistory.getIdSc();
            if (idLpcNew != null) {
                idLpcNew = em.getReference(idLpcNew.getClass(), idLpcNew.getIdLpc());
                commandHistory.setIdLpc(idLpcNew);
            }
            if (idScNew != null) {
                idScNew = em.getReference(idScNew.getClass(), idScNew.getIdSc());
                commandHistory.setIdSc(idScNew);
            }
            commandHistory = em.merge(commandHistory);
            if (idLpcOld != null && !idLpcOld.equals(idLpcNew)) {
                idLpcOld.getCommandHistoryList().remove(commandHistory);
                idLpcOld = em.merge(idLpcOld);
            }
            if (idLpcNew != null && !idLpcNew.equals(idLpcOld)) {
                idLpcNew.getCommandHistoryList().add(commandHistory);
                idLpcNew = em.merge(idLpcNew);
            }
            if (idScOld != null && !idScOld.equals(idScNew)) {
                idScOld.getCommandHistoryList().remove(commandHistory);
                idScOld = em.merge(idScOld);
            }
            if (idScNew != null && !idScNew.equals(idScOld)) {
                idScNew.getCommandHistoryList().add(commandHistory);
                idScNew = em.merge(idScNew);
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
                Integer id = commandHistory.getIdCommandHistory();
                if (findCommandHistory(id) == null) {
                    throw new NonexistentEntityException("The commandHistory with id " + id + " no longer exists.");
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
            CommandHistory commandHistory;
            try {
                commandHistory = em.getReference(CommandHistory.class, id);
                commandHistory.getIdCommandHistory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commandHistory with id " + id + " no longer exists.", enfe);
            }
            Lpc idLpc = commandHistory.getIdLpc();
            if (idLpc != null) {
                idLpc.getCommandHistoryList().remove(commandHistory);
                idLpc = em.merge(idLpc);
            }
            Sc idSc = commandHistory.getIdSc();
            if (idSc != null) {
                idSc.getCommandHistoryList().remove(commandHistory);
                idSc = em.merge(idSc);
            }
            em.remove(commandHistory);
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

    public List<CommandHistory> findCommandHistoryEntities() {
        return findCommandHistoryEntities(true, -1, -1);
    }

    public List<CommandHistory> findCommandHistoryEntities(int maxResults, int firstResult) {
        return findCommandHistoryEntities(false, maxResults, firstResult);
    }

    private List<CommandHistory> findCommandHistoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommandHistory.class));
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

    public CommandHistory findCommandHistory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommandHistory.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandHistoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommandHistory> rt = cq.from(CommandHistory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
