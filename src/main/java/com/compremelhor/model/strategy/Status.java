package com.compremelhor.model.strategy;

import java.util.Map;

public class Status {
	private Map<String, String> errors;

	public Status() {}
	public Status(Map<String, String> errors)  {
		this.errors = errors;
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
	public boolean hasErrors() {
		if (errors == null) return false;
		if (errors != null && errors.size() == 0) return false;
		return true;
	}
}
