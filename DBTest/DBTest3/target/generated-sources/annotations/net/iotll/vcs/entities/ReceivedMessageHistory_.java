package net.iotll.vcs.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReceivedMessageHistory.class)
public abstract class ReceivedMessageHistory_ {

	public static volatile SingularAttribute<ReceivedMessageHistory, Date> dateTime;
	public static volatile SingularAttribute<ReceivedMessageHistory, Integer> idReceivedMessageHistory;
	public static volatile SingularAttribute<ReceivedMessageHistory, String> message;

}

