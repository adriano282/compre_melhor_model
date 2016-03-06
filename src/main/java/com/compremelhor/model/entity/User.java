package com.compremelhor.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "user", 
    uniqueConstraints = @UniqueConstraint(columnNames={"id", "document"})) 
public class User extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String document;
	
	@Column(name = "document_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentType documentType;	
	
	@Column(name = "password", nullable = false)
	private String passwordHash;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
	private List<Address> addresses;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String toString() {
		return "User [id: " + id + ", username: " + username + ", document: " + document 
				+ ", documentType: " + documentType.name() + ", passwordHash: " + passwordHash;
	}
	public static enum DocumentType implements Serializable {
		CNPJ, CPF;
		
		public String toString() {
			return this.toString();
		}
	}
	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
}
