package com.compremelhor.model.service;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.strategy.manufacturer.UniqueNameStrategy;

@Stateless
public class ManufacturerService extends AbstractService<Manufacturer>{
	private static final long serialVersionUID = 1L;
	@Inject	private ManufacturerDao manufacturerDao;
	
	protected void setDao() { super.dao = manufacturerDao; }
	@Override 
	protected void setStrategies() {
		this.strategies = new ArrayList<>();
		this.strategies.add(new UniqueNameStrategy(manufacturerDao));
	}

	public Manufacturer findManufacturerByName(String name) {
		return manufacturerDao.findManufacturerByName(name);
	}
}
