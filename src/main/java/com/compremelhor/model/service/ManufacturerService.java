package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;

@Stateless
public class ManufacturerService extends AbstractService<Manufacturer>{
	
	@Inject	private ManufacturerDao manufacturerDao;
	
	protected void setDao() { super.dao = manufacturerDao; }
	@Override 
	protected void setStrategies() {}

	public Manufacturer findManufacturerByName(String name) {
		return manufacturerDao.findManufacturerByName(name);
	}
}
