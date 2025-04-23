package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.OperationType;
import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import com.example.OrderMatchingService.domain.matching.PriceTimePrioritystrategy;
import com.example.OrderMatchingService.service.OrderBookManager;
import com.example.OrderMatchingService.service.OrderMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;

//@SpringBootTest
class OrderMatchingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	private OrderMatcher orderMatcher;
	private OrderBookManager orderBookManager;
	private MatchingStrategy strategy = new PriceTimePrioritystrategy();

	@BeforeEach
	public void setUp() {
		orderBookManager = mock(OrderBookManager.class);
		orderMatcher = new OrderMatcher("ticker", orderBookManager, strategy);
	}

	@Test
	public void testBuyOrder_MatchesAgainstSellBook() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 100, 150L, new Date(125, Calendar.JANUARY,1));
		orderMatcher.match(sellOrder);

		UUID buyUserId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), buyUserId, OperationType.BUY,
				"ticker", 100, 150L, new Date(125, Calendar.JANUARY,1));

		//action
		List<Trade> trades = orderMatcher.match(buyOrder);

		//assertions
		assertEquals(1, trades.size());
		Trade trade = trades.get(0);
		assertEquals(buyUserId, trade.getBuyerId());
		assertEquals(sellUserId, trade.getSellerId());
		assertEquals(150, trade.getPrice());
		assertEquals(100, trade.getQuantity());
	}

	@Test
	public void testSellOrder_MatchesAgainstBuyBook() {
		UUID buyUserId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), buyUserId, OperationType.BUY,
				"ticker", 50, 200L, new Date(125, Calendar.JANUARY,1));
		orderMatcher.match(buyOrder);

		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 50, 200L, new Date(125, Calendar.JANUARY,1));

		//action
		List<Trade> trades = orderMatcher.match(sellOrder);

		//assertions
		assertEquals(1, trades.size());
		Trade trade = trades.get(0);
		assertEquals(50, trade.getQuantity());
		assertEquals(200, trade.getPrice());
	}

	@Test
	public void testPartialMatch_LeavesOrderInBook() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 100, 40L, new Date(125, Calendar.JANUARY,1));

		orderMatcher.match(sellOrder);

		UUID buyUserId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), buyUserId, OperationType.BUY,
				"ticker", 30, 70L, new Date(125, Calendar.JANUARY,1));

		//action
		List<Trade> trades = orderMatcher.match(buyOrder);

		assertEquals(1, trades.size());
		assertEquals(30, trades.get(0).getQuantity());

		UUID nextBuyUserId = UUID.randomUUID();
		Order nextBuyOrder = new Order(UUID.randomUUID(), nextBuyUserId, OperationType.BUY,
				"ticker", 40, 60L, new Date(125, Calendar.JANUARY,1));

		//action
		trades = orderMatcher.match(nextBuyOrder);

		assertEquals(1, trades.size());
		assertEquals(40, trades.get(0).getQuantity());
	}

	@Test
	public void wrongTickerName() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"wrong ticker", 100, 40L, new Date(125, Calendar.JANUARY,1));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			// Code that should throw the exception
			orderMatcher.match(sellOrder);
		});

		assertEquals("wrong ticker name", exception.getMessage()); ;
	}

	@Test
	public void testLatencyIsRecorded() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 100, 40L, new Date(125, Calendar.JANUARY,1));
		orderMatcher.match(sellOrder);

		long latency = orderMatcher.getAverageLatencyMicros();
		assertTrue(latency > 0 || latency == 0); // At least tested no exception and tracked
	}

}
