package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

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
import com.compremelhor.model.exception.UserNotFoundException;
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
				.addPackage(ManufacturerService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject	private ManufacturerService mfrService;
	@Inject private Logger logger;
	
	private Manufacturer mfr;
	
	@Before
	public void config() {
		mfr = createManufacturer(mfrService, mfr);
	}
	
	@Test
	public void editManufacturer() {
		logger.log(Level.INFO, "START: Editing a manufacturer");
		
		mfr.setName("Other Manufacturer");
		mfr.setLastUpdated(LocalDateTime.now());
		
		Manufacturer mfrR = null;
		mfrR = editManufacturer(mfrService, mfr);
		
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
		removeManufacturer(mfrService, mfr);
		mfrService.removeManufacturer(mfr);
		assertNull(findManufacturer(mfrService, mfr.getId()));
		logger.log(Level.INFO, "END: Manufacturer Deleted");
	}
	
	public Manufacturer editManufacturer(ManufacturerService service, Manufacturer manufacturer) {
		assertNotNull(service);
		return service.editManufacturer(manufacturer);
	}
	public void removeManufacturer(ManufacturerService service, Manufacturer manufacturer) {
		assertNotNull(service);
		service.removeManufacturer(findManufacturer(service, manufacturer.getId()));
		assertNull(findManufacturer(service, manufacturer.getId()));
	}
	
	public Manufacturer createManufacturer (ManufacturerService service, Manufacturer manufacturer) {
		assertNotNull(service);
		manufacturer = new Manufacturer();
		manufacturer.setName("HELLMANS");
		manufacturer.setDateCreated(LocalDateTime.now());
		manufacturer.setLastUpdated(LocalDateTime.now());		
		service.createManufacturer(manufacturer);
		Manufacturer m = findManufacturer(service, manufacturer.getId());
		assertNotNull(m);
		return m;
	}
	
	public Manufacturer findManufacturer(ManufacturerService service, int id) {
		assertNotNull(service);
		return service.findManufacturer(id);
	}
}
