package com.compremelhor.model.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Validator;

import com.compremelhor.model.dao.PurchaseDao;
import com.compremelhor.model.dao.PurchaseLineDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;

public class PurchaseService {
	
	@Inject private PurchaseDao purchaseDao;
	@Inject	private PurchaseLineDao purchaseLineDao;
	@Inject private FreightService freightService;
	@Inject private Validator validator;
	
	public void remove(Purchase purchase) {
		purchaseDao.remove(purchase);
	}
	
	public void create(Purchase purchase) {
		validator.validate(purchase);
		purchaseDao.persist(purchase);
	}
	
	public Purchase edit(Purchase purchase) {
		validator.validate(purchase);
		return purchaseDao.edit(purchase);
	}
	
	public Purchase find(int id) {
		return purchaseDao.find(id);
	}
	
	public void addItem(Purchase purchase, PurchaseLine line) {
		verifyIfPurchaseExist(purchase);
		line.setPurchase(purchase);
		validator.validate(line);
		purchaseLineDao.persist(line);
	}
		
	public List<PurchaseLine> findAllItensByPurchase(Purchase purchase) {
		return purchaseLineDao.findAllItensByPurchase(purchase);
	}
	
	public PurchaseLine findLine(int id) {
		return purchaseLineDao.find(id);
	}
	
	public PurchaseLine editItem(PurchaseLine line) {
		validator.validate(line);
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
	
	public void addFreight(Purchase purchase, Freight freight) {
		if (verifyIfPurchaseExist(purchase).getFreight() != null)
			throw new RuntimeException("Exception in PurchaseService addFreight(PURCHASE, FREIGHT): this purchase already has a freight.");
		
		freight.setPurchase(purchase);
		validator.validate(freight);
		freightService.create(freight);
	}
	
	private Purchase verifyIfPurchaseExist(Purchase purchase) {
		Purchase p = purchaseDao.find(purchase.getId()); 
		if (p == null) {
			throw new RuntimeException("Unknow Purchase with ID " + purchase.getId());
		}
		return p;
	}
}
