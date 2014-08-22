package com.fintek.ets.service;

import java.math.BigDecimal;


public class FXQuote {

	private final String symbol;
	private final BigDecimal bid;
	private final BigDecimal ask;


	public FXQuote(String symbol, BigDecimal bid, BigDecimal ask) {
		this.symbol = symbol;
		this.bid = bid;
		this.ask = ask;
	}


	public String getSymbol() {
		return symbol;
	}


	public BigDecimal getBid() {
		return bid;
	}


	public BigDecimal getAsk() {
		return ask;
	}
	
	@Override
	public String toString() {
		return "FXQuote [symbol=" + this.symbol + ", this.bid=" + bid + ", this.ask="+ask+"]";
	}
}
