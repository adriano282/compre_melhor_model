package com.compremelhor.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "partner")
public class Partner extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	
	@OneToMany(mappedBy= "partner")
	private List<SkuPartner> skuPartner;
	
	@OneToMany(mappedBy = "partner", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private List<Address> addresses;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Address> getAddresses() {
		if (addresses == null) {
			addresses = new ArrayList<>();
		}
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
}
