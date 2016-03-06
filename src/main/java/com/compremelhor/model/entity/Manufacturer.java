package com.compremelhor.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "manufacturer",
		uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
public class Manufacturer extends EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;

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
