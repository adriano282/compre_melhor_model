package com.compremelhor.model.service;

import javax.inject.Inject;

import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Sku;

public class SkuService extends AbstractService<Sku>{
	
	@Inject	private SkuDao skuDao;

	@Override
	protected void setDao() { super.dao = this.skuDao;}	
}
