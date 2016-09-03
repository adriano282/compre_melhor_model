package com.compremelhor.model.service;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.AfterServiceAction;
import com.compremelhor.model.strategy.annotations.BeforeServiceAction;
import com.compremelhor.model.strategy.annotations.OnAfterCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnAfterEditServiceAction;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;
import com.compremelhor.model.strategy.annotations.OnRemoveServiceAction;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractService<T extends EntityModel> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject	private Validator validator;	

	protected AbstractDao<T> dao;
	protected List<Strategy<T>> strategies;
			
	@PostConstruct
	void init() {
		setDao();
		setStrategies();
	}
	
	protected abstract void setDao();
	protected abstract void setStrategies();
	
	@SuppressWarnings("unchecked")
	public void create(T t) throws InvalidEntityException {
		validate(t);
		process(t, new Class [] {BeforeServiceAction.class, OnCreateServiceAction.class});
		
		if (t != null) {
			t.setDateCreated(LocalDateTime.now());
			t.setLastUpdated(LocalDateTime.now());
		}
		
		dao.persist(t);
		process(t, new Class [] {AfterServiceAction.class, OnAfterCreateServiceAction.class});
	}
	
	@SuppressWarnings("unchecked")
	public T edit(T t) throws InvalidEntityException {
		validate(t);
		process(t, new Class [] {BeforeServiceAction.class, OnEditServiceAction.class});
		
		if (t != null) {
			t.setLastUpdated(LocalDateTime.now());
		}
		
		T entity = dao.edit(t);
		
		process(t, new Class [] {AfterServiceAction.class, OnAfterEditServiceAction.class});
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public void remove(T t) {
		if (t != null) {
			try {
				process(t, new Class [] {OnRemoveServiceAction.class});
			} catch (InvalidEntityException e) {
				System.out.println("AbstractService.remove(T): " + e.getMessage());
				return;
			}
			dao.remove(t);
		}
	}
	
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

	protected void process(Object t) throws InvalidEntityException {
		Set<ConstraintViolation<Object>> errors = validator.validate(t);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void process(T t, Class<? extends Annotation> ... annotations) throws InvalidEntityException {
		
		Set<ConstraintViolation<T>> errors = validator.validate(t);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
		
		runOverStrategies(t, annotations);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void process(T t, Class group) throws InvalidEntityException {
		runOverStrategies(t);
	}

	@SuppressWarnings("rawtypes")
	protected void process(Object t, Class group) throws InvalidEntityException {
		Set<ConstraintViolation<Object>> errors = validator.validate(t, group);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
		
	}
	
	private void validate(T t) throws InvalidEntityException {
		Set<ConstraintViolation<T>> errors = validator.validate(t);
		if (errors.size() > 0) {
			throw new InvalidEntityException(
					errors.stream()
						.map(c -> c.getMessage().toString().concat("#"))
						.collect(Collectors.joining()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void runOverStrategies(EntityModel t, Class<? extends Annotation> ... annotations) throws InvalidEntityException {
		if (!isThereAnyStrategy()) 
			return;
					
		StringBuilder errs = new StringBuilder();
		
		for (@SuppressWarnings("rawtypes") Strategy s : strategies) {
			Status status;
			
			if (!isGoingToRun(s, annotations))
				continue;
							
			if ((status = s.process(t)).hasErrors()) {
				for (Map.Entry<String, String> pair : status.getErrors().entrySet()) {
					if (errs.length() > 0) errs.append("#");
					errs.append(pair.getValue());
				}
			}
		}
		if (errs.length() > 0) throw new InvalidEntityException(errs.toString());
	}
	
	private boolean isThereAnyStrategy() {
		return strategies != null && strategies.size() > 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean isGoingToRun(Strategy s, Class<? extends Annotation> ... annotations) {
		
		if (annotations != null && annotations.length > 0) {
			for (Class<? extends Annotation> annotation : annotations) {
				if (s.getClass().isAnnotationPresent(annotation)) return true;
			}
		}
		return false;
	}
}
