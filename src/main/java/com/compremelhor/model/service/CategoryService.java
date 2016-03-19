package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;

public class CategoryService extends AbstractService<Category>{
	
	@Inject	private CategoryDao categoryDao;
	
	@Override
	protected void setDao() { super.dao = categoryDao; }
	
	public Category findCategory(Object id) {
		return categoryDao.find(id);
	}	
		
	public Category findCategoryBySkuId(int skuId) {
		return categoryDao.findCategoryBySkuId(skuId);
	}

}
