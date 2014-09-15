package com.fintek.ets.oms;

import com.fintek.ets.db.model.Order;
import com.fintek.ets.db.model.Trade;

public interface OrderManagementService {
	
	Trade executeOrder(Order order);

}
