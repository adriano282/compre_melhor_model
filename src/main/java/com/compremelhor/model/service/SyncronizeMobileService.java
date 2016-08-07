package com.compremelhor.model.service;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.SyncronizeMobileDao;
import com.compremelhor.model.entity.SyncronizeMobile;
import com.compremelhor.model.exception.InvalidEntityException;

@Stateless
public class SyncronizeMobileService extends AbstractService<SyncronizeMobile>{
	private static final long serialVersionUID = 1L;

	@Inject private SyncronizeMobileDao dao;
	
	@Override
	protected void setDao() { super.dao = this.dao; }

	@Override
	protected void setStrategies() {}

	@Override
	public void create(SyncronizeMobile t) throws InvalidEntityException {
		if (t != null) {
			HashMap<String, Object> params = new HashMap<>();
			params.put("entityName", t.getEntityName());
			params.put("mobileUserIdRef", t.getMobileUserIdRef());
			
			SyncronizeMobile current = dao.find(params);
			
			if (current != null) {
				this.remove(current);
			}
		}
		super.create(t);
	}

}
