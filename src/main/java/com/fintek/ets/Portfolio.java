package com.fintek.ets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author sjamwal
 *
 */
public class Portfolio {

	private final Map<String,PortfolioPosition> positionLookup = new LinkedHashMap<String,PortfolioPosition>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();


	public List<PortfolioPosition> getPositions() {
		lock.readLock().lock();
		try {
			return new ArrayList<PortfolioPosition>(positionLookup.values());
		}finally {
			lock.readLock().unlock();
		}
	}

	public void addPosition(PortfolioPosition position) {
		lock.writeLock().lock();
		try {
			this.positionLookup.put(position.getTicker(), position);
		}finally {
			lock.writeLock().unlock();
		}
	}

	public PortfolioPosition getPortfolioPosition(String ticker) {
		lock.readLock().lock();
		try {
			return this.positionLookup.get(ticker);
		}finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * @return the updated position or null
	 */
	public PortfolioPosition buy(String ticker, int sharesToBuy) {
		PortfolioPosition position = getPortfolioPosition(ticker);
		if ((position == null) || (sharesToBuy < 1)) {
			return null;
		}
		lock.writeLock().lock();
		try {
			position = new PortfolioPosition(position, sharesToBuy);
			addPosition(position);
		}finally {
			lock.writeLock().unlock();
		}
		return position;
	}

	/**
	 * @return the updated position or null
	 */
	public PortfolioPosition sell(String ticker, int sharesToSell) {
		PortfolioPosition position = getPortfolioPosition(ticker);
		if ((position == null) || (sharesToSell < 1) || (position.getShares() < sharesToSell)) {
			return null;
		}
		lock.writeLock().lock();
		try {
			position = new PortfolioPosition(position, -sharesToSell);
			this.positionLookup.put(ticker, position);
		}finally {
			lock.writeLock().unlock();
		}
		return position;
	}

}
