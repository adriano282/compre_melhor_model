package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.service.PartnerService;

@Stateless
@Remote(EJBRemote.class)
public class PartnerEJB extends AbstractRemote<Partner>{

	@Inject private PartnerService partnerService;

	@Override
	void setService() { super.service = partnerService;}
	

}
