package com.compremelhor.model.dao;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.User;

@Stateless
public class UserDao extends AbstractDao<User> {
	private static final long serialVersionUID = 1L;
		
	public UserDao() {
		super(User.class);
	}
	
	
	public User findUserByDocument(String document) {
		
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
		
		Root<User> usr = criteriaQuery.from(User.class);
		
		criteriaQuery
			.select(usr)
			.where(cb.equal(usr.get("document"), document));
		
		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}
}
