package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Lpc.class)
public abstract class Lpc_ {

	public static volatile SingularAttribute<Lpc, Integer> idLpc;
	public static volatile SingularAttribute<Lpc, Double> current;
	public static volatile ListAttribute<Lpc, CommandHistory> commandHistoryList;
	public static volatile SingularAttribute<Lpc, Boolean> switch1;
	public static volatile SingularAttribute<Lpc, Double> temperature;
	public static volatile SingularAttribute<Lpc, Integer> luminosity;
	public static volatile SingularAttribute<Lpc, Sc> idSc;
	public static volatile ListAttribute<Lpc, LpcHistory> lpcHistoryList;
	public static volatile SingularAttribute<Lpc, Integer> dimmer;
	public static volatile SingularAttribute<Lpc, Address> idAddress;

}

