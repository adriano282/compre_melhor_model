package com.compremelhor.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;

@Stateless
public class CategoryService extends AbstractService<Category>{
	private static final long serialVersionUID = 1L;
	@Inject	private CategoryDao categoryDao;
	
	@Override
	protected void setDao() { super.dao = categoryDao; }
	@Override 
	protected void setStrategies() {}
	
	public Category findCategory(Object id) {
		return categoryDao.find(id);
	}	
		
	public Category findCategoryBySkuId(int skuId) {
		return categoryDao.findCategoryBySkuId(skuId);
	}
	
	public Category findCategoryByName(String name) {
		return categoryDao.findCategoryByName(name);
	}
	@Override
	public Category find(String attributeName, String attributeValue) {
		Map<String, Object> params = new HashMap<>();
		params.put(attributeName, attributeValue);
		return categoryDao.find(params);
	}

}
