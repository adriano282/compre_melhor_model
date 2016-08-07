package com.compremelhor.model.strategy.purchase;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLog;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.PurchaseLogService;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnAfterCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnAfterEditServiceAction;

@OnAfterCreateServiceAction
@OnAfterEditServiceAction
public class RegisterPurchaseLogStrategy implements Strategy<Purchase> {
	
	private Status status = new Status();
	private Map<String, String> errors = new HashMap<>();
	
	private PurchaseLogService purchaseLogService;
	
	public RegisterPurchaseLogStrategy(PurchaseLogService purchaseLogService) {
		this.purchaseLogService = purchaseLogService;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Status process(Purchase p) {
		PurchaseLog log = new PurchaseLog();
		log.setPurchaseId(p.getId());
		log.setStatus(p.getStatus());
		log.setLastUpdated(LocalDateTime.now());
		log.setDateCreated(LocalDateTime.now());
		try {
			purchaseLogService.create(log);
		} catch (InvalidEntityException e) {
			errors.put("purchase", e.getMessage());
			status.setErrors(errors);
		}
		return status;
	}
}
