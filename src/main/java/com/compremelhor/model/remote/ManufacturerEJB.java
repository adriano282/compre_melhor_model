package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.service.ManufacturerService;

@Stateless
@Remote(EJBRemote.class)
public class ManufacturerEJB  extends AbstractRemote<Manufacturer>{

	@Inject private ManufacturerService manufacturerService;

	@Override
	void setService() {super.service = manufacturerService;}
	
	
}
