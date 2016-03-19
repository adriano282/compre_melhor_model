package com.compremelhor.model.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.ManufacturerDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class CategoryServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Manufacturer.class.getPackage())
				.addPackage(UserNotFoundException.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(ManufacturerDao.class.getPackage())
				.addPackage(ManufacturerService.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject private CategoryService service;
	
	private Category category;
	
	@Before
	public void setup() throws InvalidEntityException {
		category = new Category();
		category.setName("BEBIDAS");
		category.setDateCreated(LocalDateTime.now());
		category.setLastUpdated(LocalDateTime.now());
		service.create(category);
		
		Assert.assertNotEquals(0, category.getId());
	}
	
	@Test
	public void edit() throws InvalidEntityException {
		category.setName("CARNES");
		service.edit(category);
		
		category = service.find(category.getId());
		Assert.assertEquals("CARNES", category.getName());
	}
	
	@After
	public void delete() {
		service.remove(category);
		
		category = service.find(category.getId());
		Assert.assertNull(category);
	}

}
