package com.compremelhor.model.strategy.stockreserve;

import java.util.HashMap;
import java.util.Map;

import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class ReserveStockStrategy implements Strategy<StockReserve> {
	
	private StockReserveService reserveService;
	
	public ReserveStockStrategy(StockReserveService reserveService) {
		this.reserveService = reserveService;
	}
	
	@Override
	public Status process(StockReserve t) {
		Status status = new Status();
		
		if (t == null) {
			throw new IllegalArgumentException("ReserveStockStrategy.validate(StockReserve): StockReserve is NULL");
		}
		
		if (t.getStock() == null) {
			throw new IllegalArgumentException("ReserveStockStrategy.validate(StockReserve): Stock is NULL");
		}
		
		if (reserveService.getAvailableStockQuantity(t.getStock(), t.getId()) 
				< t.getReservedQuantity()) {
			
			Map<String, String> errors = new HashMap<>();
			errors.put("reservedQuantity", "stockreserve.reservedquantity.there.are.not.stock.enough");
			status.setErrors(errors);
		}
		return status;
	}

}
