package com.fintek.ets.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintek.ets.Portfolio;
import com.fintek.ets.PortfolioPosition;
import com.fintek.ets.db.model.Trade;

/**
 * 
 * @author sjamwal
 *
 */
@Service
public class PortfolioServiceImpl implements PortfolioService {

	// user -> Portfolio
	private final ConcurrentMap<String, Portfolio> portfolioLookup = new ConcurrentHashMap<>();
	
	private final CachedService cachedService;
	

	@Autowired
	public PortfolioServiceImpl(CachedService cachedService) {
		this.cachedService = cachedService;		
	}
	
	@PostConstruct
	public void init() {
		loadTrades();	
	}
	
	private void loadTrades(){
		Map<String, List<Trade>> tradesByUser = cachedService.getTradesByUser();
		Set<String> keySet = tradesByUser.keySet();
		for(String key : keySet) {
			List<Trade> list = tradesByUser.get(key);
			System.out.println("trades by user "+key+" : "+list.size());
			Portfolio portfolio = new Portfolio();
			for(Trade trade : list) {
				portfolio.addPosition(new PortfolioPosition(trade.getId(), trade.getSymbol(), trade.getSide(), Double.valueOf(trade.getSize()), Double.valueOf(trade.getTradePrice()), getDateString(trade.getTradeDate())));			
			}
			this.portfolioLookup.putIfAbsent(key, portfolio);
		}		
	}


	public Portfolio findPortfolio(String username) {
		portfolioLookup.clear();
		loadTrades();
		Portfolio portfolio = this.portfolioLookup.get(username);
		if (portfolio == null) {
			System.out.print("Throwing IllegalArgumentException in findPortfolio...");
			throw new IllegalArgumentException(username);
		}
		System.out.println("PortfolioServiceImpl: returning portfolio for "+username+" with positions: "+portfolio.getPositions().size());
		return portfolio;
	}
	
	private String getDateString(Date tradeDate) {
		String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String stringDate = sdf.format(tradeDate);
		return stringDate;
	}

}
