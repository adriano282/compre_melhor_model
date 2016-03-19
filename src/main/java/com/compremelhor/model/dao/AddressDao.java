package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;

@Stateless
public class AddressDao extends AbstractDao<Address> {
	private static final long serialVersionUID = 1L;

	public AddressDao() { super(Address.class); }
	
	public List<Address> findAllAddressByUser(User user) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Address> criteriaQuery = cb.createQuery(Address.class);
		
		Root<Address> root = criteriaQuery.from(Address.class);
		
		criteriaQuery
			.select(root)
			.where(cb.equal(root.get("user"), user));
		return getEntityManager()
				.createQuery(criteriaQuery)
				.getResultList();		
	}
}
