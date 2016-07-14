package com.compremelhor.model.dao;

import java.util.stream.Stream;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Role;

@Stateless
public class AccountDao extends AbstractDao<Account> {
	private static final long serialVersionUID = 1L;

	public AccountDao() {
		super(Account.class);
	}

	@Override
	public Account find(Object id) {
		Account ac = super.find(id);
		if (ac.getRoles() != null) {
			ac.getRoles().forEach(r -> {
				if (r.getRoleName() != null)
					r.getRoleName();
			});
		}
		return ac;
	}
	
	public Stream<Account> makeRoleEager(Stream<Account> stream) {
		return stream.map( ac -> {
			if (ac.getRoles() != null) {
				for (Role r : ac.getRoles()) {
					r.getRoleName();
					}
				}
			return ac;
		});
	}
	
	public Stream<Account> getStream() {
		return findAll().stream();
	}
}
