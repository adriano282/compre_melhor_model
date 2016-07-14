package com.compremelhor.model.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.AccountDao;
import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Role;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.account.ChangeRoleAccountStrategy;
import com.compremelhor.model.strategy.account.RemoveAccountStrategy;
import com.compremelhor.model.strategy.account.UniqueUsernameStrategy;
import com.compremelhor.model.util.GeneratorPasswordHash;

@Stateless
public class AccountService extends AbstractService<Account> {	
	private static final long serialVersionUID = 1L;
	
	@Inject private AccountDao dao;
		
	public void setDao() { super.dao = this.dao;}
	
	@Override 
	protected void setStrategies() {
		strategies = new ArrayList<>();
		strategies.add(new UniqueUsernameStrategy(dao));
		strategies.add(new ChangeRoleAccountStrategy(dao));
	}
	
	public List<Account> findAllByPartnerId(int id, boolean roleEager) {
		Stream<Account> stream = dao.getStream().filter(ac -> ac.getPartner().getId() == id);
		
		if (roleEager) {
			stream = dao.makeRoleEager(stream);
		}
		
		return stream.collect(Collectors.toList());
	}
	
	@Override
	public void remove(Account t) {
		if (t == null) return;

		RemoveAccountStrategy strategy = new RemoveAccountStrategy(dao);

		Status s = null;
		if (!(s = strategy.validate(t)).hasErrors())
			super.remove(t);
		
		System.out.println(s.getErrors());
	}
	
	@Override
	public void create(Account t) throws InvalidEntityException {
		hashingPassword(t);
		super.create(t);
	}

	@Override
	public Account find(String attributeName, String attributeValue) {
		Account ac = super.find(attributeName, attributeValue);
		if (ac != null && ac.getRoles() != null) {
			for (Role r : ac.getRoles()) {
				r.getRoleName();
			}
		}
		return ac;
	}

	@Override
	public Account edit(Account t) throws InvalidEntityException {
		hashingPassword(t);
		return super.edit(t);
	}
	
	private void hashingPassword(Account t) {
		if (t != null && t.getPassword() != null && !t.getPassword().isEmpty()) {
			try {
				t.setPassword(GeneratorPasswordHash.getHash(t.getPassword()));
			} catch (NoSuchAlgorithmException e) { System.out.println("Hash Algorithm not found: Not Possible hashing password");}
		}
	}
}
