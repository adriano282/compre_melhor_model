package com.compremelhor.model.strategy.user;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;

public class UniqueDocumentStrategy extends UniqueTextFieldStrategy<User> {

	public UniqueDocumentStrategy(AbstractDao<User> dao) {
		super(dao, "document");
	}
}
