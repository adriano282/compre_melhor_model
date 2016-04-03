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
	@Override 
	protected void setStrategies() {}

	public List<Address> findAllAddressByUser(User user) {
		return dao.findAllAddressByUser(user);
	}
	
	public void removeAllAddressesByUser(int userId) {
		if (userId == 0) throw new IllegalArgumentException("For delete query, userId can not be null");
		dao.removeAllAddressByUser(userId);
	}

}
