package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.AccountDao;
import com.compremelhor.model.entity.Account;

@Stateless
public class AccountService {
	
	@Inject
	private AccountDao dao;
		
	public Account find(int id) {
		return dao.find(id);
	}
	
	public void create(Account account) {
		dao.persist(account);
	}
	
	public Account edit(Account account) {
		return dao.edit(account);
	}
	
	public void remove(Account account) {
		dao.remove(account);
	}
}
