package com.fintek.ets.oms;

import com.fintek.ets.db.model.Order;

public interface MarketService {
	
	void newOrder(Order order);

}
