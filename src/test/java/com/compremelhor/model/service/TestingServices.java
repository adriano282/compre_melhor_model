package com.compremelhor.model.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ManufacturerServiceTest.class, SkuServiceTest.class,
		UserServiceTest.class, PartnerServiceTest.class, StockServiceTest.class })
public class TestingServices {

}
