package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Customer;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.list.CustomerSearchRequest;
import jrange.myordy.entity.list.PaginatedResponse;
import jrange.myordy.v1.shop.restaurant.ShopCustomerRestaurant;

public interface CustomerDAO {
    public void delete(final Integer id);
    public Customer save(Customer customer);
	public Customer get(final Integer id);
	public Customer getNewOrdyCustomer(final Ordy ordy);
    public PaginatedResponse<Customer> list(final CustomerSearchRequest criteria);
    public List<ShopCustomerRestaurant> listCustomerV1(Long shopId);
	public Customer getByUserId(final Integer shopId, final Integer userId);
}
