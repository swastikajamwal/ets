package com.fintek.ets.db.dao;

import java.util.List;

import com.fintek.ets.db.model.Trade;


/**
 * The DAO interface for Trade table.
 * 
 * @author sjamwal
 *
 */
public interface TradeDAO {
	
	List<Trade> getTradeList();
	
	void saveTrade(Trade order);
	
	List<Trade> getTradesForUser(String userid); 
	
	void closeTrade(String id);

}
