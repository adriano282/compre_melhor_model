package com.compremelhor.model.exception;

public class InvalidFieldNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidFieldNameException(String msg) {
		super(msg);
	}
}
