package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.FreightTypeDao;
import com.compremelhor.model.entity.FreightType;

@Stateless
public class FreightTypeService extends AbstractService<FreightType>{
	private static final long serialVersionUID = 1L;
	
	@Inject private FreightTypeDao dao;
	
	@Override
	protected void setDao() { super.dao = this.dao; }

	@Override
	protected void setStrategies() {}

}
