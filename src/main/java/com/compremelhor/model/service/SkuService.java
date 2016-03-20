package com.compremelhor.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.exception.InvalidEntityException;

@Stateless
public class SkuService extends AbstractService<Sku>{
	
	@Inject	private SkuDao skuDao;
	@Inject private CategoryService categoryService; 
	@Inject private CategoryDao categoryDao;
	@Inject private Logger logger;
	
	@Override
	protected void setDao() { super.dao = this.skuDao;}
	
	public void create(Sku s) throws InvalidEntityException {
		validate(s);
		
		if (s.getCategories() != null)
			s.getCategories().stream().forEach(c -> {
				c.setDateCreated(LocalDateTime.now());
				c.setLastUpdated(LocalDateTime.now());
				resolveCategory(s, c);
			});
		
		
		dao.persist(s);
	}

	@Override
	public void remove(Sku t) {
		super.remove(t);
	}
	
	public Sku edit(Sku sku) throws InvalidEntityException {
		validate(sku);
		syncCategoriesFromSku(sku);
		return dao.edit(sku);
	}
	
	
	public void addCategory(Sku s, Category c) {
		resolveCategory(s, c);
		dao.edit(s);
	}
	
	
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
			
			if ((ca != null && !ca.getName().equals(c.getName()) ) || (ca = categoryService.findCategoryByName(c.getName())) == null) {
				logger.log(Level.WARNING, "Categoria criada: " + c.getName());
				logger.log(Level.WARNING, "Categoria criada: " + c);
				c.setDateCreated(LocalDateTime.now());
				c.setLastUpdated(LocalDateTime.now());
				categoryDao.persist(c);
				c = categoryDao.edit(c);
				logger.log(Level.WARNING, "Categoria criada: (ID) " + c.getId());
			} else {
				c = ca;
			}
		}
		
		categoriesDB.stream().forEach(cat -> {
			if (!categories.contains(cat)) {
				logger.log(Level.WARNING, "Categoria removida: " + cat.getId());
				removeSkuCategory(sku.getId(), cat.getId());
			}
		});
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
