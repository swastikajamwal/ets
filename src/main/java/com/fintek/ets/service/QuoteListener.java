package com.fintek.ets.service;

import java.util.Set;

public interface QuoteListener {
	
	void onQuote(Set<FXQuote> quotes);

}
