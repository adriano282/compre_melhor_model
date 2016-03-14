package com.compremelhor.model.service;

import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.AccountDao;
import com.compremelhor.model.entity.Account;

public class AccountService {	
	
	@Inject private AccountDao dao;
	@Inject private Validator validator;
		
	public Account find(int id) {
		return dao.find(id);
	}
	
	public void create(Account account) {
		validator.validate(account);
		dao.persist(account);
	}
	
	public Account edit(Account account) {
		validator.validate(account);
		return dao.edit(account);
	}
	
	public void remove(Account account) {
		dao.remove(account);
	}
}
