package com.compremelhor.model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
public class Manufacturer extends EntityModel implements Comparable<Manufacturer> {
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
	public boolean equals(Object m) {
		if (m instanceof Manufacturer && this.id == ((Manufacturer)m).getId())
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "[id : " + id 
				+ ",name : " + name 
				+ ",dateCreated : " + (dateCreated != null ? dateCreated.format(fmt) : null)
				+ ",lastUpdated : " + (lastUpdated != null ? lastUpdated.format(fmt) : null) + "]";
	}
	
	public int compareTo(Manufacturer m) {
		if (m == null || m.getName() == null || this.name == null) return -1;
		return this.name.compareTo(m.getName());
	}
	
	public static Manufacturer valueOf(String manufacturer) {
		String[] attrs = manufacturer.substring(1, manufacturer.length() -1).split(",");
		Manufacturer m = new Manufacturer();
		try {
			m.setId(Integer.valueOf(attrs[0].split(" : ")[1]));
			m.setName(attrs[1].split(" : ")[1]);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			m.setDateCreated(LocalDateTime.parse(attrs[2].split(" : ")[1], fmt));
			m.setLastUpdated(LocalDateTime.parse(attrs[3].split(" : ")[1], fmt));
		} catch (Exception e) { 
			System.out.println(e.getMessage());
		}
		return m;
	}
}
