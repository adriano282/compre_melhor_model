package com.compremelhor.model.remote;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.AbstractService;

public abstract class AbstractRemote<T extends EntityModel> implements EJBRemote<T> {
	
	AbstractService<T> service;
	
	@PostConstruct
	public void registerService() {
		setService();
	}
	
	abstract void setService();
	
	@Override
	public T find(String attributeName, String attributeValue) {
		return service.find(attributeName, attributeValue);
	}

	@Override
	public T find(int id) {
		return service.find(id);
	}

	@Override
	public T find(int id, Set<String> feches) {
		return service.find(id, feches);
	}

	@Override
	public T edit(T o) throws InvalidEntityException {
		return service.edit(o);
	}

	@Override
	public void delete(T o) {
		service.remove(o);
	}

	@Override
	public T create(T o) throws InvalidEntityException {
		service.create(o);
		return service.find(o.getId());
	}

	@Override
	public List<T> findAll() {
		return service.findAll();
	}

	@Override
	public List<T> findAll(int start, int size) {
		return service.findAll(start, size);
	}

	@Override
	public List<T> findAll(int start, int size, Set<String> fetches) {
		throw new RuntimeException("NOT IMPLEMENTED YET");
	}
	
	@Override
	public List<T> findAll(Set<String> feches) {
		throw new RuntimeException("NOT IMPLEMENTED YET");
	}

}
