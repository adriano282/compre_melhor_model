package com.compremelhor.model.service;

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

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Code;
import com.compremelhor.model.entity.Code.CodeType;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.util.LoggerProducer;

@RunWith(Arquillian.class)
public class SkuServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(User.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(CategoryService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject private CategoryService categoryService;
	@Inject private SkuService skuService;
	@Inject private ManufacturerService manufacturerService;
	@Inject private Logger logger;
	private Category category;
	private Sku sku;
	private Manufacturer manufacturer;
	private Code code;
	
	@Before
	public void config() {
		category = new Category();		
		category.setName("Alimentos Gelados");
		
		manufacturer = new Manufacturer();
		manufacturer.setName("HELLMANS");
		manufacturer.setDateCreated(LocalDateTime.now());
		manufacturer.setLastUpdated(LocalDateTime.now());
		
		code = new Code();
		code.setCode("CODE001");
		code.setType(CodeType.OTHER);
		
		sku = new Sku();
		sku.setName("Maionese");
		sku.setDescription("Maionese Hellmans. Qualidade garantida");
		sku.setManufacturer(manufacturer);
		sku.setUnit(UnitType.UN);
		sku.setCode(code);
		sku.addCategory(category);
		
	}
	
	@Test
	public void createAndDeleteAProduct() {
		creations();
		alterations();
		searching();
	}
	
	private void creations() {
		skuService.createProduct(sku);
		assertNotEquals(0, sku.getId());
		logger.log(Level.INFO, "Sku created: " + sku);
	}
	
	private void alterations() {
		category.setName("NOME ALTERADO da CATEGORIA");
		categoryService.editCategory(category);
		logger.log(Level.INFO, "Category altered: " + category);
		
		manufacturer.setName("NOME FABRICANTE ALTERADO");
		manufacturerService.editManufacturer(manufacturer);
		logger.log(Level.INFO, "Manufacturer altered: " + manufacturer);
		
		sku.getCode().setCode("CODIGO ALTERADO");
		skuService.editProduct(sku);
		logger.log(Level.INFO, "Sku altered: " + sku);
	}
	
	private void searching() {
		Category c = categoryService.findCategory(category.getId());
		logger.log(Level.INFO, "Category found: " + c);
		
		Manufacturer m = manufacturerService.findManufacturer(manufacturer.getId());
		logger.log(Level.INFO, "Manufacturer found: " + m);
		
		Sku p = skuService.findProduct(sku.getId());
		logger.log(Level.INFO, "Sku found: " + p);
	}
	
	@After
	public void removing() {
		skuService.removeProduct(sku);
		logger.log(Level.INFO, "Sku " + sku + " deleted");
		
		manufacturerService.removeManufacturer(manufacturer);
		logger.log(Level.INFO, "Manufacturer " + manufacturer + " deleted");
		
		categoryService.removeCategory(category);
		logger.log(Level.INFO, "Category " + category + " deleted");
	}
}
