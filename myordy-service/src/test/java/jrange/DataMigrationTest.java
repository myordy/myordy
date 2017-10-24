package jrange;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;

import jrange.myordy.dao.CustomerDAO;
import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.dao.SuburbDAO;
import jrange.myordy.dao.UserDAO;
import jrange.myordy.entity.Customer;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.Suburb;
import jrange.myordy.entity.TheUser;
import jrange.myordy.entity.TheUserAddress;
import jrange.myordy.entity.TheUserContact;
import jrange.myordy.entity.TheUserPhone;
import jrange.myordy.entity.TheUserPhoneType;
import jrange.myordy.v1.shop.restaurant.JBOptSUser;
import jrange.myordy.v1.shop.restaurant.OrderDetailsVO;
import jrange.myordy.v1.shop.restaurant.ShopCustomerRestaurant;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class DataMigrationTest {

//	@org.junit.Test
	public void xxx() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
        OrdyDAO ordyDAO = context.getBean(OrdyDAO.class);
		OrderDetailsVO v1 = ordyDAO.getV1(new Long(51090));
//		System.out.println(v1.getShopId());

		final InputStream is = new ByteArrayInputStream(v1.getOrderDetailsHtml());
        try {
            final StringBuilder out = new StringBuilder();
            final byte[] b = new byte[4096];
            int n = -1;
            while ((n = is.read(b)) != -1) {
                out.append(new String(b, 0, n));
            }
            System.out.println(out.toString());
        } finally {
            is.close();
        }

	}

	@org.junit.Test
	public void userImportTest() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
		UserDAO userDAO = context.getBean(UserDAO.class);
		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);
		Integer languageId = 1;
//		Integer shopIdV1 = 55;
		Integer shopIdV1 = 1084;
		Integer shopId = 1;

		List<ShopCustomerRestaurant> customerV1 = customerDAO.listCustomerV1(new Long(shopIdV1));
		for (ShopCustomerRestaurant cust : customerV1) {
			TheUser theUser = null;
			if (null != cust.getUserId()) {
				JBOptSUser v1 = userDAO.getV1(cust.getUserId());
				//System.out.println(v1.getEmail() + " : " + v1.getPasswordHash());
				theUser = userDAO.get(v1.getEmail());
				if (null == theUser) {
					theUser = new TheUser();
					theUser.setImportRef("V1:" + v1.getId().intValue());
					theUser.setUserId(v1.getId().intValue());
					theUser.setPasswordHash(v1.getPasswordHash());
					theUser.setContact(new TheUserContact());
					theUser.getContact().setName(v1.getName());
					theUser.getContact().setEmail(v1.getEmail());
					theUser.getContact().setLanguageId(1);
					theUser.getContact().setStatus(EntityStatus.ACTIVE);
					theUser.setStatus(EntityStatus.ACTIVE);
					theUser = userDAO.save(theUser);
				}
			}
			Customer c = makeCustomer(cust, suburbDAO, languageId, shopId);
			c.setTheUser(theUser);
			customerDAO.save(c);
		}

	}
	private Customer makeCustomer(ShopCustomerRestaurant cust, SuburbDAO suburbDAO, Integer languageId, Integer shopId) {
		final Customer result = new Customer();
		result.setContact(new TheUserContact());

		if (StringUtils.isNoneEmpty(cust.getName())) {
			result.getContact().setName(cust.getName().trim());
		} else if (StringUtils.isNoneEmpty(cust.getPhoneCSV())) {
			result.getContact().setName(cust.getPhoneCSV().trim().replaceAll(",", " "));
		} else {
			result.getContact().setName("UNKNOWN");
		}
		result.setCustomerNumber(cust.getCustomerNumber());

		importAddress(cust, result, suburbDAO);

		if (StringUtils.isNoneEmpty(cust.getPhoneCSV())) {
			HashSet<TheUserPhone> phones = new HashSet<TheUserPhone>();
			String[] phoneNumbers = cust.getPhoneCSV().trim().split(",");
			for (int i = 0; i < phoneNumbers.length; i++) {
				TheUserPhone p = new TheUserPhone();
				p.setPhoneNumber(phoneNumbers[i]);
				p.setStatus(EntityStatus.ACTIVE);
				phoneNumbers[i] = phoneNumbers[i].trim();
				if (phoneNumbers[i].startsWith("4") || phoneNumbers[i].startsWith("04")) {
					p.setPhoneType(TheUserPhoneType.MOBILE);
				} else {
					p.setPhoneType(TheUserPhoneType.HOME);
				}
				phones.add(p);
			}
			result.getContact().setPhoneList(phones);
		}

		result.getContact().setStatus(EntityStatus.ACTIVE);
		result.getContact().setLanguageId(languageId);
		result.setStatus(EntityStatus.ACTIVE);
		result.setShopId(shopId);
		result.setNote(cust.getNote());
		return result;
	}

	private static void importAddress(ShopCustomerRestaurant custV1, Customer customer, SuburbDAO suburbDAO) {
		StringBuilder sb = new StringBuilder();
		if (null != custV1.getAddress()) {
			sb.append(custV1.getAddress().trim());
		}
		if (StringUtils.isNoneEmpty(custV1.getSuburbName())) {
			sb.append(" ");
			sb.append(custV1.getSuburbName().trim());
		}
		if (StringUtils.isNoneEmpty(custV1.getPostcode())) {
			sb.append(" ");
			sb.append(custV1.getPostcode().trim());
		}

		if (StringUtils.isNoneEmpty(custV1.getSuburbName())) {
			if (StringUtils.isNoneEmpty(custV1.getPostcode())) {
				Suburb suburb = suburbDAO.get("AU" + "-" + custV1.getPostcode().trim() + "-" + custV1.getSuburbName().trim().toUpperCase().replaceAll("[^A-Za-z0-9]", ""));
				if (null == suburb) {
					appendNote(customer, "Incomplete Address [suburb not found]: " + sb.toString());
				} else {
					TheUserAddress a = new TheUserAddress();
					a.setAddress(sb.toString());
					a.setSuburb(suburb);
					a.setSuburbId(suburb.getSuburbId());
					if (null != custV1.getGeocodeLat()) {
						a.setGeocodeLatitude("" + custV1.getGeocodeLat().floatValue());
					}
					if (null != custV1.getGeocodeLng()) {
						a.setGeocodeLongitude("" + custV1.getGeocodeLng().floatValue());
					}
					if (StringUtils.isNoneEmpty(custV1.getMapRef())) {
						a.setMapRef(custV1.getMapRef().trim());
					}
					
					a.setStatus(EntityStatus.ACTIVE);
					if (null == customer.getContact().getAddressList()) {
						customer.getContact().setAddressList(new HashSet<TheUserAddress>());
					}
					customer.getContact().getAddressList().add(a);
				}


			} else {
				appendNote(customer, "Incomplete Address: " + sb.toString());
			}
		} else {
			appendNote(customer, "Incomplete Address: " + sb.toString());
		}
	}

	private static void appendNote(Customer customer, String noteToAppend) {
		final StringBuilder note = new StringBuilder();
		if (null != customer.getNote()) {
			note.append(customer.getNote());
			note.append("\r\n");
		}
		note.append(noteToAppend);
		customer.setNote(note.toString());
	}

