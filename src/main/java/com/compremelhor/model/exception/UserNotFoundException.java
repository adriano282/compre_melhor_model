package com.compremelhor.model.exception;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public UserNotFoundException(int userId) {
		super("The user with id " + userId + " wasn`t found.!" );
	}
}
