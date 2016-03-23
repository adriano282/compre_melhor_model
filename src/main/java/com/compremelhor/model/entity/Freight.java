package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "freight")
public class Freight extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
		
	public Freight () {}
	
	@Column(name = "value_ride")
	@NotNull
	private Double valueRide;
	
	@ManyToOne
	@JoinColumn(name = "address_id") 
	@NotNull
	private Address shipAddress;

	@OneToOne
	@JoinColumn(name = "purchase_id") 
	@NotNull
	private Purchase purchase;
	
	public Double getValueRide() {
		return valueRide;
	}

	public void setValueRide(Double valueRide) {
		this.valueRide = valueRide;
	}

	public Address getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(Address shipAddress) {
		this.shipAddress = shipAddress;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
}
