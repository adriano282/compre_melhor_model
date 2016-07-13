package com.compremelhor.model.strategy.category;

import java.util.HashMap;

import com.compremelhor.model.dao.CategoryDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public class UniqueNameStrategy implements Strategy<Category> {
	private CategoryDao dao;
	
	public UniqueNameStrategy(CategoryDao dao) {this.dao = dao;}
	
	@Override
	public Status validate(Category t) {
		HashMap<String, String> errors = new HashMap<>();
		
		if (t == null
				|| t.getName() == null
				|| t.getName().isEmpty()) {
			return new Status();
		}
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("name", t.getName().trim());
		
		Category c;
		if ((c = dao.find(params)) != null
				&& c.getId() != t.getId()) {
			errors.put("name", "category.name.already.used.error.message");
			return new Status(errors);
		}
		return new Status();
	}
	
}
