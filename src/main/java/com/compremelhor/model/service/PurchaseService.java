package com.compremelhor.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.compremelhor.model.dao.PurchaseDao;
import com.compremelhor.model.dao.PurchaseLineDao;
import com.compremelhor.model.dao.StockReserveDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.purchase.ComputeItemsWithReservesStrategy;
import com.compremelhor.model.strategy.purchase.ComputeStocksWithReserves;
import com.compremelhor.model.strategy.purchase.RegisterPurchaseLogStrategy;
import com.compremelhor.model.strategy.purchase.RemoveAllExpiredReservesStrategy;


@Stateless
public class PurchaseService extends AbstractService<Purchase> {
	private static final long serialVersionUID = 1L;
	
	@Inject private PurchaseDao purchaseDao;
	@Inject private FreightService freightService;
	@Inject private StockService stockService;
	@Inject private StockReserveDao reserveDao;
	@Inject private PurchaseLogService purchaseLogService;
	@Inject private PurchaseLineService purchaseLineService;
	@Inject private PurchaseLineDao purchaseLineDao;
	@Inject private StockReserveService stockReserveService;
	@Inject private PurchaseService purchaseService;
	@Inject private SyncronizeMobileService syncronizeMobileService;
	
	@Override
	protected void setDao() {super.dao = this.purchaseDao; }
	
	@Override 
	protected void setStrategies() {
		List<Strategy<Purchase>> strategies = new ArrayList<>();
		strategies.add(new RegisterPurchaseLogStrategy(purchaseLogService));
		strategies.add(new RemoveAllExpiredReservesStrategy(stockReserveService, purchaseLineService));
		strategies.add(new ComputeItemsWithReservesStrategy(stockReserveService, purchaseService, syncronizeMobileService));
		strategies.add(new ComputeStocksWithReserves(stockReserveService, stockService, reserveDao, purchaseService));
		
		super.strategies = strategies;		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(Purchase t) throws InvalidEntityException {
		super.create(t);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Purchase edit(Purchase t) throws InvalidEntityException {
		return super.edit(t);
	}
	
	public void addItem(Purchase purchase, PurchaseLine line) throws InvalidEntityException {
		verifyIfPurchaseExist(purchase);
		line.setPurchase(purchase);
		purchaseLineService.create(line);
	}
		
	public List<PurchaseLine> findAllItensByPurchase(Purchase purchase) {
		if (purchase == null) return null;
		return purchaseLineDao.findAllItensByPurchaseId(purchase.getId());
	}
	
	public Stream<PurchaseLine> getPurchaseLinesStream(Purchase purchase) {
		if (purchase == null) return null;
		List<PurchaseLine> lines = purchaseLineDao.findAllItensByPurchaseId(purchase.getId());
		if (lines != null)
			return lines.stream();
		
		return null;
	}
	
	public List<PurchaseLine> findAllItensByPurchase(Purchase purchase, boolean skuEager) {
		if (purchase == null) return null;
		List<PurchaseLine> lines = purchaseLineDao.findAllItensByPurchaseId(purchase.getId());
		
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
		return purchaseLineService.edit(line);
	}
	
	public void removeItem(PurchaseLine line) {
		purchaseLineService.remove(line);
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
}
