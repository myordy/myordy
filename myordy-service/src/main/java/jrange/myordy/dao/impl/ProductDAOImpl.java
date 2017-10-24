package jrange.myordy.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.ProductDAO;
import jrange.myordy.entity.Product;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class ProductDAOImpl extends HibernateDAOImpl implements ProductDAO {

	@Override
	public Product save(Product productArg) {
		Product product = productArg;
		if (productArg.getProductId() != null) {
			product = get(productArg.getProductId());
		}
		getCurrentSession().saveOrUpdate(product);
		return product;
	}

	@Override
	public Product get(final Integer id) {
		final Product result = (Product) getCurrentSession().get(Product.class, id);
		Hibernate.initialize(result.getBusiness());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> list() {
		return getCurrentSession().createQuery("from product").list();
	}

}
