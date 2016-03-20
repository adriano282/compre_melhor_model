package com.compremelhor.model.dao;

import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.PersistenceUnitUtil;

import com.compremelhor.model.entity.Sku;

@Stateless
public class SkuDao extends AbstractDao<Sku> {
	@Override
	public Sku find(int id, Set<String> fetches) {
		Sku s = super.find(id);		
		if (fetches.contains("categories") && s.getCategories() != null)
			s.getCategories().size();		
		return s;
	}
	
	

	@Override
	public Sku edit(Sku entity) {
		Sku s = super.edit(entity);
		getEntityManager().flush();
		return s;
	}

	@Override
	public void persist(Sku entity) {
		super.persist(entity);
		getEntityManager().flush();
	}



	private static final long serialVersionUID = 1L;

	public SkuDao() { super(Sku.class); }
	
	public boolean isLoadedCategoriesInSku(Sku sku) {
		PersistenceUnitUtil unitUtil = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
		return unitUtil.isLoaded(sku, "categories");
	}
	
	public void removeAllSkuCategory(int skuId) {
		getEntityManager().createNativeQuery(
				"delete from sku_category where sku_id = 1?")
				.setParameter(1, skuId).executeUpdate();
		getEntityManager().flush();
	}
	
	public void removeSkuCategory(int skuId, int categoryId) {
		getEntityManager().createNativeQuery(
				"delete from sku_category where sku_id = 1? "
				+ "and category_id = 2?")
				.setParameter(1, skuId)
				.setParameter(2, categoryId).executeUpdate();
		getEntityManager().flush();
	}
}
