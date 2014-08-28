package com.fintek.ets.connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.Username;

/**
 * 
 * @author sjamwal
 *
 */
public class QFJApplication implements Application {
	
	private ConcurrentMap<String, List<LogonListener>> logonListeners = new ConcurrentHashMap<String, List<LogonListener>>();

	@Override
	public void fromAdmin(Message msg, SessionID id) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		String msgType = getMsgType(msg);
		String type = "";
		if(MsgType.HEARTBEAT.compareTo(msgType) == 0)
		{
			type = "HEARTBEAT ";		    
		}
		System.out.println(type+"Message fromAdmin["+id+"] ->"+msg);
	}

	@Override
	public void fromApp(Message msg, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		String msgType = getMsgType(msg);
		if(MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH.compareTo(msgType) == 0)
		{
			System.out.println("MARKET_DATA_SNAPSHOT_FULL_REFRESH -> ");
			System.out.println(getMDSString(msg));
		}else {
			System.out.println("Message fromApp -> "+msg);
		}
		
	}

	private String getMDSString(Message msg) {
		StringBuilder mds = new StringBuilder();
		mds.append("------------------------------------------------------------------------\n");
		mds.append("|          |             Bid            |             Offer            |\n");
		mds.append("------------------------------------------------------------------------\n");
		mds.append("|   Curr   |     Price    |     Size    |     Price     |     Size     |\n");
		mds.append("------------------------------------------------------------------------\n");
		mds.append("|   ");
		mds.append("|   ");
		mds.append("----------------------------------/n");
		mds.append("----------------------------------/n");
		return mds.toString();
	}

	@Override
	public void onCreate(SessionID id) {
		System.out.println("Message onCreate -> "+id);
	}

	@Override
	public void onLogon(SessionID id) {
		System.out.println("Message onLogon -> "+id);
		List<LogonListener> listeners = logonListeners.get(id);
		if(listeners != null) {
			for(LogonListener listener : listeners) {
				listener.onLogon();				
			}
		}
	}

	@Override
	public void onLogout(SessionID id) {
		System.out.println("Message onLogout -> "+id);
	}

	@Override
	public void toAdmin(Message msg, SessionID arg1) {
		String msgType = getMsgType(msg);
		if(MsgType.LOGON.compareTo(msgType) == 0)
		{
		    msg.setString(Username.FIELD, "finfx_uat_9_q");
		    msg.setString(Password.FIELD, "k18FZHvAUk0gPZJ");
		}
		String type = "";
		if(MsgType.HEARTBEAT.compareTo(msgType) == 0)
		{
			type = "HEARTBEAT ";		    
		}
		System.out.println(type+"Message toAdmin -> "+msg);
	}

	@Override
	public void toApp(Message msg, SessionID arg1) throws DoNotSend {
		System.out.println("Message toApp -> "+msg);
	}
	
	private String getMsgType(Message msg) {
		String msgType = null;
		try {
			msgType = msg.getHeader().getString(MsgType.FIELD);
		} catch (FieldNotFound e) {
			e.printStackTrace();
		}
		return msgType;
		
	}

	public void registerLogon(String id, LogonListener listener) {
		List<LogonListener> list = new ArrayList<LogonListener>();
		list.add(listener);
		List<LogonListener> old = logonListeners.putIfAbsent(id, list);
		if(old != null) {
			synchronized (old) {
				old.add(listener);				
			}			
		}
	}

}
