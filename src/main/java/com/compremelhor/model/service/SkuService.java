package com.compremelhor.model.service;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Inject;

import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.exception.InvalidEntityException;

public class SkuService extends AbstractService<Sku>{
	
	@Inject	private SkuDao skuDao;
	@Inject private CategoryService categoryService; 
	@Inject private Logger logger;
	
	@Override
	protected void setDao() { super.dao = this.skuDao;}
	
	@Lock(LockType.WRITE)
	@Override
	public void remove(Sku t) {
		super.remove(t);
	}
	
	@Lock(LockType.READ)
	@Override
	public Sku find(int id) {
		return super.find(id);
	}
	
	@Lock(LockType.READ)
	@Override
	public List<Sku> findAll() {
		// TODO Auto-generated method stub
		return super.findAll();
	}

	@Lock(LockType.READ)
	@Override
	public List<Sku> findAll(int start, int size) {
		// TODO Auto-generated method stub
		return super.findAll(start, size);
	}

	@Lock(LockType.READ)
	@Override
	public Sku find(int id, Set<String> fetches) {
		// TODO Auto-generated method stub
		return super.find(id, fetches);
	}

	
	@Lock(LockType.WRITE)
	public void create(Sku s) throws InvalidEntityException {
		validate(s);
		
		if (s.getCategories() != null)
			s.getCategories().stream().forEach(c -> resolveCategory(s, c));
		
		
		dao.persist(s);
	}
	
	@Lock(LockType.WRITE)
	public Sku edit(Sku sku) throws InvalidEntityException {
		validate(sku);
		syncCategoriesFromSku(sku);
		return dao.edit(sku);
	}
	
	@Lock(LockType.WRITE)
	public void addCategory(Sku s, Category c) {
		resolveCategory(s, c);
		dao.edit(s);
	}
	
	@Lock(LockType.WRITE)
	public void removeSkuCategory(int skuId, int categoryId) {
		skuDao.removeSkuCategory(skuId, categoryId);
	}
	
	private void syncCategoriesFromSku(Sku sku) throws InvalidEntityException {
		List<Category> categoriesDB = categoryService.findCategoriesBySkuId(sku.getId());
		
		if (!skuDao.isLoadedCategoriesInSku(sku)) return;
		
		
		Set<Category> categories = sku.getCategories();
		
		if (categories == null) {
			logger.log(Level.WARNING, "skuId" + sku.getId());
			skuDao.removeAllSkuCategory(sku.getId());
			return;
		}
		
		for (Category c : categories) {
			Category ca = categoryService.find(c.getId());
			
			if (ca != null && !ca.getName().equals(c.getName()) || (ca = categoryService.findCategoryByName(c.getName())) == null) {
				logger.log(Level.WARNING, "Categoria criada: " + c);
				categoryService.create(c);
			} else {
				c = ca;
			}
			
			final int id = c.getId();
			if (!categoriesDB.stream().anyMatch( category -> category.getId() == id)) {
				logger.log(Level.WARNING, "Categoria removida: " + id);
				removeSkuCategory(sku.getId(), id);
			}
		}
	}
	
	private void resolveCategory(Sku sku, Category c) {
		Category ca = categoryService.findCategoryByName(c.getName());
		
		if (categoryService.findCategoryBySkuId(sku.getId()) == null) {
			if (ca == null) {
				if (!sku.getCategories().contains(c)) sku.addCategory(c);
			} else {
				sku.addCategory(ca);
			}
		} else {
			if (!sku.getCategories().contains(c)) sku.addCategory(ca);
			else {
				sku.getCategories().remove(c);
				sku.getCategories().add(ca);
			}
			
		}
	}
}
