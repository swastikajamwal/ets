package com.fintek.ets.db.model;

public class TradePortfolio {
	
	private String id;
	private String userId;
	private String totalValue;
	private String pandl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}
	public String getPandl() {
		return pandl;
	}
	public void setPandl(String pandl) {
		this.pandl = pandl;
	}

}
