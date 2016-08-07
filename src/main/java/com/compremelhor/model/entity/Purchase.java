package com.compremelhor.model.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "purchase")
public class Purchase extends EntityModel {
	private static final long serialVersionUID = 1L;
		
//	@JsonIgnore
//	@Version private int version;
	
	@Column(name = "total_value")
	@NotNull
	private Double totalValue;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Status status;
	
	@ManyToOne
	@JoinColumn( name = "user_id")
	@NotNull
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "purchase")
	@JsonIgnore
	private Set<PurchaseLine> lines;
	
	@OneToOne(mappedBy = "purchase")
	private Freight freight;
	
	public Set<PurchaseLine> getLines() {
		return lines;
	}
	
	public void setLines(Set<PurchaseLine> lines) {
		this.lines = lines;
	}
	
	public boolean removeLine(PurchaseLine line) {
		return lines.remove(line);
	}
	
	public boolean addLine(PurchaseLine line) {
		return lines.add(line);
	}
	
	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public enum Status {
		OPENED,					// When the purchase is opened on client virtual cart 
		PAID, 					// When the purchase is closed and have already been paid
		PURCHASE_SEPARATED,		// When the purchaes's items are separated for shipping 
		SHIPPED, 				// When the purchase is shipped for client address
		
		STARTED_TRANSACTION		// This status is for starting the process of closing purchase
	}

	public Freight getFreight() {
		return freight;
	}

	public void setFreight(Freight freight) {
		this.freight = freight;
		
		if (freight != null) {
			freight.setPurchase(this);
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
