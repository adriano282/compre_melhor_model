package com.compremelhor.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sku")
public class Sku implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;	
	
	private String name;
	
	private String description;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "sku_category",
			joinColumns=@JoinColumn(name="sku_id"),
			inverseJoinColumns=@JoinColumn(name="category_id"))
	private Set<Category> categories;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "unit")
	private UnitType unit;
	
	@Embedded
	private Code code;
	
	@Lob
	@Column(name = "photo")
	private byte[] photo;
	
	@OneToMany(mappedBy = "sku")
	private List<SkuPartner> skuPartners;
	
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Manufacturer manufacturer;
	
	public void setCategory(HashSet<Category> categories) {
		if (categories == null)
			this.categories = categories;
		
		this.categories.addAll(categories);
	}
	
	public void addCategory(Category c) {
		if (categories == null) {
			categories = new HashSet<>();
		}
		categories.add(c);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UnitType getUnit() {
		return unit;
	}

	public void setUnit(UnitType unit) {
		this.unit = unit;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public enum UnitType {
		CX, KG, M, L, SC, UN;
	}
	
	@Override
	public String toString() {
		return "Procuct[id: "+ id + ", name: " + name 
				+ ", description: " + description 
				+ ", unitType: " + unit.name() 
				+ ", code: " + code 
				+ ", manufacturer: " + manufacturer
				+ ", dateCreated: " + dateCreated
				+ ", lastUpdated: " + lastUpdated
				+ " ]";
	}
	@Override
	public int hashCode() {
		if (code == null || manufacturer == null)
			throw new RuntimeException("Code or Manufacturer variables in this product instance are null. Code and Manufacturer mustn't be null");
		
		return (name + code.getCode() + manufacturer.getName()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (code == null || manufacturer == null)
			throw new RuntimeException("Code or Manufacturer variables in this product instance are null. Code and Manufacturer mustn't be null");
		
		if (obj instanceof Sku 
				&& ((Sku)obj).getManufacturer() != null || ((Sku)obj).getCode() != null)
			throw new RuntimeException("Code or Manufacturer variables in the comparable object are null. Code and Manufacturer mustn't be null");
			
		
		if (obj instanceof Sku 
				&& ((Sku)obj).getName().equals(name)
				&& ((Sku)obj).getCode().equals(code)
				&& ((Sku)obj).getManufacturer().equals(manufacturer))
			return true;
		return false;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Set<Category> getCategories() {
		return categories;
	}
}

