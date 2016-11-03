package net.iotll.vcs.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LpcHistory.class)
public abstract class LpcHistory_ {

	public static volatile SingularAttribute<LpcHistory, Date> dateTime;
	public static volatile SingularAttribute<LpcHistory, Double> current;
	public static volatile SingularAttribute<LpcHistory, Lpc> idLpc;
	public static volatile SingularAttribute<LpcHistory, Boolean> switch1;
	public static volatile SingularAttribute<LpcHistory, Double> temperature;
	public static volatile SingularAttribute<LpcHistory, Integer> luminosity;
	public static volatile SingularAttribute<LpcHistory, Integer> idLpcHistory;
	public static volatile SingularAttribute<LpcHistory, Integer> dimmer;

}

