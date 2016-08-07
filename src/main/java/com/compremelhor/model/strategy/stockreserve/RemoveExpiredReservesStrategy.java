package com.compremelhor.model.strategy.stockreserve;

import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;
import com.compremelhor.model.strategy.stock.AbstractRemoveExpiredReservesStrategy;

@OnCreateServiceAction
@OnEditServiceAction
public class RemoveExpiredReservesStrategy extends AbstractRemoveExpiredReservesStrategy<StockReserve> {

	public RemoveExpiredReservesStrategy(
			StockReserveService reserveService) {
		super(reserveService);
	}

}
