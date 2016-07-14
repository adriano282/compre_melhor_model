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
@Table(name = "category", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "id"}))
public class Category extends EntityModel implements Comparable<Category>{
	private static final long serialVersionUID = 1L;

	public Category() {}
	
	@JsonCreator
	public Category(String json) {
		Gson g = new Gson();
		Category m = g.fromJson(json, Category.class);
		this.name = m.getName();
		this.id = m.getId();
		this.dateCreated = m.getDateCreated();
		this.lastUpdated = m.getLastUpdated();
	}
	
	@NotNull @Size(max=45)
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "[id : " + id 
				+ ",name : " + name 
				+ ",dateCreated : " + (dateCreated != null ? dateCreated.format(fmt) : null)
				+ ",lastUpdated : " + (lastUpdated != null ? lastUpdated.format(fmt) : null) + "]";
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Category	
				&& this.name != null 
				&& ((Category)obj).getName() != null
				&& this.name.equals((((Category)obj).getName())))
			return true;
		
		return false;
	}

	
	@Override
	public int compareTo(Category m) {
		if (m == null || m.getName() == null || this.name == null) return -1;
		return this.name.compareTo(m.getName());
	}
	
	public static Category valueOf(String ca) {
		String[] attrs = ca.substring(1, ca.length() -1).split(",");
		Category m = new Category();
		try {
			m.setId(Integer.valueOf(attrs[0].split(" : ")[1]));
			m.setName(attrs[1].split(" : ")[1]);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			m.setDateCreated(LocalDateTime.parse(attrs[2].split(" : ")[1], fmt));
			m.setLastUpdated(LocalDateTime.parse(attrs[3].split(" : ")[1], fmt));
		} catch (Exception e) { 
			System.out.println("ERROR on Category ValueOF method: " + e.getMessage());
		}
		return m;
	}
	
	public static Category fromName(String name) {
		Category m = new Category();
		m.setName(name);
		return m;
	}
	
	@Override
	public int hashCode() {
		if (this.name == null) return -1;
		
		int hashCode = 33;
		hashCode = hashCode * 19 + (this.name.hashCode());
		return hashCode;
	}

}
