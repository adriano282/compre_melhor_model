package com.compremelhor.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractDao<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Class<T> clazz;
	
	@Inject
	private EntityManager em;
	
	public AbstractDao(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public T find(Object id) {
		return em.find(clazz, id);
	}
	
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
	
	public List<T> findAll() {
		final CriteriaQuery<T> criteriaQuery = 
				em.getCriteriaBuilder().createQuery(clazz);
		
		criteriaQuery.select(criteriaQuery.from(clazz));
		return em.createQuery(criteriaQuery).getResultList();
	}
	
	public void deleteAll() {
		final CriteriaDelete<T> criteriaDelete = 
				em.getCriteriaBuilder().createCriteriaDelete(clazz);
		criteriaDelete.from(clazz);
		em.createQuery(criteriaDelete).executeUpdate();
	}
	
	public List<T> findAllByAttribute(String attributeName, String attributeValue) {
		return doQueryAndGetResult(attributeName, attributeValue);
	}
	
	public T findByAttribute(String attributeName, String attributeValue) {		
		List<T> result = doQueryAndGetResult(attributeName, attributeValue);
		
		if (result != null && result.size() > 1) {
			throw new RuntimeException("This query has been returned more than one result.");
		}		
		return result.get(0);
	}
	
	private boolean isValidAttribute(String attributeName) {
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(AbstractDao.class.getPackage().getName().concat(".entity_model_attributes"));
		} catch (Exception e) {
			try {
				rb = ResourceBundle.getBundle("resources/entity_model_attributes");
			} catch (Exception e1) {
				throw new RuntimeException("The entity model attributes file was not found. ExceptionMessage: " +e1.getMessage());
			}
		}
		
		String propertyName = clazz.getName().toLowerCase().concat(".attributes");
		String[] attributes = rb.getString(propertyName).split("#");
		for (String s : attributes) {
			if (s.equals(attributeName.trim().toLowerCase()))
				return true;
		}
		return false;
	}
	
	private List<T> doQueryAndGetResult(String attributeName, String attributeValue) {
		final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = cb.createQuery(clazz);
		
		if (!isValidAttribute(attributeName)) {
			throw new IllegalArgumentException("Unknow " + attributeName + " attribute.");
		}
		
		Root<T> selectStmt = criteriaQuery.from(clazz);
		criteriaQuery
			.select(selectStmt)
			.where(cb.equal(selectStmt.get(attributeName), attributeValue));
		
		return getEntityManager().createQuery(criteriaQuery).getResultList();
	}
	
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected Class<T> getClazz() {
		return clazz;
	}
}
