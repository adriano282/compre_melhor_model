package com.compremelhor.model.strategy.sku;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class UniqueCodeStrategy extends UniqueTextFieldStrategy<Sku> {
	public UniqueCodeStrategy(AbstractDao<Sku> dao) {
		super(dao, "code");
	}
}
