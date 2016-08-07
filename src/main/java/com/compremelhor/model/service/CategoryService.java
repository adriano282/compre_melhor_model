package com.compremelhor.model.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.strategy.category.UniqueNameStrategy;

@Stateless
public class CategoryService extends AbstractService<Category>{
	private static final long serialVersionUID = 1L;
	@Inject	private CategoryDao categoryDao;
	
	@Override
	protected void setDao() { super.dao = categoryDao; }
	@Override 
	protected void setStrategies() {
		this.strategies = new ArrayList<>();
		this.strategies.add(new UniqueNameStrategy(categoryDao));
	}
	
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
	@Override
	public void create(Category t) throws InvalidEntityException {
		if (t != null) {
			t.setDateCreated(LocalDateTime.now());
			t.setLastUpdated(LocalDateTime.now());
		}
		super.create(t);
	}
	@Override
	public Category edit(Category t) throws InvalidEntityException {
		if (t != null) {
			t.setLastUpdated(LocalDateTime.now());
		}
		return super.edit(t);
	}

}
