package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.FreightType;

@Stateless
public class FreightTypeDao extends AbstractDao<FreightType>{
	private static final long serialVersionUID = 1L;

	public FreightTypeDao() {
		super(FreightType.class);
	}

}
