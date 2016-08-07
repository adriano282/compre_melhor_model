package com.compremelhor.model.strategy.stock;

import java.util.HashMap;
import java.util.Map;

import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class SafeStockChangeStrategy implements Strategy<Stock> {

	private StockReserveService reserveService;
	
	public SafeStockChangeStrategy(StockReserveService reserveService) {
		this.reserveService = reserveService;
	}
	
	@Override
	public Status process(Stock t) {
		Status status = new Status();
		Map<String, Object> params = new HashMap<>();
		params.put("stock.id", t.getId());
		
		Double sumReservedStock = reserveService.getAvailableStockQuantity(t);
		
		if (t.getQuantity() < sumReservedStock) {
			Map<String, String> errors = new HashMap<>();
			errors.put("quantity", "stock.quantity.not.enough.for.reserves");
			status.setErrors(errors);
		}
		return status;
	}
}
