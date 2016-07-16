package com.compremelhor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "purchase_log")
public class PurchaseLog extends EntityModel{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "purchase_id")
	private int purchaseId;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private Purchase.Status status;
	
	
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Purchase.Status getStatus() {
		return status;
	}
	public void setStatus(Purchase.Status status) {
		this.status = status;
	}
}
