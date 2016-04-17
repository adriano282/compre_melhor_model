package com.compremelhor.model.strategy.user;

import java.util.HashMap;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;

public class UniqueUsernameStrategy implements Strategy<User> {
	private UserDao dao;
	
	public UniqueUsernameStrategy(UserDao dao) { this.dao = dao;}
	
	@Override
	public Status validate(User t) {
		HashMap<String, String> errors = new HashMap<>();
		
		if (t == null ||
				t.getUsername() == null || 
				t.getUsername().isEmpty()) {
			errors.put("username", "user.username.is.null.error.message");
			return new Status(errors);
		}
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("username", t.getUsername().trim());
		
		User u;
		if ( (u = dao.find(params)) != null && u.getId() != t.getId()) {
			errors.put("username", "user.username.already.registered.error.message");
			return new Status(errors);
		}
		return new Status();
	}
}
