package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;

@Stateless
public class CategoryService {	
	@Inject	private CategoryDao categoryDao;
	@Inject private Validator validator;
	
	public void createCategory(Category category) {
		validator.validate(category);
		categoryDao.persist(category);
	}
	
	public Category editCategory(Category category) {
		validator.validate(category);
		return categoryDao.edit(category);
	}
	
	public Category findCategory(int id) {
		return categoryDao.find(id);
	}
	public Category findCategory(Object id) {
		return categoryDao.find(id);
	}	
	public void removeCategory(Category category) {
		categoryDao.remove(category);
	}	
	
	public Category findCategoryBySkuId(int skuId) {
		return categoryDao.findCategoryBySkuId(skuId);
	}
}
