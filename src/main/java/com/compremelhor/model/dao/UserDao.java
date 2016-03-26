package com.compremelhor.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.fest.util.Arrays;

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
	
	public User find(Map<String, String> params) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
		
		Root<User> usr = criteriaQuery.from(User.class);
		final List<Predicate> predicates = new ArrayList<>();
		
		params.forEach((k, v) -> {
			predicates.add(cb.equal(usr.get(k), v));
			System.out.println("K: " +k);
			System.out.println("V: " +v);
		});
		
		
		criteriaQuery
			.select(usr)
			.where(predicates
					.toArray(new Predicate[predicates.size()]));
		
		System.out.println("LENGHT: " + predicates
					.toArray(new Predicate[predicates.size()]).length);
		
		List<User> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}
		
		if (result == null || result.size() == 0) {
			return null;
		}
		 User u =result.get(0);
		 System.out.println(u.getUsername());
		 System.out.println("ENDEAVOR");
		 return u;
	}
		
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
