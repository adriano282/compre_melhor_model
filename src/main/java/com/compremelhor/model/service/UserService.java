package com.compremelhor.model.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.validation.groups.UserAddress;

@Stateless
public class UserService {
	
	@Inject	private UserDao userDao;
	@Inject private AddressDao addressDao;
	@Inject private Validator validator;
	
	public boolean create(User user) {
		if (validator.validate(user).size() >0) return false;
		user.getOptionalAddresses()
		.ifPresent(ads -> {
			ads.stream()
				.forEach( ad -> { validator.validate(ad, UserAddress.class); });
		});		
		userDao.persist(user);
		return true;
	}
	
	public User edit(User user) {
		validator.validate(user);
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
	
	public Optional<List<Address>> findAllAddressByUser(User user) {
		return Optional.ofNullable(addressDao.findAllAddressByUser(user));
	}
	
	public void addAddress(int userId, Address address) throws LimitOfAddressesReachedException {
		User u = userDao.find(userId);
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
				
		Optional<List<Address>> optAds = u.getOptionalAddresses();
		
		if (optAds.isPresent() && optAds.get().size() == 3) {
			throw new LimitOfAddressesReachedException(); 
		}
		
		validator.validate(address, UserAddress.class);
		address.setUser(u);
		u.getAddresses().add(address);
		edit(u);		
	}
	
	public Address editAddress(Address address) {
		validator.validate(address, UserAddress.class);
		return addressDao.edit(address);
	}
	
	public void removeAllAddresses(int userId) {
		User u = userDao.find(userId);
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
		Optional<List<Address>> optAds = u.getOptionalAddresses();
		
		optAds.ifPresent(ads -> {
			if (ads.size() > 0) {
				ads.stream().forEach(addressDao::remove);
			}
		});
	}
	
	public void removeAddress(Address address) {
		addressDao.remove(address);
	}
}
