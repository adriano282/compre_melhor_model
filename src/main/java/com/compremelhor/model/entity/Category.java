package com.compremelhor.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "category", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "id"}))
public class Category extends EntityModel implements Serializable, Comparable<Category>{
	private static final long serialVersionUID = 1L;

	public Category() {}
	
	@NotNull @Size(max=45)
	private String name;
	
	@OneToMany(mappedBy = "category")
	private Set<Sku> skus;
	
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
		if (obj instanceof Category && this.id == (((Category)obj).getId()))
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
			System.out.println(e.getMessage());
		}
		return m;
	}

}
