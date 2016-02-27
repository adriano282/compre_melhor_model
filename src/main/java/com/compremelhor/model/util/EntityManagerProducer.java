package com.compremelhor.model.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {
	
	@Produces
	@PersistenceContext(unitName = "compre_melhor")
	private EntityManager em;
	
}