package com.compremelhor.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "freight")
public class Freight implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "value_ride")
	private Double valueRide;
	
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;
	
	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address shipAddress;

	@OneToOne
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;
	
	public Double getValueRide() {
		return valueRide;
	}

	public void setValueRide(Double valueRide) {
		this.valueRide = valueRide;
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

	public Address getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(Address shipAddress) {
		this.shipAddress = shipAddress;
	}

	public int getId() {
		return id;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

}
