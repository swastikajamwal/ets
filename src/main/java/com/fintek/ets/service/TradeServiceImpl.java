package com.fintek.ets.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fintek.ets.Portfolio;
import com.fintek.ets.PortfolioPosition;
import com.fintek.ets.db.model.Order;
import com.fintek.ets.oms.OrderManagementService;
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
	private final OrderManagementService oms;


	@Autowired
	public TradeServiceImpl(SimpMessageSendingOperations messagingTemplate, PortfolioService portfolioService, CachedService cachedService, OrderManagementService oms) {
		this.messagingTemplate = messagingTemplate;
		this.portfolioService = portfolioService;
		this.cachedService = cachedService;
		this.oms = oms;
	}

	/**
	 * A trade is executed in an external system when available, i.e. asynchronously.
	 */
	public void executeTrade(Trade trade) {
		System.out.println("executeTrade.....!!");
		Portfolio portfolio = this.portfolioService.findPortfolio(trade.getUsername());
		String symbol = trade.getSymbol();
		double size = trade.getSize();
		Side action = trade.getAction();
		if(Side.Close == action){
			handlePositionClose(trade);						
		}else{
			System.out.println("trade: "+trade.toString());
			com.fintek.ets.db.model.Trade executedTrade = oms.executeOrder(getOrder(trade));
			
			cachedService.saveTrade(executedTrade);
			System.out.println("Trade saved in executeTrade.....");
		}
		

//		PortfolioPosition newPosition = (trade.getAction() == Side.Buy) ?
//				portfolio.buy(symbol, sharesToTrade) : portfolio.sell(symbol, sharesToTrade);
//
//		if (newPosition == null) {
//			String payload = "Rejected trade " + trade;
//			this.messagingTemplate.convertAndSendToUser(trade.getUsername(), "/queue/errors", payload);
//			return;
//		}

//		this.tradeResults.add(new TradeResult(trade.getUsername(), newPosition));
		List<com.fintek.ets.db.model.Trade> tradesForUser = cachedService.getTradesForUser(trade.getUsername());
		Portfolio pfolio = new Portfolio();
		for(com.fintek.ets.db.model.Trade e : tradesForUser){
			pfolio.addPosition(new PortfolioPosition(e.getId(), e.getSymbol(), e.getSide(), Double.valueOf(e.getSize()), Double.valueOf(e.getTradePrice()), getDateString(e.getTradeDate())));			
		}
		this.messagingTemplate.convertAndSendToUser(trade.getUsername(), "/queue/position-updates", pfolio.getPositions());	
		System.out.println("Exitting executeTrade.....");
		
	}

	//to raise an opposite order at market conditions and book profits/loss. 
	private void handlePositionClose(Trade trade) {
		logger.debug("closing position... " );				
	}

	private String getDateString(Date tradeDate) {
		String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String stringDate = sdf.format(tradeDate);
		return stringDate;
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
	
	private Order getOrder(Trade trade){
		Order order = new Order();
		order.setSide(trade.getAction().name());
		order.setSize(Double.toString(trade.getSize()));
		order.setSymbol(trade.getSymbol());
		order.setUserID(trade.getUsername());
		return order;
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
