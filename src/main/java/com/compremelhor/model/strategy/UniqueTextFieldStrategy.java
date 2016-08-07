package com.compremelhor.model.strategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.compremelhor.model.dao.AbstractDao;
import com.compremelhor.model.entity.EntityModel;

public abstract class UniqueTextFieldStrategy<T extends EntityModel> implements Strategy<T>{
	private AbstractDao<T> dao;
	private String lowerCaseFieldName;

	public UniqueTextFieldStrategy(AbstractDao<T> dao, String lowerCaseFieldName) {
		this.dao = dao;
		this.lowerCaseFieldName = lowerCaseFieldName;
	}
	
	@Override
	public Status process(T t) {
		HashMap<String, String> errors = new HashMap<>();
		
		String methodName = resolveFieldNameToMethodName(lowerCaseFieldName);
		Method method = getMethod(methodName, t);
		
		if (method.getReturnType() != String.class)
			throw new IllegalArgumentException("Invalid return type: Must be String return type instead  " + method.getReturnType() + " return type.");
			
		HashMap<String, Object> params = new HashMap<>();
		
		try {
			if (t == null
					|| method.invoke(t) == null 
					|| ((String)method.invoke(t)).isEmpty()) {
				return new Status();
			}
			params.put(lowerCaseFieldName, ((String)method.invoke(t)).trim());
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			System.out.println(e.getMessage());
			return new Status();
		}
		
		T entity;
		if ((entity = dao.find(params)) != null
				&& entity.getId() != t.getId()) {
			errors.put(lowerCaseFieldName, 
					getClassName(t) + "." + lowerCaseFieldName + ".already.used.error.message");
			return new Status(errors);
		}
		return new Status();
	}

	private String resolveFieldNameToMethodName(String lowerCaseField) throws IllegalArgumentException {
		if (lowerCaseField == null || lowerCaseField.isEmpty()) 
			throw new IllegalArgumentException("Invalid filed name parameter: " + lowerCaseField);
		
		String firstLetter = lowerCaseField.substring(0, 1).toUpperCase();
		return "get".concat(firstLetter.concat(lowerCaseField.substring(1)));
	}
	
	private Method getMethod(String methodName, T entity) {
		Method[] methods = entity.getClass().getMethods();
		
		for (Method m : methods) {
			if (m.getName().equals(methodName))
				return m;
		}
		
		return null;
	}
	
	private String getClassName(T entity) {
		if (entity == null) throw new IllegalArgumentException("String UniqueTextFieldStrategy.getClassName(entity): Entity parameter must not be null.");
		return entity.getClass().getSimpleName().toLowerCase();
	}
}
