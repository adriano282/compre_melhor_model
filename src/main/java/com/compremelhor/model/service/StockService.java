package com.compremelhor.model.service;

import java.util.List;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.StockDao;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.exception.InvalidEntityException;

@Stateless
public class StockService extends AbstractService<Stock>{
	@Inject private StockDao stDao;
	@Inject private SkuPartnerService skuPartnerService;
	
	@Override
	protected void setDao() {super.dao = this.stDao;}
	
	@Lock(LockType.WRITE)
	public void create(Stock st) throws InvalidEntityException {
		super.create(st);
	}
	
	@Lock(LockType.READ)
	@Override
	public List<Stock> getAll() {
		return super.getAll();
	}
	
	@Lock(LockType.READ)
	@Override
	public List<Stock> getAll(int start, int size) {
		return super.getAll(start, size);
	}
	
	@Lock(LockType.WRITE)
	public void createStock(Partner partner, Sku sku) throws InvalidEntityException {
		final SkuPartner sp = new SkuPartner();
		sp.setPartner(partner);
		sp.setSku(sku);
		
		final Stock st = new Stock();
		st.setQuantity(0.0);
		st.setSkuPartner(sp);
		st.setUnitPrice(0.00);		
				
		skuPartnerService.create(sp);
		create(st);
	}
	
	@Lock(LockType.READ)
	@Override
	public Stock find(int id) {
		return super.find(id);
	}
	
	@Lock(LockType.WRITE)
	@Override
	public Stock edit(Stock stock) throws InvalidEntityException {
		return super.edit(stock);
	}
	
	@Lock(LockType.WRITE)
	public Stock addStock(Stock stock, Double quantity) throws InvalidEntityException { 
		Stock st = stDao.find(stock.getId());
		st.setQuantity(quantity + st.getQuantity());
		return edit(st);
	}
	
	@Lock(LockType.WRITE)
	public void removeStockAndSkuPartner(Stock st) {
		if (st == null) {
			throw new RuntimeException("In StockService.removeStockAndSkuPartner(STOCK): stock can not be null");
		}
		
		SkuPartner sp = st.getSkuPartner();
		
		if (sp == null) {
			throw new RuntimeException("In StockService.removeStockAndSkuPartner(STOCK): skuPartner stock attribute was not loaded");
		}
		remove(st);
		skuPartnerService.remove(sp);
		
	}
	
	@Lock(LockType.WRITE)
	@Override
	public void remove(Stock st) {
		super.remove(st);
	}
}
