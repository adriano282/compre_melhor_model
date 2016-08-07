package com.compremelhor.model.strategy.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.entity.SyncronizeMobile;
import com.compremelhor.model.entity.SyncronizeMobile.Action;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.service.PurchaseService;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.service.SyncronizeMobileService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnAfterCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnAfterEditServiceAction;

@OnAfterCreateServiceAction
@OnAfterEditServiceAction
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ComputeItemsWithReservesStrategy implements Strategy<Purchase> {

	private StockReserveService stockReserveService;
	private PurchaseService purchaseService;
	private SyncronizeMobileService syncronizeMobileService;
	
	private int postponeExpireInMinutes = 10;
	
	public ComputeItemsWithReservesStrategy(StockReserveService stockReserveService, PurchaseService purchaseService, SyncronizeMobileService syncronizeMobileService) {
		this.stockReserveService = stockReserveService;
		this.purchaseService = purchaseService;
		this.syncronizeMobileService = syncronizeMobileService;
	}
	
	@Override
	public Status process(Purchase p) {
		Status status = new Status();
		
		if (p == null) return status;
		
		if (p.getStatus() != Purchase.Status.STARTED_TRANSACTION)
			return status;
		
		
		HashMap<String, String> errors = new HashMap<>();

		try {
			
			Stream<PurchaseLine> lines = purchaseService.getPurchaseLinesStream(p);
							
			for (PurchaseLine line : lines.collect(Collectors.toList())) {
				
				if (!isReserved(line, p)) {
					// There isn't a reserve
					
					double qtde = stockReserveService.getAvailableStockQuantity(line.getStock());
					// Is it not possible to make a reserve again?
					if (line.getQuantity() > qtde) {
						errors.put("purchase", "purchase.purchaseLine.quantity.no.more.available");
						purchaseService.removeItem(line);
						registerSyncPendency(line, p);
					} else {
						// Yes, it is possible  
						reserveItemQuantityAgain(line, p);
					}
				}
			}
			status.setErrors(errors);
			return status;
			
		} catch (Exception e) {
			System.out.println("ComputeItemsWithReservesStrategy.validate(Purchase): " + e.getMessage());
			errors.put("purchase", "internal.server.error");
			status.setErrors(errors);
			return status;
		}
	}

	private boolean isReserved(final PurchaseLine line, final Purchase p) throws UnknownAttributeException {
		// Is there a reserve for this item?
		Predicate<StockReserve> isItReserve = (reserve) -> {
			return line.getStock().getId() == reserve.getStock().getId()
					&& p.getUser().getId() == reserve.getPurchase().getUser().getId(); 
		};
		
		return getReserves(p).stream().anyMatch(isItReserve);
	}
	
	private List<StockReserve> getReserves(Purchase p) throws UnknownAttributeException {
		Map<String, Object> params = new HashMap<>();
		params.put("purchase.id", p.getId());
	
		return stockReserveService.findAll(params);
	}
	
	private void registerSyncPendency(PurchaseLine line, Purchase p) throws InvalidEntityException {
		SyncronizeMobile sm = new SyncronizeMobile();
		sm.setAction(Action.REMOVED);
		sm.setEntityId(line.getId());
		sm.setEntityName("purchaseLine");
		sm.setMobileUserIdRef(p.getUser().getId());
		
		syncronizeMobileService.create(sm);
	}
	
	
	private void reserveItemQuantityAgain(PurchaseLine line, Purchase p) throws InvalidEntityException {
		StockReserve sr = new StockReserve();
		sr.setPurchase(p);
		sr.setDateCreated(line.getDateCreated().plusMinutes(postponeExpireInMinutes));
		sr.setStock(line.getStock());
		sr.setReservedQuantity(line.getQuantity());
		
		stockReserveService.create(sr);
	}
}
