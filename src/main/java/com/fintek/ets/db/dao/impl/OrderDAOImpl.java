package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.fintek.ets.db.dao.OrderDAO;
import com.fintek.ets.db.model.Order;

/**
 * 
 * @author sjamwal
 *
 */
public class OrderDAOImpl implements OrderDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<Order> getOrderList() {
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			List<Order> ls = session.createQuery("from Order").list();
			trans.commit();
			return ls;
		}catch(Throwable t){
			System.err.println("Exception in getOrderList()....");
			t.printStackTrace();
			trans.rollback();						
		}finally{
			if(session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public void saveOrder(Order order) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(order);
	}

	

}
