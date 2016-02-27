package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;

@Stateless
public class ManufacturerService {
	@Inject
	private ManufacturerDao manufacturerDao;
	
	public void createManufacturer(Manufacturer mfr) {
		manufacturerDao.persist(mfr);
	}
	
	public Manufacturer editManufacturer(Manufacturer mfr) {
		return manufacturerDao.edit(mfr);
	}
	
	public void removeManufacturer(Manufacturer mfr) {
		manufacturerDao.remove(mfr);
	}
	
	public Manufacturer findManufacturer(int id) {
		return manufacturerDao.find(id);
	}
	
	public Manufacturer findManufacturerByName(String name) {
		return manufacturerDao.findManufacturerByName(name);
	}
}
