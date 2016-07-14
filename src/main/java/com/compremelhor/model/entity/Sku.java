package com.compremelhor.model.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sku")
@SecondaryTable(name="sku_photo", pkJoinColumns = 
		@PrimaryKeyJoinColumn(name="sku_id", 
			referencedColumnName="id")
)
public class Sku extends EntityModel {
	private static final long serialVersionUID = 1L;
	
	@NotNull @Size(max= 45)
	private String name;
	
	@NotNull @Size(max= 250, 
			message="O número de caracteres não deve ultrapassar 250")
	private String description;
	
	@ManyToOne	
	@Basic(fetch = FetchType.EAGER)
	private Category category;
	
	@ManyToOne
	@NotNull
	@Basic(fetch = FetchType.EAGER)
	private Manufacturer manufacturer;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "unit")
	@NotNull
	@Basic(fetch = FetchType.EAGER)
	private UnitType unit;
	
	@Size(min= 13, max = 13, 
			message = "O código de barras deve conter 13 dígitos")
	private String code;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.PUBLICADO;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "photo", table="sku_photo")
	private byte[] photo;
	
	@OneToMany(mappedBy = "sku")
	private List<SkuPartner> skuPartners;
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public UnitType getUnit() { return unit; }

	public void setUnit(UnitType unit) { this.unit = unit; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	
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
		
		return (name + code + manufacturer.getName()).hashCode();
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

	public Category getCategory() { return category; }
	
	public enum UnitType { CX, KG, M, L, SC, UN, PCT; }
	public enum Status { PUBLICADO, DELETADO }
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	};
}

