package com.compremelhor.model.dao;

import java.util.Set;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Partner;

@Stateless
public class PartnerDao extends AbstractDao<Partner>{
	private static final long serialVersionUID = 1L;

	public PartnerDao() { super(Partner.class); }
	
	public Partner find(int id, Set<String> fetches) {
		Partner p = find(id);
		if (p == null) return p;
		
		if (fetches.contains("addresses")) {
			if (p.getAddresses() != null) {
				p.getAddresses().size();
			};
		}
		else if (fetches.contains("freightTypes")) {
			if (p.getFreightTypes() != null) {
				p.getFreightTypes().size();
			}
		}
		
		return p;
	}
}
