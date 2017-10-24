package jrange.myordy.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateDAOImpl {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SessionFactory sessionFactoryV1;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected Session getCurrentSessionV1() {
		return sessionFactoryV1.getCurrentSession();
	}

	public void printQueryResult(final String query) {
		System.out.println(">>>>> : " + query);
		SQLQuery sQLQuery = getCurrentSession().createSQLQuery(query);
		List<Object> list = sQLQuery.list();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] cols = (Object[])iterator.next();
			for (Object c : cols) {
				System.out.println(c);
			}
		}
	}

}
