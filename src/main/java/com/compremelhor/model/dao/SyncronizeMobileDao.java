package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.SyncronizeMobile;

@Stateless
public class SyncronizeMobileDao extends AbstractDao<SyncronizeMobile>{
	private static final long serialVersionUID = 1L;

	public SyncronizeMobileDao() {
		super(SyncronizeMobile.class);
	}

}
