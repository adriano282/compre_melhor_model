package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Account;

@Stateless
public class AccountDao extends AbstractDao<Account> {
	private static final long serialVersionUID = 1L;

	public AccountDao() {
		super(Account.class);
	}
}
