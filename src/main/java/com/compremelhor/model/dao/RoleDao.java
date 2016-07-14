package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Role;

@Stateless
public class RoleDao extends AbstractDao<Role> {
	private static final long serialVersionUID = 1L;
	
	public RoleDao() {
		super(Role.class);
	}
}
