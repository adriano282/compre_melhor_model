package com.compremelhor.model.exception;

public class PartnerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public PartnerNotFoundException(int partnerId) {
		super("The partner with id " + partnerId + " wasn`t found.!" );
	}
}
