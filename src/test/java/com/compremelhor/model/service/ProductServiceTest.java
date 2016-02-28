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
import com.compremelhor.model.entity.Product;
import com.compremelhor.model.entity.Product.UnitType;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.util.LoggerProducer;

@RunWith(Arquillian.class)
public class ProductServiceTest {

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
		/*return ArquillianWarnUtils.getBasicWebArchive()
				.addPackage(User.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(UserService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage());
				*/
	}
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ManufacturerService manufacturerService;
	
	@Inject
	private Logger logger;
	
	private Category category;
	
	private Product product;
	
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
		
		product = new Product();
		product.setName("Maionese");
		product.setDescription("Maionese Hellmans. Qualidade garantida");
		product.setManufacturer(manufacturer);
		product.setUnit(UnitType.UN);
		product.setCode(code);
		product.addCategory(category);
		
	}
	
	@Test
	public void createAndDeleteAProduct() {
		creations();
		alterations();
		searching();
	}
	
	private void creations() {
		categoryService.createCategory(category);
		assertNotEquals(0, category.getId());
		logger.log(Level.INFO, "Category created: " + category);
		
		manufacturerService.createManufacturer(manufacturer);
		assertNotEquals(0, manufacturer.getId());
		logger.log(Level.INFO, "ManufacturerCreated: " + manufacturer);
		
		
		productService.createProduct(product);
		assertNotEquals(0, product.getId());
		logger.log(Level.INFO, "Product created: " + product);
	}
	
	private void alterations() {
		category.setName("NOME ALTERADO da CATEGORIA");
		categoryService.editCategory(category);
		logger.log(Level.INFO, "Category altered: " + category);
		
		manufacturer.setName("NOME FABRICANTE ALTERADO");
		manufacturerService.editManufacturer(manufacturer);
		logger.log(Level.INFO, "Manufacturer altered: " + manufacturer);
		
		product.getCode().setCode("CODIGO ALTERADO");
		productService.editProduct(product);
		logger.log(Level.INFO, "Product altered: " + product);
	}
	
	private void searching() {
		Category c = categoryService.findCategory(category.getId());
		logger.log(Level.INFO, "Category found: " + c);
		
		Manufacturer m = manufacturerService.findManufacturer(manufacturer.getId());
		logger.log(Level.INFO, "Manufacturer found: " + m);
		
		Product p = productService.findProduct(product.getId());
		logger.log(Level.INFO, "Product found: " + p);
	}
	
	@After
	public void removing() {
		productService.removeProduct(product);
		logger.log(Level.INFO, "Product " + product + " deleted");
		
		manufacturerService.removeManufacturer(manufacturer);
		logger.log(Level.INFO, "Manufacturer " + manufacturer + " deleted");
		
		categoryService.removeCategory(category);
		logger.log(Level.INFO, "Category " + category + " deleted");
	}

}
