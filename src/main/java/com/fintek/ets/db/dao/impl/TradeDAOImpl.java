package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.fintek.ets.db.dao.TradeDAO;
import com.fintek.ets.db.model.Trade;


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
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			return session.createQuery("from Trade").list();
		}catch(Throwable t){
			System.err.println("Exception in getTradeList()....");
			t.printStackTrace();
			trans.rollback();						
		}finally{
			if(session.isConnected()) {
				trans.commit();
			}
		}
		return null;
	}

	@Override
	public void saveTrade(Trade trade) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(trade);
	}
	

}
