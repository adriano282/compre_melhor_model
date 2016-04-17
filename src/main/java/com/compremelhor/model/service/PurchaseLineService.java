package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.PurchaseLineDao;
import com.compremelhor.model.entity.PurchaseLine;

@Stateless
public class PurchaseLineService extends AbstractService<PurchaseLine>{
	
	@Inject private PurchaseLineDao dao;
	
	@Override
	protected void setDao() {super.dao = this.dao;}
	@Override 
	protected void setStrategies() {}
}
