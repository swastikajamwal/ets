package com.fintek.ets.service;

import com.fintek.ets.Portfolio;


/**
 * A portfolio service interface.
 * @author sjamwal
 *
 */
public interface PortfolioService {

	Portfolio findPortfolio(String username);

}
