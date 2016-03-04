package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;

@Stateless
public class SkuPartnerDao extends AbstractDao<SkuPartner>{
	private static final long serialVersionUID = 1L;

	public SkuPartnerDao() {
		super(SkuPartner.class);
	}

	public SkuPartner findSkuPartnerBySku(Sku sku) {
		if (sku == null) {
			throw new NullPointerException("In SkuPartnerDao.findSkuPartnerBySku(SKU): sku must not be null");
		} else if (sku.getId() == 0) {
			throw new IllegalArgumentException("In SkuPartnerDao.findSkuPartnerBySku(SKU): id property on sku can not be null");
		}
		
		return getEntityManager().createQuery(
				"select sp from SkuPartner sp "
				+ "JOIN sp.sku s "
				+ "JOIN sp.partner p "
				+ "WHERE s.id = ?1 ", SkuPartner.class)
				.setParameter(1, sku.getId())
				.getResultList().get(0);
	}
}