//	@org.junit.Test
	public void customerImportTestX() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
		UserDAO userDAO = context.getBean(UserDAO.class);

		List<ShopCustomerRestaurant> customerV1 = customerDAO.listCustomerV1(new Long(55));
		for (ShopCustomerRestaurant cust : customerV1) {
			if (null != cust.getUserId()) {
				JBOptSUser v1 = userDAO.getV1(cust.getUserId());
				System.out.println(v1.getEmail() + " : " + v1.getPasswordHash());
				//System.out.println(cust.getName() + " : " + cust.getAddress() + " : " + cust.getPhoneCSV());
			}
		}
	}

//	@org.junit.Test
	public void customerImportTestXX() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
		List<ShopCustomerRestaurant> customerV1 = customerDAO.listCustomerV1(new Long(55));
		HashSet<String> x = new HashSet<String>();
		for (ShopCustomerRestaurant cust : customerV1) {
//			System.out.println(cust.getName());
			if (ShopCustomerRestaurant.STATUS_ACTIVE == cust.getStatus().byteValue()) {
				if (StringUtils.isNoneEmpty(cust.getAddress())) {
					if (StringUtils.isNoneEmpty(cust.getSuburbName())) {
						if (StringUtils.isNoneEmpty(cust.getPostcode())) {
							x.add("AU" + "-" + cust.getPostcode().trim() + "-" + cust.getSuburbName().trim().toUpperCase().replaceAll("[^A-Za-z0-9]", ""));
						} else {
//							x.add("AU" + "-NOTFOUND-" + cust.getSuburbName().trim().toUpperCase().replaceAll("[^A-Za-z0-9]", ""));
//							System.out.println(">>>>>>>>>>> : address without postcode ... cust number: " + cust.getCustomerNumber());
//							System.out.println(cust);
							//System.out.println(">>> NOT FOUND : " + cust.getSuburbName());
							// TODO Find by suburb name
						}
					} else {
						System.out.println(">>>>>>>>>>> : address without suburb name ... cust number: " + cust.getCustomerNumber());
						System.out.println(cust);
					}
				}
			}
		}
		for (String string : x) {
			System.out.println(string);
		}
	}

	// /home/myordy/myordy/prod-01/mysql-5.1.46-linux-x86_64-glibc23/bin/mysqldump -t --socket=/tmp/mysql-23306.sock --user=root jbopts 5_shop_customer_restaurant --where="shop_id_fk = 55"
	// Query: select id,name,phone_csv,address,suburb_name,postcode,suburb_code,geocode_lat,geocode_lng,map_ref,shop_customer_number,last_order_date,order_count,note,user_id_fk,shop_id_fk,status from 5_shop_customer_restaurant where shop_id_fk = 55 INTO OUTFILE '//home/myordy/sylvesters-customers.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n';
	// Header: id,name,phone_csv,address,suburb_name,postcode,suburb_code,geocode_lat,geocode_lng,map_ref,shop_customer_number,last_order_date,order_count,note,user_id_fk,shop_id_fk,status
