package jrange.myordy.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.BusinessDAO;
import jrange.myordy.entity.Business;

import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class BusinessDAOImpl extends HibernateDAOImpl implements BusinessDAO {

	@Override
	public Business save(Business businessArg) {
		Business business = businessArg;
		getCurrentSession().saveOrUpdate(business);
		return business;
	}

	@Override
	public Business get(final Integer id) {
		final Business result = (Business) getCurrentSession().get(Business.class, id);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Business> list() {
		return getCurrentSession().createQuery("from business").list();
	}

}
