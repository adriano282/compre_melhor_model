package com.compremelhor.model.remote;

import java.util.List;

import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.exception.InvalidEntityException;

public interface EJBRemote<T extends EntityModel> {
	public T get(int id);
	public T edit(T o) throws InvalidEntityException;
	public void delete(T o);
	public T create(T o) throws InvalidEntityException;
	public List<T> getAll();
	public List<T> getAll(int start, int size);
}
