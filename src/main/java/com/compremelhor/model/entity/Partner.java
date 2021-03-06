package com.compremelhor.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "partner")
public class Partner extends EntityModel {
	private static final long serialVersionUID = 1L;
	
	@NotNull @Size(max=45)
	private String name;
	
	@OneToMany(mappedBy= "partner")
	private List<SkuPartner> skuPartner;
	
	@OneToMany(mappedBy = "partner", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@Valid
	private List<Address> addresses;
	
	@OneToMany(mappedBy = "partner")
	@Valid
	@JsonIgnore
	private List<FreightType> freightTypes;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public List<Address> getAddresses() {
		if (addresses == null) {
			addresses = new ArrayList<>();
		}
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public List<FreightType> getFreightTypes() {
		return freightTypes;
	}
	public void setFreightTypes(List<FreightType> freightTypes) {
		this.freightTypes = freightTypes;
	}
	
}
