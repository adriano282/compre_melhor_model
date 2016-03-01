package com.compremelhor.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "user", 
    uniqueConstraints = @UniqueConstraint(columnNames={"id", "document"})) 
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String document;
	
	@Column(name = "document_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentType documentType;	
	
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
	private List<Address> addresses;
	
	@Column(name = "date_created", nullable = false)
	private LocalDateTime dateCreated;
	
	@Column(name = "last_updated", nullable = false)
	private LocalDateTime lastUpdated;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
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
