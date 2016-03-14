package com.compremelhor.model.dao;

import javax.ejb.Stateless;

import com.compremelhor.model.entity.Partner;

@Stateless
public class PartnerDao extends AbstractDao<Partner>{
	private static final long serialVersionUID = 1L;

	public PartnerDao() { super(Partner.class); }
}
