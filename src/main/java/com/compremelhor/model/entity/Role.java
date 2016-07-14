package com.compremelhor.model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "role")
public class Role extends EntityModel {
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String roleName;
	
	@ManyToMany(mappedBy="roles")
	private List<Account> accounts;
	
	@Override
	public int hashCode() {
		int hash = 0;
		if (roleName != null && !roleName.isEmpty())
			hash += (roleName.hashCode() * 23);
		return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Role 
				&& o != null
				&& ((Role)o).getRoleName() != null
				&& !((Role)o).getRoleName().isEmpty()
				&& this.roleName != null
				&& !this.roleName.isEmpty()
				&& this.roleName.equals(((Role)o).getRoleName())) {
			return true;
		}
		return false;
	}
	
	public static Role valueOf(String role) {
		String[] attrs = role.substring(1, role.length() -1).split(",");
		Role r = new Role();
		try {
			r.setId(Integer.valueOf(attrs[0].split(" : ")[1]));
			r.setRoleName(attrs[1].split(" : ")[1]);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			r.setDateCreated(LocalDateTime.parse(attrs[2].split(" : ")[1], fmt));
			r.setLastUpdated(LocalDateTime.parse(attrs[3].split(" : ")[1], fmt));
		} catch (Exception e) { 
			System.out.println(e.getMessage());
		}
		return r;
	}

	@Override
	public String toString() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "[id : " + id 
				+ ",roleName : " + roleName 
				+ ",dateCreated : " + (dateCreated != null ? dateCreated.format(fmt) : null)
				+ ",lastUpdated : " + (lastUpdated != null ? lastUpdated.format(fmt) : null) + "]";
	}

	public String getRoleName() { return roleName; }
	public void setRoleName(String roleName) { this.roleName = roleName; }
}
