package com.compremelhor.model.strategy.account;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.compremelhor.model.dao.AccountDao;
import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Role;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public class RemoveAccountStrategy implements Strategy<Account>{
	private AccountDao dao;
	
	public RemoveAccountStrategy(AccountDao dao) {
			this.dao = dao;
	}
	
	@Override
	public Status validate(Account t) {
		Predicate<Role> isAdmin = role -> 
		role.getRoleName().equals("admin");
	
		Predicate<Account> containsAdminRole = ac -> 
			ac.getRoles().stream().anyMatch(isAdmin);
		
		Status status = new Status();
		if (!containsAdminRole.test(t)
			|| dao.getStream().filter(containsAdminRole).count() > 1L
			) {
			return status;
		}
		
		Map<String, String> errors = new HashMap<>();
		errors.put("role", "account.role.must.exist.one.admin.account");
		status.setErrors(errors);
		
		return status;
	}
}
