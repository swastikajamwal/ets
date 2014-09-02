package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

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
		// TODO Auto-generated method stub
		return this.sessionFactory.getCurrentSession().createQuery("from ets.Portfolio portfolio").list();
	}

	@Override
	public void savePortfolio(TradePortfolio portfolio) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(portfolio);
	}

	

	

}
