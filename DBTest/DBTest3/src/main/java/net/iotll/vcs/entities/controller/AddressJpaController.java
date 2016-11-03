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
import net.iotll.vcs.entities.Sc;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import net.iotll.vcs.entities.Address;
import net.iotll.vcs.entities.Lpc;
import net.iotll.vcs.entities.controller.exceptions.NonexistentEntityException;
import net.iotll.vcs.entities.controller.exceptions.RollbackFailureException;

/**
 *
 * @author langhanz
 */
public class AddressJpaController implements Serializable {

    public AddressJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) throws RollbackFailureException, Exception {
        if (address.getScList() == null) {
            address.setScList(new ArrayList<Sc>());
        }
        if (address.getLpcList() == null) {
            address.setLpcList(new ArrayList<Lpc>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Sc> attachedScList = new ArrayList<Sc>();
            for (Sc scListScToAttach : address.getScList()) {
                scListScToAttach = em.getReference(scListScToAttach.getClass(), scListScToAttach.getIdSc());
                attachedScList.add(scListScToAttach);
            }
            address.setScList(attachedScList);
            List<Lpc> attachedLpcList = new ArrayList<Lpc>();
            for (Lpc lpcListLpcToAttach : address.getLpcList()) {
                lpcListLpcToAttach = em.getReference(lpcListLpcToAttach.getClass(), lpcListLpcToAttach.getIdLpc());
                attachedLpcList.add(lpcListLpcToAttach);
            }
            address.setLpcList(attachedLpcList);
            em.persist(address);
            for (Sc scListSc : address.getScList()) {
                Address oldIdAddressOfScListSc = scListSc.getIdAddress();
                scListSc.setIdAddress(address);
                scListSc = em.merge(scListSc);
                if (oldIdAddressOfScListSc != null) {
                    oldIdAddressOfScListSc.getScList().remove(scListSc);
                    oldIdAddressOfScListSc = em.merge(oldIdAddressOfScListSc);
                }
            }
            for (Lpc lpcListLpc : address.getLpcList()) {
                Address oldIdAddressOfLpcListLpc = lpcListLpc.getIdAddress();
                lpcListLpc.setIdAddress(address);
                lpcListLpc = em.merge(lpcListLpc);
                if (oldIdAddressOfLpcListLpc != null) {
                    oldIdAddressOfLpcListLpc.getLpcList().remove(lpcListLpc);
                    oldIdAddressOfLpcListLpc = em.merge(oldIdAddressOfLpcListLpc);
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

    public void edit(Address address) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Address persistentAddress = em.find(Address.class, address.getIdAddress());
            List<Sc> scListOld = persistentAddress.getScList();
            List<Sc> scListNew = address.getScList();
            List<Lpc> lpcListOld = persistentAddress.getLpcList();
            List<Lpc> lpcListNew = address.getLpcList();
            List<Sc> attachedScListNew = new ArrayList<Sc>();
            for (Sc scListNewScToAttach : scListNew) {
                scListNewScToAttach = em.getReference(scListNewScToAttach.getClass(), scListNewScToAttach.getIdSc());
                attachedScListNew.add(scListNewScToAttach);
            }
            scListNew = attachedScListNew;
            address.setScList(scListNew);
            List<Lpc> attachedLpcListNew = new ArrayList<Lpc>();
            for (Lpc lpcListNewLpcToAttach : lpcListNew) {
                lpcListNewLpcToAttach = em.getReference(lpcListNewLpcToAttach.getClass(), lpcListNewLpcToAttach.getIdLpc());
                attachedLpcListNew.add(lpcListNewLpcToAttach);
            }
            lpcListNew = attachedLpcListNew;
            address.setLpcList(lpcListNew);
            address = em.merge(address);
            for (Sc scListOldSc : scListOld) {
                if (!scListNew.contains(scListOldSc)) {
                    scListOldSc.setIdAddress(null);
                    scListOldSc = em.merge(scListOldSc);
                }
            }
            for (Sc scListNewSc : scListNew) {
                if (!scListOld.contains(scListNewSc)) {
                    Address oldIdAddressOfScListNewSc = scListNewSc.getIdAddress();
                    scListNewSc.setIdAddress(address);
                    scListNewSc = em.merge(scListNewSc);
                    if (oldIdAddressOfScListNewSc != null && !oldIdAddressOfScListNewSc.equals(address)) {
                        oldIdAddressOfScListNewSc.getScList().remove(scListNewSc);
                        oldIdAddressOfScListNewSc = em.merge(oldIdAddressOfScListNewSc);
                    }
                }
            }
            for (Lpc lpcListOldLpc : lpcListOld) {
                if (!lpcListNew.contains(lpcListOldLpc)) {
                    lpcListOldLpc.setIdAddress(null);
                    lpcListOldLpc = em.merge(lpcListOldLpc);
                }
            }
            for (Lpc lpcListNewLpc : lpcListNew) {
                if (!lpcListOld.contains(lpcListNewLpc)) {
                    Address oldIdAddressOfLpcListNewLpc = lpcListNewLpc.getIdAddress();
                    lpcListNewLpc.setIdAddress(address);
                    lpcListNewLpc = em.merge(lpcListNewLpc);
                    if (oldIdAddressOfLpcListNewLpc != null && !oldIdAddressOfLpcListNewLpc.equals(address)) {
                        oldIdAddressOfLpcListNewLpc.getLpcList().remove(lpcListNewLpc);
                        oldIdAddressOfLpcListNewLpc = em.merge(oldIdAddressOfLpcListNewLpc);
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
                Integer id = address.getIdAddress();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
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
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getIdAddress();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            List<Sc> scList = address.getScList();
            for (Sc scListSc : scList) {
                scListSc.setIdAddress(null);
                scListSc = em.merge(scListSc);
            }
            List<Lpc> lpcList = address.getLpcList();
            for (Lpc lpcListLpc : lpcList) {
                lpcListLpc.setIdAddress(null);
                lpcListLpc = em.merge(lpcListLpc);
            }
            em.remove(address);
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

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
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

    public Address findAddress(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
