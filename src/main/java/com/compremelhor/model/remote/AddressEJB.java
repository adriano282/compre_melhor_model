package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Address;
import com.compremelhor.model.service.AddressService;

@Stateless
@Remote(EJBRemote.class)
public class AddressEJB extends AbstractRemote<Address>{

	@Inject private AddressService service;

	@Override
	void setService() { super.service = this.service;}
	
	
}
