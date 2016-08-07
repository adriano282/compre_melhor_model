package com.compremelhor.model.strategy.category;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class UniqueNameStrategy extends UniqueTextFieldStrategy<Category> {
	public UniqueNameStrategy(AbstractDao<Category> dao) {
		super(dao,"name");
	}
}
