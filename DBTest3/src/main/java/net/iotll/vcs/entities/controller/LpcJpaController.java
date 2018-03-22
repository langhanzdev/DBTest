/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import net.iotll.vcs.entities.Address;
import net.iotll.vcs.entities.Sc;
import net.iotll.vcs.entities.CommandHistory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.Lpc;
import net.iotll.vcs.entities.LpcHistory;
import net.iotll.vcs.entities.controller.exceptions.IllegalOrphanException;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class LpcJpaController implements Serializable {

    public LpcJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lpc lpc) throws RollbackFailureException, Exception {
        if (lpc.getCommandHistoryList() == null) {
            lpc.setCommandHistoryList(new ArrayList<CommandHistory>());
        }
        if (lpc.getLpcHistoryList() == null) {
            lpc.setLpcHistoryList(new ArrayList<LpcHistory>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Address idAddress = lpc.getIdAddress();
            if (idAddress != null) {
                idAddress = em.getReference(idAddress.getClass(), idAddress.getIdAddress());
                lpc.setIdAddress(idAddress);
            }
            Sc idSc = lpc.getIdSc();
            if (idSc != null) {
                idSc = em.getReference(idSc.getClass(), idSc.getIdSc());
                lpc.setIdSc(idSc);
            }
            List<CommandHistory> attachedCommandHistoryList = new ArrayList<CommandHistory>();
            for (CommandHistory commandHistoryListCommandHistoryToAttach : lpc.getCommandHistoryList()) {
                commandHistoryListCommandHistoryToAttach = em.getReference(commandHistoryListCommandHistoryToAttach.getClass(), commandHistoryListCommandHistoryToAttach.getIdCommandHistory());
                attachedCommandHistoryList.add(commandHistoryListCommandHistoryToAttach);
            }
            lpc.setCommandHistoryList(attachedCommandHistoryList);
            List<LpcHistory> attachedLpcHistoryList = new ArrayList<LpcHistory>();
            for (LpcHistory lpcHistoryListLpcHistoryToAttach : lpc.getLpcHistoryList()) {
                lpcHistoryListLpcHistoryToAttach = em.getReference(lpcHistoryListLpcHistoryToAttach.getClass(), lpcHistoryListLpcHistoryToAttach.getIdLpcHistory());
                attachedLpcHistoryList.add(lpcHistoryListLpcHistoryToAttach);
            }
            lpc.setLpcHistoryList(attachedLpcHistoryList);
            em.persist(lpc);
            if (idAddress != null) {
                idAddress.getLpcList().add(lpc);
                idAddress = em.merge(idAddress);
            }
            if (idSc != null) {
                idSc.getLpcList().add(lpc);
                idSc = em.merge(idSc);
            }
            for (CommandHistory commandHistoryListCommandHistory : lpc.getCommandHistoryList()) {
                Lpc oldIdLpcOfCommandHistoryListCommandHistory = commandHistoryListCommandHistory.getIdLpc();
                commandHistoryListCommandHistory.setIdLpc(lpc);
                commandHistoryListCommandHistory = em.merge(commandHistoryListCommandHistory);
                if (oldIdLpcOfCommandHistoryListCommandHistory != null) {
                    oldIdLpcOfCommandHistoryListCommandHistory.getCommandHistoryList().remove(commandHistoryListCommandHistory);
                    oldIdLpcOfCommandHistoryListCommandHistory = em.merge(oldIdLpcOfCommandHistoryListCommandHistory);
                }
            }
            for (LpcHistory lpcHistoryListLpcHistory : lpc.getLpcHistoryList()) {
                Lpc oldIdLpcOfLpcHistoryListLpcHistory = lpcHistoryListLpcHistory.getIdLpc();
                lpcHistoryListLpcHistory.setIdLpc(lpc);
                lpcHistoryListLpcHistory = em.merge(lpcHistoryListLpcHistory);
                if (oldIdLpcOfLpcHistoryListLpcHistory != null) {
                    oldIdLpcOfLpcHistoryListLpcHistory.getLpcHistoryList().remove(lpcHistoryListLpcHistory);
                    oldIdLpcOfLpcHistoryListLpcHistory = em.merge(oldIdLpcOfLpcHistoryListLpcHistory);
                }
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

    public void edit(Lpc lpc) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpc persistentLpc = em.find(Lpc.class, lpc.getIdLpc());
            Address idAddressOld = persistentLpc.getIdAddress();
            Address idAddressNew = lpc.getIdAddress();
            Sc idScOld = persistentLpc.getIdSc();
            Sc idScNew = lpc.getIdSc();
            List<CommandHistory> commandHistoryListOld = persistentLpc.getCommandHistoryList();
            List<CommandHistory> commandHistoryListNew = lpc.getCommandHistoryList();
            List<LpcHistory> lpcHistoryListOld = persistentLpc.getLpcHistoryList();
            List<LpcHistory> lpcHistoryListNew = lpc.getLpcHistoryList();
            List<String> illegalOrphanMessages = null;
            for (LpcHistory lpcHistoryListOldLpcHistory : lpcHistoryListOld) {
                if (!lpcHistoryListNew.contains(lpcHistoryListOldLpcHistory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LpcHistory " + lpcHistoryListOldLpcHistory + " since its idLpc field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAddressNew != null) {
                idAddressNew = em.getReference(idAddressNew.getClass(), idAddressNew.getIdAddress());
                lpc.setIdAddress(idAddressNew);
            }
            if (idScNew != null) {
                idScNew = em.getReference(idScNew.getClass(), idScNew.getIdSc());
                lpc.setIdSc(idScNew);
            }
            List<CommandHistory> attachedCommandHistoryListNew = new ArrayList<CommandHistory>();
            for (CommandHistory commandHistoryListNewCommandHistoryToAttach : commandHistoryListNew) {
                commandHistoryListNewCommandHistoryToAttach = em.getReference(commandHistoryListNewCommandHistoryToAttach.getClass(), commandHistoryListNewCommandHistoryToAttach.getIdCommandHistory());
                attachedCommandHistoryListNew.add(commandHistoryListNewCommandHistoryToAttach);
            }
            commandHistoryListNew = attachedCommandHistoryListNew;
            lpc.setCommandHistoryList(commandHistoryListNew);
            List<LpcHistory> attachedLpcHistoryListNew = new ArrayList<LpcHistory>();
            for (LpcHistory lpcHistoryListNewLpcHistoryToAttach : lpcHistoryListNew) {
                lpcHistoryListNewLpcHistoryToAttach = em.getReference(lpcHistoryListNewLpcHistoryToAttach.getClass(), lpcHistoryListNewLpcHistoryToAttach.getIdLpcHistory());
                attachedLpcHistoryListNew.add(lpcHistoryListNewLpcHistoryToAttach);
            }
            lpcHistoryListNew = attachedLpcHistoryListNew;
            lpc.setLpcHistoryList(lpcHistoryListNew);
            lpc = em.merge(lpc);
            if (idAddressOld != null && !idAddressOld.equals(idAddressNew)) {
                idAddressOld.getLpcList().remove(lpc);
                idAddressOld = em.merge(idAddressOld);
            }
            if (idAddressNew != null && !idAddressNew.equals(idAddressOld)) {
                idAddressNew.getLpcList().add(lpc);
                idAddressNew = em.merge(idAddressNew);
            }
            if (idScOld != null && !idScOld.equals(idScNew)) {
                idScOld.getLpcList().remove(lpc);
                idScOld = em.merge(idScOld);
            }
            if (idScNew != null && !idScNew.equals(idScOld)) {
                idScNew.getLpcList().add(lpc);
                idScNew = em.merge(idScNew);
            }
            for (CommandHistory commandHistoryListOldCommandHistory : commandHistoryListOld) {
                if (!commandHistoryListNew.contains(commandHistoryListOldCommandHistory)) {
                    commandHistoryListOldCommandHistory.setIdLpc(null);
                    commandHistoryListOldCommandHistory = em.merge(commandHistoryListOldCommandHistory);
                }
            }
            for (CommandHistory commandHistoryListNewCommandHistory : commandHistoryListNew) {
                if (!commandHistoryListOld.contains(commandHistoryListNewCommandHistory)) {
                    Lpc oldIdLpcOfCommandHistoryListNewCommandHistory = commandHistoryListNewCommandHistory.getIdLpc();
                    commandHistoryListNewCommandHistory.setIdLpc(lpc);
                    commandHistoryListNewCommandHistory = em.merge(commandHistoryListNewCommandHistory);
                    if (oldIdLpcOfCommandHistoryListNewCommandHistory != null && !oldIdLpcOfCommandHistoryListNewCommandHistory.equals(lpc)) {
                        oldIdLpcOfCommandHistoryListNewCommandHistory.getCommandHistoryList().remove(commandHistoryListNewCommandHistory);
                        oldIdLpcOfCommandHistoryListNewCommandHistory = em.merge(oldIdLpcOfCommandHistoryListNewCommandHistory);
                    }
                }
            }
            for (LpcHistory lpcHistoryListNewLpcHistory : lpcHistoryListNew) {
                if (!lpcHistoryListOld.contains(lpcHistoryListNewLpcHistory)) {
                    Lpc oldIdLpcOfLpcHistoryListNewLpcHistory = lpcHistoryListNewLpcHistory.getIdLpc();
                    lpcHistoryListNewLpcHistory.setIdLpc(lpc);
                    lpcHistoryListNewLpcHistory = em.merge(lpcHistoryListNewLpcHistory);
                    if (oldIdLpcOfLpcHistoryListNewLpcHistory != null && !oldIdLpcOfLpcHistoryListNewLpcHistory.equals(lpc)) {
                        oldIdLpcOfLpcHistoryListNewLpcHistory.getLpcHistoryList().remove(lpcHistoryListNewLpcHistory);
                        oldIdLpcOfLpcHistoryListNewLpcHistory = em.merge(oldIdLpcOfLpcHistoryListNewLpcHistory);
                    }
                }
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
                Integer id = lpc.getIdLpc();
                if (findLpc(id) == null) {
                    throw new NonexistentEntityException("The lpc with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpc lpc;
            try {
                lpc = em.getReference(Lpc.class, id);
                lpc.getIdLpc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lpc with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LpcHistory> lpcHistoryListOrphanCheck = lpc.getLpcHistoryList();
            for (LpcHistory lpcHistoryListOrphanCheckLpcHistory : lpcHistoryListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Lpc (" + lpc + ") cannot be destroyed since the LpcHistory " + lpcHistoryListOrphanCheckLpcHistory + " in its lpcHistoryList field has a non-nullable idLpc field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Address idAddress = lpc.getIdAddress();
            if (idAddress != null) {
                idAddress.getLpcList().remove(lpc);
                idAddress = em.merge(idAddress);
            }
            Sc idSc = lpc.getIdSc();
            if (idSc != null) {
                idSc.getLpcList().remove(lpc);
                idSc = em.merge(idSc);
            }
            List<CommandHistory> commandHistoryList = lpc.getCommandHistoryList();
            for (CommandHistory commandHistoryListCommandHistory : commandHistoryList) {
                commandHistoryListCommandHistory.setIdLpc(null);
                commandHistoryListCommandHistory = em.merge(commandHistoryListCommandHistory);
            }
            em.remove(lpc);
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

    public List<Lpc> findLpcEntities() {
        return findLpcEntities(true, -1, -1);
    }

    public List<Lpc> findLpcEntities(int maxResults, int firstResult) {
        return findLpcEntities(false, maxResults, firstResult);
    }

    private List<Lpc> findLpcEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lpc.class));
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

    public Lpc findLpc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lpc.class, id);
        } finally {
            em.close();
        }
    }

    public int getLpcCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lpc> rt = cq.from(Lpc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
