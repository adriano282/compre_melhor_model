package com.compremelhor.model.dao;

import java.util.Map;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Purchase;

@Stateless
public class PurchaseDao  extends AbstractDao<Purchase> {
	private static final long serialVersionUID = 1L;
	
	public PurchaseDao() { super(Purchase.class); }

	@Override
	public Purchase find(Map<String, Object> params) {
		
		params.compute("status", (k, v) -> { 
			if (v == null) return null;

			boolean valid = false;
			for (Purchase.Status status : Purchase.Status.values()) {
				if (status.toString().equals(v.toString().trim().toUpperCase()))
					valid = true;
			}
			
			if (!valid) return null;
			return Purchase.Status.valueOf(v.toString().toUpperCase());
		});
		return super.find(params);
	}
}
