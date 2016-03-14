package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Stock;

@Stateless
public class StockDao extends AbstractDao<Stock>{
	private static final long serialVersionUID = 1L;

	public StockDao() { super(Stock.class); }

}
