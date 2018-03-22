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
import net.iotll.vcs.entities.CommandHistory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.Lpc;
import net.iotll.vcs.entities.Sc;
import net.iotll.vcs.entities.controller.exceptions.IllegalOrphanException;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class ScJpaController implements Serializable {

    public ScJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sc sc) throws RollbackFailureException, Exception {
        if (sc.getCommandHistoryList() == null) {
            sc.setCommandHistoryList(new ArrayList<CommandHistory>());
        }
        if (sc.getLpcList() == null) {
            sc.setLpcList(new ArrayList<Lpc>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Address idAddress = sc.getIdAddress();
            if (idAddress != null) {
                idAddress = em.getReference(idAddress.getClass(), idAddress.getIdAddress());
                sc.setIdAddress(idAddress);
            }
            List<CommandHistory> attachedCommandHistoryList = new ArrayList<CommandHistory>();
            for (CommandHistory commandHistoryListCommandHistoryToAttach : sc.getCommandHistoryList()) {
                commandHistoryListCommandHistoryToAttach = em.getReference(commandHistoryListCommandHistoryToAttach.getClass(), commandHistoryListCommandHistoryToAttach.getIdCommandHistory());
                attachedCommandHistoryList.add(commandHistoryListCommandHistoryToAttach);
            }
            sc.setCommandHistoryList(attachedCommandHistoryList);
            List<Lpc> attachedLpcList = new ArrayList<Lpc>();
            for (Lpc lpcListLpcToAttach : sc.getLpcList()) {
                lpcListLpcToAttach = em.getReference(lpcListLpcToAttach.getClass(), lpcListLpcToAttach.getIdLpc());
                attachedLpcList.add(lpcListLpcToAttach);
            }
            sc.setLpcList(attachedLpcList);
            em.persist(sc);
            if (idAddress != null) {
                idAddress.getScList().add(sc);
                idAddress = em.merge(idAddress);
            }
            for (CommandHistory commandHistoryListCommandHistory : sc.getCommandHistoryList()) {
                Sc oldIdScOfCommandHistoryListCommandHistory = commandHistoryListCommandHistory.getIdSc();
                commandHistoryListCommandHistory.setIdSc(sc);
                commandHistoryListCommandHistory = em.merge(commandHistoryListCommandHistory);
                if (oldIdScOfCommandHistoryListCommandHistory != null) {
                    oldIdScOfCommandHistoryListCommandHistory.getCommandHistoryList().remove(commandHistoryListCommandHistory);
                    oldIdScOfCommandHistoryListCommandHistory = em.merge(oldIdScOfCommandHistoryListCommandHistory);
                }
            }
            for (Lpc lpcListLpc : sc.getLpcList()) {
                Sc oldIdScOfLpcListLpc = lpcListLpc.getIdSc();
                lpcListLpc.setIdSc(sc);
                lpcListLpc = em.merge(lpcListLpc);
                if (oldIdScOfLpcListLpc != null) {
                    oldIdScOfLpcListLpc.getLpcList().remove(lpcListLpc);
                    oldIdScOfLpcListLpc = em.merge(oldIdScOfLpcListLpc);
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

    public void edit(Sc sc) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sc persistentSc = em.find(Sc.class, sc.getIdSc());
            Address idAddressOld = persistentSc.getIdAddress();
            Address idAddressNew = sc.getIdAddress();
            List<CommandHistory> commandHistoryListOld = persistentSc.getCommandHistoryList();
            List<CommandHistory> commandHistoryListNew = sc.getCommandHistoryList();
            List<Lpc> lpcListOld = persistentSc.getLpcList();
            List<Lpc> lpcListNew = sc.getLpcList();
            List<String> illegalOrphanMessages = null;
            for (Lpc lpcListOldLpc : lpcListOld) {
                if (!lpcListNew.contains(lpcListOldLpc)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Lpc " + lpcListOldLpc + " since its idSc field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAddressNew != null) {
                idAddressNew = em.getReference(idAddressNew.getClass(), idAddressNew.getIdAddress());
                sc.setIdAddress(idAddressNew);
            }
            List<CommandHistory> attachedCommandHistoryListNew = new ArrayList<CommandHistory>();
            for (CommandHistory commandHistoryListNewCommandHistoryToAttach : commandHistoryListNew) {
                commandHistoryListNewCommandHistoryToAttach = em.getReference(commandHistoryListNewCommandHistoryToAttach.getClass(), commandHistoryListNewCommandHistoryToAttach.getIdCommandHistory());
                attachedCommandHistoryListNew.add(commandHistoryListNewCommandHistoryToAttach);
            }
            commandHistoryListNew = attachedCommandHistoryListNew;
            sc.setCommandHistoryList(commandHistoryListNew);
            List<Lpc> attachedLpcListNew = new ArrayList<Lpc>();
            for (Lpc lpcListNewLpcToAttach : lpcListNew) {
                lpcListNewLpcToAttach = em.getReference(lpcListNewLpcToAttach.getClass(), lpcListNewLpcToAttach.getIdLpc());
                attachedLpcListNew.add(lpcListNewLpcToAttach);
            }
            lpcListNew = attachedLpcListNew;
            sc.setLpcList(lpcListNew);
            sc = em.merge(sc);
            if (idAddressOld != null && !idAddressOld.equals(idAddressNew)) {
                idAddressOld.getScList().remove(sc);
                idAddressOld = em.merge(idAddressOld);
            }
            if (idAddressNew != null && !idAddressNew.equals(idAddressOld)) {
                idAddressNew.getScList().add(sc);
                idAddressNew = em.merge(idAddressNew);
            }
            for (CommandHistory commandHistoryListOldCommandHistory : commandHistoryListOld) {
                if (!commandHistoryListNew.contains(commandHistoryListOldCommandHistory)) {
                    commandHistoryListOldCommandHistory.setIdSc(null);
                    commandHistoryListOldCommandHistory = em.merge(commandHistoryListOldCommandHistory);
                }
            }
            for (CommandHistory commandHistoryListNewCommandHistory : commandHistoryListNew) {
                if (!commandHistoryListOld.contains(commandHistoryListNewCommandHistory)) {
                    Sc oldIdScOfCommandHistoryListNewCommandHistory = commandHistoryListNewCommandHistory.getIdSc();
                    commandHistoryListNewCommandHistory.setIdSc(sc);
                    commandHistoryListNewCommandHistory = em.merge(commandHistoryListNewCommandHistory);
                    if (oldIdScOfCommandHistoryListNewCommandHistory != null && !oldIdScOfCommandHistoryListNewCommandHistory.equals(sc)) {
                        oldIdScOfCommandHistoryListNewCommandHistory.getCommandHistoryList().remove(commandHistoryListNewCommandHistory);
                        oldIdScOfCommandHistoryListNewCommandHistory = em.merge(oldIdScOfCommandHistoryListNewCommandHistory);
                    }
                }
            }
            for (Lpc lpcListNewLpc : lpcListNew) {
                if (!lpcListOld.contains(lpcListNewLpc)) {
                    Sc oldIdScOfLpcListNewLpc = lpcListNewLpc.getIdSc();
                    lpcListNewLpc.setIdSc(sc);
                    lpcListNewLpc = em.merge(lpcListNewLpc);
                    if (oldIdScOfLpcListNewLpc != null && !oldIdScOfLpcListNewLpc.equals(sc)) {
                        oldIdScOfLpcListNewLpc.getLpcList().remove(lpcListNewLpc);
                        oldIdScOfLpcListNewLpc = em.merge(oldIdScOfLpcListNewLpc);
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
                Integer id = sc.getIdSc();
                if (findSc(id) == null) {
                    throw new NonexistentEntityException("The sc with id " + id + " no longer exists.");
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
            Sc sc;
            try {
                sc = em.getReference(Sc.class, id);
                sc.getIdSc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sc with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Lpc> lpcListOrphanCheck = sc.getLpcList();
            for (Lpc lpcListOrphanCheckLpc : lpcListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sc (" + sc + ") cannot be destroyed since the Lpc " + lpcListOrphanCheckLpc + " in its lpcList field has a non-nullable idSc field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Address idAddress = sc.getIdAddress();
            if (idAddress != null) {
                idAddress.getScList().remove(sc);
                idAddress = em.merge(idAddress);
            }
            List<CommandHistory> commandHistoryList = sc.getCommandHistoryList();
            for (CommandHistory commandHistoryListCommandHistory : commandHistoryList) {
                commandHistoryListCommandHistory.setIdSc(null);
                commandHistoryListCommandHistory = em.merge(commandHistoryListCommandHistory);
            }
            em.remove(sc);
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

    public List<Sc> findScEntities() {
        return findScEntities(true, -1, -1);
    }

    public List<Sc> findScEntities(int maxResults, int firstResult) {
        return findScEntities(false, maxResults, firstResult);
    }

    private List<Sc> findScEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sc.class));
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

    public Sc findSc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sc.class, id);
        } finally {
            em.close();
        }
    }

    public int getScCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sc> rt = cq.from(Sc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
