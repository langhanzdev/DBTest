package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import net.iotll.vcs.entities.Lpcs;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-25T13:01:44")
@StaticMetamodel(LpcsHist.class)
public class LpcsHist_ { 

    public static volatile SingularAttribute<LpcsHist, Integer> idLpcHist;
    public static volatile SingularAttribute<LpcsHist, Double> current;
    public static volatile SingularAttribute<LpcsHist, Boolean> switch1;
    public static volatile SingularAttribute<LpcsHist, Double> temperature;
    public static volatile SingularAttribute<LpcsHist, String> luminosity;
    public static volatile SingularAttribute<LpcsHist, Integer> dimmer;
    public static volatile SingularAttribute<LpcsHist, Lpcs> lpc;

}