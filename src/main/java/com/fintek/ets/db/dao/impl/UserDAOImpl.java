package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fintek.ets.db.dao.UserDAO;
import com.fintek.ets.db.model.User;

public class UserDAOImpl implements UserDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<User> getUserList() {
		return this.sessionFactory.getCurrentSession().createQuery("from ets.User user").list();
	}

	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(user);
	}

}
