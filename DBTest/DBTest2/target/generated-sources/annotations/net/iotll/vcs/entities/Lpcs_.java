package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import net.iotll.vcs.entities.Addresses;
import net.iotll.vcs.entities.CommandHist;
import net.iotll.vcs.entities.LpcsHist;
import net.iotll.vcs.entities.Scs;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-21T16:59:13")
@StaticMetamodel(Lpcs.class)
public class Lpcs_ { 

    public static volatile SingularAttribute<Lpcs, Scs> sc;
    public static volatile ListAttribute<Lpcs, CommandHist> commandHistList;
    public static volatile SingularAttribute<Lpcs, Integer> idLPC;
    public static volatile SingularAttribute<Lpcs, Double> current;
    public static volatile SingularAttribute<Lpcs, Addresses> address;
    public static volatile SingularAttribute<Lpcs, Double> temperature;
    public static volatile SingularAttribute<Lpcs, Boolean> swicth;
    public static volatile SingularAttribute<Lpcs, Integer> luminosity;
    public static volatile SingularAttribute<Lpcs, Integer> dimmer;
    public static volatile ListAttribute<Lpcs, LpcsHist> lpcsHistList;

}