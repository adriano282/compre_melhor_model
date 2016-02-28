package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.ProductDao;
import com.compremelhor.model.entity.Product;

public class ProductService {
	@Inject
	private ProductDao productDao;
	
	public void createProduct(Product product) {
		productDao.persist(product);
	}
	
	public Product editProduct(Product product) {
		return productDao.edit(product);
	}
	
	public Product findProduct(int id) {
		return productDao.find(id);
	}
	
	public void removeProduct(Product product) {
		productDao.remove(product);
	}
}
