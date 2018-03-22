/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities.persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.iotll.vcs.entities.ReceivedMessageHistory;

/**
 *
 * @author langhanz
 */
@Stateless
public class ReceivedMessageHistoryFacade extends AbstractFacade<ReceivedMessageHistory> {

    @PersistenceContext(unitName = "net.iotll.vcs_DBTest4_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReceivedMessageHistoryFacade() {
        super(ReceivedMessageHistory.class);
    }
    
}
