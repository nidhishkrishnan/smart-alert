package com.codechef.smartalert.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void persist(final Object entity) {
		getSession().persist(entity);
	}

	public int delete(final Object entity) {
		try {
			getSession().delete(entity);
			return 1;
		} catch (Exception exception) {
			return 0;
		}
	}

	public int save(final Object entity) {
		return (Integer) getSession().save(entity);
	}
	
	public void saveOrUpdate(final Object entity) {
		getSession().saveOrUpdate(entity);
	}
}
