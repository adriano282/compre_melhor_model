package com.compremelhor.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.PurchaseDao;
import com.compremelhor.model.dao.PurchaseLineDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;

@Stateless
public class PurchaseService extends AbstractService<Purchase> {
	private static final long serialVersionUID = 1L;
	@Inject private PurchaseDao purchaseDao;
	@Inject	private PurchaseLineDao purchaseLineDao;
	@Inject private FreightService freightService;
	

	@Override
	protected void setDao() {super.dao = this.purchaseDao; }
	@Override 
	protected void setStrategies() {}
	
	public void addItem(Purchase purchase, PurchaseLine line) throws InvalidEntityException {
		verifyIfPurchaseExist(purchase);
		line.setPurchase(purchase);
		validate(purchase);
		purchaseLineDao.persist(line);
	}
		
	public List<PurchaseLine> findAllItensByPurchase(Purchase purchase) {
		return purchaseLineDao.findAllItensByPurchase(purchase);
	}
	
	public List<PurchaseLine> findAllItensByPurchase(Purchase purchase, boolean skuEager) {
		List<PurchaseLine> lines = purchaseLineDao.findAllItensByPurchase(purchase);
		
		if (lines != null && skuEager) {
			for (PurchaseLine pl : lines) {
				if (pl.getStock() != null &&
						pl.getStock().getSkuPartner() != null &&
						pl.getStock().getSkuPartner().getSku() != null) {
					pl.getStock().getSkuPartner().getSku();
				}
			}
		}
		
		return lines;
	}
	
	public PurchaseLine findLine(int id) {
		return purchaseLineDao.find(id);
	}
	
	public PurchaseLine editItem(PurchaseLine line) throws InvalidEntityException {
		validate(line);
		return purchaseLineDao.edit(line);
	}
	
	public void removeItem(PurchaseLine line) {
		purchaseLineDao.remove(line);
	}
	
	public Freight findFreightByPurchase(Purchase purchase) {
		return freightService.findFreightByPurchase(purchase);
	}
	
	public void removeFreight(Freight freight) {
		freightService.remove(freight);
	}
	
	public Freight findFreight(int id) {
		return freightService.find(id);
	}
	
	public void addFreight(Purchase purchase, Freight freight) throws InvalidEntityException {
		if (verifyIfPurchaseExist(purchase).getFreight() != null)
			throw new RuntimeException("Exception in PurchaseService addFreight(PURCHASE, FREIGHT): this purchase already has a freight.");
		
		freight.setPurchase(purchase);
		freightService.create(freight);
	}
	
	private Purchase verifyIfPurchaseExist(Purchase purchase) {
		Purchase p = purchaseDao.find(purchase.getId()); 
		if (p == null) {
			throw new RuntimeException("Unknow Purchase with ID " + purchase.getId());
		}
		return p;
	}
	@Override
	public Purchase find(Map<String, Object> params) throws UnknownAttributeException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try {
			props.load(classLoader.getResourceAsStream("entity_properties.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String attrs = (String) props.get("purchase");
		
		Set<Map.Entry<String, Object>> entries = params.entrySet();
		
		for (Map.Entry<String, Object> pair : entries) {
			if (!Arrays.asList(attrs.split("#")).contains(pair.getKey().trim())) {
				throw new UnknownAttributeException("Unknown purchase attribute: " + pair.getValue());
			}
		}
		return purchaseDao.find(params);
	}

}
