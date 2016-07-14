package com.compremelhor.model.strategy.account;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.Account;
import com.compremelhor.model.strategy.UniqueTextFieldStrategy;

public class UniqueUsernameStrategy extends UniqueTextFieldStrategy<Account>{
	public UniqueUsernameStrategy(AbstractDao<Account> dao) {
		super(dao, "username");
	}
}
