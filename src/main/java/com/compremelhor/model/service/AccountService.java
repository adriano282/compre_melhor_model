package com.compremelhor.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.compremelhor.model.dao.AccountDao;
import com.compremelhor.model.entity.Account;

public class AccountService extends AbstractService<Account> {	
	private static final long serialVersionUID = 1L;
	
	@Inject private AccountDao dao;
		
	public void setDao() { super.dao = this.dao;}
	@Override 
	protected void setStrategies() {}
	
	
	@Override
	public Account find(String attributeName, String attributeValue) {
		if (attributeName == null || attributeValue == null)
			return null;
		
		Map<String, Object> params = new HashMap<>();
		params.put(attributeName, attributeValue);
		
		return dao.find(params);
	}
	
}
