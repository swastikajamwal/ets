package com.fintek.ets.db.dao;

import java.util.List;

import com.fintek.ets.db.model.Order;
import com.fintek.ets.db.model.User;

/**
 * The DAO interface for Order table.
 * 
 * @author sjamwal
 *
 */
public interface OrderDAO {
	
	public List<Order> getOrderList();
	
	public void saveOrder(Order order);

}
