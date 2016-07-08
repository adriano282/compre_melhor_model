package com.compremelhor.model.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "category", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "id"}))
public class Category extends EntityModel implements Serializable{
	private static final long serialVersionUID = 1L;

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
		return "[id: " + id + ", name: " + name +"]";
	}
	
	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (name == null || obj == null || ((Category)obj).getName() == null) return false;
		
		if (obj instanceof Category	&& ((Category)obj).getName().equals(name))
			return true;
		
		return false;
	}
}
