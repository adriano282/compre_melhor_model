package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Address;
import javax.inject.Named;

@Named
@Stateless
public class AddressDao extends AbstractDao<Address> {
	private static final long serialVersionUID = 1L;

	public AddressDao() {
		super(Address.class);
	}
}