//	@org.junit.Test
	public void customerImportTest() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ShopDAO shopDAO = context.getBean(ShopDAO.class);
		CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
		Shop shop = shopDAO.get(1);
		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
		Language language = languageDAO.get(1);
		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);

        Reader in = new FileReader("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/sylvesters/myordy-v2/sylvesters-customers-3-11-2015.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"id","name","phone_csv","address","suburb_name","postcode","suburb_code","geocode_lat","geocode_lng","map_ref","shop_customer_number","last_order_date","order_count","note","user_id_fk","shop_id_fk","status"
		).parse(in);
		HashSet<String> x = new HashSet<String>();
		for (CSVRecord record : records) {
			if (StringUtils.isNoneEmpty(record.get("suburb_name"))) {
//				suburbDAO.get("");
				if (StringUtils.isNoneEmpty(record.get("postcode"))) {
					x.add("AU" + "-" + record.get("postcode").trim() + "-" + record.get("suburb_name").trim().toUpperCase().replaceAll("[^A-Za-z0-9]", ""));
				} else {
					// TODO Find by suburb name
				}
			}
/*
			System.out.println(record.get("name") + " " + record.get("phone_csv") + " " + record.get("address") + " " + record.get("suburb_name") + " " + record.get("postcode"));

			Customer c = new Customer();
			c.setContact(new TheUserContact());
//			if (StringUtils.isNoneEmpty(record.get("id"))) {
//				int customerId = Integer.parseInt(record.get("id").trim());
//				if (customerId < 0) {
//					c.setCustomerId(customerId);
//				}
//			}
			if (StringUtils.isNoneEmpty(record.get("name"))) {
				c.getContact().setName(record.get("name").trim());
			} else if (StringUtils.isNoneEmpty(record.get("phone_csv"))) {
				c.getContact().setName(record.get("phone_csv").trim().replaceAll(",", " "));
			} else {
				c.getContact().setName("UNKNOWN");
			}
			if (StringUtils.isNoneEmpty(record.get("shop_customer_number"))) {
				c.setCustomerNumber(new Integer(record.get("shop_customer_number").trim()));
			}

			if (StringUtils.isNoneEmpty(record.get("address"))) {
				HashSet<TheUserAddress> addresses = new HashSet<TheUserAddress>();
				TheUserAddress a = new TheUserAddress();
				StringBuilder sb = new StringBuilder();
				sb.append(record.get("address").trim());
				if (StringUtils.isNoneEmpty(record.get("suburb_name"))) {
					sb.append(" ");
					sb.append(record.get("suburb_name").trim());
				}
				if (StringUtils.isNoneEmpty(record.get("postcode"))) {
					sb.append(" ");
					sb.append(record.get("postcode").trim());
				}
				if (StringUtils.isNoneEmpty(record.get("suburb_name")) && StringUtils.isNoneEmpty(record.get("postcode"))) {
//					"AU" + "-" + record.get("postcode").trim() + "-" + record.get("suburb_name").trim().toUpperCase().replaceAll("[^A-Za-z0-9]", "")
					//a.setSuburb(suburb); // TODO Find suburb by name
				}
				a.setAddress(sb.toString());
				if (StringUtils.isNoneEmpty(record.get("geocode_lat"))) {
					a.setGeocodeLatitude(record.get("geocode_lat").trim());
				}
				if (StringUtils.isNoneEmpty(record.get("geocode_lng"))) {
					a.setGeocodeLongitude(record.get("geocode_lng").trim());
				}
				if (StringUtils.isNoneEmpty(record.get("map_ref"))) {
					a.setMapRef(record.get("map_ref").trim());
				}
				
				addresses.add(a);
				a.setStatus(EntityStatus.ACTIVE);
				c.getContact().setAddressList(addresses);
			}

			if (StringUtils.isNoneEmpty(record.get("phone_csv"))) {
				HashSet<TheUserPhone> phones = new HashSet<TheUserPhone>();
				String[] phoneNumbers = record.get("phone_csv").trim().split(",");
				for (int i = 0; i < phoneNumbers.length; i++) {
					TheUserPhone p = new TheUserPhone();
					p.setPhoneNumber(phoneNumbers[i]);
					p.setStatus(EntityStatus.ACTIVE);
					phoneNumbers[i] = phoneNumbers[i].trim();
					if (phoneNumbers[i].startsWith("4") || phoneNumbers[i].startsWith("04")) {
						p.setPhoneType(TheUserPhoneType.MOBILE);
					} else {
						p.setPhoneType(TheUserPhoneType.HOME);
					}
					phones.add(p);
				}
				c.getContact().setPhoneList(phones);


//				TheUserPhone p = new TheUserPhone();
//				p.setPhoneNumber("0422272591");
//				p.setPhoneType(TheUserPhoneType.MOBILE);
//				p.setStatus(EntityStatus.ACTIVE);
//				phones.add(p);
//	
//				p = new TheUserPhone();
//				p.setPhoneNumber("034567893");
//				p.setPhoneType(TheUserPhoneType.HOME);
//				p.setStatus(EntityStatus.ACTIVE);
//				phones.add(p);
//
//				c.getContact().setPhoneList(phones);
			}

//			HashSet<TheUserAddress> addresses = new HashSet<TheUserAddress>();

//			TheUserAddress a = new TheUserAddress();
//			a.setAddress("469 Paynters Road Earlston VIC 3669");
//			addresses.add(a);
//			a.setStatus(EntityStatus.ACTIVE);

//			a = new TheUserAddress();
//			a.setAddress("3 Bedford Street Reservoir VIC 3073");
//			addresses.add(a);
//			a.setStatus(EntityStatus.ACTIVE);

//			c.getContact().setAddressList(addresses);

//			HashSet<TheUserPhone> phones = new HashSet<TheUserPhone>();

//			TheUserPhone p = new TheUserPhone();
//			p.setPhoneNumber("0422272591");
//			p.setPhoneType(TheUserPhoneType.MOBILE);
//			p.setStatus(EntityStatus.ACTIVE);
//			phones.add(p);
//
//			p = new TheUserPhone();
//			p.setPhoneNumber("034567893");
//			p.setPhoneType(TheUserPhoneType.HOME);
//			p.setStatus(EntityStatus.ACTIVE);
//			phones.add(p);

//			c.getContact().setPhoneList(phones);

			c.getContact().setStatus(EntityStatus.ACTIVE);
			c.getContact().setLanguageId(language.getLanguageId());
			c.setStatus(EntityStatus.ACTIVE);
			c.setShopId(shop.getShopId());
			customerDAO.save(c);
*/
		}
		for (String string : x) {
			System.out.println(string);
		}
	}

