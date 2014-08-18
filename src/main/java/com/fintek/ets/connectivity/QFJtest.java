package com.fintek.ets.connectivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.AggregatedBook;
import quickfix.field.DeliverToCompID;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateType;
import quickfix.field.MarketDepth;
import quickfix.field.Product;
import quickfix.field.SenderSubID;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.fix42.MarketDataRequest;

public class QFJtest implements LogonListener {
	
	private static volatile boolean runQFJ = true; 
	private QFJApplication app;
	private static final String SESS = "FIX.4.4:Q082->PXMD";

	public static void main(String[] args) throws FileNotFoundException, ConfigError {
		QFJtest test = new QFJtest();
		test.start();
	}
	
	public void start() throws FileNotFoundException, ConfigError {
		org.apache.log4j.BasicConfigurator.configure();
		
		app = new QFJApplication();
		app.registerLogon(SESS, this);
		
		final SessionSettings settings = new SessionSettings(new FileInputStream("target/classes/qfj.cfg"));
		final MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		final LogFactory logFactory = new FileLogFactory(settings);
		final MessageFactory messageFactory = new DefaultMessageFactory();
		
		final Initiator initiator = new SocketInitiator(app, storeFactory, settings, logFactory, messageFactory);
		initiator.start();
		System.out.println("Initiator started.....");
		
		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runQFJ = false;
			}
			
		});
		t.start();
		
		while(runQFJ) {}
		
		System.err.println("SHUTTING DOWN.........");
		initiator.stop();		
	}

	@Override
	public void onLogon() {
		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					MarketDataRequest req = createNewMDR();
					try {
						System.out.println("Number of sessions->"+Session.numSessions());
						System.out.println("exist="+Session.doesSessionExist(new SessionID(SESS)));
						Session.sendToTarget(req, new SessionID(SESS));
					} catch (SessionNotFound e) {
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		t.start();		
	}
	
	private MarketDataRequest createNewMDR() {
		MarketDataRequest req = new MarketDataRequest();
		req.set(new MDReqID("MDR_1"));
		req.set(new SubscriptionRequestType('1'));
		req.set(new MarketDepth(1));
		req.set(new MDUpdateType(0));
		req.set(new AggregatedBook(true));
		MarketDataRequest.NoMDEntryTypes entryTypes = new MarketDataRequest.NoMDEntryTypes();
		MDEntryType bid = new MDEntryType('0');
		entryTypes.set(bid);
		req.addGroup(entryTypes);
		MDEntryType offer = new MDEntryType('1');
		entryTypes.set(offer);
		req.addGroup(entryTypes);
		MarketDataRequest.NoRelatedSym relatedSymbol = new MarketDataRequest.NoRelatedSym();
		relatedSymbol.set(new Symbol("GBP/USD"));
		relatedSymbol.setField(new Product(4));
		req.addGroup(relatedSymbol);
		MarketDataRequest.NoRelatedSym relatedSymbol2 = new MarketDataRequest.NoRelatedSym();
		relatedSymbol2.set(new Symbol("EUR/USD"));
		relatedSymbol2.setField(new Product(4));
		req.addGroup(relatedSymbol2);
		req.setField(new SenderSubID("PXMD100082"));
		req.setField(new DeliverToCompID("PXMD100082"));
		
		return req;		
	}

}
