package com.fintek.ets.db.dao;

import java.util.List;

import com.fintek.ets.db.model.User;

/**
 * The DAO interface for User table.
 * 
 * @author sjamwal
 *
 */
public interface UserDAO {
	
	public List<User> getUserList();
	
	public void saveUser(User user);

}
