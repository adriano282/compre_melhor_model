package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;

@Stateless
public class CategoryService {
	
	@Inject
	private CategoryDao categoryDao;
	
	public void createCategory(Category category) {
		categoryDao.persist(category);
	}
	
	public Category editCategory(Category category) {
		return categoryDao.edit(category);
	}
	
	public Category findCategory(int id) {
		return categoryDao.find(id);
	}
	
	public void removeCategory(Category category) {
		categoryDao.remove(category);
	}	
}
