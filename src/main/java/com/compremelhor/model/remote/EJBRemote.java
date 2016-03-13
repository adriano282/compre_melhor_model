package com.compremelhor.model.remote;

import com.compremelhor.model.exception.InvalidEntityException;

public interface EJBRemote {
	public Object get(int id);
	public Object edit(Object o) throws InvalidEntityException;
	public void delete(Object o);
	public Object create(Object o) throws InvalidEntityException;
	public Object getAll();
}
