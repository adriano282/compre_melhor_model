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
		
		if (fetches.contains("addresses")) {
			if (p != null) {
				if (p.getAddresses() != null) {
					p.getAddresses().size();
				};
			}
		}
		return p;
	}
}
