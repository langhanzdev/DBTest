package net.iotll.vcs.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CommandHistory.class)
public abstract class CommandHistory_ {

	public static volatile SingularAttribute<CommandHistory, Date> dateTime;
	public static volatile SingularAttribute<CommandHistory, Lpc> idLpc;
	public static volatile SingularAttribute<CommandHistory, Integer> idCommandHistory;
	public static volatile SingularAttribute<CommandHistory, Sc> idSc;
	public static volatile SingularAttribute<CommandHistory, String> category;
	public static volatile SingularAttribute<CommandHistory, String> type;
	public static volatile SingularAttribute<CommandHistory, String> message;
	public static volatile SingularAttribute<CommandHistory, String> value;
	public static volatile SingularAttribute<CommandHistory, String> status;

}

