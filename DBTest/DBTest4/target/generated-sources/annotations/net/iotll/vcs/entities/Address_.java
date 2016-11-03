package net.iotll.vcs.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Address.class)
public abstract class Address_ {

	public static volatile SingularAttribute<Address, String> address;
	public static volatile SingularAttribute<Address, Double> latitude;
	public static volatile SingularAttribute<Address, String> descriptionLocation;
	public static volatile ListAttribute<Address, Sc> scList;
	public static volatile ListAttribute<Address, Lpc> lpcList;
	public static volatile SingularAttribute<Address, Integer> idAddress;
	public static volatile SingularAttribute<Address, Double> longitude;

}

