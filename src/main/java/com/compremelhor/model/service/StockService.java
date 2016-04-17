package com.compremelhor.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
import com.compremelhor.model.exception.UnknownAttributeException;

@Stateless
public class StockService extends AbstractService<Stock>{
	@Inject private StockDao stDao;
	@Inject private SkuPartnerService skuPartnerService;
	
	@Override
	protected void setDao() {super.dao = this.stDao;}
	
	@Override 
	protected void setStrategies() {}
	
	@Lock(LockType.WRITE)
	public void create(Stock st) throws InvalidEntityException {
		if (st.getSkuPartner() != null) {
			SkuPartner sp = st.getSkuPartner();
			
			if (sp.getId() != 0) {
				sp = skuPartnerService.find(sp.getId());
				if (sp == null) throw new InvalidEntityException("stock.skupartner.not.found");
			}
			
			Sku s = sp.getSku();
			Partner p = sp.getPartner();
			
			if (s == null) {
				throw new InvalidEntityException("skupartner.sku.is.null.message.error");
			} else if (p == null){
				throw new InvalidEntityException("skupartner.partner.is.null.message.error");
			}
			
			if (skuPartnerService.findSkuPartnerBySkuIdAndPartnerId(s.getId(), p.getId()) == null) {
				skuPartnerService.create(sp);
			}
			
			if (stDao.findStockBySkuPartnerId(sp.getId()) != null) {
				throw new InvalidEntityException("stock.skupartner.duplicate.message.error");
			}
		}
		super.create(st);
	}
	
	@Lock(LockType.READ)
	@Override
	public List<Stock> findAll() {
		return super.findAll();
	}
	
	@Lock(LockType.READ)
	@Override
	public List<Stock> findAll(int start, int size) {
		return super.findAll(start, size);
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
	
	private void removeStockAndSkuPartner(Stock st) {
		if (st == null) {
			throw new RuntimeException("In StockService.removeStockAndSkuPartner(STOCK): stock can not be null");
		}
		
		SkuPartner sp = st.getSkuPartner();
		
		if (sp == null) {
			throw new RuntimeException("In StockService.removeStockAndSkuPartner(STOCK): skuPartner stock attribute was not loaded");
		}
		stDao.remove(st);
		skuPartnerService.remove(sp);
		
	}
	
	@Lock(LockType.WRITE)
	@Override
	public void remove(Stock st) {
		removeStockAndSkuPartner(st);
	}
	
	@Lock(LockType.READ)
	@Override
	public Stock find(Map<String, Object> params) throws UnknownAttributeException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try {
			props.load(classLoader.getResourceAsStream("entity_properties.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String attrs = (String) props.get("stock");
		
		Set<Map.Entry<String, Object>> entries = params.entrySet();
		
		for (Map.Entry<String, Object> pair : entries) {
			if (!Arrays.asList(attrs.split("#")).contains(pair.getKey().trim())) {
				throw new UnknownAttributeException("Unknown stock attribute: " + pair.getValue());
			}
		}
		return stDao.find(params);
	}

}
