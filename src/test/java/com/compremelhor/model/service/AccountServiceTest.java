package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.util.LoggerProducer;

@RunWith(Arquillian.class)
public class AccountServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Partner.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(PartnerDao.class.getPackage())
				.addPackage(PartnerService.class.getPackage())
				.addPackage(LimitOfAddressesReachedException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	
	@Inject private AccountService accountService;
	@Inject private PartnerService partnerService;
	
	private Account account;
	private Partner partner;
	
	private PartnerServiceTest pst;
	

	@Before
	public void config() {
		pst = new PartnerServiceTest();
		partner = pst.createPartner(partnerService, partner);
		account = createAccount(accountService, account, partner);
	}
	
	@Test
	public void changeUserName() {
		account.setUsername("adriano");
		accountService.edit(account);
		
		account = accountService.find(account.getId());
		assertNotNull(account);
		assertEquals("adriano", account.getUsername());
		
	}
	
	@After
	public void clean() {
		removeAccount(accountService, account);
		pst.removePartner(partnerService, partner);
	}
	
	public void removeAccount(AccountService service, Account account) {
		assertNotNull(service);
		assertNotNull(account);
		
		service.remove(account);
		assertNull(service.find(account.getId()));

	}
	
	public Account createAccount(AccountService service, Account account, Partner partner) {
		assertNotNull(service);
		assertNotNull(partner);
		
		account = new Account();
		account.setPartner(partner);
		account.setPassword("teste");
		account.setUsername("parceiro@gmail.com");
		
		service.create(account);
		
		account = service.find(account.getId());
		assertNotNull(account);
		return account;
	}
}
