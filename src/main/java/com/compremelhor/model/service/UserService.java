package com.compremelhor.model.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.util.GeneratorPasswordHash;
import com.compremelhor.model.validation.groups.UserAddress;

@Stateless
public class UserService extends AbstractService<User>{
	
	@Override
	public void create(User t) throws InvalidEntityException {
		try {
			if (t.getPasswordHash() != null)
				t.setPasswordHash(GeneratorPasswordHash.getHash(t.getPasswordHash()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error on hashing password");
		}
		super.create(t);
	}



	@Inject	private UserDao userDao;
	@Inject private AddressService addressService;
	
	
	@Override
	protected void setDao() { super.dao = this.userDao;}
	
	@Override
	public User find(String attributeName, String attributeValue) {
		return userDao.findByAttribute(attributeName, attributeValue);
	}
	
	public User findUserByDocument(String document) {
		return userDao.findUserByDocument(document);
	}
	
	public Optional<List<Address>> findAllAddressByUser(User user) {
		return Optional.ofNullable(addressService.findAllAddressByUser(user));
	}
	
	public void addAddress(int userId, Address address) throws LimitOfAddressesReachedException, InvalidEntityException {
		User u = userDao.find(userId);
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
				
		Optional<List<Address>> optAds = u.getOptionalAddresses();
		
		if (optAds.isPresent() && optAds.get().size() == 3) {
			throw new LimitOfAddressesReachedException(); 
		}
		
		address.setUser(u);
		validate(address, UserAddress.class);
		u.getAddresses().add(address);
		edit(u);		
	}
	
	public Address editAddress(Address address) throws InvalidEntityException {
		validate(address, UserAddress.class);
		return addressService.edit(address);
	}
	
	public void removeAllAddresses(int userId) {
		User u = userDao.find(userId);
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
		Optional<List<Address>> optAds = u.getOptionalAddresses();
		
		optAds.ifPresent(ads -> {
			if (ads.size() > 0) {
				ads.stream().forEach(addressService::remove);
			}
		});
	}
	
	public void removeAddress(Address address) {
		addressService.remove(address);
	}


}
