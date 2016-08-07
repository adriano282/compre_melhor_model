package com.compremelhor.model.strategy.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.service.PurchaseLineService;
import com.compremelhor.model.service.PurchaseService;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;
import com.compremelhor.model.strategy.stock.AbstractRemoveExpiredReservesStrategy;

@OnCreateServiceAction
@OnEditServiceAction
public class RemoveAllExpiredReservesStrategy implements Strategy<Purchase>{

	private StockReserveService reserveService;
	private PurchaseService purchaseService;
	private PurchaseLineService lineService;
	
	public RemoveAllExpiredReservesStrategy(StockReserveService reserveService, PurchaseService purchaseService, PurchaseLineService purchaseLineService) {
		this.reserveService = reserveService;
		this.purchaseService = purchaseService;
		this.lineService = purchaseLineService;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Status process(Purchase t) {
		HashMap<String, Object> p = new HashMap<>();
		p.put("purchase.id", t.getId());
		List<PurchaseLine> lines = new ArrayList<>();
		try {
			lines = lineService.findAll(p);
		} catch (UnknownAttributeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		HashMap<String, Object> params = null;
		for (PurchaseLine l : lines) {
			try {
				params = new HashMap<>();
				params.put("stock.id", l.getStock().getId());
			
				List<StockReserve> stockReserves =  reserveService.findAll(params);
				
				if (stockReserves != null)
				System.out.println(stockReserves.size());
				
				if (stockReserves != null) {
					for (StockReserve s : stockReserves) {
						if (AbstractRemoveExpiredReservesStrategy.isExpired(s.getDateCreated())) {
							reserveService.remove(s);
							System.out.println("Removed Reserve: " + s);
						}
					}
				}
				
			} catch (UnknownAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("REMOVED ALL EXPIRED");
		return new Status();
	}
}
