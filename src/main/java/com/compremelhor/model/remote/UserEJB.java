package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.User;
import com.compremelhor.model.service.UserService;

@Stateless
@Remote(EJBRemote.class)
public class UserEJB extends AbstractRemote<User>{

	@Inject private UserService service;
	
	@Override
	void setService() { super.service = this.service;}

}
