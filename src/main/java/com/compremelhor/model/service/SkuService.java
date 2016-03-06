package com.compremelhor.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Sku;

@Stateless
public class SkuService {
	@Inject	private SkuDao skuDao;
	@Inject private Validator validator;
	
	public void createProduct(Sku sku) {
		validate(sku);
		skuDao.persist(sku);
	}
	
	public Sku editProduct(Sku sku) {
		validate(sku);
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
	
	private void validate(Sku sku) {
		validator.validate(sku);
		validator.validate(sku.getCode());
	}
}
