package com.compremelhor.model.exception;

public class LimitOfAddressesReachedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LimitOfAddressesReachedException() {
		super("LimitOfAddressesReachedException: The limit of addresses is three for an user.");
	}
}
