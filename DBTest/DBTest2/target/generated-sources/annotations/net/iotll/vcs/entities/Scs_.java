package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import net.iotll.vcs.entities.Addresses;
import net.iotll.vcs.entities.CommandHist;
import net.iotll.vcs.entities.Lpcs;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-25T13:01:44")
@StaticMetamodel(Scs.class)
public class Scs_ { 

    public static volatile ListAttribute<Scs, CommandHist> commandHistList;
    public static volatile ListAttribute<Scs, Lpcs> lpcsList;
    public static volatile SingularAttribute<Scs, Integer> idSegmentController;
    public static volatile SingularAttribute<Scs, String> ipSegmentController;
    public static volatile SingularAttribute<Scs, Addresses> idAddress;

}