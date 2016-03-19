package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.service.PurchaseService;

@Stateless
@Remote(EJBRemote.class)
public class PurchaseEJB extends AbstractRemote<Purchase>{

	@Inject private PurchaseService service;
	
	@Override
	void setService() { super.service = this.service;}

}
