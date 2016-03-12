package com.compremelhor.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

@Entity
@Table(	name = "manufacturer",
		uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
@JsonAutoDetect
public class Manufacturer extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public Manufacturer() {}
	
	@JsonCreator
	public Manufacturer(String json) {
		super(json);
		Gson g = new Gson();
		Manufacturer m = g.fromJson(json, Manufacturer.class);
		this.name = m.getName();
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
