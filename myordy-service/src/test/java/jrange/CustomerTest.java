package jrange;

import java.util.ArrayList;
import java.util.HashSet;

import jrange.myordy.dao.CustomerDAO;
import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.Customer;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.TheUserAddress;
import jrange.myordy.entity.TheUserContact;
import jrange.myordy.entity.TheUserPhone;
import jrange.myordy.entity.TheUserPhoneType;
import jrange.myordy.entity.list.CustomerSearchRequest;
import jrange.myordy.entity.list.PaginatedResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CustomerTest {

	@org.junit.Test
	public void testY() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ShopDAO shopDAO = context.getBean(ShopDAO.class);
		CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
		OrdyDAO ordyDAO = context.getBean(OrdyDAO.class);
		Ordy customerLastOrdy = ordyDAO.getCustomerLastOrdy(3841, 1);
		System.out.println(customerLastOrdy);
		
		/*

		Customer c = new Customer();
		c.setContact(new TheUserContact());
		c.getContact().setName("Jiten Baudh");
		HashSet<TheUserAddress> addresses = new HashSet<TheUserAddress>();

		TheUserAddress a = new TheUserAddress();
		a.setAddress("469 Paynters Road Earlston VIC 3669");
		addresses.add(a);
		a.setStatus(EntityStatus.ACTIVE);

		a = new TheUserAddress();
		a.setAddress("3 Bedford Street Reservoir VIC 3073");
		addresses.add(a);
		a.setStatus(EntityStatus.ACTIVE);

		c.getContact().setAddressList(addresses);

		HashSet<TheUserPhone> phones = new HashSet<TheUserPhone>();

		TheUserPhone p = new TheUserPhone();
		p.setPhoneNumber("0422272591");
		p.setPhoneType(TheUserPhoneType.MOBILE);
		p.setStatus(EntityStatus.ACTIVE);
		phones.add(p);

		p = new TheUserPhone();
		p.setPhoneNumber("034567893");
		p.setPhoneType(TheUserPhoneType.HOME);
		p.setStatus(EntityStatus.ACTIVE);
		phones.add(p);

		c.getContact().setPhoneList(phones);

		c.getContact().setStatus(EntityStatus.ACTIVE);
		c.setStatus(EntityStatus.ACTIVE);
		c.setShop(shop);
		customerDAO.save(c);
*/
/*
		for (int i = 0; i < 100; i++) {
			Customer c = new Customer();
			c.setContact(new TheUserContact());
			c.getContact().setName("Test Customer " + System.currentTimeMillis());
			c.getContact().setStatus(EntityStatus.ACTIVE);
			c.setStatus(EntityStatus.ACTIVE);
			c.setShop(shop);
			customerDAO.save(c);
			Thread.sleep(500);
		}
*/
/*
		CustomerSearchRequest r = new CustomerSearchRequest();
		r.setShopId(1);
		r.setCurrentPageNumber(1);
		r.setMaxResults(10);
//		r.setAddress("paynter");
		r.setName("baudh");
//		r.setPhone("2591");
//		r.setPhone(phone);
//		r.setFirstResult(0);
		PaginatedResponse<Customer> response = customerDAO.list(r);
		System.out.println(response.getTotalItems());
		for (Customer cust : response.getItems()) {
			System.out.println(cust.getCustomerId() + " : " + cust.getContact().getTheUserContactId() + " : " + cust.getContact().getName());
//			System.out.println(cust.getShop());
			
			for (TheUserAddress adr : cust.getContact().getAddressList()) {
				System.out.println("\t" + adr.getTheUserAddressId() + " " + adr.getAddress() + " " + adr.getStatus());
			}
			for (TheUserPhone phn : cust.getContact().getPhoneList()) {
				System.out.println("\t" + phn.getPhoneNumber());
			}
		}
*/
	}

}
