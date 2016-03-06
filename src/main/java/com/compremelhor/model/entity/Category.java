package com.compremelhor.model.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany(mappedBy = "categories")
	private Set<Sku> skus;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "[id: " + id + ", name: " + name + ", skus: " +	skus + "]";
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Category	&& ((Category)obj).getName().equals(name))
			return true;
		
		return false;
	}
}
