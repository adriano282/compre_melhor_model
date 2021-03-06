package com.compremelhor.model.dao;

import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;

@Stateless
public class FreightDao extends AbstractDao<Freight> {
	@Override
	public Freight find(Map<String, Object> params) {
		Freight freight = super.find(params);
		
		if (freight != null) {
			if (freight.getShipAddress() != null) {
				freight.getShipAddress().getNumber();
			}
		}
		return freight;
	}

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
