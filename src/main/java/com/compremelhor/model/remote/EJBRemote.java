package com.compremelhor.model.remote;

import java.util.List;
import java.util.Set;

import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;

public interface EJBRemote<T extends EntityModel> {
	public T get(int id);
	public T get(int id, Set<String> feches);
	public T edit(T o) throws InvalidEntityException;
	public void delete(T o);
	public T create(T o) throws InvalidEntityException;
	public List<T> getAll();
	public List<T> getAll(Set<String> feches);
	public List<T> getAll(int start, int size);
	public List<T> getAll(int start, int size, Set<String> feches);
}
