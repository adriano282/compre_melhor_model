package com.compremelhor.model.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.PartnerNotFoundException;
import com.compremelhor.model.validation.groups.PartnerAddress;

public class PartnerService {
	
	@Inject	private PartnerDao dao;	
	@Inject private AddressDao addressDao;
	@Inject private Validator validator;
	
	public void create(Partner partner) {
		validate(partner);
		dao.persist(partner);
	}
	
	public Partner edit(Partner partner) {
		validate(partner);
		return dao.edit(partner);
	}
	
	public Partner find(int id) { return dao.find(id); }
	
	public List<Partner> findAll() { return dao.findAll(); }
	
	public void remove(Partner partner) { dao.remove(partner); }
	
	public void addAddress(int partnerId, Address address) {
		Partner p = dao.find(partnerId);
		
		if (p == null) {
			throw new PartnerNotFoundException(partnerId);
		}
		address.setPartner(p);
		validator.validate(address, PartnerAddress.class);
		p.getAddresses().add(address);
		edit(p);
	}
	
	public Address editAddress(Address address) {
		validator.validate(address, PartnerAddress.class);
		return addressDao.edit(address);
	}
	
	public void removeAddress(Address address) { addressDao.remove(address); }
		
	private void validate(Partner partner) {
		validator.validate(partner);
		partner.getAddresses()
			.stream()
			.forEach(ad -> {
				validator.validate(partner, PartnerAddress.class);
			});
	}
}
