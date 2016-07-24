package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.SyncronizeMobile;
import com.compremelhor.model.service.SyncronizeMobileService;

@Stateless
@Remote(EJBRemote.class)
public class SyncMobEJB extends AbstractRemote<SyncronizeMobile> {
	
	@Inject private SyncronizeMobileService service;
	
	@Override
	void setService() { super.service = this.service; }

}
