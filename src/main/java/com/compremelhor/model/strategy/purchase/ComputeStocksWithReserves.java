package com.compremelhor.model.strategy.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.compremelhor.model.dao.StockReserveDao;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;
import com.compremelhor.model.service.PurchaseService;
import com.compremelhor.model.service.StockReserveService;
import com.compremelhor.model.service.StockService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.BeforeServiceAction;

@BeforeServiceAction
public class ComputeStocksWithReserves implements Strategy<Purchase> {
	
	private StockReserveService stockReserveService;
	private PurchaseService purchaseService;
	private StockService stockService;
	private StockReserveDao reserveDao;
	
	private Status status = new Status();
	HashMap<String, String> errors = new HashMap<>();
	
	public ComputeStocksWithReserves(StockReserveService stockReserveService, StockService stockService, StockReserveDao reserveDao, PurchaseService purchaseService) {
		this.stockReserveService = stockReserveService;
		this.stockService = stockService;		
		this.reserveDao = reserveDao;
		this.purchaseService = purchaseService;
	}
	
	private List<StockReserve> getReserves(Purchase p) throws UnknownAttributeException {
		Map<String, Object> params = new HashMap<>();
		params.put("purchase.id", p.getId());
	
		return stockReserveService.findAll(params);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Status process(Purchase p) {
		if (p == null) return status;

		if (p.getStatus() != Purchase.Status.PAID)
			return status;
		
		try {
			List<StockReserve> reserves = getReserves(p);
			
			
			Stream<PurchaseLine> lines = purchaseService.getPurchaseLinesStream(p);
			if (reserves != null 
					&&	lines != null 
					&& reserves.size() < lines.count()) {
				errors.put("purchase", "purchase.expired.items");
				status.setErrors(errors);
				System.out.println(this.getClass().getSimpleName() + ".validate(Purchase): purchase.expired.items");
				return status;
			}
			

			
			for (StockReserve reserve : reserves) {
				Stock st = stockService.find(reserve.getStock().getId());
				
				if (st != null) {
					Double newQuantity = st.getQuantity() - reserve.getReservedQuantity();
					st.setQuantity(newQuantity);
					stockService.edit(st);
					reserveDao.remove(reserve);
				}
			}
			
		} catch (UnknownAttributeException e) {
			errors.put("purchase", "internal.server.error");
			status.setErrors(errors);
			System.out.println(this.getClass().getSimpleName() + ".validate(Purchase): Error while trying find attribute on StockReserve class");
			
		} catch (InvalidEntityException e) {
			System.out.println(this.getClass().getSimpleName() + ".validate(Purchase): Error while trying upgrade stocks");
			errors.put("purchase", e.getMessage());
			status.setErrors(errors);
		}
		System.out.println("COMPUTE STOCKS WITH RESERVES");
		return status;
	}
}
