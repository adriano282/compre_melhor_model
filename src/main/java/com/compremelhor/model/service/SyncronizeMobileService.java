package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.SyncronizeMobileDao;
import com.compremelhor.model.entity.SyncronizeMobile;

@Stateless
public class SyncronizeMobileService extends AbstractService<SyncronizeMobile>{
	private static final long serialVersionUID = 1L;

	@Inject private SyncronizeMobileDao dao;
	
	@Override
	protected void setDao() { super.dao = this.dao; }

	@Override
	protected void setStrategies() {}

}
