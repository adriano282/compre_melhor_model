package com.compremelhor.model.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.ManufacturerService;

@Stateless
@Remote(EJBRemote.class)
public class ManufacturerEJB implements EJBRemote<Manufacturer> {

	@Inject private ManufacturerService manufacturerService;
	
	@Override
	public Manufacturer get(int id) {
		return manufacturerService.findManufacturer(id);
	}

	@Override
	public Manufacturer edit(Manufacturer o) throws InvalidEntityException {
		try {
			return manufacturerService.editManufacturer(o);
		} catch (Exception e) {
			throw new InvalidEntityException(e.getMessage());
		}
	}

	@Override
	public void delete(Manufacturer o) {
		manufacturerService.removeManufacturer(o);
	}

	@Override
	public Manufacturer create(Manufacturer o) throws InvalidEntityException {
		try {
			manufacturerService.createManufacturer(o);
		} catch (Exception e) {
			throw new InvalidEntityException(e.getMessage());
		}
		return manufacturerService.findManufacturer(o.getId());			
	}

	@Override
	public List<Manufacturer> getAll() {
		return manufacturerService.getAll();
	}

	@Override
	public List<Manufacturer> getAll(int start, int size) {
		return manufacturerService.getAll(start, size);
	}

	@Override
	public Manufacturer get(int id, Set<String> feches) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Manufacturer> getAll(Set<String> feches) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Manufacturer> getAll(int start, int size, Set<String> feches) {
		// TODO Auto-generated method stub
		return null;
	}
}
