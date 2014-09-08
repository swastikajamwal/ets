package com.fintek.ets.db.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.fintek.ets.db.dao.UserDAO;
import com.fintek.ets.db.model.User;


/**
 * 
 * @author sjamwal
 *
 */
public class UserDAOImpl implements UserDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<User> getUserList() {
		Transaction trans = null;
		Session session = sessionFactory.openSession();
		try {
			trans = session.beginTransaction();
			return session.createQuery("from User").list();
		}catch(Throwable t){
			System.err.println("Exception in getUserList()....");
			t.printStackTrace();
			trans.rollback();						
		}finally{
			if(session.isConnected()) {
				trans.commit();
			}
		}
		return null;
	}

	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(user);
	}

}
