package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.fintek.ets.db.dao.PortfolioDAO;
import com.fintek.ets.db.model.TradePortfolio;

/**
 * 
 * @author sjamwal
 *
 */
public class PortfolioDAOImpl implements PortfolioDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<TradePortfolio> getPortfolioList() {
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			return session.createQuery("from TradePortfolio").list();
		}catch(Throwable t){
			System.err.println("Exception in getPortfolioList()....");
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
	public void savePortfolio(TradePortfolio portfolio) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(portfolio);
	}

	

	

}
