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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Code;
import com.compremelhor.model.entity.Code.CodeType;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.UserNotFoundException;
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
	public void config() {
		sku = createSkuAndCategoryAndManufacturer(skuService, sku);
	}
	
	@Test
	public void searchAndEditAProduct() {		
		alterations();
		searching();
	}
	
	private void alterations() {
		sku.getCode().setCode("CODIGO ALTERADO");
		sku = skuService.editProduct(sku);
		logger.log(Level.INFO, "Sku altered: " + sku);
		sku = findSku(skuService, sku.getId());
		assertNotNull(sku);
	}
	
	private void searching() {
		logger.log(Level.INFO, "Category found: " + categoryService.findCategoryBySkuId(sku.getId()).getName());
		
		Manufacturer m = manufacturerService.findManufacturer(sku.getManufacturer().getId());
		logger.log(Level.INFO, "Manufacturer found: " + m);
		
		sku = findSku(skuService, sku.getId());
		logger.log(Level.INFO, "Sku found: " + sku);
		
	}
	
	@After
	public void removing() {
		removeSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
		logger.log(Level.INFO, "Sku " + sku + " deleted");
	}
	
	public void removeSkuAndCategoryAndManufacturer(SkuService service, ManufacturerService manufacturerService, CategoryService cs, Sku sku) {
		assertNotNull(service);		
		assertNotNull(sku);
		
		Manufacturer m = sku.getManufacturer();
		assertNotNull(m);
		
		assertNotEquals(0, sku.getId());
		Category c = cs.findCategoryBySkuId(sku.getId());
		assertNotNull(c);
		
		service.removeProduct(sku);
		assertNull(findSku(service, sku.getId()));
		
		cs.removeCategory(c);
		assertNull(cs.findCategory(c.getId()));
		
		manufacturerService.removeManufacturer(m);
		assertNull(manufacturerService.findManufacturer(m.getId()));
	}
	
	public Sku findSku(SkuService service, int id) {
		assertNotNull(service);
		return service.findProduct(id);
	}
	
	public Sku createSkuAndCategoryAndManufacturer(SkuService service, Sku sku) {
		assertNotNull(service);
				
		Code code = new Code();
		code.setCode("CODE001");
		code.setType(CodeType.OTHER);
		
		Category category = new Category();		
		category.setName("Alimentos Gelados");
		
		sku = new Sku();
		sku.setName("Maionese");
		sku.setDescription("Maionese Hellmans. Qualidade garantida");
		
		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setName("HELLMANS");
		manufacturer.setLastUpdated(LocalDateTime.now());
		manufacturer.setDateCreated(LocalDateTime.now());
		
		sku.setManufacturer(manufacturer);
		sku.setUnit(UnitType.UN);
		sku.setCode(code);
		sku.addCategory(category);
		
			
		service.createProduct(sku);
		Sku s = service.findProduct(sku.getId());
		assertNotNull(s);
		return s;
	}
}

