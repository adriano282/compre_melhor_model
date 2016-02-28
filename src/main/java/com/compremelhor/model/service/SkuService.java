package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Sku;

public class SkuService {
	@Inject
	private SkuDao skuDao;
	
	public void createProduct(Sku sku) {
		skuDao.persist(sku);
	}
	
	public Sku editProduct(Sku sku) {
		return skuDao.edit(sku);
	}
	
	public Sku findProduct(int id) {
		return skuDao.find(id);
	}
	
	public void removeProduct(Sku sku) {
		skuDao.remove(sku);
	}
}
