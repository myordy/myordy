package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Product;

public interface ProductDAO {
    public Product save(Product product);
	public Product get(final Integer id);
    public List<Product> list();
}
