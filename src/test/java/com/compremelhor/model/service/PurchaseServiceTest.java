package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.List;

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
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.Freight;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.SkuPartner;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class PurchaseServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Stock.class.getPackage())
				.addPackage(UserNotFoundException.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(CategoryService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.merge(SkuServiceTest.createTestArchive())
				.merge(PartnerServiceTest.createTestArchive());
	}

	
	@Inject private PartnerService partnerService;
	@Inject private SkuService skuService;
	@Inject private StockService stockService;
	@Inject private SkuPartnerService skuPartnerService;
		
	private Partner partner;
	private Stock st;
	private SkuPartner sp;
	private Sku sku;
	private User user;
		
	@Inject private CategoryService categoryService;
	@Inject private ManufacturerService manufacturerService;
	@Inject private PurchaseService purchaseService;
	@Inject private UserService userService;
	
	private UserServiceTest ust;
	private SkuServiceTest sst;
	private PartnerServiceTest pst;
	private StockServiceTest stockServiceTest;
	
	private Purchase purchase;
	private PurchaseLine purchaseLine;
	private Freight freight;
	
	@Before
	public void config() {
		ust = new UserServiceTest();
		sst = new SkuServiceTest();
		pst = new PartnerServiceTest();
		stockServiceTest = new StockServiceTest();
		
		user = ust.createUser(userService, user);
		sku = sst.createSkuAndCategoryAndManufacturer(skuService, sku);	
		partner = pst.createPartner(partnerService, partner);
		st = stockServiceTest.createStock(stockService, skuPartnerService, partnerService, skuService, partner, sku);
		sp = st.getSkuPartner();
		assertNotNull(sp);
		
		purchase = createPurchase(purchaseService, userService, purchase, user, freight);
		purchaseLine = addLine(purchaseService, purchase, purchaseLine, st);
	}
	
	@Test
	public void checkUser() {
		assertNotNull(purchase.getUser());
	}
	
	@Test
	public void findItens() {
		List<PurchaseLine> itens = purchaseService.findAllItensByPurchase(purchase);
		assertNotNull(itens);
		assertEquals(1, itens.size());
		
		PurchaseLine line2 = null;
		line2 = addLine(purchaseService, purchase, line2, st);
		
		itens = purchaseService.findAllItensByPurchase(purchase);
		assertNotNull(itens);
		assertEquals(2, itens.size());
		
		purchaseService.removeItem(line2);
		line2 = purchaseService.findLine(line2.getId());
		
		assertNull(line2);
		
		purchase = purchaseService.find(purchase.getId());
	}

	@After
	public void removing() {
		
		removePurchaseAndItensAndFreight(purchaseService, purchase, freight);
		stockServiceTest.removeStockAndSkuPartner(stockService, skuPartnerService, st, sp);	
		sst.removeSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
		pst.removePartner(partnerService, partner);
		ust.removeUser(userService, user);
	}
	
	public void removePurchaseAndItensAndFreight(PurchaseService service, Purchase purchase, Freight freight) {
		assertNotNull(service);
		assertNotNull(purchase);
										
		service.remove(purchase);
		purchase = service.find(purchase.getId());
		assertNull(purchase);
	}
	
	public Purchase createPurchase(PurchaseService service, UserService userService, Purchase purchase, User user, Freight freight) {
		assertNotNull(service);
		assertNotNull(user);
		
		freight = new Freight();
		freight.setDateCreated(LocalDateTime.now());
		freight.setLastUpdated(LocalDateTime.now());
		freight.setPurchase(purchase);
		freight.setValueRide(20.00);
		
		Address ad = userService
			.findAllAddressByUser(user)
			.orElseThrow(RuntimeException::new)
			.get(0);
		
		freight.setShipAddress(ad);
		
		purchase = new Purchase();
		purchase.setUser(user);
		purchase.setDateCreated(LocalDateTime.now());
		purchase.setLastUpdated(LocalDateTime.now());
		purchase.setStatus(Purchase.Status.PROCESSING);
		purchase.setTotalValue(0.0);
		purchase.setFreight(freight);
				
		service.create(purchase);
		
		purchase = service.find(purchase.getId());
		assertNotNull(purchase);
		return purchase;
	}
	
	public PurchaseLine addLine(PurchaseService service, Purchase purchase, PurchaseLine line, Stock stock) {
		assertNotNull(service);
		assertNotNull(purchase);
		assertNotNull(stock);
		
		line = new PurchaseLine();
		line.setDateCreated(LocalDateTime.now());
		line.setLastUpdated(LocalDateTime.now());
		line.setPurchase(purchase);
		line.setQuantity(10.00);
		line.setStock(stock);
		line.setUnitPrice(stock.getUnitPrice());
		
		service.addItem(purchase, line);
		
		line = service.findLine(line.getId());
		assertNotNull(line);
		return line;
	}

}
