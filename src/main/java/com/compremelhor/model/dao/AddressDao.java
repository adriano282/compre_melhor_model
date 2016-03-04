package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Address;

@Stateless
public class AddressDao extends AbstractDao<Address> {
	private static final long serialVersionUID = 1L;

	public AddressDao() {
		super(Address.class);
	}
}
