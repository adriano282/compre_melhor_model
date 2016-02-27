package com.compremelhor.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.compremelhor.model.entity.Manufacturer;

@Stateless
public class ManufacturerDao extends AbstractDao<Manufacturer> {
	private static final long serialVersionUID = 1L;

	public ManufacturerDao() {
		super(Manufacturer.class);
	}
	
	public Manufacturer findManufacturerByName(String name) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Manufacturer> criteriaQuery = cb.createQuery(Manufacturer.class);
		
		Root<Manufacturer> mfr = criteriaQuery.from(Manufacturer.class);
		criteriaQuery
			.select(mfr)
			.where(cb.equal(mfr.get("name"), name));
		
		List<Manufacturer> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result");
		}
		return result.get(0);
	}
}
