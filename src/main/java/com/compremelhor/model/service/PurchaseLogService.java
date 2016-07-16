package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.PurchaseLogDao;
import com.compremelhor.model.entity.PurchaseLog;

@Stateless
public class PurchaseLogService extends AbstractService<PurchaseLog>{
	private static final long serialVersionUID = 1L;
	
	@Inject private PurchaseLogDao dao;
	
	@Override
	protected void setDao() { super.dao = this.dao; }

	@Override
	protected void setStrategies() {}

}
