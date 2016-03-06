package com.compremelhor.model.service;


import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.StockDao;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.entity.Stock;

@Stateless
public class StockService {
	@Inject private StockDao stDao;
	@Inject private SkuPartnerService skuPartnerService;
	@Inject private Validator validator;
	
	@Lock(LockType.WRITE)
	public void createStock(Partner partner, Sku sku) {
		final SkuPartner sp = new SkuPartner();
		sp.setPartner(partner);
		sp.setSku(sku);
		
		final Stock st = new Stock();
		st.setQuantity(0.0);
		st.setSkuPartner(sp);
		st.setUnitPrice(0.00);
		
		
				
		skuPartnerService.create(sp);
		validator.validate(st);
		stDao.persist(st);
	}
	
	@Lock(LockType.READ)
	public Stock find(int id) {
		return stDao.find(id);
	}
	
	@Lock(LockType.WRITE)
	public Stock edit(Stock stock) {
		validator.validate(stock);
		validator.validate(stock.getSkuPartner());		
		return stDao.edit(stock);
	}
	
	@Lock(LockType.WRITE)
	public Stock addStock(Stock stock, Double quantity) { 
		Stock st = stDao.find(stock.getId());
		st.setQuantity(quantity + st.getQuantity());
		validator.validate(stock);
		return stDao.edit(st);
	}
	
	@Lock(LockType.WRITE)
	public synchronized void removeStockAndSkuPartner(Stock st) {
		if (st == null) {
			throw new RuntimeException("In StockService.removeStockAndSkuPartner(STOCK): stock can not be null");
		}
		
		SkuPartner sp = st.getSkuPartner();
		
		if (sp == null) {
			throw new RuntimeException("In StockService.removeStockAndSkuPartner(STOCK): skuPartner stock attribute was not loaded");
		}
		skuPartnerService.remove(sp);
	}
}
