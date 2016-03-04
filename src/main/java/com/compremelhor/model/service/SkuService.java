package com.compremelhor.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Sku;

@Stateless
public class SkuService {
	@Inject
	private SkuDao skuDao;
	
	public void createProduct(Sku sku) {
		skuDao.persist(sku);
	}
	
	public Sku editProduct(Sku sku) {
		return skuDao.edit(sku);
	}
	
	public List<Sku> findAll() {
		return skuDao.findAll();
	}
	
	public Sku findProduct(int id) {
		return skuDao.find(id);
	}
	
	public void removeProduct(Sku sku) {
		skuDao.remove(sku);
	}
}
