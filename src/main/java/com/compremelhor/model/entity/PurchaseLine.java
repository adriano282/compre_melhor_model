package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "purchase_line")
public class PurchaseLine extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Version private int version;
	
	@NotNull
	private Double quantity;
	
	@Column(name = "unit_price")
	@NotNull
	private Double unitPrice;
	
	@Column(name = "sub_total")
	@NotNull
	private Double subTotal;
	
	@ManyToOne
	@JoinColumn(name = "stock_id")
	@NotNull
	private Stock stock;

	@ManyToOne
	@JoinColumn(name = "purchase_id")
	@NotNull
	private Purchase purchase;
	
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
		subTotal = quantity * (getUnitPrice() == null ? 0 : getUnitPrice());
	}	

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
		subTotal = getQuantity() * unitPrice;
	}
	
	public Double getQuantity() { return quantity; }
	
	public Double getUnitPrice() { return unitPrice; }
	
	public Double getSubTotal() { return subTotal; }

	public Stock getStock() { return stock;	}

	public void setStock(Stock stock) {	this.stock = stock;	}

	public Purchase getPurchase() { return purchase; }

	public void setPurchase(Purchase purchase) { this.purchase = purchase; }
	
	@Override
	public int hashCode() {
		if (stock == null || 
				stock.getId() == 0 ||
				purchase == null ||
				purchase.getUser() == null)
			return -1;
		
		return stock.getId() * purchase.getUser().getId() / 10000;		
	}

	@Override
	public boolean equals(Object o) {
		
		if (stock == null || 
				stock.getId() == 0 ||
				purchase == null ||
				purchase.getUser() == null)
			return false;
		
		if (o instanceof PurchaseLine &&
				((PurchaseLine)o).getStock() != null &&
				((PurchaseLine)o).getStock().getId() != 0 &&
				((PurchaseLine)o).getStock().getId() == stock.getId() &&
				((PurchaseLine)o).getPurchase() != null &&
				((PurchaseLine)o).getPurchase().getUser() != null &&
				((PurchaseLine)o).getPurchase().getUser().getId() == purchase.getUser().getId()) {
			return true;
		}
		return false;
	}
}
