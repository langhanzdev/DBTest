package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.Scs;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-21T16:59:13")
@StaticMetamodel(Addresses.class)
public class Addresses_ { 

    public static volatile SingularAttribute<Addresses, String> address;
    public static volatile ListAttribute<Addresses, Lpcs> lpcsList;
    public static volatile ListAttribute<Addresses, Scs> scsList;
    public static volatile SingularAttribute<Addresses, Double> latitude;
    public static volatile SingularAttribute<Addresses, String> descriptionLocation;
    public static volatile SingularAttribute<Addresses, Integer> idAddress;
    public static volatile SingularAttribute<Addresses, Double> longitude;

}