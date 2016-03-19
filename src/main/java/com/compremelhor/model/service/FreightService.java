package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.FreightDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;

public class FreightService extends AbstractService<Freight>{
	
	@Inject	private FreightDao dao;
	
	@Override
	protected void setDao() { super.dao = this.dao; }
	
	public Freight findFreightByPurchase(Purchase purchase) {
		return dao.findFreightByPurchase(purchase);
	}

}
