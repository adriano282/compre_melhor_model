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
		System.out.println("CHEGOU");
		if (fetches.contains("addresses")) {
			System.out.println("CONTAINS ADDRESSES");
			System.out.println(p.getAddresses());
			if (p != null) {
				System.out.println("PARTNER NOT NULL");
				if (p.getAddresses() != null) {
					System.out.println("ADDRESSES NOT NULL");
					p.getAddresses().size();
					System.out.println(p.getAddresses().size());
				};
			}
		}
		System.out.println(p.getAddresses());
		return p;
	}
}
