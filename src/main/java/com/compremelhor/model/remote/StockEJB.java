package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.service.StockService;

@Stateless
@Remote(EJBRemote.class)
public class StockEJB extends AbstractRemote<Stock>{

	@Inject private StockService stockService;
	
	@Override
	void setService() { super.service = stockService;}
}
