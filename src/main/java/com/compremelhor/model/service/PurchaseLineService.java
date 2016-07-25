package com.compremelhor.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.compremelhor.model.dao.PurchaseLineDao;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.StockReserve;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UnknownAttributeException;

@Stateless
public class PurchaseLineService extends AbstractService<PurchaseLine>{
	private static final long serialVersionUID = 1L;
	
	@Inject private PurchaseLineDao dao;
	@Inject private StockReserveService stockReserveService;
	
	@Override
	protected void setDao() {super.dao = this.dao;}
	@Override 
	protected void setStrategies() {}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(PurchaseLine t) throws InvalidEntityException {
		reserveStock(t);
		super.create(t);
	}
	
	private void reserveStock(PurchaseLine t) throws InvalidEntityException {
		StockReserve reserve = new StockReserve();
		reserve.setReservedQuantity(t.getQuantity());
		reserve.setStock(t.getStock());
		reserve.setPurchase(t.getPurchase());
		stockReserveService.create(reserve);
	}
	
	private void unreserveStock(PurchaseLine t) {
		Map<String, Object> params = new HashMap<>();
		params.put("purchase.id", t.getPurchase().getId());
		
		StockReserve reserve = null;
		
		try {
			reserve = stockReserveService.find(params);
		} catch (UnknownAttributeException e) {
			e.printStackTrace();
		}
		
		if (reserve != null) {
			stockReserveService.remove(reserve);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PurchaseLine edit(PurchaseLine t) throws InvalidEntityException {
		Map<String, Object> params = new HashMap<>();
		params.put("purchase.id", t.getPurchase().getId());
		
		StockReserve reserve = null;
		try {
			reserve = stockReserveService.find(params);
		} catch (UnknownAttributeException e) {
			e.printStackTrace();
		}
		
		reserve.setReservedQuantity(t.getQuantity());
		stockReserveService.edit(reserve);
		return super.edit(t);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void remove(PurchaseLine t) {
		unreserveStock(t);
		super.remove(t);
	}

}
