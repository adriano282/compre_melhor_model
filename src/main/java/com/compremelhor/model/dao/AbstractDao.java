package com.compremelhor.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractDao<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;	
	private final Class<T> clazz;	
	@Inject	private EntityManager em;
	
	public AbstractDao(Class<T> clazz) { this.clazz = clazz; }
	
	public T find(Object id) { return em.find(clazz, id); }
	
	public void remove(T entity) {
		em.remove(em.merge(entity));
		em.flush();
	}
	
	public T edit(T entity) {
		T t = em.merge(entity);
		em.flush();
		return t;
	}
	
	public void persist(final T entity) { 
		em.persist(entity);
	}
	
	public T find(Map<String, Object> params) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = cb.createQuery(clazz);
		
		Root<T> usr = criteriaQuery.from(clazz);
		final List<Predicate> predicates = new ArrayList<>();
		
		params.forEach((k, v) -> {
			
			
			if (k.contains(".")) {
				String segment = k.substring(0, k.indexOf("."));
				String rest = k.substring(k.indexOf(".") + 1);
				Path path = usr.get(segment);
				
				do {
					segment = rest.substring(0, rest.contains(".") ? rest.indexOf(".") : rest.length());
					rest = rest.substring(rest.contains(".") ? rest.indexOf(".") + 1 : rest.length());
					path = path.get(segment);
				} while (rest.length() > 0);
				predicates.add(cb.equal(path, v));			
			} else {
				predicates.add(cb.equal(usr.get(k), v));
			}
		});

		
		criteriaQuery
			.select(usr)
			.where(predicates
					.toArray(new Predicate[predicates.size()]));
		
		List<T> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}
		
		if (result == null || result.size() == 0) {
			return null;
		}
		T u = result.get(0);
		return u;
	}

	
	public List<T> findAll() {
		final CriteriaQuery<T> criteriaQuery = 
				em.getCriteriaBuilder().createQuery(clazz);
		
		criteriaQuery.select(criteriaQuery.from(clazz));
		return em.createQuery(criteriaQuery).getResultList();
	}
	
	public List<T> findAll(int start, int size) {
		final CriteriaQuery<T> criteriaQuery = 
				em.getCriteriaBuilder().createQuery(clazz);
		
		criteriaQuery.select(criteriaQuery.from(clazz));
		List<T> result = em.createQuery(criteriaQuery).getResultList();
		
		if (result == null) {
			return null;
		} else if (result.size() < size){
			return new ArrayList<>(result.subList(start,result.size()));
		} else {
			return new ArrayList<>(result.subList(start, size));
		}
	}
	
	public void deleteAll() {
		final CriteriaDelete<T> criteriaDelete = 
				em.getCriteriaBuilder().createCriteriaDelete(clazz);
		criteriaDelete.from(clazz);
		em.createQuery(criteriaDelete).executeUpdate();
	}
	
	public T find(int id, Set<String> fetches) {
		throw new RuntimeException("NOT IMPLEMENT YET");
	}
		
	protected EntityManager getEntityManager() { return em; }
	
	protected Class<T> getClazz() {	return clazz; }
}
