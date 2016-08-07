package com.compremelhor.model.strategy.user;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class UniqueUsernameStrategy extends UniqueTextFieldStrategy<User> {

	public UniqueUsernameStrategy(AbstractDao<User> dao) {
		super(dao, "username");
	}
}
