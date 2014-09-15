package com.fintek.ets.service;

/**
 * A class that models a trade
 * 
 * @author sjamwal
 *
 */
public class Trade {

	private String symbol;
	private double size;
	private Side action;
	private String username;

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Side getAction() {
		return this.action;
	}

	public void setAction(Side action) {
		this.action = action;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "[symbol=" + this.symbol + ", size=" + this.size
				+ ", action=" + this.action + ", username=" + this.username + "]";
	}


	public enum Side {
		Buy, Sell, Close;
	}

}