/*

mysql> describe 5_shop_customer_restaurant;
+----------------------+---------------+------+-----+---------+----------------+
| Field                | Type          | Null | Key | Default | Extra          |
+----------------------+---------------+------+-----+---------+----------------+
| id                   | bigint(20)    | NO   | PRI | NULL    | auto_increment |
| name                 | varchar(64)   | YES  |     | NULL    |                |
| phone_csv            | varchar(256)  | YES  |     | NULL    |                |
| address              | varchar(256)  | YES  |     | NULL    |                |
| suburb_name          | varchar(128)  | YES  |     | NULL    |                |
| postcode             | varchar(64)   | YES  |     | NULL    |                |
| suburb_code          | varchar(64)   | YES  |     | NULL    |                |
| geocode_lat          | float(10,6)   | YES  |     | NULL    |                |
| geocode_lng          | float(10,6)   | YES  |     | NULL    |                |
| map_ref              | varchar(64)   | YES  |     | NULL    |                |
| shop_customer_number | int(11)       | YES  |     | NULL    |                |
| last_order_date      | datetime      | YES  |     | NULL    |                |
| order_count          | bigint(20)    | NO   |     | NULL    |                |
| note                 | varchar(1024) | YES  |     | NULL    |                |
| user_id_fk           | bigint(20)    | YES  |     | NULL    |                |
| shop_id_fk           | bigint(20)    | NO   |     | NULL    |                |
| status               | smallint(6)   | NO   |     | NULL    |                |
+----------------------+---------------+------+-----+---------+----------------+


*/

}
