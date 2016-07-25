package com.compremelhor.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.StockReserveDao;
import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.stock.RemoveExpiredReservesStrategy;
import com.compremelhor.model.strategy.stock.ReserveStockStrategy;

@Stateless
public class StockReserveService extends AbstractService<StockReserve> {
	private static final long serialVersionUID = 1L;
	
	@Inject private StockReserveDao dao;
		
	@Override
	protected void setDao() {
		super.dao = this.dao;
	}

	@Override
	protected void setStrategies() {
		List<Strategy<? extends EntityModel>> strategies = new ArrayList<>();
		strategies.add(new ReserveStockStrategy(this));
		strategies.add(new RemoveExpiredReservesStrategy(this));
		super.strategies = strategies;
	}
	
	public double getAvailableStockQuantity(Stock stock, int ... reserveIdsToIgnore) {
		if (stock == null || stock.getId() == 0) 
			throw new IllegalArgumentException("StockReserveService.getAvailableStockQuantity(Stock): Stock and it's id mustn't be null.");
		
		if (stock.getQuantity() == null)
			throw new IllegalArgumentException("StockReserveService.getAvailableStockQuantity(Stock): Stock Quantity mustn't be null.");
		
		Map<String, Object> params = new HashMap<>();
		params.put("id", stock.getId());

		double sumReservedStock = getSum(dao.findAll(params).stream(), reserveIdsToIgnore);
		
		return stock.getQuantity().doubleValue() - sumReservedStock;
	}
	
	private double getSum(Stream<StockReserve> stream, int ... reserveIdsToIgnore) {
		if (reserveIdsToIgnore != null && reserveIdsToIgnore.length > 0) {
			final List<Integer> idsToIgnore = IntStream.of(reserveIdsToIgnore).boxed().collect(Collectors.toList());
			return stream
					.filter( r -> !idsToIgnore.contains(r.getId()))
					.mapToDouble(r -> r.getReservedQuantity())
					.sum();
		}
		return stream
				.mapToDouble(r -> r.getReservedQuantity())
				.sum();
	}
}
