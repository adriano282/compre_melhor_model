package com.compremelhor.model.strategy.category;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;

public class UniqueNameStrategy extends UniqueTextFieldStrategy<Category> {
	public UniqueNameStrategy(AbstractDao<Category> dao) {
		super(dao,"name");
	}
}
