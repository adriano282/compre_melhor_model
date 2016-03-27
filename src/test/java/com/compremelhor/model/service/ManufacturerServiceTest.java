package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
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
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class ManufacturerServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Manufacturer.class.getPackage())
				.addPackage(UserNotFoundException.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(ManufacturerDao.class.getPackage())
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(ManufacturerService.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject	private ManufacturerService mfrService;
	@Inject private Logger logger;
	
	private Manufacturer mfr;
	
	@Before
	public void config() throws InvalidEntityException {
		mfr = create(mfrService, mfr);
	}
	
	@Test
	public void edit() throws InvalidEntityException {
		logger.log(Level.INFO, "START: Editing a manufacturer");
		
		mfr.setName("Other Manufacturer");
		mfr.setLastUpdated(LocalDateTime.now());
		
		Manufacturer mfrR = null;
		mfrR = edit(mfrService, mfr);
		
		logger.log(Level.INFO, "END: Manufacturer Edited");
		assertEquals(mfrR.getName(), "Other Manufacturer");
		assertNotEquals(0, mfrR.getId());
		assertNotNull(mfrR.getLastUpdated());
		assertNotNull(mfrR.getLastUpdated());
		logger.log(Level.WARNING, "" + mfrR);
	}
	
	@After
	public void clean() {
		logger.log(Level.INFO,"Start: Deleting the manufacturer: " + mfr);
		remove(mfrService, mfr);
		mfrService.remove(mfr);
		assertNull(find(mfrService, mfr.getId()));
		logger.log(Level.INFO, "END: Manufacturer Deleted");
	}
	
	public Manufacturer edit(ManufacturerService service, Manufacturer manufacturer) throws InvalidEntityException {
		assertNotNull(service);
		return service.edit(manufacturer);
	}
	public void remove(ManufacturerService service, Manufacturer manufacturer) {
		assertNotNull(service);
		service.remove(find(service, manufacturer.getId()));
		assertNull(find(service, manufacturer.getId()));
	}
	
	public Manufacturer create (ManufacturerService service, Manufacturer manufacturer) throws InvalidEntityException {
		assertNotNull(service);
		manufacturer = new Manufacturer();
		manufacturer.setName("HELLMANS");
		manufacturer.setDateCreated(LocalDateTime.now());
		manufacturer.setLastUpdated(LocalDateTime.now());		
		service.create(manufacturer);
		Manufacturer m = find(service, manufacturer.getId());
		assertNotNull(m);
		return m;
	}
	
	public Manufacturer find(ManufacturerService service, int id) {
		assertNotNull(service);
		return service.find(id);
	}
}
