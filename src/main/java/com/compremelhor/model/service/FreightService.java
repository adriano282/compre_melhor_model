package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.FreightDao;
import com.compremelhor.model.entity.Freight;

@Stateless
public class FreightService {
	@Inject
	private FreightDao dao;
	
	public void create(Freight freight) {
		if (freight.getPurchase() == null) {
			throw new RuntimeException("Exception in FreightService.create(Freight): purchase can not be null");
		}		
		dao.persist(freight);
	}
	
	public Freight edit(Freight freight) {
		return dao.edit(freight);
	}
	
	public void remove(Freight freight) {
		dao.remove(freight);
	}
	
	public Freight find(int id) {
		return dao.find(id);
	}
	
}
