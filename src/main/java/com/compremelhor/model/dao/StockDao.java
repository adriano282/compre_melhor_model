package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Stock;

@Stateless
public class StockDao extends AbstractDao<Stock>{
	private static final long serialVersionUID = 1L;

	public StockDao() { super(Stock.class); }
	
	public Stock findStockBySkuPartnerId(int skuPartnerId) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Stock> criteriaQuery = cb.createQuery(Stock.class);
		
		Root<Stock> stock = criteriaQuery.from(Stock.class);
		
		criteriaQuery
			.select(stock)
			.where(cb.equal(stock.get("skuPartner").get("id"), skuPartnerId));
		
		List<Stock> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 0) return result.get(0);
		return null;
	}

}
