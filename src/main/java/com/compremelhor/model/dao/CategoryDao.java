package com.compremelhor.model.dao;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Category;

@Stateless
public class CategoryDao extends AbstractDao<Category> {
	private static final long serialVersionUID = 1L;

	public CategoryDao() { super(Category.class); }
	
	public Category find(int id) {
		return getEntityManager().find(Category.class, id);
	}
	
	public Category find(Object ob, Set<String> fetches) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Category> criteriaQuery = cb.createQuery(Category.class);
		Root<Category> cat = criteriaQuery.from(Category.class);
		
		if (fetches.contains("skus"))
			cat.fetch("skus");
		
		criteriaQuery
			.select(cat)
			.where(cb.equal(cat.get("id"), ob));
		
		List<Category> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result == null) return null;
		if (result.size() == 0) return null;
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}		
		
		return result.get(0);		
	}
	
	public Category findCategoryByName(String name) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Category> criteriaQuery = cb.createQuery(Category.class);
		
		Root<Category> mfr = criteriaQuery.from(Category.class);
		criteriaQuery
			.select(mfr)
			.where(cb.equal(mfr.get("name"), name));
		
		List<Category> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result");
		}
		
		if (result != null && result.size() == 0) return null;
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> findCategoriesBySkuId(int skuId) {
		return (List<Category>) getEntityManager().createNativeQuery(
				"select c.id, c.name, c.date_created, c.last_updated from sku_category sc "
						+ "inner join category c on c.id = sc.category_id "
						+ "where sc.sku_id = ?1", Category.class)
						.setParameter(1, skuId).getResultList();
	}
	
	public Category findCategoryBySkuId(int skuId) {
		@SuppressWarnings("unchecked")
		List<Category> result = (List<Category>) getEntityManager().createNativeQuery(
				"select c.id, c.name, c.date_created, c.last_updated from sku_category sc "
				+ "inner join category c on c.id = sc.category_id "
				+ "where sc.sku_id = ?1", Category.class)
				.setParameter(1, skuId).getResultList();
		
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
		
	}
}
