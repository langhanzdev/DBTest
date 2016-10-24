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
import net.iotll.vcs.entities.Scs;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.Addresses;
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.controller.exceptions.IllegalOrphanException;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class AddressesJpaController implements Serializable {

    public AddressesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Addresses addresses) throws RollbackFailureException, Exception {
        if (addresses.getScsList() == null) {
            addresses.setScsList(new ArrayList<Scs>());
        }
        if (addresses.getLpcsList() == null) {
            addresses.setLpcsList(new ArrayList<Lpcs>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Scs> attachedScsList = new ArrayList<Scs>();
            for (Scs scsListScsToAttach : addresses.getScsList()) {
                scsListScsToAttach = em.getReference(scsListScsToAttach.getClass(), scsListScsToAttach.getIdSegmentController());
                attachedScsList.add(scsListScsToAttach);
            }
            addresses.setScsList(attachedScsList);
            List<Lpcs> attachedLpcsList = new ArrayList<Lpcs>();
            for (Lpcs lpcsListLpcsToAttach : addresses.getLpcsList()) {
                lpcsListLpcsToAttach = em.getReference(lpcsListLpcsToAttach.getClass(), lpcsListLpcsToAttach.getIdLPC());
                attachedLpcsList.add(lpcsListLpcsToAttach);
            }
            addresses.setLpcsList(attachedLpcsList);
            em.persist(addresses);
            for (Scs scsListScs : addresses.getScsList()) {
                Addresses oldIdAddressOfScsListScs = scsListScs.getIdAddress();
                scsListScs.setIdAddress(addresses);
                scsListScs = em.merge(scsListScs);
                if (oldIdAddressOfScsListScs != null) {
                    oldIdAddressOfScsListScs.getScsList().remove(scsListScs);
                    oldIdAddressOfScsListScs = em.merge(oldIdAddressOfScsListScs);
                }
            }
            for (Lpcs lpcsListLpcs : addresses.getLpcsList()) {
                Addresses oldAddressOfLpcsListLpcs = lpcsListLpcs.getAddress();
                lpcsListLpcs.setAddress(addresses);
                lpcsListLpcs = em.merge(lpcsListLpcs);
                if (oldAddressOfLpcsListLpcs != null) {
                    oldAddressOfLpcsListLpcs.getLpcsList().remove(lpcsListLpcs);
                    oldAddressOfLpcsListLpcs = em.merge(oldAddressOfLpcsListLpcs);
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

    public void edit(Addresses addresses) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Addresses persistentAddresses = em.find(Addresses.class, addresses.getIdAddress());
            List<Scs> scsListOld = persistentAddresses.getScsList();
            List<Scs> scsListNew = addresses.getScsList();
            List<Lpcs> lpcsListOld = persistentAddresses.getLpcsList();
            List<Lpcs> lpcsListNew = addresses.getLpcsList();
            List<String> illegalOrphanMessages = null;
            for (Scs scsListOldScs : scsListOld) {
                if (!scsListNew.contains(scsListOldScs)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Scs " + scsListOldScs + " since its idAddress field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Scs> attachedScsListNew = new ArrayList<Scs>();
            for (Scs scsListNewScsToAttach : scsListNew) {
                scsListNewScsToAttach = em.getReference(scsListNewScsToAttach.getClass(), scsListNewScsToAttach.getIdSegmentController());
                attachedScsListNew.add(scsListNewScsToAttach);
            }
            scsListNew = attachedScsListNew;
            addresses.setScsList(scsListNew);
            List<Lpcs> attachedLpcsListNew = new ArrayList<Lpcs>();
            for (Lpcs lpcsListNewLpcsToAttach : lpcsListNew) {
                lpcsListNewLpcsToAttach = em.getReference(lpcsListNewLpcsToAttach.getClass(), lpcsListNewLpcsToAttach.getIdLPC());
                attachedLpcsListNew.add(lpcsListNewLpcsToAttach);
            }
            lpcsListNew = attachedLpcsListNew;
            addresses.setLpcsList(lpcsListNew);
            addresses = em.merge(addresses);
            for (Scs scsListNewScs : scsListNew) {
                if (!scsListOld.contains(scsListNewScs)) {
                    Addresses oldIdAddressOfScsListNewScs = scsListNewScs.getIdAddress();
                    scsListNewScs.setIdAddress(addresses);
                    scsListNewScs = em.merge(scsListNewScs);
                    if (oldIdAddressOfScsListNewScs != null && !oldIdAddressOfScsListNewScs.equals(addresses)) {
                        oldIdAddressOfScsListNewScs.getScsList().remove(scsListNewScs);
                        oldIdAddressOfScsListNewScs = em.merge(oldIdAddressOfScsListNewScs);
                    }
                }
            }
            for (Lpcs lpcsListOldLpcs : lpcsListOld) {
                if (!lpcsListNew.contains(lpcsListOldLpcs)) {
                    lpcsListOldLpcs.setAddress(null);
                    lpcsListOldLpcs = em.merge(lpcsListOldLpcs);
                }
            }
            for (Lpcs lpcsListNewLpcs : lpcsListNew) {
                if (!lpcsListOld.contains(lpcsListNewLpcs)) {
                    Addresses oldAddressOfLpcsListNewLpcs = lpcsListNewLpcs.getAddress();
                    lpcsListNewLpcs.setAddress(addresses);
                    lpcsListNewLpcs = em.merge(lpcsListNewLpcs);
                    if (oldAddressOfLpcsListNewLpcs != null && !oldAddressOfLpcsListNewLpcs.equals(addresses)) {
                        oldAddressOfLpcsListNewLpcs.getLpcsList().remove(lpcsListNewLpcs);
                        oldAddressOfLpcsListNewLpcs = em.merge(oldAddressOfLpcsListNewLpcs);
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
                Integer id = addresses.getIdAddress();
                if (findAddresses(id) == null) {
                    throw new NonexistentEntityException("The addresses with id " + id + " no longer exists.");
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
            Addresses addresses;
            try {
                addresses = em.getReference(Addresses.class, id);
                addresses.getIdAddress();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The addresses with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Scs> scsListOrphanCheck = addresses.getScsList();
            for (Scs scsListOrphanCheckScs : scsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Addresses (" + addresses + ") cannot be destroyed since the Scs " + scsListOrphanCheckScs + " in its scsList field has a non-nullable idAddress field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Lpcs> lpcsList = addresses.getLpcsList();
            for (Lpcs lpcsListLpcs : lpcsList) {
                lpcsListLpcs.setAddress(null);
                lpcsListLpcs = em.merge(lpcsListLpcs);
            }
            em.remove(addresses);
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

    public List<Addresses> findAddressesEntities() {
        return findAddressesEntities(true, -1, -1);
    }

    public List<Addresses> findAddressesEntities(int maxResults, int firstResult) {
        return findAddressesEntities(false, maxResults, firstResult);
    }

    private List<Addresses> findAddressesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Addresses.class));
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

    public Addresses findAddresses(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Addresses.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Addresses> rt = cq.from(Addresses.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
