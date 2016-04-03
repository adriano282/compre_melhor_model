package com.compremelhor.model.strategy.user;

import java.util.HashMap;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public class UniqueDocumentStrategy implements Strategy<User> {
	private UserDao dao;
	
	public UniqueDocumentStrategy(UserDao dao) { this.dao = dao;}

	@Override
	public Status validate(User t) {
		HashMap<String, String> errors = new HashMap<>();
		
		if (t == null ||
				t.getDocument() == null || 
				t.getDocument().isEmpty()) {
			return new Status();
		}
		
		HashMap<String, String> params = new HashMap<>();
		params.put("document", t.getDocument().trim());
		
		User u;
		if ( (u = dao.find(params)) != null && u.getId() != t.getId()) {
			errors.put("document", "user.document.already.registered.error.message");
			return new Status(errors);
		}
		return new Status();	}

}
