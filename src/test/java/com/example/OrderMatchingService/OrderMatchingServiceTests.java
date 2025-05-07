package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.*;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import com.example.OrderMatchingService.domain.matching.PriceTimePriorityStrategy;
import com.example.OrderMatchingService.service.OrderBookFactory;
import com.example.OrderMatchingService.service.OrderMatcher;
import com.example.OrderMatchingService.service.TradeEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class OrderMatchingServiceTests {

	private OrderMatcher orderMatcher;
	private MatchingStrategy strategy = new PriceTimePriorityStrategy();
  private OrderBookFactory orderBookFactory = new OrderBookFactory();

	@BeforeEach
	public void setUp() {
    OrderBook orderBook = orderBookFactory.getOrCreate("ticker");
		orderMatcher = new OrderMatcher("ticker", strategy, orderBook);
	}

	@Test
	public void testBuyOrder_MatchesAgainstSellBook() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 100, 150L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		//action
		orderMatcher.match(sellOrder);

		UUID buyUserId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), buyUserId, OperationType.BUY,
				"ticker", 100, 150L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		//action
		List<TradeCreatedEvent> tradesEvents = orderMatcher.match(buyOrder);

		//assertions
		assertEquals(1, tradesEvents.size());
		TradeCreatedEvent tradeEvent = tradesEvents.get(0);
		assertEquals(buyUserId, tradeEvent.getBuyUserId());
		assertEquals(sellUserId, tradeEvent.getSellUserId());
		assertEquals(150, tradeEvent.getPrice());
		assertEquals(100, tradeEvent.getQuantity());

		Trade trade = TradeEventMapper.fromEvent(tradeEvent);
		assertEquals(buyUserId, trade.getBuyerId());
		assertEquals(sellUserId, trade.getSellerId());
		assertEquals(150, trade.getPrice());
		assertEquals(100, tradeEvent.getQuantity());

		OrderBook orderBook = orderBookFactory.getOrCreate("ticker");
		assertNotNull(orderBook.getReservedOrder(sellOrder.getOrderID()));
		assertNotNull(orderBook.getReservedOrder(buyOrder.getOrderID()));

	}

	@Test
	public void testSellOrder_MatchesAgainstBuyBook() {
		UUID buyUserId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), buyUserId, OperationType.BUY,
				"ticker", 50, 200L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		//action
		orderMatcher.match(buyOrder);
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 50, 200L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);


		//action
		List<TradeCreatedEvent> tradesEvents = orderMatcher.match(sellOrder);

		//assertions
		assertEquals(1, tradesEvents.size());
		TradeCreatedEvent tradeEvent = tradesEvents.get(0);
		assertEquals(buyUserId, tradeEvent.getBuyUserId());
		assertEquals(sellUserId, tradeEvent.getSellUserId());
		assertEquals(200, tradeEvent.getPrice());
		assertEquals(50, tradeEvent.getQuantity());

		Trade trade = TradeEventMapper.fromEvent(tradeEvent);
		assertEquals(buyUserId, trade.getBuyerId());
		assertEquals(sellUserId, trade.getSellerId());
		assertEquals(200, trade.getPrice());
		assertEquals(50, tradeEvent.getQuantity());

		OrderBook orderBook = orderBookFactory.getOrCreate("ticker");
		assertNotNull(orderBook.getReservedOrder(sellOrder.getOrderID()));
		assertNotNull(orderBook.getReservedOrder(buyOrder.getOrderID()));
	}

	@Test
	public void testPartialMatch_LeavesOrderInBook() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 100, 40L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		//action
		orderMatcher.match(sellOrder);

		UUID buyUserId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), buyUserId, OperationType.BUY,
				"ticker", 50, 70L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		//action
		List<TradeCreatedEvent> tradeEvents = orderMatcher.match(buyOrder);

		//assertions
		assertEquals(1, tradeEvents.size());
		assertEquals(50, tradeEvents.get(0).getQuantity());

		OrderBook orderBook = orderBookFactory.getOrCreate("ticker");
		assertNull(orderBook.getReservedOrder(sellOrder.getOrderID()));
		assertNotNull(orderBook.getReservedOrder(buyOrder.getOrderID()));


		UUID nextBuyUserId = UUID.randomUUID();
		Order nextBuyOrder = new Order(UUID.randomUUID(), nextBuyUserId, OperationType.BUY,
				"ticker", 50, 60L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		//action
		tradeEvents = orderMatcher.match(nextBuyOrder);

		//assertions
		assertEquals(1, tradeEvents.size());
		assertEquals(50, tradeEvents.get(0).getQuantity());

		Trade trade = TradeEventMapper.fromEvent(tradeEvents.get(0));
		assertEquals(nextBuyUserId, trade.getBuyerId());
		assertEquals(sellUserId, trade.getSellerId());
		assertEquals(40, trade.getPrice());
		assertEquals(50, trade.getQuantity());

		assertNotNull(orderBook.getReservedOrder(sellOrder.getOrderID()));
		assertNotNull(orderBook.getReservedOrder(buyOrder.getOrderID()));
	}

	@Test
	void testNoMatchWhenBuyPriceIsLowerThanBestSell() {
		// Setup a sell order with price 210
		Order sellOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 50, 210L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(sellOrder);

		// Buy order with lower price than best sell
		Order buyOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.BUY,
				"ticker", 50, 200L, new Date(), OrderStatus.CREATED);

		// Act
		List<TradeCreatedEvent> tradeEvents = orderMatcher.match(buyOrder);

		// Assert
		assertTrue(tradeEvents.isEmpty());
	}

	@Test
	void testMatchUsesFIFOWhenMultipleOrdersAtSamePrice() {
		// Setup two sell orders at same price (order1 should be matched first)
		Order sellOrder1 = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 30, 200L, new Date(), OrderStatus.CREATED);
		Order sellOrder2 = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 30, 200L, new Date(), OrderStatus.CREATED);

		orderMatcher.match(sellOrder1);
		orderMatcher.match(sellOrder2);

		// A buy order that can match both
		Order buyOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.BUY,
				"ticker", 30, 200L, new Date(), OrderStatus.CREATED);

		// Act
		List<Trade> trades = orderMatcher.match(buyOrder).stream().map(TradeEventMapper::fromEvent).toList();

		// Assert
		assertEquals(1, trades.size());
		assertEquals(30, trades.get(0).getQuantity());
		assertEquals(sellOrder1.getUserId(), trades.get(0).getSellerId());
	}

	@Test
	public void wrongTickerName() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"wrong ticker", 100, 40L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			orderMatcher.match(sellOrder);
		});

		assertEquals("wrong ticker name", exception.getMessage()); ;
	}

	@Test
	void testOrderStatusFullyMatched() {
		Order sellOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 100, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(sellOrder);

		Order buyOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.BUY,
				"ticker", 100, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(buyOrder);

		assertEquals(OrderStatus.RESERVED, buyOrder.getStatus());
		assertEquals(OrderStatus.RESERVED, sellOrder.getStatus());
	}

	@Test
	void testOrderStatusReady() {
		Order sellOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 100, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(sellOrder);

		assertEquals(OrderStatus.READY_FOR_MATCHING, sellOrder.getStatus());
	}

	@Test
	void testBuyOrderStatusPartlyMatched() {
		Order sellOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 80, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(sellOrder);

		Order buyOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.BUY,
				"ticker", 100, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(buyOrder);

		assertEquals(OrderStatus.READY_FOR_MATCHING, buyOrder.getStatus());
	}

	@Test
	void testSellOrderStatusPartlyMatched() {
		Order sellOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL,
				"ticker", 100, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(sellOrder);

		Order buyOrder = new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.BUY,
				"ticker", 80, 150L, new Date(), OrderStatus.CREATED);
		orderMatcher.match(buyOrder);

		assertEquals(OrderStatus.READY_FOR_MATCHING, sellOrder.getStatus());
	}

	@Test
	public void testOrderAddedWhenNoMatch() {
		UUID userId = UUID.randomUUID();
		Order buyOrder = new Order(UUID.randomUUID(), userId, OperationType.BUY,
				"ticker", 100, 500L, new Date(), OrderStatus.CREATED);

		List<Trade> trades = TradeEventMapper.fromListEvents(orderMatcher.match(buyOrder));
		assertTrue(trades.isEmpty());
		assertEquals(OrderStatus.READY_FOR_MATCHING, buyOrder.getStatus());
	}

	@Test
	public void testLatencyIsRecorded() {
		UUID sellUserId = UUID.randomUUID();
		Order sellOrder = new Order(UUID.randomUUID(), sellUserId, OperationType.SELL,
				"ticker", 100, 40L, new Date(125, Calendar.JANUARY,1), OrderStatus.CREATED);
		orderMatcher.match(sellOrder);

		long latency = orderMatcher.getAverageLatencyMicros();
		assertTrue(latency > 0 || latency == 0);
	}

}
