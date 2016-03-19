package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.service.SkuPartnerService;

@Stateless
@Remote(EJBRemote.class)
public class SkuPartnerEJB extends AbstractRemote<SkuPartner>{
	
	@Inject private SkuPartnerService service;
	
	@Override
	void setService() { super.service = this.service;}

}
