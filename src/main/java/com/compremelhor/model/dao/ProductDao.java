package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Product;

@Stateless
public class ProductDao extends AbstractDao<Product> {
	private static final long serialVersionUID = 1L;

	public ProductDao() {
		super(Product.class);
	}
	
	
}
