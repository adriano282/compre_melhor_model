package com.compremelhor.model.exception;

public class InvalidEntityException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidEntityException(String messsageError) {
		super(messsageError);
	}

}
