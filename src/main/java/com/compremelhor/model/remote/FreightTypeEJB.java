package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.FreightType;
import com.compremelhor.model.service.FreightTypeService;

@Stateless
@Remote(EJBRemote.class)
public class FreightTypeEJB extends AbstractRemote<FreightType> {
	
	@Inject private FreightTypeService service;
	@Override
	void setService() { super.service = this.service; }

}
