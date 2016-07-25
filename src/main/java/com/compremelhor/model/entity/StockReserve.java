package com.compremelhor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stock_reserve")
public class StockReserve extends EntityModel {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	@OneToOne
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;
	
	@Column(name = "reserved_quantity")
	private Double reservedQuantity;
	
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public Double getReservedQuantity() {
		return reservedQuantity;
	}
	public void setReservedQuantity(Double reservedQuantity) {
		this.reservedQuantity = reservedQuantity;
	}
	public Purchase getPurchase() {
		return purchase;
	}
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
}
