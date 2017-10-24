package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Business;

public interface BusinessDAO {
    public Business save(Business business);
	public Business get(final Integer id);
    public List<Business> list();
}
