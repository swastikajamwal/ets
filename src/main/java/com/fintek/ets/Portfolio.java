package com.fintek.ets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The portfolio class.
 * 
 * @author sjamwal
 *
 */
public class Portfolio {

	private final List<PortfolioPosition> positions = new LinkedList<PortfolioPosition>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();


	public List<PortfolioPosition> getPositions() {
		lock.readLock().lock();
		try {
			return Collections.unmodifiableList(positions);
		}finally {
			lock.readLock().unlock();
		}
	}

	public void addPosition(PortfolioPosition position) {
		lock.writeLock().lock();
		try {
			this.positions.add(position);
		}finally {
			lock.writeLock().unlock();
		}
	}

	public PortfolioPosition getPortfolioPosition(String id) {
		lock.readLock().lock();
		try {
			for(PortfolioPosition position : positions){
				if(position.getOrder().equals(id)){
					return position;					
				}
				
			}
		}finally {
			lock.readLock().unlock();
		}
		return null;
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
			this.positions.add(position);
		}finally {
			lock.writeLock().unlock();
		}
		return position;
	}

}
