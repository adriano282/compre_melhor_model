package com.compremelhor.model.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.PartnerService;

@Stateless
@Remote(EJBRemote.class)
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

	@Override
	public List<Partner> getAll(int start, int size) {
		return partnerService.findAll(start, size);
	}

	@Override
	public Partner get(int id, Set<String> feches) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Partner> getAll(Set<String> feches) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Partner> getAll(int start, int size, Set<String> feches) {
		// TODO Auto-generated method stub
		return null;
	}
}
