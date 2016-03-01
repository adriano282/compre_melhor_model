package com.compremelhor.model.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.exception.UserNotFoundException;


@Stateless
public class UserService {
	@Inject	private UserDao userDao;
	@Inject private AddressDao addressDao;
	
	@Inject private Logger logger;
	public void create(User user) {
		userDao.persist(user);
	}
	
	public User edit(User user) {
		User u = userDao.edit(user);
		return u;
	}
	
	public void remove(User user) {
		userDao.remove(user);
	}
	
	public User find(int id) {
		return userDao.find(id);
	}
	
	public User findUserByDocument(String document) {
		return userDao.findUserByDocument(document);
	}
	
	public void addAddress(int userId, Address address) throws LimitOfAddressesReachedException {
		User u = userDao.find(userId);
		logger.warning(u + "");
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
		
		
		List<Address> ads = u.getAddresses();
		logger.warning(ads.size() + "");
		if (ads != null && ads.size() == 3)
			throw new LimitOfAddressesReachedException();
		
		address.setUser(u);
		u.getAddresses().add(address);
		edit(u);
		
	}
	
	public Address editAddress(Address address) {
		return addressDao.edit(address);
	}
	
	public void removeAllAddresses(int userId) {
		User u = userDao.find(userId);
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
		if (u.getAddresses() != null && u.getAddresses().size() > 0) {
			u.getAddresses().stream().forEach(ad -> addressDao.remove(ad));
		}
		
	}
	
	public void removeAddress(Address address) {
		addressDao.remove(address);
	}

}
