package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.AccountDao;
import com.compremelhor.model.entity.Account;

public class AccountService extends AbstractService<Account> {	
	
	@Inject private AccountDao dao;
		
	public void setDao() { super.dao = this.dao;}
	@Override 
	protected void setStrategies() {}
	
}
