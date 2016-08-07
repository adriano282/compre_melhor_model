package com.compremelhor.model.strategy.stock;

import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class RemoveExpiredReservesStrategy extends AbstractRemoveExpiredReservesStrategy<Stock>{

	public RemoveExpiredReservesStrategy(StockReserveService reserveService) {
		super(reserveService);
	}
}
