package com.compremelhor.model.remote;

import java.util.List;
import java.util.Set;

import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;

public interface EJBRemote<T extends EntityModel> {
	public T find(int id);
	public T find(int id, Set<String> feches);
	public T edit(T o) throws InvalidEntityException;
	public void delete(T o);
	public T create(T o) throws InvalidEntityException;
	public List<T> findAll();
	public List<T> findAll(Set<String> feches);
	public List<T> findAll(int start, int size);
	public List<T> findAll(int start, int size, Set<String> fetches);
}
