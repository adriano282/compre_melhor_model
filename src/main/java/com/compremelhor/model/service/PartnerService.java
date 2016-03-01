package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Partner;

@Stateless
public class PartnerService {
	
	@Inject	private PartnerDao dao;	
	@Inject private AddressDao addressDao;
	
	public void create(Partner partner) {
		dao.persist(partner);
	}
	
	public Partner edit(Partner partner) {
		return dao.edit(partner);
	}
	
	public Partner find(int id) {
		return dao.find(id);
	}
	
	public void remove(Partner partner) {
		dao.remove(partner);
	}
	
	public void addAddress(Address address) {
		addressDao.persist(address);
	}
	
	public Address editAddress(Address address) {
		return addressDao.edit(address);
	}
	
	public void removeAddress(Address address) {
		addressDao.remove(address);
	}
}
