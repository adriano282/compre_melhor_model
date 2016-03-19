package com.compremelhor.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sku")
@SecondaryTable(name="sku_photo", pkJoinColumns = 
		@PrimaryKeyJoinColumn(name="sku_id", 
			referencedColumnName="id")
)
public class Sku extends EntityModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotNull @Size(max= 45)
	private String name;
	
	@NotNull @Size(max= 45)
	private String description;
	
	@ManyToMany(cascade = {CascadeType.PERSIST})
	@JoinTable(name = "sku_category",
			joinColumns=@JoinColumn(name="sku_id"),
			inverseJoinColumns=@JoinColumn(name="category_id"))
	private Set<Category> categories;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "unit")
	@NotNull
	private UnitType unit;
	
	@Embedded
	@NotNull
	@Valid
	private Code code;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "photo", table="sku_photo")
	private byte[] photo;
	
	@OneToMany(mappedBy = "sku")
	private List<SkuPartner> skuPartners;
	
	@ManyToOne
	@NotNull
	private Manufacturer manufacturer;
	
	public void setCategory(HashSet<Category> categories) {
		if (this.categories == null) {
			this.categories = categories;
		} else if (categories != null) {
			this.categories.addAll(categories);
		} else {
			this.categories = null;
		}
		
	}
	
	public void addCategory(Category c) {
		if (categories == null) {
			categories = new HashSet<>();
		}
		categories.add(c);
	}
	
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public UnitType getUnit() { return unit; }

	public void setUnit(UnitType unit) { this.unit = unit; }

	public Code getCode() { return code; }

	public void setCode(Code code) { this.code = code; }
	public Manufacturer getManufacturer() { return manufacturer; }

	public void setManufacturer(Manufacturer manufacturer) { this.manufacturer = manufacturer; }
	
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
			return -1;
		
		return (name + code.getCode() + manufacturer.getName()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (code == null || manufacturer == null)
			return false;
		
		if (obj instanceof Sku 
				&& ((Sku)obj).getManufacturer() != null || ((Sku)obj).getCode() != null)
			return false;
			
		
		if (obj instanceof Sku 
				&& ((Sku)obj).getName().equals(name)
				&& ((Sku)obj).getCode().equals(code)
				&& ((Sku)obj).getManufacturer().equals(manufacturer))
			return true;
		return false;
	}

	public byte[] getPhoto() { return photo; }

	public void setPhoto(byte[] photo) { this.photo = photo; }

	public Set<Category> getCategories() { return categories; }
	
	public enum UnitType { CX, KG, M, L, SC, UN; }
	
}

