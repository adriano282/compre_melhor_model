package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.service.AccountService;

@Stateless
@Remote(EJBRemote.class)
public class AccountEJB extends AbstractRemote<Account>{

	@Inject private AccountService service;
	
	void setService() {super.service = this.service;}

}
