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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.util.GeneratorPasswordHash;
import com.compremelhor.model.util.LoggerProducer;

@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(Arquillian.class)
public class UserServiceTest {
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(User.class.getPackage())
				.addPackage(LocalDateTimeAttributeConverter.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(UserService.class.getPackage())
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
	private UserService userService;
	
	@Inject
	private Logger logger;
	
	private User user;
	
	@Before
	public void config() throws NoSuchAlgorithmException {
		user = new User();
		user.setUsername("Adriano de Jesus");
		user.setDocument("42.761.057-6");
		user.setPasswordHash(GeneratorPasswordHash.getHash("teste123"));
		user.setDocumentType(User.DocumentType.CPF);
		user.setDateCreated(LocalDateTime.now());
		user.setLastUpdated(LocalDateTime.now());
	}
	
	@Test
	public void createEditAndDeleteAnUser() throws NoSuchAlgorithmException {
		logger.log(Level.INFO, "Start: Creating an user...");
		userService.createUser(user);		
		User usrResult = userService.findUserByDocument("42.761.057-6");			
		assertNotNull(usrResult);
		logger.log(Level.INFO, "End: User created -- " + usrResult);
		
		
		logger.log(Level.INFO, "Start: Editing an user...");
		user.setUsername("Pedro");
		user.setDocument("42.761.057-7");
		user.setLastUpdated(LocalDateTime.now());
		user.setPasswordHash(GeneratorPasswordHash.getHash("teste456"));
		user.setDocumentType(User.DocumentType.CNPJ);
		
		User userR = null;
		userR = userService.editUser(user);
		
		logger.log(Level.INFO, "End: User edited -- " + user);
		
		assertEquals(userR.getDocument(), "42.761.057-7");
		assertEquals(userR.getDocumentType(), User.DocumentType.CNPJ);
		assertEquals(userR.getUsername(), "Pedro");
		assertEquals(userR.getPasswordHash(), GeneratorPasswordHash.getHash("teste456"));
		
		logger.log(Level.INFO, "Start: Deleting the user -- ID: " + userR.getId());
		userService.removeUser(user);
		assertNull(userService.findUser(user.getId()));
		logger.log(Level.INFO, "End: User deleted." );
	}
}
