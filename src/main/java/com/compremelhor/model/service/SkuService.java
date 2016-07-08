package com.compremelhor.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;

@Stateless
public class SkuService extends AbstractService<Sku> implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject	private SkuDao skuDao;
	@Inject private CategoryService categoryService; 
	@Inject private CategoryDao categoryDao;
	@Inject private Logger logger;
	
	@Override
	protected void setDao() { super.dao = this.skuDao;}
	@Override 
	protected void setStrategies() {}
	
	public void create(Sku s) throws InvalidEntityException {
		validate(s);
		
		if (s.getCategory() != null) {
			s.setDateCreated(LocalDateTime.now());
			s.setLastUpdated(LocalDateTime.now());
		}
		
		
		dao.persist(s);
	}

	public Sku edit(Sku sku) throws InvalidEntityException {
		validate(sku);
		return dao.edit(sku);
	}
	
	
	public void addCategory(Sku s, Category c) {
		dao.edit(s);
	}
	
	@Override
	public Sku find(Map<String, Object> params) throws UnknownAttributeException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try {
			props.load(classLoader.getResourceAsStream("entity_properties.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String attrs = (String) props.get("sku");
		
		Set<Map.Entry<String, Object>> entries = params.entrySet();
		
		for (Map.Entry<String, Object> pair : entries) {
			if (!Arrays.asList(attrs.split("#")).contains(pair.getKey().trim())) {
				throw new UnknownAttributeException("Unknown sku attribute: " + pair.getValue());
			}
		}
		return skuDao.find(params);
	}
}
