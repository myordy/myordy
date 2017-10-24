package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Category;

public interface CategoryDAO {
    public Category save(Category category);
	public Category get(final Integer id);
    public List<Category> list();
}
