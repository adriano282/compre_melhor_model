package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Freight;

@Stateless
public class FreightDao extends AbstractDao<Freight> {
	private static final long serialVersionUID = 1L;
	public FreightDao() {
		super(Freight.class);
	}

}
