package com.compremelhor.model.strategy.manufacturer;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class UniqueNameStrategy extends UniqueTextFieldStrategy<Manufacturer>{

	public UniqueNameStrategy(AbstractDao<Manufacturer> dao) {
		super(dao, "name");
	}
	
}
