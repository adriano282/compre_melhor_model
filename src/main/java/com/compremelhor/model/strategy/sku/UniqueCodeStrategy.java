package com.compremelhor.model.strategy.sku;

import java.util.HashMap;

import com.compremelhor.model.dao.SkuDao;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public class UniqueCodeStrategy implements Strategy<Sku> {
	private SkuDao dao;
	
	public UniqueCodeStrategy(SkuDao dao) {this.dao = dao;}
	
	@Override
	public Status validate(Sku s) {
		HashMap<String, String> errors = new HashMap<>();
		
		if (s == null
				|| s.getCode() == null
				|| s.getCode().isEmpty()) {
			return new Status();
		}
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("code", s.getCode().trim());
		
		Sku t;
		if ((t = dao.find(params)) != null
				&& t.getId() != s.getId()) {
			errors.put("code", "sku.code.already.used.error.message");
			return new Status(errors);
		}
		return new Status();
	}
}
