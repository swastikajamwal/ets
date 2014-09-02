package com.fintek.ets.db.dao;

import java.util.List;

import com.fintek.ets.db.model.TradePortfolio;
import com.fintek.ets.db.model.User;

/**
 * The DAO interface for Portfolio table.
 * 
 * @author sjamwal
 *
 */
public interface PortfolioDAO {
	
	public List<TradePortfolio> getPortfolioList();
	
	public void savePortfolio(TradePortfolio order);

}
