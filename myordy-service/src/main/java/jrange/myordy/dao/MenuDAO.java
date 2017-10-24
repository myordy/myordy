package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Menu;
import jrange.myordy.entity.MenuDiscountCoupon;
import jrange.myordy.entity.Suburb;

public interface MenuDAO {
    public Menu save(Menu menu);
	public Menu get(final Integer id);
	public Menu getLite(final Integer id);
	public Menu getByCode(final Integer businessId, final String code);
    public List<Menu> list();
    public List<Suburb> getMenuServiceSuburbList(final Integer menuId, final Integer languageId);
	public List<Suburb> getBusinessMenuServiceSuburbList(final Integer businessId, final Integer languageId);
    public MenuDiscountCoupon saveMenuDiscountCoupon(final MenuDiscountCoupon menuDiscountCoupon);
}
