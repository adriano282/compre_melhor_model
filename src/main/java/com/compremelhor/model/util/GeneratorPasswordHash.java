package com.compremelhor.model.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GeneratorPasswordHash {
	public static String getHash(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] passwordByte = password.getBytes();
		byte[] hash = md.digest(passwordByte);
		
		return Base64.getEncoder().encodeToString(hash);
	}
}
