package com.fintek.ets.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintek.ets.Portfolio;
import com.fintek.ets.PortfolioPosition;
import com.fintek.ets.db.dao.TradeDAO;
import com.fintek.ets.service.Trade.Side;

/**
 * The trade service class responsible for execution of trades and updation of portfolio.
 * 
 * @author sjamwal
 *
 */
@Service
public class TradeServiceImpl implements TradeService {

	private static final Log logger = LogFactory.getLog(TradeServiceImpl.class);
	private final SimpMessageSendingOperations messagingTemplate;
	private final PortfolioService portfolioService;
	private final List<TradeResult> tradeResults = new CopyOnWriteArrayList<>();
	private final List<Trade> trades = new CopyOnWriteArrayList<>();
	private final CachedService cachedService;


	@Autowired
	public TradeServiceImpl(SimpMessageSendingOperations messagingTemplate, PortfolioService portfolioService, CachedService cachedService) {
		this.messagingTemplate = messagingTemplate;
		this.portfolioService = portfolioService;
		this.cachedService = cachedService;
	}

	/**
	 * A trade is executed in an external system when available, i.e. asynchronously.
	 */
	public void executeTrade(Trade trade) {
		System.out.println("executeTrade.....!!");
		Portfolio portfolio = this.portfolioService.findPortfolio(trade.getUsername());
		String symbol = trade.getSymbol();
		int sharesToTrade = trade.getSize();

		PortfolioPosition newPosition = (trade.getAction() == Side.Buy) ?
				portfolio.buy(symbol, sharesToTrade) : portfolio.sell(symbol, sharesToTrade);

		if (newPosition == null) {
			String payload = "Rejected trade " + trade;
			this.messagingTemplate.convertAndSendToUser(trade.getUsername(), "/queue/errors", payload);
			return;
		}

		this.tradeResults.add(new TradeResult(trade.getUsername(), newPosition));
	}

	@Scheduled(fixedDelay=1500)
	public void sendTradeNotifications() {

		for (TradeResult result : this.tradeResults) {
			if (System.currentTimeMillis() >= (result.timestamp + 1500)) {
				logger.debug("Sending position update: " + result.position);
				this.messagingTemplate.convertAndSendToUser(result.user, "/queue/position-updates", result.position);
				this.tradeResults.remove(result);
			}
		}
	}
	
	@PostConstruct
	public void start() {
	}


	private static class TradeResult {

		private final String user;
		private final PortfolioPosition position;
		private final long timestamp;

		public TradeResult(String user, PortfolioPosition position) {
			this.user = user;
			this.position = position;
			this.timestamp = System.currentTimeMillis();
		}
	}

}
