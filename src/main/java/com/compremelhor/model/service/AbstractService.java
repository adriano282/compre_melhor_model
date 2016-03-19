package com.compremelhor.model.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;

public abstract class AbstractService<T extends EntityModel> {
	@Inject 
	private Validator validator;
	
	protected AbstractDao<T> dao;
		
	@PostConstruct
	public void registerDAO() {
		setDao();
	}
	
	protected abstract void setDao();
	
	public void create(T t) throws InvalidEntityException {
		validate(t);
		dao.persist(t);
	}
	
	public T edit(T t) throws InvalidEntityException {
		validate(t);
		return dao.edit(t);
	}
	
	public void remove(T t) {
		dao.remove(t);
	}
	
	public T find(int id) {
		return dao.find(id);
	}
	
	public List<T> getAll() {
		return dao.findAll();
	}
	
	public List<T> getAll(int start, int size) {
		return dao.findAll(start, size);
	}
	
	protected void validate(EntityModel t) throws InvalidEntityException {
		Set<ConstraintViolation<EntityModel>> errors = validator.validate(t);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getPropertyPath().toString().concat(": ").concat(c.getMessage().toString()).concat("#"))
						.collect(Collectors.joining()));
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void validate(EntityModel t, Class group) throws InvalidEntityException {
		Set<ConstraintViolation<EntityModel>> errors = validator.validate(t, group);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getPropertyPath().toString().concat(": ").concat(c.getMessage().toString()).concat("#"))
						.collect(Collectors.joining()));
		}
	}
}
