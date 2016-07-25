package com.compremelhor.model.strategy.user;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;

public class UniqueUsernameStrategy extends UniqueTextFieldStrategy<User> {

	public UniqueUsernameStrategy(AbstractDao<User> dao) {
		super(dao, "username");
	}
}
