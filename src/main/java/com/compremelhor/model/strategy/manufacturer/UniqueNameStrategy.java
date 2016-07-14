package com.compremelhor.model.strategy.manufacturer;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;

public class UniqueNameStrategy extends UniqueTextFieldStrategy<Manufacturer>{

	public UniqueNameStrategy(AbstractDao<Manufacturer> dao) {
		super(dao, "name");
	}
	
}
