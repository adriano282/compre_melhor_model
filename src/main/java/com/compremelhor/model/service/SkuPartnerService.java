package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.SkuPartnerDao;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;

@Stateless
public class SkuPartnerService {
	
	@Inject
	private SkuPartnerDao dao;
	
	public SkuPartner findSkuPartnerBySku(Sku sku) {
		return dao.findSkuPartnerBySku(sku);
	}
}
