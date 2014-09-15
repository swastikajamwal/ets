package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.Query;
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
		System.out.println("getTradeList.....");
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			List<Trade> ls = session.createQuery("from Trade t where t.status='open'").list();
			trans.commit();
			return ls;
		}catch(Throwable t){
			System.err.println("Exception in getTradeList()....");
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
	public void saveTrade(Trade trade) {
		System.out.println("savingTrade.....");
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			session.save(trade);
			trans.commit();
		}catch(Throwable t){
			System.err.println("Exception in saveTrade()....");
			t.printStackTrace();
			trans.rollback();						
		}finally{
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public List<Trade> getTradesForUser(String userid) {
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			List<Trade> ls = session.createQuery("from Trade t where t.userID='"+userid+"' ").list();
			trans.commit();
			return ls;
		}catch(Throwable t){
			System.err.println("Exception in getTradeList()....");
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
	public void closeTrade(String id) {
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			Query query = session.createQuery("update Trade t set t.status='closed' where t.id='"+id+"' ");
			query.executeUpdate();
			trans.commit();
		}catch(Throwable t){
			System.err.println("Exception in getTradeList()....");
			t.printStackTrace();
			trans.rollback();						
		}finally{
			if(session != null) {
				session.close();
			}
		}
	}
	

}
