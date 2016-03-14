package com.compremelhor.model.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;

public class ManufacturerService {
	
	@Inject	private ManufacturerDao manufacturerDao;
	@Inject private Validator validator;
	
	public void createManufacturer(Manufacturer mfr) {
		validator.validate(mfr);
		manufacturerDao.persist(mfr);
	}
	
	public Manufacturer editManufacturer(Manufacturer mfr) {
		validator.validate(mfr);
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
	
	public List<Manufacturer> getAll() {
		return manufacturerDao.findAll();
	}
}
