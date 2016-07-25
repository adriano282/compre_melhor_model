package com.compremelhor.model.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
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
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public abstract class AbstractService<T extends EntityModel> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject 
	private Validator validator;	

	protected AbstractDao<T> dao;
	protected List<Strategy<? extends EntityModel>> strategies;
		
	@PostConstruct
	void init() {
		setDao();
		setStrategies();
	}
	
	protected abstract void setDao();
	protected abstract void setStrategies();
	
	public T find(String attributeName, String attributeValue) {
		if (attributeName == null || attributeValue == null)
			return null;
		
		Map<String, Object> params = new HashMap<>();
		params.put(attributeName, attributeValue);
		
		return dao.find(params);
	}
	
	public T find(Map<String, Object> params) throws UnknownAttributeException {
		return dao.find(params);
	}
	
	public List<T> findAll(Map<String, Object> params) throws UnknownAttributeException {
		return dao.findAll(params);
	}
	
	public void create(T t) throws InvalidEntityException {
		validate(t);
		if (t != null) {
			t.setLastUpdated(LocalDateTime.now());
			t.setDateCreated(LocalDateTime.now());
		}
		dao.persist(t);
	}
	
	public T edit(T t) throws InvalidEntityException {
		validate(t);
		if (t != null) t.setLastUpdated(LocalDateTime.now());
		return dao.edit(t);
	}
	
	public void remove(T t) {
		if (t != null)
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
						if (errs.length() > 0) errs.append("#");
						errs.append(pair.getValue());
					}
				}
			}
			if (errs.length() > 0) throw new InvalidEntityException(errs.toString());
		}
	}
}
