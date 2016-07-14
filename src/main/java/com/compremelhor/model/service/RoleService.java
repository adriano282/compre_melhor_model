package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.RoleDao;
import com.compremelhor.model.entity.Role;

@Stateless
public class RoleService extends AbstractService<Role>{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private RoleDao dao;

	@Override
	protected void setStrategies() {}

	@Override
	protected void setDao() {super.dao = this.dao;}
	
}
