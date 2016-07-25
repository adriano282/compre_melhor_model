package com.compremelhor.model.strategy.sku;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;

public class UniqueCodeStrategy extends UniqueTextFieldStrategy<Sku> {
	public UniqueCodeStrategy(AbstractDao<Sku> dao) {
		super(dao, "code");
	}
}
