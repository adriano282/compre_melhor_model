package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.util.LoggerProducer;

@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(Arquillian.class)
public class PartnerServiceTest {
	
	@Inject private PartnerService partnerService;
	@Inject private Logger logger;
	
	private Partner partner;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Partner.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(PartnerDao.class.getPackage())
				.addPackage(PartnerService.class.getPackage())
				.addPackage(LimitOfAddressesReachedException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Before
	public void config() {
		partner = new Partner();
		partner.setName("Super Mercado da Gente");
		assertNotNull(partnerService);
		partnerService.create(partner);
		
		
		Partner par = partnerService.find(partner.getId());
		partner = par;
		assertNotNull(partner);
		
		logger.log(Level.INFO, "Partner created: " + partner);
	}
	
	
	
	@Test
	public void editingPartner() {
		partner.setName("Nome Alterado");
		partnerService.edit(partner);
		
		assertEquals("Nome Alterado", partnerService.find(partner.getId()).getName());
		
		logger.log(Level.INFO, "Partner altered: " + partner);
	}
	
	@Test
	public void additioningAnAddress() {
		partnerService.addAddress(partner.getId(), getAnAddress());
		partner = partnerService.find(partner.getId());
		assertEquals(1, partner.getAddresses().size());
		logger.log(Level.INFO, "One address added.");
						
		partnerService.addAddress(partner.getId(), getAnAddress());
		partner = partnerService.find(partner.getId());
		assertEquals(2, partner.getAddresses().size());
		logger.log(Level.INFO, "Two address added.");
		
		partnerService.addAddress(partner.getId(), getAnAddress());
		partner = partnerService.find(partner.getId());
		assertEquals(3, partner.getAddresses().size());
		logger.log(Level.INFO, "Three address added.");
		
		partnerService.addAddress(partner.getId(), getAnAddress());
		partner = partnerService.find(partner.getId());
		assertEquals(4, partner.getAddresses().size());
		logger.log(Level.INFO, "Four address added.");
		
	}
	
	private Address getAnAddress() {
		Address ad = new Address();
		ad = new Address();
		ad.setCity("Mogi das Cruzes");                         
		ad.setNumber("49");
		ad.setQuarter("Vila Brasileira");
		ad.setState("SP");
		ad.setStreet("Rua Alfredo Gomes Loureiro");
		ad.setZipcode("08738290");
		ad.setPartner(partner);
		return ad;
	}
	@After
	public void removing() {
		
		Partner p = partnerService.find(partner.getId());
		assertNotNull(p);
		partnerService.remove(p);
		assertNull(partnerService.find(partner.getId()));
		logger.log(Level.INFO, "Partner deleted. ID: " + partner.getId());
	}

}