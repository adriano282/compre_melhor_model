package com.compremelhor.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "freight")
@SecondaryTable(name = "freight_setup",
		pkJoinColumns=@PrimaryKeyJoinColumn(name="freight_id"))
public class Freight extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
		
	public Freight () {}
	
	@Column(name = "value_ride")
	@NotNull(message = "freight.valueride.is.null.message.error")
	private Double valueRide;
	
	@ManyToOne
	@JoinColumn(name = "address_id") 
	@NotNull(message = "freight.shipaddress.is.null.message.error")
	private Address shipAddress;

	@OneToOne
	@JoinColumn(name = "purchase_id") 
	@NotNull(message = "freight.purchase.is.null.message.error")
	@JsonIgnore
	private Purchase purchase;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "freight.type.is.null.message.error")
	private FreightType type;
	
	@Column(name = "starting_time", table = "freight_setup")
	private LocalTime startingTime;
	
	@Column(name = "starting_date", table = "freight_setup")
	private LocalDate startingDate;
	
	public FreightType getType() {
		return type;
	}

	public void setType(FreightType type) {
		this.type = type;
	}

	public LocalTime getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(LocalTime startingTime) {
		this.startingTime = startingTime;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

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
	
	public enum FreightType {
		EXPRESS, SCHEDULED;
	}
}
