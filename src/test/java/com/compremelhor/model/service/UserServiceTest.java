package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.security.NoSuchAlgorithmException;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.Address;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.LimitOfAddressesReachedException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.GeneratorPasswordHash;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class UserServiceTest {
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(User.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(UserService.class.getPackage())
				.addPackage(Strategy.class.getPackage())
				.addPackage(UniqueUsernameStrategy.class.getPackage())
				.addPackage(InvalidEntityException.class.getPackage())
				.addPackage(PartnerAddress.class.getPackage())
				.addPackage(LimitOfAddressesReachedException.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject private AddressService addressService;
	@Inject	private UserService userService;
	@Inject	private Logger logger;
	private User user;
	
	@Before
	public void config() throws NoSuchAlgorithmException, InvalidEntityException {
		user = createUser(userService,addressService, user);
	}
	
//	@Test
	public void gettingLimitOfAddressError()  {
		for (int i = 0; i < 4; i++) {
			Address ad2 = new Address();
			ad2.setCity("Mogi das Cruzes");
			ad2.setNumber("49");
			ad2.setQuarter("Vila Brasileira");
			ad2.setState("SP");
			ad2.setStreet("Rua Alfredo Gomes Loureiro");
			ad2.setZipcode("08738290");
			try {
				userService.addAddress(user.getId(), ad2);
			} catch (Exception e) {
				Assert.assertTrue(e.getMessage().equals("user.addresses.max.number.excedded.error.message#"));
				return;
			}
		}
	}

	@Test
	public void gettingUniqueDocumentAndUsernameError() {
		User user = new User();
		user.setUsername("Adriano de Jesus");
		user.setDocument("41831058898");
		
		try {user.setPasswordHash(GeneratorPasswordHash.getHash("teste123")); } 
		catch(Exception e) { throw new RuntimeException(e);}
		
		user.setDocumentType(User.DocumentType.CPF);
		user.setDateCreated(LocalDateTime.now());
		user.setLastUpdated(LocalDateTime.now());
		
		try {
			createUser(userService, addressService, user);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage()
					.equals("user.document.already.registered.error.message#user.username.already.registered.error.message"));
		}
	}
	
	@Test
	public void gettingInvalidDocumentError() {
		User user = new User();
		user.setUsername("Adriano de JesusS");
		user.setDocument("42/761.057-6");
		
		try {user.setPasswordHash(GeneratorPasswordHash.getHash("teste123")); } 
		catch(Exception e) { throw new RuntimeException(e);}
		
		user.setDocumentType(User.DocumentType.CPF);
		user.setDateCreated(LocalDateTime.now());
		user.setLastUpdated(LocalDateTime.now());
		
		try {
			createUser(userService,addressService, user);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
			Assert.assertTrue(e.getMessage().equals("user.document.invalid.format.error.message#"));
		}
	}

//	@Test
	public void gettingUniqueUsernameError() {
		User user = new User();
		user.setUsername("Adriano de Jesus");
		user.setDocument("999.999.99977");
		
		try {user.setPasswordHash(GeneratorPasswordHash.getHash("teste123")); } 
		catch(Exception e) { throw new RuntimeException(e);}
		
		user.setDocumentType(User.DocumentType.CPF);
		user.setDateCreated(LocalDateTime.now());
		user.setLastUpdated(LocalDateTime.now());
		
		try {
			user = createUser(userService, addressService, user);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().equals("user.username.already.registered.error.message"));
		}
	}
	
//	@Test
	public void editUser() throws NoSuchAlgorithmException, InvalidEntityException {
				
		logger.log(Level.INFO, "Start: Editing an user...");
		user.setUsername("Pedro");
		user.setDocument("41831058898");
		user.setLastUpdated(LocalDateTime.now());
		user.setPasswordHash(GeneratorPasswordHash.getHash("teste456"));
		user.setDocumentType(User.DocumentType.CNPJ);
		
		User userR = null;
		userR = userService.edit(user);
		
		logger.log(Level.INFO, "End: User edited -- " + user);
		
		assertEquals(userR.getDocument(), "41831058898");
		assertEquals(userR.getDocumentType(), User.DocumentType.CNPJ);
		assertEquals(userR.getUsername(), "Pedro");
		assertEquals(userR.getPasswordHash(), GeneratorPasswordHash.getHash("teste456"));
		
		userService.findAllAddressByUser(userR).ifPresent(ads -> assertEquals(1, ads.size()));
	}
	
	@After
	public void clear() {
		removeUser(userService, addressService, user);
	}
	
	public void removeUser(UserService service,AddressService addressService, User user) {
		assertNotNull(service);
		assertNotNull(user);
		addressService.removeAllAddressesByUser(user.getId());
		service.remove(service.find(user.getId()));
		
		assertNull(service.find(user.getId()));
	}
	
	public User createUser(UserService service, AddressService addressService, User user) throws InvalidEntityException {
		assertNotNull(service);
				
		if (user == null) {
			user = new User();
			user.setUsername("Adriano de Jesus");
			user.setDocument("41831058898");
			
			try {user.setPasswordHash(GeneratorPasswordHash.getHash("teste123")); } 
			catch(Exception e) { throw new RuntimeException(e);}
			
			user.setDocumentType(User.DocumentType.CPF);
			user.setDateCreated(LocalDateTime.now());
			user.setLastUpdated(LocalDateTime.now());
		}
				
		Address ad = new Address();
		ad.setCity("Mogi das Cruzes");                         
		ad.setNumber("49");
		ad.setQuarter("Vila Brasileira");
		ad.setState("SP");
		ad.setStreet("Rua Alfredo Gomes Loureiro");
		ad.setZipcode("08738290");
		
		ad.setUser(user);
		
		service.create(user);
		addressService.create(ad);
		
		user = service.findUserByDocument("41831058898");			
		assertNotNull(user);
		
		return user;
	}
}
