package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
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
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
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
	@Inject private AddressService addressService;
		
	private Partner partner;
	private Stock st;
	private SkuPartner sp;
	private Sku sku;
	private User user;
		
	@Inject private CategoryService categoryService;
	@Inject private ManufacturerService manufacturerService;
	@Inject private PurchaseService purchaseService;
	@Inject private UserService userService;
	@Inject private FreightService freightService;
	
	private UserServiceTest ust;
	private SkuServiceTest sst;
	private PartnerServiceTest pst;
	private StockServiceTest stockServiceTest;
	
	private Purchase purchase;
	private PurchaseLine purchaseLine;
	private Freight freight;
	
	@Before
	public void config() throws InvalidEntityException {
		ust = new UserServiceTest();
		sst = new SkuServiceTest();
		pst = new PartnerServiceTest();
		stockServiceTest = new StockServiceTest();
		
		user = ust.createUser(userService, addressService, user);
		sku = sst.createSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);	
		partner = pst.createPartner(partnerService, partner);
		st = stockServiceTest.createStock(stockService, skuPartnerService, partnerService, skuService, partner, sku);
		sp = st.getSkuPartner();
		assertNotNull(sp);
		
		purchase = createPurchase(purchaseService, freightService, userService, purchase, user, freight);
		freight = purchase.getFreight();
		purchaseLine = addLine(purchaseService, purchase, purchaseLine, st);
	}
	
	@Test
	public void checkUser() {
		assertNotNull(purchase.getUser());
	}
	
	@Test
	public void findItens() throws InvalidEntityException {
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
		freight = purchase.getFreight();
	}

	@After
	public void removing() {
		
		removePurchaseAndItensAndFreight(purchaseService, freightService, purchase, freight);
		stockServiceTest.removeStockAndSkuPartner(stockService, skuPartnerService, st, sp);	
		sst.removeSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
		pst.removePartner(partnerService, partner);
		ust.removeUser(userService, addressService, user);
	}
	
	public void removePurchaseAndItensAndFreight(PurchaseService service, FreightService freightService, Purchase purchase, Freight freight) {
		assertNotNull(service);
		assertNotNull(purchase);
		assertNotNull(freight);
		
								
		freightService.remove(freight);
		
		freight = freightService.find(freight.getId());
		assertNull(freight);
		
		service.remove(purchase);
		purchase = service.find(purchase.getId());
		assertNull(purchase);
	}
	
	public Purchase createPurchase(PurchaseService service, FreightService freightService, UserService userService, Purchase purchase, User user, Freight freight) throws InvalidEntityException {
		assertNotNull(service);
		assertNotNull(user);
		
		freight = new Freight();
		freight.setDateCreated(LocalDateTime.now());
		freight.setLastUpdated(LocalDateTime.now());
		freight.setValueRide(20.00);
		freight.setStartingDate(LocalDate.now().plusDays(2));
		freight.setStartingTime(LocalTime.now().plusHours(2));
		
		Address ad = userService
			.findAllAddressByUser(user)
			.orElseThrow(RuntimeException::new)
			.get(0);
		
		freight.setShipAddress(ad);
		
		purchase = new Purchase();
		purchase.setUser(user);
		purchase.setDateCreated(LocalDateTime.now());
		purchase.setLastUpdated(LocalDateTime.now());
		purchase.setStatus(Purchase.Status.OPENED);
		purchase.setTotalValue(0.0);
			
		
		service.create(purchase);
		
		freight.setPurchase(purchase);
		freightService.create(freight);
		purchase.setFreight(freight);
		
		purchase = service.find(purchase.getId());
		assertNotNull(purchase);
		return purchase;
	}
	
	public PurchaseLine addLine(PurchaseService service, Purchase purchase, PurchaseLine line, Stock stock) throws InvalidEntityException {
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
