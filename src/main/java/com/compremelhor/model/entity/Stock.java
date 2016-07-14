package com.compremelhor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "stock", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "sku_partner_id"}))
@JsonIgnoreProperties(ignoreUnknown=true)
public class Stock extends EntityModel {	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "stock.quantity.is.null.message.error")
	private Double quantity;
	
	@Column(name = "unit_price")
	@NotNull(message = "stock.unitprice.is.null.message.error")
	private Double unitPrice;
	
	@OneToOne
	@JoinColumn(name="sku_partner_id")
	@NotNull(message = "stock.skupartner.is.null.message.error")
	private SkuPartner skuPartner;

	@Version private int version;
	
	public Double getQuantity() { return quantity; }

	public void setQuantity(Double quantity) { this.quantity = quantity; }

	public Double getUnitPrice() { return unitPrice; }

	public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

	public SkuPartner getSkuPartner() { return skuPartner; }

	public void setSkuPartner(SkuPartner skuPartner) { this.skuPartner = skuPartner; }
	
	public String toString() {
		return "Stock: [id: " + id + ", quantity: " + quantity + ", unitprice: " + unitPrice + ", SkuPartner: " + skuPartner + "]";
	}
}
