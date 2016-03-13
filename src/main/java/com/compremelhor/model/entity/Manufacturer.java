package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.gson.Gson;

@Entity
@Table(	name = "manufacturer",
		uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
public class Manufacturer extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public Manufacturer() {}
	
	@JsonCreator
	public Manufacturer(String json) {
		Gson g = new Gson();
		Manufacturer m = g.fromJson(json, Manufacturer.class);
		this.name = m.getName();
		this.id = m.getId();
		this.dateCreated = m.getDateCreated();
		this.lastUpdated = m.getLastUpdated();
	}
	
	@NotNull @Size(max = 20)
	private String name;
	
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	@Override
	public String toString() {
		return "Manufacturer[id: " + id 
				+ ", name: " + name 
				+ ", dateCreated: " + dateCreated
				+ ", lastUpdated: " + lastUpdated +"]";
	}
}
