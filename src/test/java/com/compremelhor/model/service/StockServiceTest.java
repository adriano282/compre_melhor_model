package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.List;
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

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Code;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.Code.CodeType;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.util.LoggerProducer;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.DEFAULT)
public class StockServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Stock.class.getPackage())
				.addPackage(UserNotFoundException.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(CategoryService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.merge(SkuServiceTest.createTestArchive())
				.merge(PartnerServiceTest.createTestArchive());
	}
	
	@Inject private PartnerService partnerService;
	@Inject private SkuService skuService;
	@Inject private StockService stockService;
	@Inject private SkuPartnerService skuPartnerService;
	@Inject private Logger logger;
	
	private Category category;
	private Manufacturer manufacturer;
	private Code code;
	private Partner partner;
	private Stock st;
	private SkuPartner sp;
	private Sku sku;
		
	@Inject private CategoryService categoryService;
	@Inject private ManufacturerService manufacturerService;
	
	
	@Before
	public void create() {
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
		
		skuService.createProduct(sku);
		assertNotEquals(0, sku.getId());
		logger.log(Level.INFO, "Sku created: " + sku);

		partner = new Partner();
		partner.setName("Super Mercado da Gente");
		assertNotNull(partnerService);
		partnerService.create(partner);
		
		Partner par = partnerService.find(partner.getId());
		partner = par;
		assertNotNull(partner);
		
		logger.log(Level.INFO, "Partner created: " + partner);

		stockService.createStock(partner, sku);
	
		sp = skuPartnerService.findSkuPartnerBySku(sku);
		assertNotNull(sp);
		st = sp.getStock();
		assertNotNull(st);
	}
	
	@Test
	public void editStock() {
		logger.log(Level.WARNING, "TESTE");
		st.setUnitPrice(20.00);
		stockService.edit(st);
		
		assertEquals(Double.valueOf(20.00), stockService.find(st.getId()).getUnitPrice());
		
		stockService.addStock(st, 100.00);
		
		assertEquals(Double.valueOf(100.00), stockService.find(st.getId()).getQuantity());		
	}
	
	@After
	public void removing() {
		logger.log(Level.WARNING, "AFTER");
		
		sp = skuPartnerService.findSkuPartnerBySku(sku);
		assertNotNull(sp);
		st = sp.getStock();
		stockService.removeStockAndSkuPartner(st);
		
		skuService.removeProduct(sku);
		logger.log(Level.INFO, "Sku " + sku + " deleted");
		
		manufacturerService.removeManufacturer(manufacturer);
		logger.log(Level.INFO, "Manufacturer " + manufacturer + " deleted");
		
		categoryService.removeCategory(category);
		logger.log(Level.INFO, "Category " + category + " deleted");
		
		Partner p = partnerService.find(partner.getId());
		assertNotNull(p);
		partnerService.remove(p);
		assertNull(partnerService.find(partner.getId()));
		logger.log(Level.INFO, "Partner deleted. ID: " + partner.getId());
	}
	
}
