package com.fintek.ets.db.dao;

import java.util.List;

import com.fintek.ets.db.model.User;
import com.fintek.ets.service.Trade;

/**
 * The DAO interface for Trade table.
 * 
 * @author sjamwal
 *
 */
public interface TradeDAO {
	
	public List<Trade> getTradeList();
	
	public void saveTrade(Trade order);

}
