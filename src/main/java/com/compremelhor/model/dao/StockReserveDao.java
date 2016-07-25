package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.StockReserve;

@Stateless
public class StockReserveDao extends AbstractDao<StockReserve>{
	private static final long serialVersionUID = 1L;

	public StockReserveDao() {
		super(StockReserve.class);
	}
}
