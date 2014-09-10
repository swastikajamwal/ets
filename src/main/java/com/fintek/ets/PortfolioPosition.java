package com.fintek.ets;

/**
 * The portfolio position class.
 * 
 * @author sjamwal
 *
 */
public class PortfolioPosition {

	private final String company;
	private final String ticker;
	private final double price;
	private final int shares;
	
	private final long updateTime;	
	private String order;
	private String time;
	private String type;
	private double size;
	private String symbol;
	
	public PortfolioPosition(String order, String symbol, String type, double size, double price) {
		this.order = order;
		this.symbol = symbol;
		this.type = type;
		this.size = size;
		this.price = price;
		
		this.ticker = symbol;
		this.shares = (int) size;
		this.company = symbol;
		this.updateTime = System.currentTimeMillis();
	}


	public PortfolioPosition(String company, String ticker, double price, int shares) {
		this.company = company;
		this.ticker = ticker;
		this.price = price;
		this.shares = shares;
		this.updateTime = System.currentTimeMillis();
	}

	public PortfolioPosition(PortfolioPosition other, int sharesToAddOrSubtract) {
		this.company = other.company;
		this.ticker = other.ticker;
		this.price = other.price;
		this.shares = other.shares + sharesToAddOrSubtract;
		this.updateTime = System.currentTimeMillis();
	}

	public String getCompany() {
		return this.company;
	}

	public String getTicker() {
		return this.ticker;
	}

	public double getPrice() {
		return this.price;
	}

	public int getShares() {
		return this.shares;
	}

	public long getUpdateTime() {
		return this.updateTime;
	}

	@Override
	public String toString() {
		return "PortfolioPosition [order=" + this.order + ", symbol=" + this.symbol + ", company=" + this.company + ", ticker=" + this.ticker
				+ ", price=" + this.price + ", shares=" + this.shares + "]";
	}

	public String getOrder() {
		return order;
	}

	public String getTime() {
		return time;
	}

	public String getType() {
		return type;
	}

	public double getSize() {
		return size;
	}

	public String getSymbol() {
		return symbol;
	}


	public void setOrder(String order) {
		this.order = order;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public void setType(String type) {
		this.type = type;
	}


	public void setSize(double size) {
		this.size = size;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
