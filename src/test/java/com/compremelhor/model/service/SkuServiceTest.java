package com.compremelhor.model.service;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.sku.UniqueCodeStrategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class SkuServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(User.class.getPackage())
				.addPackage(UserNotFoundException.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueCodeStrategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addPackage(CategoryService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject private CategoryService categoryService;
	@Inject private SkuService skuService;
	@Inject private ManufacturerService manufacturerService;
	@Inject private Logger logger;
	
	private Sku sku;
				 
	@Before
	public void config() throws InvalidEntityException {
		sku = createSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
	}
	
	@Test
	public void searchAndEditAProduct() throws InvalidEntityException {		
		alterations();
		searching();
	}
	
	private void alterations() throws InvalidEntityException {
		
		sku = skuService.find(sku.getId());
		
		sku.setCode("1234567831111");
		sku = skuService.edit(sku);
		logger.log(Level.INFO, "Sku altered: " + sku);
		sku = findSku(skuService, sku.getId());
		assertNotNull(sku);
		Assert.assertEquals(sku.getCode(), "1234567831111");		
	}
	
	private void searching() {
		logger.log(Level.INFO, "Category found: " + categoryService.findCategoryBySkuId(sku.getId()).getName());
		
		Manufacturer m = manufacturerService.find(sku.getManufacturer().getId());
		logger.log(Level.INFO, "Manufacturer found: " + m);
		
		sku = findSku(skuService, sku.getId());
		logger.log(Level.INFO, "Sku found: " + sku);
		
	}
	
	@After
	public void removing() {
		removeSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
		logger.log(Level.INFO, "Sku " + sku + " deleted");
	}
	
	public void removeSkuAndCategoryAndManufacturer(SkuService service, ManufacturerService manufacturerService, CategoryService cs, final Sku sku) {
		assertNotNull(service);		
		assertNotNull(sku);
		
		Manufacturer m = sku.getManufacturer();
		assertNotNull(m);
		
		assertNotEquals(0, sku.getId());
		
		service.remove(sku);
		assertNull(findSku(service, sku.getId()));
		
		if (sku.getCategory() != null) {
			cs.remove(sku.getCategory());
		}
		manufacturerService.remove(m);
		assertNull(manufacturerService.find(m.getId()));
	}
	
	public Sku findSku(SkuService service, int id) {
		assertNotNull(service);
		return service.find(id);
	}
	
	public Sku createSkuAndCategoryAndManufacturer(SkuService service, ManufacturerService manufacturerService,CategoryService categoryService, Sku sku) throws InvalidEntityException {
		assertNotNull(service);
						
		Category category = new Category();		
		category.setName("TAlimentos Gelados");
		
		categoryService.create(category);
		
		sku = new Sku();
		sku.setName("Maionese");
		sku.setCode("1234567890000");
		sku.setDescription("Maionese Hellmans. Qualidade garantida");
		
		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setName("HELLMANSW");
		manufacturer.setLastUpdated(LocalDateTime.now());
		manufacturer.setDateCreated(LocalDateTime.now());
		
		manufacturerService.create(manufacturer);
		manufacturer = manufacturerService.find(manufacturer.getId());
				
		sku.setManufacturer(manufacturer);
		sku.setUnit(UnitType.UN);
		
		sku.setCategory(category);
		
			
		service.create(sku);
		Sku s = service.find(sku.getId());
		assertNotNull(s);
		return s;
	}
}

