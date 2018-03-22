package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Sc.class)
public abstract class Sc_ {

	public static volatile ListAttribute<Sc, CommandHistory> commandHistoryList;
	public static volatile SingularAttribute<Sc, String> ipSc;
	public static volatile SingularAttribute<Sc, Integer> idSc;
	public static volatile ListAttribute<Sc, Lpc> lpcList;
	public static volatile SingularAttribute<Sc, Address> idAddress;

}

