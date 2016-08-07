package com.compremelhor.model.strategy.stock;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compremelhor.model.entity.EntityModel;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public abstract class AbstractRemoveExpiredReservesStrategy<T extends EntityModel> implements	Strategy<T> {
	
	private StockReserveService reserveService;
	
	public AbstractRemoveExpiredReservesStrategy(StockReserveService reserveService) {
		this.reserveService = reserveService;		
	}
	
	private static LocalDateTime now = LocalDateTime.now();
	
//	Later, this can be storage into database and manageable by web application
	private static long daysToExpireReserve = 0;
	private static long hoursToExpireReserve = 4;
	private static long minutesToExpireReserve = 30;
		
	@Override
	public Status process(T t) {
		Status s = new Status();
		
		if (t == null || t.getId() == 0) {
			return s;
		}
		
		Stock st = bindStockObject(t);
		
		Map<String, Object> params = new HashMap<>();
		params.put("stock.id", st.getId());
		
		try {
			List<StockReserve> reservedStocks = reserveService.findAll(params);
			reservedStocks.stream()
				.forEach( (r) -> {
					
					if (isExpired(r.getDateCreated())) {
						reserveService.remove(r);
					}
					
				});
		} catch (UnknownAttributeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return s;
	}
	
	public static boolean isExpired(LocalDateTime reserveCreationTime) {	
		now = LocalDateTime.now();
		long years = reserveCreationTime.until(now, ChronoUnit.YEARS);
		long months = reserveCreationTime.until(now, ChronoUnit.MONTHS);
		long days = reserveCreationTime.until(now, ChronoUnit.DAYS);
		long seconds = reserveCreationTime.until(now, ChronoUnit.SECONDS);
		
		if (years != 0 
				|| months != 0) return true;
		
		if (days < 0 
				|| days > daysToExpireReserve) return true;
		
		if (seconds < 0 
				|| seconds > ((hoursToExpireReserve * 60 * 60) + 
								(minutesToExpireReserve * 60)
							)) return true;

		return false;
	}
	
	private Stock bindStockObject(T t) {
		if (t instanceof Stock)
			return (Stock) t;
		else if (t instanceof StockReserve)
			return ((StockReserve)t).getStock();
		
		throw new IllegalArgumentException("AbstractRemoveExpiredReservesStrategy.bindStockObject(T): "
				+ "invalid object type. The object must be either of type Stock or StockReserve");
		
	}

	protected LocalDateTime getNow() {
		return now;
	}

	protected void setNow(LocalDateTime now) {
		AbstractRemoveExpiredReservesStrategy.now = now;
	}

	protected long getDaysToExpireReserve() {
		return daysToExpireReserve;
	}

	protected void setDaysToExpireReserve(long daysToExpireReserve) {
		AbstractRemoveExpiredReservesStrategy.daysToExpireReserve = daysToExpireReserve;
	}

	protected long getHoursToExpireReserve() {
		return hoursToExpireReserve;
	}

	protected void setHoursToExpireReserve(long hoursToExpireReserve) {
		AbstractRemoveExpiredReservesStrategy.hoursToExpireReserve = hoursToExpireReserve;
	}

	protected long getMinutesToExpireReserve() {
		return minutesToExpireReserve;
	}

	protected void setMinutesToExpireReserve(long minutesToExpireReserve) {
		AbstractRemoveExpiredReservesStrategy.minutesToExpireReserve = minutesToExpireReserve;
	}

}
