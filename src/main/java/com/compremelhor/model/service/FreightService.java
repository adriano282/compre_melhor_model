package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.FreightDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;

@Stateless
public class FreightService {
	@Inject	private FreightDao dao;
	@Inject private Validator validator;
	
	public void create(Freight freight) {
		validator.validate(freight);
		dao.persist(freight);
	}
	
	public Freight edit(Freight freight) {
		validator.validate(freight);
		return dao.edit(freight);
	}
	
	public void remove(Freight freight) {
		dao.remove(freight);
	}
	
	public Freight find(int id) {
		return dao.find(id);
	}
	
	public Freight findFreightByPurchase(Purchase purchase) {
		return dao.findFreightByPurchase(purchase);
	}
	
}
