package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.service.FreightService;

@Stateless
@Remote(EJBRemote.class)
public class FreightEJB extends AbstractRemote<Freight> {

	@Inject private FreightService freightService;
	
	@Override
	void setService() { super.service = this.freightService; }

}
