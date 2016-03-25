package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.User;

@Stateless
public class UserDao extends AbstractDao<User> {
	@Override
	public User find(Object id) {
		User u = super.find(id);
		
		if (u != null) {
			if (u.getAddresses() != null) {
				u.getAddresses().size();
			}
		}
		
		return u;
	}

	private static final long serialVersionUID = 1L;
		
	public UserDao() { super(User.class); }	
	
	public User findByAttribute(String attributeName, String attributeValue) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
		
		Root<User> usr = criteriaQuery.from(User.class);
		criteriaQuery
			.select(usr)
			.where(cb.equal(usr.get(attributeName.trim()), attributeValue.trim()));
		
		List<User> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}
		
		if (result == null || result.size() == 0) {
			return null;
		}
		
		return result.get(0);
	}
	
	public User findUserByDocument(String document) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
		
		Root<User> usr = criteriaQuery.from(User.class);
		criteriaQuery
			.select(usr)
			.where(cb.equal(usr.get("document"), document));
		
		List<User> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}		
		return result.get(0);
	}
}
