package com.compremelhor.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;

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
	
	public void persist(final T entity) { em.persist(entity); }
	
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
		return em.createQuery(criteriaQuery)
				.getResultList()
				.subList(start, start + size);
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
