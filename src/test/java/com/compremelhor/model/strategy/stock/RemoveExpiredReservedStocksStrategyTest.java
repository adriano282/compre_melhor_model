package com.compremelhor.model.strategy.stock;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RemoveExpiredReservedStocksStrategyTest extends RemoveExpiredReservesStrategy {

	final private RemoveExpiredReservesStrategy target;
	private LocalDateTime date;
	private boolean result;
	
	@Parameters
	public static Iterable<Object[]> data() {
		System.out.println("Run @Parameters");
		return Arrays.asList(new Object[][] {
			{LocalDateTime.of(2015, 7, 24, 17, 45, 10), true},
			{LocalDateTime.of(2016, 6, 24, 17, 45, 10), true},
			{LocalDateTime.of(2016, 7, 23, 17, 45, 10), true},
			{LocalDateTime.of(2016, 7, 24, 13, 16, 10), false},
			{LocalDateTime.of(2017, 7, 24, 13, 16, 10), true},
			{LocalDateTime.of(2016, 7, 24, 13, 15, 11), false},
			{LocalDateTime.of(2016, 7, 24, 13, 15, 10), false},
			{LocalDateTime.of(2016, 7, 24, 13, 15, 9), true},
			{LocalDateTime.of(2016, 7, 24, 13, 14, 10), true}
		});
	}

	public RemoveExpiredReservedStocksStrategyTest(LocalDateTime date, boolean result) 
	{	
		super(null);
		target = new RemoveExpiredReservesStrategy(null);
		target.setNow(LocalDateTime.of(2016, 7, 24, 17, 45, 10));
		this.date = date;
		this.result = result;
		System.out.println("Run Constructor");
		System.out.println(" -- dateCreated=" + date + " / result=" + result);
	}

	@Before
	public void setUp() {
		System.out.println("Run @Before");
		System.out.println(" -- dateCreated=" + date);
	}
	
	@Test
	public void test() {
		System.out.println("Run @Test");
		assertEquals(result, target.isExpired(date));
	}

}
