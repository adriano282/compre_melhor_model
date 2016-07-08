package com.compremelhor.model.service;

import java.util.List;

import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;

public class CategoryService extends AbstractService<Category>{
	
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

}
