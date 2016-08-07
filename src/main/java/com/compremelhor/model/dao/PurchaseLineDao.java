package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.PurchaseLine;

@Stateless
public class PurchaseLineDao  extends AbstractDao<PurchaseLine> {
	private static final long serialVersionUID = 1L;
	
	public PurchaseLineDao() { super(PurchaseLine.class); }
	
	public List<PurchaseLine> findAllItensByPurchaseId(int purchaseId) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<PurchaseLine> criteriaQuery = cb.createQuery(PurchaseLine.class);
		
		Root<PurchaseLine> root = criteriaQuery.from(PurchaseLine.class);
		
		criteriaQuery
			.select(root)
			.where(cb.equal(root.get("purchase").get("id"), purchaseId));
		
		List<PurchaseLine> result = getEntityManager()
						.createQuery(criteriaQuery)
						.getResultList();
		
		return result;
	}
}
