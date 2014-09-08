package com.fintek.ets.service;


public interface CachedService {
	
	String getUserRole(String userName);
	boolean isValidUser(String userName, String password);

}
