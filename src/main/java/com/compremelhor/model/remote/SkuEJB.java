package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.service.SkuService;

@Stateless
@Remote(EJBRemote.class)
public class SkuEJB extends AbstractRemote<Sku>{

	@Inject private SkuService skuService;
	
	@Override
	void setService() {super.service = this.skuService;}

}
