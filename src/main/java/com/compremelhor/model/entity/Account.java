package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account")
public class Account extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Um username precisa ser definido.")
	@Size(max = 20)
	@Column(name = "username")
	private String username;
	
	@Size(max = 20)
	@NotNull(message = "Senha não informada.")
	@Column(name = "password")
	private String password;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "partner_id")
	private Partner partner;
		
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
}
