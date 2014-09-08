package com.fintek.ets.service.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintek.ets.db.dao.OrderDAO;
import com.fintek.ets.db.dao.PortfolioDAO;
import com.fintek.ets.db.dao.TradeDAO;
import com.fintek.ets.db.dao.UserDAO;
import com.fintek.ets.db.model.Order;
import com.fintek.ets.db.model.Trade;
import com.fintek.ets.db.model.TradePortfolio;
import com.fintek.ets.db.model.User;
import com.fintek.ets.service.CachedService;

@Service
public class CachedServiceImpl implements CachedService {
	
	private static final Log logger = LogFactory.getLog(CachedServiceImpl.class);
	private final List<User> userList = new CopyOnWriteArrayList<>();
	private final List<Order> orderList = new CopyOnWriteArrayList<>();
	private final List<Trade> trades = new CopyOnWriteArrayList<>();
	private final List<TradePortfolio> portfolios = new CopyOnWriteArrayList<>();
	
	private final UserDAO userDAO;
	private final OrderDAO orderDAO;
	private final TradeDAO tradeDAO;
	private final PortfolioDAO portfolioDAO;
	
	
	@Autowired
	public CachedServiceImpl(UserDAO userDAO, TradeDAO tradeDAO, OrderDAO orderDAO, PortfolioDAO portfolioDAO) {
		super();
		this.userDAO = userDAO;
		this.tradeDAO = tradeDAO;
		this.orderDAO = orderDAO;
		this.portfolioDAO = portfolioDAO;
	}

	@PostConstruct
	public void init() {
		loadUsers();
		loadTrades();	
		loadPortfolios();
		loadOrders();
	}
	
	private void loadUsers() {
		final List<User> userList = userDAO.getUserList();
		for(User e : this.userList) {
			userList.add(e);
		}
		logger.info("loaded Users..... "+userList.size());
	}
	
	private void loadTrades() {
		List<Trade> tradeList = tradeDAO.getTradeList();
		for(Trade e : tradeList) {
			trades.add(e);			
		}
		logger.info("Trades loaded..... "+trades.size());
	}
	
	private void loadOrders() {
		List<Order> list = orderDAO.getOrderList();		
		for(Order e : list) {
			orderList.add(e);
		}
		logger.info("Orders loaded..... "+orderList.size());
	}
	
	private void loadPortfolios() {
		List<TradePortfolio> list = portfolioDAO.getPortfolioList();		
		for(TradePortfolio e : list) {
			portfolios.add(e);
		}
		logger.info("portfolios loaded..... "+portfolios.size());
	}
	
	@Override
	public String getUserRole(String userName) {
		return null;
	}

	@Override
	public boolean isValidUser(String userName, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
