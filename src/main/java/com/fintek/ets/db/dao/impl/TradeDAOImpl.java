package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fintek.ets.db.dao.Order;
import com.fintek.ets.db.dao.OrderDAO;
import com.fintek.ets.db.dao.TradeDAO;
import com.fintek.ets.db.dao.UserDAO;
import com.fintek.ets.db.model.User;
import com.fintek.ets.service.Trade;

/**
 * The DAO class for Trade table
 * 
 * @author sjamwal
 *
 */
public class TradeDAOImpl implements TradeDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<Trade> getTradeList() {
		return this.sessionFactory.getCurrentSession().createQuery("from ets.Order order").list();
	}

	@Override
	public void saveTrade(Trade trade) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(trade);
	}
	

}
