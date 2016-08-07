package com.compremelhor.model.strategy.account;

import java.security.NoSuchAlgorithmException;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;
import com.compremelhor.model.util.GeneratorPasswordHash;

@OnCreateServiceAction
@OnEditServiceAction
public class HashingPasswordStrategy implements Strategy<Account> {

	@Override
	public Status process(Account t) {
		if (t != null && t.getPassword() != null && !t.getPassword().isEmpty()) {
			try {
				t.setPassword(GeneratorPasswordHash.getHash(t.getPassword()));
			} catch (NoSuchAlgorithmException e) { System.out.println("Hash Algorithm not found: Not Possible hashing password");}
		}
		return new Status();
	}

}
