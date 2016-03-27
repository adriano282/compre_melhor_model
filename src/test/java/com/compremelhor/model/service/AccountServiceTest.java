package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import com.compremelhor.model.dao.PartnerDao;
import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class AccountServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(Partner.class.getPackage())
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(PartnerDao.class.getPackage())
				.addPackage(PartnerService.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addPackage(LimitOfAddressesReachedException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	
	@Inject private AccountService accountService;
	@Inject private PartnerService partnerService;
	@Inject private Logger logger;
	
	private Account account;
	private Partner partner;
	
	private PartnerServiceTest pst;
	

	@Before
	public void config() throws InvalidEntityException {
		pst = new PartnerServiceTest();
		partner = pst.createPartner(partnerService, partner);
		account = createAccount(accountService, account, partner);
	}
	
	@Test
	public void shouldThrowAnEntityInvalidException() {
		account.setUsername(null);
		try {
			accountService.edit(account);
		} catch (Exception e) {
			logger.log(Level.INFO, "Exception trown: " + e.getMessage());
			assertTrue(e instanceof InvalidEntityException);
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeUserName() throws InvalidEntityException {
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
	
	public Account createAccount(AccountService service, Account account, Partner partner) throws InvalidEntityException {
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
