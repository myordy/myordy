package jrange.myordy.dao;

import java.util.List;

import org.hibernate.Session;

import jrange.myordy.entity.CustomerNumberSequence;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.ShopImage;

public interface ShopDAO {
    public Shop save(Shop shop);
	public Shop get(final Integer id);
    public List<Shop> list();
    public List<Shop> listLite(Language language);
	public Shop getLite(final Integer id);
	public Shop getLiteNoMenu(final Integer id);
	public CustomerNumberSequence getCustomerNumberSequence(final Integer shopId);
	public Session getCurrentSessionTemp();
	public Integer getShopId(final String serverName);
	public ShopImage getShopImage(final Integer shopId, final String imageCode);
	public ShopImage saveShopImage(final ShopImage shopImage);
}
