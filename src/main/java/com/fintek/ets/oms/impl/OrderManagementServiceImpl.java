package com.fintek.ets.oms.impl;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintek.ets.db.model.Order;
import com.fintek.ets.db.model.Side;
import com.fintek.ets.db.model.Trade;
import com.fintek.ets.oms.MarketService;
import com.fintek.ets.oms.OrderManagementService;
import com.fintek.ets.service.FXQuote;
import com.fintek.ets.service.QuoteListener;
import com.fintek.ets.service.QuoteService;

@Service
public class OrderManagementServiceImpl implements OrderManagementService, QuoteListener {
	
	private MarketService market;
	private Set<FXQuote> quotes;
	private final QuoteService quoteService;
	
	@Autowired
	public OrderManagementServiceImpl(QuoteService quoteService){
		this.quoteService = quoteService;		
	}
	
	@PostConstruct
	public void init(){
		quoteService.registerQuoteListener(this);		
	}

	@Override
	public Trade executeOrder(Order order) {
		Trade trade = new Trade();
		trade.setSide(order.getSide());
		trade.setSize(order.getSize());
		trade.setSymbol(order.getSymbol());
		trade.setTradeDate(Calendar.getInstance().getTime());
		trade.setUserID(order.getUserID());
		trade.setStatus("position");
		if(order.getSide().equals(Side.BUY)){
			trade.setTradePrice(getQuote(order.getSymbol()).getAsk()+"");
		}else{
			trade.setTradePrice(getQuote(order.getSymbol()).getBid()+"");
		}
		return trade;
	}

	private FXQuote getQuote(String symbol){
		for(FXQuote quote : quotes){
			if(quote.getSymbol().equals(symbol)){
				return quote;				
			}
		}
		return null;
	}
	
	@Override
	public void onQuote(Set<FXQuote> quotes) {
		this.quotes = quotes;		
	}
	
}
