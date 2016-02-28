package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Category;

@Stateless
public class CategoryDao extends AbstractDao<Category> {
	private static final long serialVersionUID = 1L;

	public CategoryDao() {
		super(Category.class);
	}
	
	@Override
	public Category find(Object ob) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Category> criteriaQuery = cb.createQuery(Category.class);
		Root<Category> cat = criteriaQuery.from(Category.class);
		cat.fetch("skus");
		criteriaQuery
			.select(cat)
			.where(cb.equal(cat.get("id"), ob));
		
		List<Category> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}		
		return result.get(0);		
	}
}
