package jrange.myordy.model;

import java.io.Serializable;

import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.TheUserPhoneType;

public final class ReferenceData implements Serializable {

	private static final long serialVersionUID = 1L;

	private final EntityStatus[] entityStatusList = EntityStatus.values();
	private final TheUserPhoneType[] theUserPhoneTypeList = TheUserPhoneType.values();

	public EntityStatus[] getEntityStatusList() {
		return entityStatusList;
	}

	public TheUserPhoneType[] getTheUserPhoneTypeList() {
		return theUserPhoneTypeList;
	}

	public static final ReferenceData INSTANCE = new ReferenceData();

}
