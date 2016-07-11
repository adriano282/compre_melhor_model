package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class PartnerServiceTest {
	
	@Inject private PartnerService partnerService;
	@Inject private AddressService addressService;
	@Inject private Logger logger;
	
	private Partner partner;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Partner.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(PartnerDao.class.getPackage())
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(PartnerService.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addPackage(LimitOfAddressesReachedException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Before
	public void config() throws InvalidEntityException {
		partner = createPartner(partnerService, partner);
		logger.log(Level.INFO, "Partner created: " + partner);
	}
	
	@Test
	public void editingPartner() throws InvalidEntityException {
		partner.setName("Nome Alterado");
		partnerService.edit(partner);
		assertEquals("Nome Alterado", partnerService.find(partner.getId()).getName());
		logger.log(Level.INFO, "Partner altered: " + partner);
	}
	
	@Test
	public void additioningAnAddress() throws InvalidEntityException {
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
	
	@Test
	public void removingAddresses() {
		Set<String> fetches = new HashSet<>();
		fetches.add("addresses");
		partner = partnerService.find(partner.getId(), fetches);
		partner.getAddresses().stream().forEach(a -> addressService.remove(a));
		
		partner = partnerService.find(partner.getId(), fetches);
		Assert.assertEquals(0, partner.getAddresses().size());
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
		removePartner(partnerService, partner);
		logger.log(Level.INFO, "Partner deleted. ID: " + partner.getId());
	}
	
	public void removePartner(PartnerService service, Partner partner) {
		assertNotNull(service);
		service.remove(partner);
		partner = service.find(partner.getId());
		assertNull(partner);
	}
	
	public Partner createPartner(PartnerService service, Partner partner) throws InvalidEntityException {
		assertNotNull(service);
		partner = new Partner();
		partner.setName("Supermercado TESTE");
		
		service.create(partner);
		
		partner = service.find(partner.getId());
		assertNotNull(partner);
		return partner;	
	}
}
