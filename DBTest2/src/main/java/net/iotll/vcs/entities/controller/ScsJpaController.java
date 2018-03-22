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
import net.iotll.vcs.entities.CommandHist;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.Scs;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class ScsJpaController implements Serializable {

    public ScsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Scs scs) throws RollbackFailureException, Exception {
        if (scs.getCommandHistList() == null) {
            scs.setCommandHistList(new ArrayList<CommandHist>());
        }
        if (scs.getLpcsList() == null) {
            scs.setLpcsList(new ArrayList<Lpcs>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Addresses idAddress = scs.getIdAddress();
            if (idAddress != null) {
                idAddress = em.getReference(idAddress.getClass(), idAddress.getIdAddress());
                scs.setIdAddress(idAddress);
            }
            List<CommandHist> attachedCommandHistList = new ArrayList<CommandHist>();
            for (CommandHist commandHistListCommandHistToAttach : scs.getCommandHistList()) {
                commandHistListCommandHistToAttach = em.getReference(commandHistListCommandHistToAttach.getClass(), commandHistListCommandHistToAttach.getIdCommandHist());
                attachedCommandHistList.add(commandHistListCommandHistToAttach);
            }
            scs.setCommandHistList(attachedCommandHistList);
            List<Lpcs> attachedLpcsList = new ArrayList<Lpcs>();
            for (Lpcs lpcsListLpcsToAttach : scs.getLpcsList()) {
                lpcsListLpcsToAttach = em.getReference(lpcsListLpcsToAttach.getClass(), lpcsListLpcsToAttach.getIdLPC());
                attachedLpcsList.add(lpcsListLpcsToAttach);
            }
            scs.setLpcsList(attachedLpcsList);
            em.persist(scs);
            if (idAddress != null) {
                idAddress.getScsList().add(scs);
                idAddress = em.merge(idAddress);
            }
            for (CommandHist commandHistListCommandHist : scs.getCommandHistList()) {
                Scs oldScOfCommandHistListCommandHist = commandHistListCommandHist.getSc();
                commandHistListCommandHist.setSc(scs);
                commandHistListCommandHist = em.merge(commandHistListCommandHist);
                if (oldScOfCommandHistListCommandHist != null) {
                    oldScOfCommandHistListCommandHist.getCommandHistList().remove(commandHistListCommandHist);
                    oldScOfCommandHistListCommandHist = em.merge(oldScOfCommandHistListCommandHist);
                }
            }
            for (Lpcs lpcsListLpcs : scs.getLpcsList()) {
                Scs oldScOfLpcsListLpcs = lpcsListLpcs.getSc();
                lpcsListLpcs.setSc(scs);
                lpcsListLpcs = em.merge(lpcsListLpcs);
                if (oldScOfLpcsListLpcs != null) {
                    oldScOfLpcsListLpcs.getLpcsList().remove(lpcsListLpcs);
                    oldScOfLpcsListLpcs = em.merge(oldScOfLpcsListLpcs);
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

    public void edit(Scs scs) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Scs persistentScs = em.find(Scs.class, scs.getIdSegmentController());
            Addresses idAddressOld = persistentScs.getIdAddress();
            Addresses idAddressNew = scs.getIdAddress();
            List<CommandHist> commandHistListOld = persistentScs.getCommandHistList();
            List<CommandHist> commandHistListNew = scs.getCommandHistList();
            List<Lpcs> lpcsListOld = persistentScs.getLpcsList();
            List<Lpcs> lpcsListNew = scs.getLpcsList();
            if (idAddressNew != null) {
                idAddressNew = em.getReference(idAddressNew.getClass(), idAddressNew.getIdAddress());
                scs.setIdAddress(idAddressNew);
            }
            List<CommandHist> attachedCommandHistListNew = new ArrayList<CommandHist>();
            for (CommandHist commandHistListNewCommandHistToAttach : commandHistListNew) {
                commandHistListNewCommandHistToAttach = em.getReference(commandHistListNewCommandHistToAttach.getClass(), commandHistListNewCommandHistToAttach.getIdCommandHist());
                attachedCommandHistListNew.add(commandHistListNewCommandHistToAttach);
            }
            commandHistListNew = attachedCommandHistListNew;
            scs.setCommandHistList(commandHistListNew);
            List<Lpcs> attachedLpcsListNew = new ArrayList<Lpcs>();
            for (Lpcs lpcsListNewLpcsToAttach : lpcsListNew) {
                lpcsListNewLpcsToAttach = em.getReference(lpcsListNewLpcsToAttach.getClass(), lpcsListNewLpcsToAttach.getIdLPC());
                attachedLpcsListNew.add(lpcsListNewLpcsToAttach);
            }
            lpcsListNew = attachedLpcsListNew;
            scs.setLpcsList(lpcsListNew);
            scs = em.merge(scs);
            if (idAddressOld != null && !idAddressOld.equals(idAddressNew)) {
                idAddressOld.getScsList().remove(scs);
                idAddressOld = em.merge(idAddressOld);
            }
            if (idAddressNew != null && !idAddressNew.equals(idAddressOld)) {
                idAddressNew.getScsList().add(scs);
                idAddressNew = em.merge(idAddressNew);
            }
            for (CommandHist commandHistListOldCommandHist : commandHistListOld) {
                if (!commandHistListNew.contains(commandHistListOldCommandHist)) {
                    commandHistListOldCommandHist.setSc(null);
                    commandHistListOldCommandHist = em.merge(commandHistListOldCommandHist);
                }
            }
            for (CommandHist commandHistListNewCommandHist : commandHistListNew) {
                if (!commandHistListOld.contains(commandHistListNewCommandHist)) {
                    Scs oldScOfCommandHistListNewCommandHist = commandHistListNewCommandHist.getSc();
                    commandHistListNewCommandHist.setSc(scs);
                    commandHistListNewCommandHist = em.merge(commandHistListNewCommandHist);
                    if (oldScOfCommandHistListNewCommandHist != null && !oldScOfCommandHistListNewCommandHist.equals(scs)) {
                        oldScOfCommandHistListNewCommandHist.getCommandHistList().remove(commandHistListNewCommandHist);
                        oldScOfCommandHistListNewCommandHist = em.merge(oldScOfCommandHistListNewCommandHist);
                    }
                }
            }
            for (Lpcs lpcsListOldLpcs : lpcsListOld) {
                if (!lpcsListNew.contains(lpcsListOldLpcs)) {
                    lpcsListOldLpcs.setSc(null);
                    lpcsListOldLpcs = em.merge(lpcsListOldLpcs);
                }
            }
            for (Lpcs lpcsListNewLpcs : lpcsListNew) {
                if (!lpcsListOld.contains(lpcsListNewLpcs)) {
                    Scs oldScOfLpcsListNewLpcs = lpcsListNewLpcs.getSc();
                    lpcsListNewLpcs.setSc(scs);
                    lpcsListNewLpcs = em.merge(lpcsListNewLpcs);
                    if (oldScOfLpcsListNewLpcs != null && !oldScOfLpcsListNewLpcs.equals(scs)) {
                        oldScOfLpcsListNewLpcs.getLpcsList().remove(lpcsListNewLpcs);
                        oldScOfLpcsListNewLpcs = em.merge(oldScOfLpcsListNewLpcs);
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
                Integer id = scs.getIdSegmentController();
                if (findScs(id) == null) {
                    throw new NonexistentEntityException("The scs with id " + id + " no longer exists.");
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
            Scs scs;
            try {
                scs = em.getReference(Scs.class, id);
                scs.getIdSegmentController();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The scs with id " + id + " no longer exists.", enfe);
            }
            Addresses idAddress = scs.getIdAddress();
            if (idAddress != null) {
                idAddress.getScsList().remove(scs);
                idAddress = em.merge(idAddress);
            }
            List<CommandHist> commandHistList = scs.getCommandHistList();
            for (CommandHist commandHistListCommandHist : commandHistList) {
                commandHistListCommandHist.setSc(null);
                commandHistListCommandHist = em.merge(commandHistListCommandHist);
            }
            List<Lpcs> lpcsList = scs.getLpcsList();
            for (Lpcs lpcsListLpcs : lpcsList) {
                lpcsListLpcs.setSc(null);
                lpcsListLpcs = em.merge(lpcsListLpcs);
            }
            em.remove(scs);
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

    public List<Scs> findScsEntities() {
        return findScsEntities(true, -1, -1);
    }

    public List<Scs> findScsEntities(int maxResults, int firstResult) {
        return findScsEntities(false, maxResults, firstResult);
    }

    private List<Scs> findScsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Scs.class));
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

    public Scs findScs(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Scs.class, id);
        } finally {
            em.close();
        }
    }

    public int getScsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Scs> rt = cq.from(Scs.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
