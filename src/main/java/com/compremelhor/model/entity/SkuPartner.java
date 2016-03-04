package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sku_partner")
public class SkuPartner implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name ="sku_id")
	private Sku sku;
	
	@ManyToOne
	@JoinColumn(name = "partner_id")
	private Partner partner;
	
	@OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "skuPartner")
	private Stock stock;

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
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
	
	public String toString() {
		return "SkuPartner: [sku: " + sku + ", partner: " + partner + "]";
	}
}
