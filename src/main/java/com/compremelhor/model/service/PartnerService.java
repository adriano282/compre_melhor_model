package com.compremelhor.model.service;

import java.util.Set;

import javax.inject.Inject;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.PartnerNotFoundException;
import com.compremelhor.model.validation.groups.PartnerAddress;

public class PartnerService extends AbstractService<Partner>{
	
	@Inject	private PartnerDao dao;	
	@Inject private AddressDao addressDao;
	
	@Override
	protected void setDao() { super.dao = this.dao; }
	
	public Partner find(int id, Set<String> fetches) {
		return dao.find(id, fetches);
	}
	
	
	public void addAddress(int partnerId, Address address) throws InvalidEntityException {
		Partner p = dao.find(partnerId);
		
		if (p == null) {
			throw new PartnerNotFoundException(partnerId);
		}
		address.setPartner(p);
		validate(address, PartnerAddress.class);
		p.getAddresses().add(address);
		edit(p);
	}
	
	public Address editAddress(Address address) throws InvalidEntityException {
		validate(address, PartnerAddress.class);
		return addressDao.edit(address);
	}
	
	public void removeAddress(Address address) { addressDao.remove(address); }
}
