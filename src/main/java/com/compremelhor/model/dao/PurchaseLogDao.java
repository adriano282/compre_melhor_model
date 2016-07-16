package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.PurchaseLog;

@Stateless
public class PurchaseLogDao extends AbstractDao<PurchaseLog>{
	private static final long serialVersionUID = 1L;

	public PurchaseLogDao() {
		super(PurchaseLog.class);
	}
}
