package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.*;
import com.example.events.OrderMatchedEvent;
import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import com.example.OrderMatchingService.domain.matching.PriceTimePriorityStrategy;
import com.example.OrderMatchingService.service.OrderBookFactory;
import com.example.OrderMatchingService.service.OrderMatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class OrderMatchingServiceTests {

//	private static final String TICKER = "ticker";
//
//	private OrderMatcher orderMatcher;
//	private final MatchingStrategy strategy = new PriceTimePriorityStrategy();
//	private final OrderBookFactory orderBookFactory = new OrderBookFactory();
//
//	@BeforeEach
//	public void setUp() {
//		orderMatcher = new OrderMatcher(TICKER, strategy, orderBookFactory.getOrCreate(TICKER));
//	}
//
//	@AfterEach
//	void tearDown() {
//		orderBookFactory.clear();
//	}
//
//	@Test
//	void givenBuyOrder_whenMatchesSellOrder_thenTradeIsCreated() {
//		Order sellOrder = TestOrderFactory.sell(TICKER, 100L, new BigDecimal(150));
//		orderMatcher.match(sellOrder);
//
//		Order buyOrder = TestOrderFactory.buy(TICKER, 100L, new BigDecimal(150));
//		List<OrderMatchedEvent> tradeEvents = orderMatcher.match(buyOrder);
//
//		assertEquals(1, tradeEvents.size());
//		assertTradeEvent(tradeEvents.get(0), buyOrder.getUserId(), sellOrder.getUserId(), new BigDecimal(150), 100);
//		assertReservedOrdersExist(sellOrder, buyOrder);
//	}
//
//	@Test
//	void givenSellOrder_whenMatchesBuyOrder_thenTradeIsCreated() {
//		Order buyOrder = TestOrderFactory.buy(TICKER, 50L, new BigDecimal(200));
//		orderMatcher.match(buyOrder);
//
//		Order sellOrder = TestOrderFactory.sell(TICKER, 50L, new BigDecimal(200));
//		List<OrderMatchedEvent> tradeEvents = orderMatcher.match(sellOrder);
//
//		assertEquals(1, tradeEvents.size());
//		assertTradeEvent(tradeEvents.get(0), buyOrder.getUserId(), sellOrder.getUserId(), new BigDecimal(200), 50);
//		assertReservedOrdersExist(buyOrder, sellOrder);
//	}
//
//	@Test
//	void givenPartialMatch_thenUnmatchedPartRemainsInBook() {
//		Order sellOrder = TestOrderFactory.sell(TICKER, 100L, new BigDecimal(40));
//		orderMatcher.match(sellOrder);
//
//		Order buyOrder = TestOrderFactory.buy(TICKER, 50L, new BigDecimal(70));
//		List<OrderMatchedEvent> tradeEvents = orderMatcher.match(buyOrder);
//		assertEquals(1, tradeEvents.size());
//		assertEquals(50, tradeEvents.get(0).getQuantity());
//
//		OrderBook orderBook = orderBookFactory.getOrCreate(TICKER);
//		assertNull(orderBook.getReservedOrder(sellOrder.getOrderID()));
//		assertNotNull(orderBook.getReservedOrder(buyOrder.getOrderID()));
//
//		Order nextBuyOrder = TestOrderFactory.buy(TICKER, 50L, new BigDecimal(60));
//		tradeEvents = orderMatcher.match(nextBuyOrder);
//		assertEquals(1, tradeEvents.size());
//		assertEquals(50, tradeEvents.get(0).getQuantity());
//	}
//
//	@Test
//	void givenBuyPriceBelowSellPrice_thenNoMatch() {
//		orderMatcher.match(TestOrderFactory.sell(TICKER, 50L, new BigDecimal(210)));
//		List<OrderMatchedEvent> tradeEvents = orderMatcher.match(TestOrderFactory.buy(TICKER, 50L, new BigDecimal(200)));
//		assertTrue(tradeEvents.isEmpty());
//	}
//
//	@Test
//	void givenMultipleSellOrdersAtSamePrice_whenBuyOrder_thenFifoPriorityUsed() {
//		Order sell1 = TestOrderFactory.sell(TICKER, 30L, new BigDecimal(200));
//		Order sell2 = TestOrderFactory.sell(TICKER, 30L, new BigDecimal(200));
//		orderMatcher.match(sell1);
//		orderMatcher.match(sell2);
//
//		Order buyOrder = TestOrderFactory.buy(TICKER, 30L, new BigDecimal(200));
//		Trade trade = TradeEventMapper.fromEvent(orderMatcher.match(buyOrder).get(0));
//		assertEquals(sell1.getUserId(), trade.getSellerId());
//	}
//
//	@Test
//	void givenWrongTicker_thenThrowsIllegalArgumentException() {
//		Order wrongOrder = TestOrderFactory.sell("wrong ticker", 100L, new BigDecimal(40));
//		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderMatcher.match(wrongOrder));
//		assertEquals("wrong ticker name", exception.getMessage());
//	}
//
//	@Test
//	void whenOrdersFullyMatch_thenStatusReserved() {
//		Order sell = TestOrderFactory.sell(TICKER, 100L, new BigDecimal(150));
//		orderMatcher.match(sell);
//		Order buy = TestOrderFactory.buy(TICKER, 100L, new BigDecimal(150));
//		orderMatcher.match(buy);
//		assertEquals(OrderStatus.RESERVED, sell.getStatus());
//		assertEquals(OrderStatus.RESERVED, buy.getStatus());
//	}
//
//	@Test
//	void whenNoMatch_thenStatusReady() {
//		Order order = TestOrderFactory.sell(TICKER, 100L, new BigDecimal(150));
//		orderMatcher.match(order);
//		assertEquals(OrderStatus.ACTIVE, order.getStatus());
//	}
//
//	@Test
//	void whenBuyPartiallyFulfills_thenStatusReady() {
//		Order sell = TestOrderFactory.sell(TICKER, 80L, new BigDecimal(150));
//		orderMatcher.match(sell);
//		Order buy = TestOrderFactory.buy(TICKER, 100L, new BigDecimal(150));
//		orderMatcher.match(buy);
//		assertEquals(OrderStatus.ACTIVE, buy.getStatus());
//	}
//
//	@Test
//	void whenSellPartiallyFulfills_thenStatusReady() {
//		Order sell = TestOrderFactory.sell(TICKER, 100L, new BigDecimal(150));
//		orderMatcher.match(sell);
//		Order buy = TestOrderFactory.buy(TICKER, 80L, new BigDecimal(150));
//		orderMatcher.match(buy);
//		assertEquals(OrderStatus.ACTIVE, sell.getStatus());
//	}
//
//	@Test
//	void whenNoTrade_thenOrderInBook() {
//		Order buyOrder = TestOrderFactory.buy(TICKER, 100L, new BigDecimal(500));
//		List<OrderMatchedEvent> tradeEvents = orderMatcher.match(buyOrder);
//		assertTrue(tradeEvents.isEmpty());
//		assertEquals(OrderStatus.ACTIVE, buyOrder.getStatus());
//		assertEquals(orderBookFactory.getOrCreate(TICKER).getBuyBook().get(buyOrder.getPrice()).get(buyOrder.getCreatedAt()).get(0).getOrderID(), buyOrder.getOrderID());
//	}
//
//	@Test
//	void whenOrderProcessed_thenLatencyMeasured() {
//		orderMatcher.match(TestOrderFactory.sell(TICKER, 100L, new BigDecimal(40)));
//		assertTrue(orderMatcher.getAverageLatencyMicros() >= 0);
//	}
//
//	private void assertTradeEvent(OrderMatchedEvent trade, UUID buyId, UUID sellId, BigDecimal price, int quantity) {
//		assertEquals(buyId, trade.getBuyUserId());
//		assertEquals(sellId, trade.getSellUserId());
//		assertEquals(price, trade.getPrice());
//		assertEquals(quantity, trade.getQuantity());
//	}
//
//	private void assertReservedOrdersExist(Order... orders) {
//		OrderBook orderBook = orderBookFactory.getOrCreate(TICKER);
//		for (Order order : orders) {
//			assertNotNull(orderBook.getReservedOrder(order.getOrderID()));
//		}
//	}
//}
//
//class TestOrderFactory {
//	private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(2000, 1, 1, 0, 0);
//
//	static Order buy(String ticker, Long quantity, BigDecimal price) {
//		return new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.BUY, ticker, quantity, price, DEFAULT_DATE, OrderStatus.CREATED);
//	}
//
//	static Order sell(String ticker, Long quantity, BigDecimal price) {
//		return new Order(UUID.randomUUID(), UUID.randomUUID(), OperationType.SELL, ticker, quantity, price, DEFAULT_DATE, OrderStatus.CREATED);
//	}

}
