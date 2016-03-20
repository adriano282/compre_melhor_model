package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;

@Stateless
public class SkuPartnerDao extends AbstractDao<SkuPartner>{
	private static final long serialVersionUID = 1L;

	public SkuPartnerDao() { super(SkuPartner.class); }

	public SkuPartner findSkuPartnerBySku(Sku sku) {
		if (sku == null) {
			throw new NullPointerException("In SkuPartnerDao.findSkuPartnerBySku(SKU): sku must not be null");
		} else if (sku.getId() == 0) {
			throw new IllegalArgumentException("In SkuPartnerDao.findSkuPartnerBySku(SKU): id property on sku can not be null");
		}
		
		List<SkuPartner> skuPartners = getEntityManager().createQuery(
				"select sp from SkuPartner sp "
				+ "JOIN sp.sku s "
				+ "JOIN sp.partner p "
				+ "WHERE s.id = ?1 ", SkuPartner.class)
				.setParameter(1, sku.getId())
				.getResultList();
		
		if (skuPartners == null) return null;
		if (skuPartners.size() == 0) return null;
		return skuPartners.get(0);
	}
	
	public SkuPartner findSKuPartnerBySkuIdAndPartnerId(int skuId, int partnerId) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<SkuPartner> criteriaQuery = cb.createQuery(SkuPartner.class);
		
		Root<SkuPartner> skuPartner = criteriaQuery.from(SkuPartner.class);
		
		criteriaQuery.select(skuPartner)
			.where(cb.equal(skuPartner.get("sku").get("id"), skuId))
			.where(cb.equal(skuPartner.get("partner").get("id"), partnerId));
		
		List<SkuPartner> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 0) return result.get(0);
		return null;
	}
}
