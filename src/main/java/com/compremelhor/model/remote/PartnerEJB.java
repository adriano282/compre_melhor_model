package com.compremelhor.model.remote;

import java.util.List;

import javax.inject.Inject;

import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.PartnerService;

public class PartnerEJB implements EJBRemote<Partner>{

	@Inject private PartnerService partnerService;
	
	@Override
	public Partner get(int id) {
		return partnerService.find(id);
	}

	@Override
	public Partner edit(Partner o) throws InvalidEntityException {
		try {
			return partnerService.edit(o);
		} catch(Exception e) {
			throw new InvalidEntityException(e.getMessage());
		}
	}

	@Override
	public void delete(Partner o) {
		partnerService.remove(o);		
	}

	@Override
	public Partner create(Partner o) throws InvalidEntityException {
		try {
			partnerService.create(o);
		} catch (Exception e) {
			throw new InvalidEntityException(e.getMessage());
		}
		return partnerService.find(o.getId());
	}

	@Override
	public List<Partner> getAll() {
		return partnerService.findAll();
	}

}
