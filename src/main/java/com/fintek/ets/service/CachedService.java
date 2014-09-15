package com.fintek.ets.service;

import java.util.List;
import java.util.Map;


public interface CachedService {
	
	String getUserRole(String userName);
	
	boolean isValidUser(String userName, String password);
	
	Map<String, List<com.fintek.ets.db.model.Trade>> getTradesByUser();
	
	void saveTrade(com.fintek.ets.db.model.Trade trade);
	
	List<com.fintek.ets.db.model.Trade> getTradesForUser(String userid);
	
	void closeTrade(String id);

}
