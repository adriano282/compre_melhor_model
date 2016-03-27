package com.compremelhor.model.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Code;
import com.compremelhor.model.entity.Code.CodeType;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.converter.LocalDateTimeAttributeConverter;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.exception.UserNotFoundException;
import com.compremelhor.model.strategy.Strategy;
import com.compremelhor.model.strategy.user.UniqueUsernameStrategy;
import com.compremelhor.model.util.LoggerProducer;
import com.compremelhor.model.validation.groups.PartnerAddress;

@RunWith(Arquillian.class)
public class SkuServiceTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(User.class.getPackage())
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
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject private CategoryService categoryService;
	@Inject private SkuService skuService;
	@Inject private ManufacturerService manufacturerService;
	@Inject private Logger logger;
	
	private Sku sku;
				 
	@Before
	public void config() throws InvalidEntityException {
		sku = createSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
	}
	
	@Test
	public void searchAndEditAProduct() throws InvalidEntityException {		
		alterations();
		searching();
	}
	
	private void alterations() throws InvalidEntityException {
		
		sku = skuService.find(sku.getId());
		
		sku.getCode().setCode("CODIGO ALTERADO");
		sku = skuService.edit(sku);
		logger.log(Level.INFO, "Sku altered: " + sku);
		sku = findSku(skuService, sku.getId());
		assertNotNull(sku);
		Assert.assertEquals(sku.getCode().getCode(), "CODIGO ALTERADO");		
	}
	
	@Test
	public void additioningCategory() throws InvalidEntityException {
		Set<String> fetches = new HashSet<String>();
		fetches.add("categories");
		sku = skuService.find(sku.getId(), fetches);
		
		int qtdeCa = sku.getCategories().size();
		
		Category c = new Category();
		c.setName("OUTRA CATEGORIA - B");
		sku.addCategory(c);
		
		sku = skuService.edit(sku);
		
		Assert.assertEquals(qtdeCa + 1, sku.getCategories().size());
	}
	
	@Test
	public void removingCategoryFromSku() throws InvalidEntityException {
		
		Set<String> fetches = new HashSet<String>();
		fetches.add("categories");
		
		sku = skuService.find(sku.getId(), fetches);
		
		int qtdeCa = sku.getCategories().size();
		
		logger.log(Level.WARNING, "SIZE " + qtdeCa);
		
		Category category = new Category();		
		category.setName("Alimentos Gelados");
		
		
				
		if (sku.getCategories().remove(category) ) {
			logger.log(Level.WARNING, "REMOVED");
		}
		
		sku.getCategories().stream().forEach(s -> System.out.println(s));
		
		sku = skuService.edit(sku);	
		Assert.assertEquals(qtdeCa -1, sku.getCategories().size());
		
		Category cat = categoryService.findCategoryByName(category.getName());
		
		categoryService.remove(cat);
		Assert.assertNull(categoryService.findCategoryByName(category.getName()));
		
	}
	
	private void searching() {
		logger.log(Level.INFO, "Category found: " + categoryService.findCategoryBySkuId(sku.getId()).getName());
		
		Manufacturer m = manufacturerService.find(sku.getManufacturer().getId());
		logger.log(Level.INFO, "Manufacturer found: " + m);
		
		sku = findSku(skuService, sku.getId());
		logger.log(Level.INFO, "Sku found: " + sku);
		
	}
	
	@After
	public void removing() {
		removeSkuAndCategoryAndManufacturer(skuService, manufacturerService, categoryService, sku);
		logger.log(Level.INFO, "Sku " + sku + " deleted");
	}
	
	public void removeSkuAndCategoryAndManufacturer(SkuService service, ManufacturerService manufacturerService, CategoryService cs, final Sku sku) {
		assertNotNull(service);		
		assertNotNull(sku);
		
		Manufacturer m = sku.getManufacturer();
		assertNotNull(m);
		
		assertNotEquals(0, sku.getId());
		
		Optional<List<Category>> categories = Optional.ofNullable(cs.findCategoriesBySkuId(sku.getId()));
		categories.ifPresent(list -> {
				list.stream().forEach(c -> {
					service.removeSkuCategory(sku.getId(), c.getId());
				});
			});
			
				
		service.remove(sku);
		assertNull(findSku(service, sku.getId()));
		
		manufacturerService.remove(m);
		assertNull(manufacturerService.find(m.getId()));
		
		categories.ifPresent(list -> list.stream().forEach(c -> cs.remove(c)));

	}
	
	public Sku findSku(SkuService service, int id) {
		assertNotNull(service);
		return service.find(id);
	}
	
	public Sku createSkuAndCategoryAndManufacturer(SkuService service, ManufacturerService manufacturerService,CategoryService categoryService, Sku sku) throws InvalidEntityException {
		assertNotNull(service);
				
		Code code = new Code();
		code.setCode("CODE001");
		code.setType(CodeType.OTHER);
		
		Category category = new Category();		
		category.setName("Alimentos Gelados");
		
		categoryService.create(category);
		
		sku = new Sku();
		sku.setName("Maionese");
		sku.setDescription("Maionese Hellmans. Qualidade garantida");
		
		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setName("HELLMANS");
		manufacturer.setLastUpdated(LocalDateTime.now());
		manufacturer.setDateCreated(LocalDateTime.now());
		
		manufacturerService.create(manufacturer);
		manufacturer = manufacturerService.find(manufacturer.getId());
				
		sku.setManufacturer(manufacturer);
		sku.setUnit(UnitType.UN);
		sku.setCode(code);
		
		sku.addCategory(category);
		
			
		service.create(sku);
		Sku s = service.find(sku.getId());
		assertNotNull(s);
		return s;
	}
}

