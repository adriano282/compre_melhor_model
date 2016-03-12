package com.compremelhor.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user", 
    uniqueConstraints = @UniqueConstraint(columnNames={"id", "document"})) 
public class User extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public User() {}
	@Column(nullable = false)
	@NotNull @Size(max=45)
	private String username;
	
	@Column(nullable = false)
	@NotNull 
	@Pattern(regexp= "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\-]?[0-9])|([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})")
	private String document;
	
	@Column(name = "document_type")
	@Enumerated(EnumType.STRING) @NotNull	
	private DocumentType documentType;	
	
	@Column(name = "password")
	@NotNull
	private String passwordHash;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
	@Valid
	private List<Address> addresses;
	
	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }

	public String getDocument() { return document; }

	public void setDocument(String document) { this.document = document; }

	public DocumentType getDocumentType() { return documentType; }

	public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

	public String getPasswordHash() { return passwordHash; }

	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

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
	
	public Optional<List<Address>> getOptionalAddresses() {
		return Optional.ofNullable(addresses);
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
}
