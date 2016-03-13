package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.ManufacturerService;

@Stateless
@Remote(EJBRemote.class)
public class ManufacturerEJB implements EJBRemote {

	@Inject private ManufacturerService manufacturerService;
	
	@Override
	public Object get(int id) {
		return manufacturerService.findManufacturer(id);
	}

	@Override
	public Object edit(Object o) throws InvalidEntityException {
		if (o instanceof Manufacturer) {
			try {
				return manufacturerService.editManufacturer((Manufacturer)o);
			} catch (Exception e) {
				throw new InvalidEntityException(e.getMessage());
			}		
		}
		return null;
	}

	@Override
	public void delete(Object o) {
		if (o instanceof Manufacturer) {
			manufacturerService.removeManufacturer((Manufacturer)o);
		}
	}

	@Override
	public Object create(Object o) throws InvalidEntityException {
		if (o instanceof Manufacturer) {
			try {
				manufacturerService.createManufacturer((Manufacturer)o);
			} catch (Exception e) {
				throw new InvalidEntityException(e.getMessage());
			}
			return manufacturerService.findManufacturer(((Manufacturer) o).getId());			
		}	
		return null;
	}

	@Override
	public Object getAll() {
		return manufacturerService.getAll();
	}

}
