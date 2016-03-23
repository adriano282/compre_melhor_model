package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.service.PurchaseLineService;

@Stateless
@Remote(EJBRemote.class)
public class PurchaseLineEJB extends AbstractRemote<PurchaseLine>{

	@Inject PurchaseLineService service;
	
	@Override
	void setService() { super.service = this.service;}

}
