package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.entity.Stock;
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
	
	private SkuServiceTest sst;
	private PartnerServiceTest pst;
	
	private Stock st;
	private SkuPartner sp;
	private Sku sku;
		
	
	@Before
	public void create() {
		sst = new SkuServiceTest();
		pst = new PartnerServiceTest();
		sst.config();
		pst.config();
		List<Sku> skus = skuService.findAll();
		
		assertNotNull(skus);
		assertNotNull(skus.get(0));
		sku = skus.get(0);
		
		List<Partner> partners = partnerService.findAll();
		
		assertNotNull(partners);
		assertNotNull(partners.get(0));
		
		logger.log(Level.WARNING, partners.get(0) + "");
		stockService.createStock(partners.get(0), sku);
	
		sp = skuPartnerService.findSkuPartnerBySku(sku);
		assertNotNull(sp);
		st = sp.getStock();
		assertNotNull(st);
		
		logger.log(Level.WARNING, "BEFORE");
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
		sst.removing();
		pst.removing();
	}
	
}
