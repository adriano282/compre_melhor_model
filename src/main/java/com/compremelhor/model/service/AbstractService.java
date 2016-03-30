package com.compremelhor.model.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.NotImplementedException;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public abstract class AbstractService<T extends EntityModel> {
	@Inject 
	private Validator validator;	

	protected AbstractDao<T> dao;
	protected List<Strategy<T>> strategies;
		
	@PostConstruct
	void init() {
		setDao();
		setStrategies();
	}
	
	protected abstract void setDao();
	protected abstract void setStrategies();
	
	
	public T find(Map<String, String> params) throws UnknownAttributeException {
		throw new NotImplementedException();
	}
	
	public T find(String attributeName, String attributeValue) {
		throw new NotImplementedException();
	}
	
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
	
	public List<T> findAll() {
		return dao.findAll();
	}
	
	public List<T> findAll(int start, int size) {
		return dao.findAll(start, size);
	}
	
	public T find(int id, Set<String> fetches) {
		return dao.find(id, fetches);
	}

	protected void validate(Object t) throws InvalidEntityException {
		Set<ConstraintViolation<Object>> errors = validator.validate(t);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
	}
	
	protected void validate(T t) throws InvalidEntityException {
		Set<ConstraintViolation<T>> errors = validator.validate(t);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
		
		runOverStrategies(t);
	}
	
	@SuppressWarnings("rawtypes")
	protected void validate(T t, Class group) throws InvalidEntityException {
		Set<ConstraintViolation<T>> errors = validator.validate(t, group);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
		
		runOverStrategies(t);
	}

	@SuppressWarnings("rawtypes")
	protected void validate(Object t, Class group) throws InvalidEntityException {
		Set<ConstraintViolation<Object>> errors = validator.validate(t, group);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void runOverStrategies(EntityModel t) throws InvalidEntityException {
		if (strategies != null && strategies.size() > 0) {			
			StringBuilder errs = new StringBuilder();
			for (@SuppressWarnings("rawtypes") Strategy s : strategies) {
				Status status;
				if ((status = s.validate(t)).hasErrors()) {
					for (Map.Entry<String, String> pair : status.getErrors().entrySet()) {
						errs.append(pair.getValue().concat("#"));
					}
				}
			}
			if (errs.length() > 0) throw new InvalidEntityException(errs.toString());
		}
	}
}
