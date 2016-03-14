package com.compremelhor.model.dao;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;

@Stateless
public class FreightDao extends AbstractDao<Freight> {
	private static final long serialVersionUID = 1L;
	
	public FreightDao() { super(Freight.class);	}
	
	public Freight findFreightByPurchase(Purchase purchase) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Freight> criteriaQuery = cb.createQuery(Freight.class);
		
		Root<Freight> root = criteriaQuery.from(Freight.class);
		
		criteriaQuery
			.select(root)
			.where(cb.equal(root.get("purchase"), purchase));
			
		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}
}
