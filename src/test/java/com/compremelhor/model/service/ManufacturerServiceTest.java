package com.compremelhor.model.service;

import static org.junit.Assert.*;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.util.LoggerProducer;

@RunWith(Arquillian.class)
public class ManufacturerServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Manufacturer.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(ManufacturerDao.class.getPackage())
				.addPackage(ManufacturerService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
				
	}
	@Inject 
	private ManufacturerService mfrService;
	
	@Inject 
	private Logger logger;
	
	private Manufacturer mfr;
	
	@Before
	public void config() {
		mfr = new Manufacturer();
		mfr.setName("Lider");
		mfr.setDateCreated(LocalDateTime.now());
		mfr.setLastUpdated(LocalDateTime.now());
	}
	
	@Test
	public void test() {
		logger.log(Level.INFO, "START: Creating a Manufacturer...");
		
		mfrService.createManufacturer(mfr);
		Manufacturer mfrR = mfrService.findManufacturerByName("Lider");
		assertNotNull(mfrR);
		
		logger.log(Level.INFO, "END: Manufacturer Created " + mfr);
		
		logger.log(Level.INFO, "START: Editing a manufacturer");
		mfr.setName("Other Manufacturer");
		mfr.setLastUpdated(LocalDateTime.now());
		
		mfrR = null;
		mfrR = mfrService.editManufacturer(mfr);
		
		logger.log(Level.INFO, "END: Manufacturer Edited");
		assertEquals(mfrR.getName(), "Other Manufacturer");
		
		logger.log(Level.INFO,"Start: Deleting the manufacturer: " + mfrR);
		mfrService.removeManufacturer(mfrR);
		assertNull(mfrService.findManufacturer(mfr.getId()));
		logger.log(Level.INFO, "END: Manufacturer Deleted");
		
	}

}
