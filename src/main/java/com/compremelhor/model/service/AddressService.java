package com.compremelhor.model.service;


import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.compremelhor.model.dao.AddressDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.exception.InvalidEntityException;

public class AddressService {
	
	@Inject private AddressDao dao;
	@Inject private Validator validator;
	
	public void create(Address address) throws InvalidEntityException {
		validate(address);
		dao.persist(address);
	}
	
	public Address edit(Address address) throws InvalidEntityException {
		validate(address);
		return dao.edit(address);
	}
	
	public Address find(int id) {
		return dao.find(id);
	}
	
	public void remove(Address address) {
		dao.remove(address);
	}
	
	private void validate(Address address) throws InvalidEntityException {
		Set<ConstraintViolation<Address>> errors = validator.validate(address);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getPropertyPath().toString().concat(": ").concat(c.getMessage().toString()).concat("#"))
						.collect(Collectors.joining()));
		}
	}
}
