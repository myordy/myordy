package jrange.myordy.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.CategoryDAO;
import jrange.myordy.entity.Category;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class CategoryDAOImpl extends HibernateDAOImpl implements CategoryDAO {

	@Override
	public Category save(Category categoryArg) {
		Category category = categoryArg;
		if (categoryArg.getCategoryId() != null) {
			category = get(categoryArg.getCategoryId());
		}
		getCurrentSession().saveOrUpdate(category);
		return category;
	}

	@Override
	public Category get(final Integer id) {
		final Category result = (Category) getCurrentSession().get(Category.class, id);
		Hibernate.initialize(result.getBusiness());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> list() {
		return getCurrentSession().createQuery("from category").list();
	}

}
