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
import javax.persistence.Table;

@Entity
@Table(name = "purchase_line")
public class PurchaseLine  implements Serializable {
	private static final long serialVersionUID = 1L;
			
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Double quantity;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@Column(name = "sub_total")
	private Double subTotal;
	
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;
	
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;

	@ManyToOne
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;
	
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
		subTotal = quantity * (getUnitPrice() == null ? 0 : getUnitPrice());
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
		subTotal = getQuantity() * unitPrice;
	}

	public Double getSubTotal() {
		return subTotal;
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

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
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
