package com.compremelhor.model.strategy.manufacturer;

import java.util.HashMap;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public class UniqueNameStrategy implements Strategy<Manufacturer> {
	private ManufacturerDao dao;
	
	public UniqueNameStrategy(ManufacturerDao dao) {this.dao = dao;}
	@Override
	public Status validate(Manufacturer t) {
		HashMap<String, String> errors = new HashMap<>();
		
		if (t == null
				|| t.getName() == null
				|| t.getName().isEmpty()) {
			return new Status();
		}
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("name", t.getName().trim());
		
		Manufacturer c;
		if ((c = dao.find(params)) != null
				&& c.getId() != t.getId()) {
			errors.put("name", "manufacturer.name.already.used.error.message");
			return new Status(errors);
		}
		return new Status();
	}


}
