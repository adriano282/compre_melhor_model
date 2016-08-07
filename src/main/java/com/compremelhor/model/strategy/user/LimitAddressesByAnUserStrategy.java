package com.compremelhor.model.strategy.user;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.strategy.Status;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.annotations.OnCreateServiceAction;
import com.compremelhor.model.strategy.annotations.OnEditServiceAction;

@OnCreateServiceAction
@OnEditServiceAction
public class LimitAddressesByAnUserStrategy implements Strategy<User>{
	private UserDao dao;
	
	public LimitAddressesByAnUserStrategy(UserDao dao) { this.dao = dao;}
	
	@Override
	public Status process(User t) {
		User u = dao.find(t.getId());
		
		if (u == null) return new Status();
		Optional<List<Address>> optAds = u.getOptionalAddresses();
		if (optAds.isPresent() && optAds.get().size() > 3) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("addresses", "user.addresses.max.number.excedded.error.message");
			return new Status(errors);
		}
		return new Status();
	}
}
