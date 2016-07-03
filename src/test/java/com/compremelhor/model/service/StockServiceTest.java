package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
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
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.merge(SkuServiceTest.createTestArchive())
				.merge(PartnerServiceTest.createTestArchive());
	}

	@Inject private CategoryService categoryService;
	@Inject private ManufacturerService manufacturerService;
	@Inject private PartnerService partnerService;
	@Inject private SkuService skuService;
	@Inject private StockService stockService;
	@Inject private SkuPartnerService skuPartnerService;
	@Inject private Logger logger;

	private SkuServiceTest sst;
	private PartnerServiceTest pst;

	private Partner partner;
	private Stock st;
	private SkuPartner sp;
	private Sku sku;
	
	@Before
	public void create() throws InvalidEntityException {
		sst = new SkuServiceTest();
		pst = new PartnerServiceTest();
		
		sku = sst.createSkuAndCategoryAndManufacturer(skuService,manufacturerService, categoryService, sku);
		logger.log(Level.INFO, "Sku created: " + sku);

		partner = pst.createPartner(partnerService, partner);
		st = createStock(stockService,skuPartnerService, partnerService, skuService, partner, sku);
	}
	
	@Test
	public void editStock() throws InvalidEntityException {
		logger.log(Level.WARNING, "TESTE");
		st.setUnitPrice(5.00);
		st = stockService.edit(st);		
		assertEquals(Double.valueOf(5.00), stockService.find(st.getId()).getUnitPrice());
		st = stockService.addStock(st, 100.00);		
		assertEquals(Double.valueOf(100.00), stockService.find(st.getId()).getQuantity());
		
		
	}
	
	@After
	public void removing() {
		assertNotNull(st);
		sp = st.getSkuPartner();
		
		removeStockAndSkuPartner(stockService, skuPartnerService, st, sp);		
		
		sst.removeSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
		
		pst.removePartner(partnerService, partner);
	}
	
	public void removeStockAndSkuPartner(StockService service, SkuPartnerService skuPartnerService, Stock stock, SkuPartner skuPartner) {
		assertNotNull(stock);
		assertNotNull(skuPartner);
		assertNotNull(skuPartner.getSku());
		
		service.remove(stock);
		
		skuPartner = skuPartnerService.findSkuPartnerBySku(skuPartner.getSku());
		assertNull(skuPartner);		
	}
	
	public Stock createStock(StockService service, SkuPartnerService sps, PartnerService partnerService, SkuService skuService, Partner partner, Sku sku) throws InvalidEntityException {
		assertNotNull(service);
		assertNotNull(partner);
		assertNotNull(sku);
		
		assertNotNull(partnerService.find(partner.getId()));
		assertNotNull(skuService.find(sku.getId()));
		
		service.createStock(partner, sku);
		
		SkuPartner sp = sps.findSkuPartnerBySku(sku);
		
		assertNotNull(sp);
		
		Stock st = sp.getStock();
		assertNotNull(st);
		
		return st;
	}
}
