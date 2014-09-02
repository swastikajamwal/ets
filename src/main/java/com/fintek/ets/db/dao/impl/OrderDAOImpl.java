package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fintek.ets.db.dao.Order;
import com.fintek.ets.db.dao.OrderDAO;
import com.fintek.ets.db.dao.UserDAO;
import com.fintek.ets.db.model.User;

public class OrderDAOImpl implements OrderDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<Order> getOrderList() {
		return this.sessionFactory.getCurrentSession().createQuery("from ets.Order order").list();
	}

	@Override
	public void saveOrder(Order order) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(order);
	}

	

}
