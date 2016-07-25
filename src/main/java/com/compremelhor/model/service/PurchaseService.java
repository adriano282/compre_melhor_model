package com.compremelhor.model.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.compremelhor.model.dao.PurchaseDao;
import com.compremelhor.model.dao.PurchaseLineDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.PurchaseLog;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;

@Stateless
public class PurchaseService extends AbstractService<Purchase> {
	private static final long serialVersionUID = 1L;
	
	@Inject private PurchaseDao purchaseDao;
	@Inject private FreightService freightService;
	@Inject private PurchaseLogService purchaseLogService;
	@Inject private PurchaseLineService purchaseLineService;
	@Inject private PurchaseLineDao purchaseLineDao;
	@Inject private StockReserveService stockReserceService;
	@Inject private StockService stockService; 
	
	@Override
	protected void setDao() {super.dao = this.purchaseDao; }
	@Override 
	protected void setStrategies() {}
			
	@Lock(LockType.WRITE)
	public void addItem(Purchase purchase, PurchaseLine line) throws InvalidEntityException {
		verifyIfPurchaseExist(purchase);
		line.setPurchase(purchase);
		validate(purchase);
		purchaseLineService.create(line);
	}
		
	@Lock(LockType.READ)
	public List<PurchaseLine> findAllItensByPurchase(Purchase purchase) {
		return purchaseLineDao.findAllItensByPurchase(purchase);
	}
	
	@Lock(LockType.READ)
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
	
	@Lock(LockType.READ)
	public PurchaseLine findLine(int id) {
		return purchaseLineDao.find(id);
	}
	
	@Lock(LockType.WRITE)
	public PurchaseLine editItem(PurchaseLine line) throws InvalidEntityException {
		validate(line);
		return purchaseLineService.edit(line);
	}
	
	@Lock(LockType.WRITE)
	public void removeItem(PurchaseLine line) {
		purchaseLineService.remove(line);
	}
	
	@Lock(LockType.READ)
	public Freight findFreightByPurchase(Purchase purchase) {
		return freightService.findFreightByPurchase(purchase);
	}
	
	@Lock(LockType.WRITE)
	public void removeFreight(Freight freight) {
		freightService.remove(freight);
	}
	
	@Lock(LockType.READ)
	public Freight findFreight(int id) {
		return freightService.find(id);
	}
	
	@Lock(LockType.WRITE)
	public void addFreight(Purchase purchase, Freight freight) throws InvalidEntityException {
		if (verifyIfPurchaseExist(purchase).getFreight() != null)
			throw new RuntimeException("Exception in PurchaseService addFreight(PURCHASE, FREIGHT): this purchase already has a freight.");
		
		freight.setPurchase(purchase);
		freightService.create(freight);
	}
	
	@Override
	@Lock(LockType.WRITE)
	public void create(Purchase t) throws InvalidEntityException {
		try {
			super.create(t);
			logHistory(t);
			
		} catch (InvalidEntityException e) {
			throw e;
		}
	}
	
	@Override
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Purchase edit(Purchase t) throws InvalidEntityException {
		try {
			Purchase p = super.edit(t);
			
			if (t.getStatus().equals(Purchase.Status.PAID)) {
				upgradeStocksWithReserves(p);
			}
			
			logHistory(p);
			return p;
		} catch (InvalidEntityException e) {
			throw e;
		}
	}
	
	@Lock(LockType.WRITE)
	private void upgradeStocksWithReserves(Purchase p) {
		Map<String, Object> params = new HashMap<>();
		params.put("purchase.id", p.getId());
		
		try {
			List<StockReserve> reserves = stockReserceService.findAll(params);
			
			for (StockReserve reserve : reserves) {
				Stock st = stockService.find(reserve.getStock().getId());
				
				if (st != null) {
					Double newQuantity = st.getQuantity() - reserve.getReservedQuantity();
					st.setQuantity(newQuantity);
					stockService.edit(st);
					stockReserceService.remove(reserve);
				}
			}
			
		} catch (UnknownAttributeException e) {
			System.out.println("PurchaseService.UpgradeStocksWithReserves(Purchase): Error while trying find attribute on StockReserve class");
			throw new RuntimeException(e);
		} catch (InvalidEntityException e) {
			System.out.println("PurchaseService.UpgradeStocksWithReserves(Purchase): Error while trying upgrade stocks");
			throw new RuntimeException(e);
		}
	}

	@Lock(LockType.READ)
	private Purchase verifyIfPurchaseExist(Purchase purchase) {
		Purchase p = purchaseDao.find(purchase.getId()); 
		if (p == null) {
			throw new RuntimeException("Unknow Purchase with ID " + purchase.getId());
		}
		return p;
	}
	
	@Lock(LockType.WRITE)
	private void logHistory(Purchase p) {
		PurchaseLog log = new PurchaseLog();
		log.setPurchaseId(p.getId());
		log.setStatus(p.getStatus());
		log.setLastUpdated(LocalDateTime.now());
		log.setDateCreated(LocalDateTime.now());
		try {
			purchaseLogService.create(log);
		} catch (InvalidEntityException e) {
			e.printStackTrace();
		}
	}
}
