package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock extends EntityModel implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private Double quantity;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@OneToOne(cascade = {CascadeType.PERSIST})
	@JoinColumn(name="sku_partner_id")
	private SkuPartner skuPartner;

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public SkuPartner getSkuPartner() {
		return skuPartner;
	}

	public void setSkuPartner(SkuPartner skuPartner) {
		this.skuPartner = skuPartner;
	}
	
	public String toString() {
		return "Stock: [id: " + id + ", quantity: " + quantity + ", unitprice: " + unitPrice + ", SkuPartner: " + skuPartner + "]";
	}
}
