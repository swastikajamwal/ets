package com.fintek.ets.service;

/**
 * 
 * @author sjamwal
 *
 */
public class Trade {

	private String ticker;
	private int shares;
	private Side action;
	private String username;

	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public int getShares() {
		return this.shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
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
		return "[ticker=" + this.ticker + ", shares=" + this.shares
				+ ", action=" + this.action + ", username=" + this.username + "]";
	}


	public enum Side {
		Buy, Sell;
	}

}
