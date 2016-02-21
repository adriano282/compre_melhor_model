package com.compremelhor.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.util.GeneratorPasswordHash;
import com.compremelhor.model.util.LoggerProducer;

@RunWith(Arquillian.class)
public class UserServiceTest {
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(User.class.getPackage())
				.addPackage(UserDao.class.getPackage())
				.addPackage(UserService.class.getPackage())
				.addPackage(LoggerProducer.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private UserService userService;
	
	private User user;
	
	@Before
	public void config() throws NoSuchAlgorithmException {
		user = new User();
		user.setName("Adriano de Jesus");
		user.setDocument("42.761.057-6");
		user.setPasswordHash(GeneratorPasswordHash.getHash("teste123"));
		user.setDocumentType(User.DocumentType.CPF);
		user.setDateCreated(LocalDateTime.now());
		user.setLastUpdated(LocalDateTime.now());
	}
	
	@Test
	public void createAnUser() {
		userService.createUser(user);		
		User usrResult = userService.findUserByDocument("42.761.057-6");			
		assertNotNull(usrResult);
	}
	
	@Test
	public void editAnUser() throws NoSuchAlgorithmException {
		user.setName("Pedro");
		user.setDocument("42.761.057-7");
		user.setLastUpdated(LocalDateTime.now());
		user.setPasswordHash(GeneratorPasswordHash.getHash("teste456"));
		user.setDocumentType(User.DocumentType.CNPJ);
		
		user = userService.editUser(user);
		
		assertEquals(user.getDocument(), "42.761.057-7");
		assertEquals(user.getDocumentType(), User.DocumentType.CNPJ);
		assertEquals(user.getName(), "Pedro");
		assertEquals(user.getPasswordHash(), GeneratorPasswordHash.getHash("teste456"));
	}
	
	@Test
	public void removeUser() {
		userService.removeUser(user);
		assertNull(userService.findUser(user.getId()));
	}
	

}
