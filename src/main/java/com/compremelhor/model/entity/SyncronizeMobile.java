package com.compremelhor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "syncronize_mobile")
public class SyncronizeMobile extends EntityModel {
	private static final long serialVersionUID = 1L;

	@Column(name = "entity_name")
	private String entityName;
	
	@Column(name = "entity_id")
	private int entityId;
	
	@Column(name = "mobile_user_id_ref")
	private int mobileUserIdRef;
	
	@Column(name = "action")
	@Enumerated(EnumType.STRING)
	private Action action;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public int getMobileUserIdRef() {
		return mobileUserIdRef;
	}

	public void setMobileUserIdRef(int mobileUserIdRef) {
		this.mobileUserIdRef = mobileUserIdRef;
	}
	
	public static enum Action {
		REMOVED, EDITED, CREATED;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
