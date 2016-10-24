/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entites.persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.iotll.vcs.entities.LpcsHist;

/**
 *
 * @author langhanz
 */
@Stateless
public class LpcsHistFacade extends AbstractFacade<LpcsHist> {

    @PersistenceContext(unitName = "com.mycompany_DBTest2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LpcsHistFacade() {
        super(LpcsHist.class);
    }
    
}
