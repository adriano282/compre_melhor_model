package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.compremelhor.model.validation.groups.PartnerAddress;
import com.compremelhor.model.validation.groups.UserAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "address")
public class Address extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Address() {}
	
	@NotNull @Size(max=45)
	private String street;
	
	@NotNull @Size(max= 10)
	private String number;
	
	@NotNull @Pattern(regexp= "[0-9]{5}\\-?[0-9]{3}")
	private String zipcode;
	
	@NotNull @Size(max= 45)
	private String quarter;
	
	@NotNull @Size(max= 45)
	private String city;
	
	@NotNull @Size(max= 10)
	private String state;
	
	@ManyToOne
	@NotNull(groups=PartnerAddress.class)
	@JsonIgnore
	private Partner partner;
	
	@ManyToOne
	@NotNull(groups=UserAddress.class)
	@JsonIgnore
	private User user;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
