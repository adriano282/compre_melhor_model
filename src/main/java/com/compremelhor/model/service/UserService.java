package com.compremelhor.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.LimitAddressesByAnUserStrategy;
import com.compremelhor.model.strategy.user.UniqueDocumentStrategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.GeneratorPasswordHash;
import com.compremelhor.model.validation.groups.UserAddress;

@Stateless
public class UserService extends AbstractService<User>{
	@Inject	private UserDao userDao;
	@Inject private AddressService addressService;
	
	
	@Override
	protected void setDao() { super.dao = this.userDao;}
	
	@Override 
	protected void setStrategies() {
		List<Strategy<User>> strategies = new ArrayList<>();
		strategies.add(new UniqueDocumentStrategy(userDao));
		strategies.add(new UniqueUsernameStrategy(userDao));
		strategies.add(new LimitAddressesByAnUserStrategy(userDao));
		super.strategies = strategies;
	}
	
	@Override
	public User find(Map<String, String> params) throws UnknownAttributeException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try {
			props.load(classLoader.getResourceAsStream("entity_properties.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String attrs = (String) props.get("user");
		
		Set<Map.Entry<String, String>> entries = params.entrySet();
		
		for (Map.Entry<String, String> pair : entries) {
			if (!Arrays.asList(attrs.split("#")).contains(pair.getKey().trim())) {
				throw new UnknownAttributeException("Unknown user attribute: " + pair.getValue());
			}
		}
		return userDao.find(params);
	}
	
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
	
	public void addAddress(int userId, Address address) throws LimitOfAddressesReachedException, InvalidEntityException {
		User u = userDao.find(userId);
		if (u == null) {
			throw new UserNotFoundException(userId);
		}
		
		validate(u);
		
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
