package com.fintek.ets.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 
 * @author sjamwal
 *
 */
public class FXQuoteGenerator {
	
	private final Random random = new Random();
	private final Map<String, FXQuoteFeeder> prices = new LinkedHashMap<>(30);
	
	public FXQuoteGenerator() {
		this.prices.put("USDCHF", new FXQuoteFeeder("USDCHF","0.91137","0.00009", 5, "####.#####"));
		this.prices.put("GBPUSD", new FXQuoteFeeder("GBPUSD","1.65902","0.00011", 5, "####.#####"));
		this.prices.put("EURUSD", new FXQuoteFeeder("EURUSD","1.32757","0.00007", 5, "####.#####"));
		this.prices.put("USDJPY", new FXQuoteFeeder("USDJPY","103.667","0.008", 3, "####.###"));
		this.prices.put("USDCAD", new FXQuoteFeeder("USDCAD","1.09513","0.00014", 5, "####.#####"));
		this.prices.put("AUDUSD", new FXQuoteFeeder("AUDUSD","0.93219","0.00013", 5, "####.#####"));
		this.prices.put("EURGBP", new FXQuoteFeeder("EURGBP","0.80017","0.00012", 5, "####.#####"));
		this.prices.put("EURAUD", new FXQuoteFeeder("EURAUD","1.42428","0.00022", 5, "####.#####"));
		this.prices.put("EURCHF", new FXQuoteFeeder("EURCHF","1.20984","0.00013", 5, "####.#####"));
		this.prices.put("EURJPY", new FXQuoteFeeder("EURJPY","137.631","0.016", 3, "####.###"));
		this.prices.put("GBPCHF", new FXQuoteFeeder("GBPCHF","1.51143","0.00020", 5, "####.#####"));
		this.prices.put("CADJPY", new FXQuoteFeeder("CADJPY","94.645","0.011", 3, "####.###"));
		this.prices.put("GBPJPY", new FXQuoteFeeder("GBPJPY","171.943","0.015", 3, "####.###"));
		this.prices.put("AUDNZD", new FXQuoteFeeder("AUDNZD","1.10741","0.00022", 5, "####.#####"));
		this.prices.put("AUDCAD", new FXQuoteFeeder("AUDCAD","1.02075","0.00018", 5, "####.#####"));
		this.prices.put("AUDCHF", new FXQuoteFeeder("AUDCHF","0.84921","0.00014", 5, "####.#####"));
		this.prices.put("AUDJPY", new FXQuoteFeeder("AUDJPY","96.623","0.018", 3, "####.###"));
		this.prices.put("CHFJPY", new FXQuoteFeeder("CHFJPY","113.778","0.017", 3, "####.###"));
		this.prices.put("EURNZD", new FXQuoteFeeder("EURNZD","1.57731","0.00027", 5, "####.#####"));
		this.prices.put("EURCAD", new FXQuoteFeeder("EURCAD","1.45407","0.00017", 5, "####.#####"));
		this.prices.put("CADCHF", new FXQuoteFeeder("CADCHF","0.83175","0.00021", 5, "####.#####"));
		this.prices.put("NZDJPY", new FXQuoteFeeder("NZDJPY","87.227","0.021", 3, "####.###"));
		this.prices.put("NZDUSD", new FXQuoteFeeder("NZDUSD","0.84156","0.00015", 5, "####.#####"));
		
	}
	
	public Set<FXQuote> generateQuotes() {
		Set<FXQuote> quotes = new LinkedHashSet<>();
		for (String ticker : this.prices.keySet()) {
			FXQuote quote = getQuote(ticker);
			quotes.add(quote);
		}
		return quotes;
	}

	private FXQuote getQuote(String symbol) {
		BigDecimal seedPrice = new BigDecimal(this.prices.get(symbol).getPrice());
		double range = new BigDecimal(this.prices.get(symbol).getSpread()).doubleValue();
		BigDecimal priceChange = new BigDecimal(String.valueOf(this.random.nextDouble() * range)).divide(new BigDecimal(2.0));
		DecimalFormat myFormatter = this.prices.get(symbol).getFormatter();
		BigDecimal change = new BigDecimal(myFormatter.format(priceChange));
		return new FXQuote(symbol, seedPrice.subtract(change), seedPrice.add(change));
	}
	
	private static class FXQuoteFeeder {
		final private String symbol;
		final private String price;
		final private String spread;
		final private MathContext mathContext;
		final private DecimalFormat formatter;
		
		public FXQuoteFeeder(String symbol, String price, String spread, int precision, String format) {
			this.symbol = symbol;
			this.price = price;
			this.spread = spread;
			this.mathContext = new MathContext(precision);
			formatter = new DecimalFormat(format);
		}

		public String getSymbol() {
			return symbol;
		}

		public String getPrice() {
			return price;
		}

		public String getSpread() {
			return spread;
		}

		public MathContext getMathContext() {
			return mathContext;
		}

		public DecimalFormat getFormatter() {
			return formatter;
		}
		
	}
	
	public static void main(String[] args) {
		FXQuoteGenerator gen = new FXQuoteGenerator();
		for(int x=0; x<5; x++) {
			for(FXQuote quote : gen.generateQuotes()) {
				System.out.println("Symbol: "+quote.getSymbol()+", Bid: "+quote.getBid() + ", Ask:"+quote.getAsk());
			}
			System.out.println();
			
		}
	}

}
