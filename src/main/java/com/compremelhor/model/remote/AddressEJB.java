package com.compremelhor.model.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.util.NotImplementedException;

import com.compremelhor.model.entity.Address;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.AddressService;

@Stateless
@Remote(EJBRemote.class)
public class AddressEJB implements EJBRemote<Address>{

	@Inject private AddressService service;
	
	@Override
	public Address get(int id) { return service.find(id); }

	@Override
	public Address edit(Address o) throws InvalidEntityException { return service.edit(o); }

	@Override
	public void delete(Address o) { service.remove(o); }

	@Override
	public Address create(Address o) throws InvalidEntityException {
		service.create(o);
		return service.find(o.getId());
	}

	@Override
	public Address get(int id, Set<String> feches) { throw new NotImplementedException(); }
	
	@Override
	public List<Address> getAll() {
		throw new NotImplementedException();
	}

	@Override
	public List<Address> getAll(Set<String> feches) {
		throw new NotImplementedException();
	}

	@Override
	public List<Address> getAll(int start, int size) {
		throw new NotImplementedException();
	}

	@Override
	public List<Address> getAll(int start, int size, Set<String> feches) {
		throw new NotImplementedException();
	}

}
