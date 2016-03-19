package com.compremelhor.model.service;


import java.util.List;

import javax.inject.Inject;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;

public class AddressService extends AbstractService<Address>{
	
	@Inject private AddressDao dao;

	@Override
	protected void setDao() { super.dao = this.dao;}

	public List<Address> findAllAddressByUser(User user) {
		return dao.findAllAddressByUser(user);
	}
}
