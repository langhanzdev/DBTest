/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entites.persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.iotll.vcs.entities.CommandHist;

/**
 *
 * @author langhanz
 */
@Stateless
public class CommandHistFacade extends AbstractFacade<CommandHist> {

    @PersistenceContext(unitName = "com.mycompany_DBTest2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommandHistFacade() {
        super(CommandHist.class);
    }
    
}
