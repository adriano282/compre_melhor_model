package com.compremelhor.model.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.SkuPartnerDao;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;

@Stateless
public class SkuPartnerService {
	
	@Inject private SkuPartnerDao dao;
	@Inject private Validator validator;
	
	public SkuPartner findSkuPartnerBySku(Sku sku) {
		return dao.findSkuPartnerBySku(sku);
	}
	
	public void create(SkuPartner skuPartner) {
		validator.validate(skuPartner);
		dao.persist(skuPartner);
	}
	
	public SkuPartner edit(SkuPartner skuPartner) {
		validator.validate(skuPartner);
		dao.edit(skuPartner);
		return skuPartner;
	}
	
	public void remove(SkuPartner skuPartner) {
		dao.remove(skuPartner);
	}
}
