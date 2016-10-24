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
import net.iotll.vcs.entities.Addresses;
import net.iotll.vcs.entities.Scs;
import net.iotll.vcs.entities.CommandHist;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.LpcsHist;
import net.iotll.vcs.entities.controller.exceptions.IllegalOrphanException;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class LpcsJpaController implements Serializable {

    public LpcsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lpcs lpcs) throws RollbackFailureException, Exception {
        if (lpcs.getCommandHistList() == null) {
            lpcs.setCommandHistList(new ArrayList<CommandHist>());
        }
        if (lpcs.getLpcsHistList() == null) {
            lpcs.setLpcsHistList(new ArrayList<LpcsHist>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Addresses address = lpcs.getAddress();
            if (address != null) {
                address = em.getReference(address.getClass(), address.getIdAddress());
                lpcs.setAddress(address);
            }
            Scs sc = lpcs.getSc();
            if (sc != null) {
                sc = em.getReference(sc.getClass(), sc.getIdSegmentController());
                lpcs.setSc(sc);
            }
            List<CommandHist> attachedCommandHistList = new ArrayList<CommandHist>();
            for (CommandHist commandHistListCommandHistToAttach : lpcs.getCommandHistList()) {
                commandHistListCommandHistToAttach = em.getReference(commandHistListCommandHistToAttach.getClass(), commandHistListCommandHistToAttach.getIdCommandHist());
                attachedCommandHistList.add(commandHistListCommandHistToAttach);
            }
            lpcs.setCommandHistList(attachedCommandHistList);
            List<LpcsHist> attachedLpcsHistList = new ArrayList<LpcsHist>();
            for (LpcsHist lpcsHistListLpcsHistToAttach : lpcs.getLpcsHistList()) {
                lpcsHistListLpcsHistToAttach = em.getReference(lpcsHistListLpcsHistToAttach.getClass(), lpcsHistListLpcsHistToAttach.getIdLpcHist());
                attachedLpcsHistList.add(lpcsHistListLpcsHistToAttach);
            }
            lpcs.setLpcsHistList(attachedLpcsHistList);
            em.persist(lpcs);
            if (address != null) {
                address.getLpcsList().add(lpcs);
                address = em.merge(address);
            }
            if (sc != null) {
                sc.getLpcsList().add(lpcs);
                sc = em.merge(sc);
            }
            for (CommandHist commandHistListCommandHist : lpcs.getCommandHistList()) {
                Lpcs oldLpcOfCommandHistListCommandHist = commandHistListCommandHist.getLpc();
                commandHistListCommandHist.setLpc(lpcs);
                commandHistListCommandHist = em.merge(commandHistListCommandHist);
                if (oldLpcOfCommandHistListCommandHist != null) {
                    oldLpcOfCommandHistListCommandHist.getCommandHistList().remove(commandHistListCommandHist);
                    oldLpcOfCommandHistListCommandHist = em.merge(oldLpcOfCommandHistListCommandHist);
                }
            }
            for (LpcsHist lpcsHistListLpcsHist : lpcs.getLpcsHistList()) {
                Lpcs oldLpcOfLpcsHistListLpcsHist = lpcsHistListLpcsHist.getLpc();
                lpcsHistListLpcsHist.setLpc(lpcs);
                lpcsHistListLpcsHist = em.merge(lpcsHistListLpcsHist);
                if (oldLpcOfLpcsHistListLpcsHist != null) {
                    oldLpcOfLpcsHistListLpcsHist.getLpcsHistList().remove(lpcsHistListLpcsHist);
                    oldLpcOfLpcsHistListLpcsHist = em.merge(oldLpcOfLpcsHistListLpcsHist);
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

    public void edit(Lpcs lpcs) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lpcs persistentLpcs = em.find(Lpcs.class, lpcs.getIdLPC());
            Addresses addressOld = persistentLpcs.getAddress();
            Addresses addressNew = lpcs.getAddress();
            Scs scOld = persistentLpcs.getSc();
            Scs scNew = lpcs.getSc();
            List<CommandHist> commandHistListOld = persistentLpcs.getCommandHistList();
            List<CommandHist> commandHistListNew = lpcs.getCommandHistList();
            List<LpcsHist> lpcsHistListOld = persistentLpcs.getLpcsHistList();
            List<LpcsHist> lpcsHistListNew = lpcs.getLpcsHistList();
            List<String> illegalOrphanMessages = null;
            for (LpcsHist lpcsHistListOldLpcsHist : lpcsHistListOld) {
                if (!lpcsHistListNew.contains(lpcsHistListOldLpcsHist)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LpcsHist " + lpcsHistListOldLpcsHist + " since its lpc field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (addressNew != null) {
                addressNew = em.getReference(addressNew.getClass(), addressNew.getIdAddress());
                lpcs.setAddress(addressNew);
            }
            if (scNew != null) {
                scNew = em.getReference(scNew.getClass(), scNew.getIdSegmentController());
                lpcs.setSc(scNew);
            }
            List<CommandHist> attachedCommandHistListNew = new ArrayList<CommandHist>();
            for (CommandHist commandHistListNewCommandHistToAttach : commandHistListNew) {
                commandHistListNewCommandHistToAttach = em.getReference(commandHistListNewCommandHistToAttach.getClass(), commandHistListNewCommandHistToAttach.getIdCommandHist());
                attachedCommandHistListNew.add(commandHistListNewCommandHistToAttach);
            }
            commandHistListNew = attachedCommandHistListNew;
            lpcs.setCommandHistList(commandHistListNew);
            List<LpcsHist> attachedLpcsHistListNew = new ArrayList<LpcsHist>();
            for (LpcsHist lpcsHistListNewLpcsHistToAttach : lpcsHistListNew) {
                lpcsHistListNewLpcsHistToAttach = em.getReference(lpcsHistListNewLpcsHistToAttach.getClass(), lpcsHistListNewLpcsHistToAttach.getIdLpcHist());
                attachedLpcsHistListNew.add(lpcsHistListNewLpcsHistToAttach);
            }
            lpcsHistListNew = attachedLpcsHistListNew;
            lpcs.setLpcsHistList(lpcsHistListNew);
            lpcs = em.merge(lpcs);
            if (addressOld != null && !addressOld.equals(addressNew)) {
                addressOld.getLpcsList().remove(lpcs);
                addressOld = em.merge(addressOld);
            }
            if (addressNew != null && !addressNew.equals(addressOld)) {
                addressNew.getLpcsList().add(lpcs);
                addressNew = em.merge(addressNew);
            }
            if (scOld != null && !scOld.equals(scNew)) {
                scOld.getLpcsList().remove(lpcs);
                scOld = em.merge(scOld);
            }
            if (scNew != null && !scNew.equals(scOld)) {
                scNew.getLpcsList().add(lpcs);
                scNew = em.merge(scNew);
            }
            for (CommandHist commandHistListOldCommandHist : commandHistListOld) {
                if (!commandHistListNew.contains(commandHistListOldCommandHist)) {
                    commandHistListOldCommandHist.setLpc(null);
                    commandHistListOldCommandHist = em.merge(commandHistListOldCommandHist);
                }
            }
            for (CommandHist commandHistListNewCommandHist : commandHistListNew) {
                if (!commandHistListOld.contains(commandHistListNewCommandHist)) {
                    Lpcs oldLpcOfCommandHistListNewCommandHist = commandHistListNewCommandHist.getLpc();
                    commandHistListNewCommandHist.setLpc(lpcs);
                    commandHistListNewCommandHist = em.merge(commandHistListNewCommandHist);
                    if (oldLpcOfCommandHistListNewCommandHist != null && !oldLpcOfCommandHistListNewCommandHist.equals(lpcs)) {
                        oldLpcOfCommandHistListNewCommandHist.getCommandHistList().remove(commandHistListNewCommandHist);
                        oldLpcOfCommandHistListNewCommandHist = em.merge(oldLpcOfCommandHistListNewCommandHist);
                    }
                }
            }
            for (LpcsHist lpcsHistListNewLpcsHist : lpcsHistListNew) {
                if (!lpcsHistListOld.contains(lpcsHistListNewLpcsHist)) {
                    Lpcs oldLpcOfLpcsHistListNewLpcsHist = lpcsHistListNewLpcsHist.getLpc();
                    lpcsHistListNewLpcsHist.setLpc(lpcs);
                    lpcsHistListNewLpcsHist = em.merge(lpcsHistListNewLpcsHist);
                    if (oldLpcOfLpcsHistListNewLpcsHist != null && !oldLpcOfLpcsHistListNewLpcsHist.equals(lpcs)) {
                        oldLpcOfLpcsHistListNewLpcsHist.getLpcsHistList().remove(lpcsHistListNewLpcsHist);
                        oldLpcOfLpcsHistListNewLpcsHist = em.merge(oldLpcOfLpcsHistListNewLpcsHist);
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
                Integer id = lpcs.getIdLPC();
                if (findLpcs(id) == null) {
                    throw new NonexistentEntityException("The lpcs with id " + id + " no longer exists.");
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
            Lpcs lpcs;
            try {
                lpcs = em.getReference(Lpcs.class, id);
                lpcs.getIdLPC();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lpcs with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LpcsHist> lpcsHistListOrphanCheck = lpcs.getLpcsHistList();
            for (LpcsHist lpcsHistListOrphanCheckLpcsHist : lpcsHistListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Lpcs (" + lpcs + ") cannot be destroyed since the LpcsHist " + lpcsHistListOrphanCheckLpcsHist + " in its lpcsHistList field has a non-nullable lpc field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Addresses address = lpcs.getAddress();
            if (address != null) {
                address.getLpcsList().remove(lpcs);
                address = em.merge(address);
            }
            Scs sc = lpcs.getSc();
            if (sc != null) {
                sc.getLpcsList().remove(lpcs);
                sc = em.merge(sc);
            }
            List<CommandHist> commandHistList = lpcs.getCommandHistList();
            for (CommandHist commandHistListCommandHist : commandHistList) {
                commandHistListCommandHist.setLpc(null);
                commandHistListCommandHist = em.merge(commandHistListCommandHist);
            }
            em.remove(lpcs);
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

    public List<Lpcs> findLpcsEntities() {
        return findLpcsEntities(true, -1, -1);
    }

    public List<Lpcs> findLpcsEntities(int maxResults, int firstResult) {
        return findLpcsEntities(false, maxResults, firstResult);
    }

    private List<Lpcs> findLpcsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lpcs.class));
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

    public Lpcs findLpcs(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lpcs.class, id);
        } finally {
            em.close();
        }
    }

    public int getLpcsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lpcs> rt = cq.from(Lpcs.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
