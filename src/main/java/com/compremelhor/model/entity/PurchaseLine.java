package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchase_line")
public class PurchaseLine extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
			
	private Double quantity;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@Column(name = "sub_total")
	private Double subTotal;
	
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
