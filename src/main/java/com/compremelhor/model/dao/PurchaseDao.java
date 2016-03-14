package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Purchase;

@Stateless
public class PurchaseDao  extends AbstractDao<Purchase> {
	private static final long serialVersionUID = 1L;
	
	public PurchaseDao() { super(Purchase.class); }
}
