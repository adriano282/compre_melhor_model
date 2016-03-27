package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.SkuPartnerDao;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;

public class SkuPartnerService extends AbstractService<SkuPartner>{
	
	@Inject private SkuPartnerDao dao;
		
	@Override
	protected void setDao() { super.dao = this.dao; }
	@Override 
	protected void setStrategies() {}
	
	public SkuPartner findSkuPartnerBySku(Sku sku) {
		return dao.findSkuPartnerBySku(sku);
	}
	
	public SkuPartner findSkuPartnerBySkuIdAndPartnerId(int skuId, int partnerId) {
		return dao.findSKuPartnerBySkuIdAndPartnerId(skuId, partnerId);
	}
}
