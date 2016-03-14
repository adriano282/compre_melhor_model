package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Sku;

@Stateless
public class SkuDao extends AbstractDao<Sku> {
	private static final long serialVersionUID = 1L;

	public SkuDao() { super(Sku.class); }
}
