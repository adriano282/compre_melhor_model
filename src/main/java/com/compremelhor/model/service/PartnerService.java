package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.PartnerNotFoundException;

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
	
	public void addAddress(int partnerId, Address address) {
		Partner p = dao.find(partnerId);
		
		if (p == null) {
			throw new PartnerNotFoundException(partnerId);
		}
		address.setPartner(p);
		p.getAddresses().add(address);
		edit(p);
	}
	
	public Address editAddress(Address address) {
		return addressDao.edit(address);
	}
	
	public void removeAddress(Address address) {
		addressDao.remove(address);
	}
	
	public Partner findByAttribute(String attributeName, String attributeValue) {
		return dao.findByAttribute(attributeName, attributeValue);
	}
}
