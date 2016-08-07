package com.compremelhor.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;

import com.compremelhor.model.dao.FreightDao;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.exception.UnknownAttributeException;

public class FreightService extends AbstractService<Freight>{
	
	private static final long serialVersionUID = 1L;
	@Inject	private FreightDao freightDao;
	
	@Override
	protected void setDao() { super.dao = freightDao; }
	@Override 
	protected void setStrategies() {}
	
	public Freight findFreightByPurchase(Purchase purchase) {
		return freightDao.findFreightByPurchase(purchase);
	}
	
	@Override
	public Freight find(Map<String, Object> params) throws UnknownAttributeException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try {
			props.load(classLoader.getResourceAsStream("entity_properties.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		String attrs = (String) props.get("freight");
		
		Set<Map.Entry<String, Object>> entries = params.entrySet();
		
		for (Map.Entry<String, Object> pair : entries) {
			if (!Arrays.asList(attrs.split("#")).contains(pair.getKey().trim())) {
				throw new UnknownAttributeException("Unknown freight attribute: " + pair.getValue());
			}
		}
		return freightDao.find(params);
	}


}
