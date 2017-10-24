package jrange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrange.myordy.dao.BusinessDAO;
import jrange.myordy.dao.CategoryDAO;
import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.MenuItemDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.dao.ProductDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.dao.SuburbDAO;
import jrange.myordy.dao.TagDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.Category;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.MenuOrdyConfig;
import jrange.myordy.entity.MenuOrdyServiceSuburb;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.OrdyItem;
import jrange.myordy.entity.Product;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.Suburb;
import jrange.myordy.entity.Tag;
import jrange.myordy.entity.TheUserAddress;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;


public class AppTest {
//	@org.junit.Test
	public void testYYYY() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ShopDAO shopDAO = context.getBean(ShopDAO.class);
		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
		Language language = languageDAO.get(1);
		Shop shop = shopDAO.get(2);
		
		Set<Message> messages = new HashSet<Message>();
		Message x = new Message();
		x.setCode("shopName2");
		x.setMessage("chuchu");
		messages.add(x);

        MessageDAO messageDAO = context.getBean(MessageDAO.class);
//		messageDAO.updateMessageByCode(shop, language, messages);
		
	}

//	@org.junit.Test
	public void testYYY() throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
//		ShopDAO shopDAO = context.getBean(ShopDAO.class);
//		MenuDAO menuDAO = context.getBean(MenuDAO.class);
//		MessageDAO messageDAO = context.getBean(MessageDAO.class);
//		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
//
//		Shop shop = shopDAO.get(2);
////		shop = shopDAO.save(shop);
//		Language language = languageDAO.get(1);
////		insertMessage(messageDAO, String.format("shopName%d", shop.getShopId()), "Priv Punjab", shop, language);
//
//		Menu menuDelivery = new Menu();
//		menuDelivery.setShop(shop);
//		menuDelivery = menuDAO.save(menuDelivery);
//		insertMessage(messageDAO, String.format("menu%dN", menuDelivery.getMenuId()), "Delivery", shop, language);
//
//		Menu menuPickup = new Menu();
//		menuPickup.setShop(shop);
//		menuPickup = menuDAO.save(menuPickup);
//		insertMessage(messageDAO, String.format("menu%dN", menuPickup.getMenuId()), "Pickup", shop, language);

	}
//	@org.junit.Test
	public void testYY() throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
//		ShopDAO shopDAO = context.getBean(ShopDAO.class);
//		MessageDAO messageDAO = context.getBean(MessageDAO.class);
//		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
//
//		Shop shop = new Shop();
//		shop.setStatus(EntityStatus.ACTIVE);
//		shop = shopDAO.save(shop);
//		Language language = languageDAO.get(1);
//		insertMessage(messageDAO, String.format("shopName%d", shop.getShopId()), "Priv Punjab", shop, language);
//
//		List<Shop> listLight = shopDAO.listLite(language);
//		final ObjectMapper om = new ObjectMapper();
//		System.out.println(om.writeValueAsString(listLight));

	}

//	@org.junit.Test
	public void testYXX() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		MenuItemDAO menuItemDAO = context.getBean(MenuItemDAO.class);
		menuItemDAO.list();
//		menuItemDAO.x();
	}

//	@org.junit.Test
	public void testYX() throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
//
//        ProductDAO productDAO = context.getBean(ProductDAO.class);
//        CategoryDAO categoryDAO = context.getBean(CategoryDAO.class);
//		MessageDAO messageDAO = context.getBean(MessageDAO.class);
//		MenuDAO menuDAO = context.getBean(MenuDAO.class);
//		MenuItemDAO menuItemDAO = context.getBean(MenuItemDAO.class);
//		TagDAO tagDAO = context.getBean(TagDAO.class);
//
//		Shop shop = new Shop();
//		shop.setStatus(EntityStatus.ACTIVE);
//		ShopDAO shopDAO = context.getBean(ShopDAO.class);
//		shop = shopDAO.save(shop);
//
//
//		Language language = new Language();
//		language.setName("English (Australia)");
//		language.setCode("en-au");
//		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
//		language = languageDAO.save(language);
//
//		insertMessage(messageDAO, String.format("shopName%d", shop.getShopId()), "Test Shop", shop, language);
//
//
//		Menu menuDelivery = new Menu();
//		menuDelivery.setStatus(EntityStatus.ACTIVE);
//		menuDelivery.setShop(shop);
//		menuDelivery = menuDAO.save(menuDelivery);
//		insertMessage(messageDAO, String.format("menu%dN", menuDelivery.getMenuId()), "Delivery", shop, language);
//
//		Category category = new Category();
//		category.setShop(shop);
//		category.setStatus(EntityStatus.ACTIVE);
//		category = categoryDAO.save(category);
//		insertMessage(messageDAO, String.format("cat%dN", category.getCategoryId()), "Drinks", shop, language);
//		MenuItem menuItem = new MenuItem();
//		menuItem.setCategory(category);
//		menuItem.setMenu(menuDelivery);
//		menuItem.setStatus(EntityStatus.ACTIVE);
//		menuItemDAO.save(menuItem);
//
//		category = new Category();
//		category.setShop(shop);
//		category.setStatus(EntityStatus.ACTIVE);
//		category = categoryDAO.save(category);
//		insertMessage(messageDAO, String.format("cat%dN", category.getCategoryId()), "Dessert", shop, language);
//		Integer menuItemId = menuItem.getMenuItemId();
//		menuItem = new MenuItem();
//		menuItem.setCategory(category);
//		menuItem.setMenu(menuDelivery);
//		menuItem.setStatus(EntityStatus.ACTIVE);
//		menuItem.setPreviousMenuItemId(menuItemId);
//		menuItemDAO.save(menuItem);
//
//		shop = shopDAO.get(shop.getShopId());
//		x(shop, language);
	}

//	@org.junit.Test
	public void testCreateOrder() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
//		Language language = languageDAO.get(1);
//		ShopDAO shopDAO = context.getBean(ShopDAO.class);
//		Shop shop = shopDAO.get(1);
//		MenuItemDAO menuItemDAO = context.getBean(MenuItemDAO.class);
//		MenuItem menuItem = menuItemDAO.get(212);
		
		OrdyDAO ordyDAO = context.getBean(OrdyDAO.class);
		Ordy ordy = new Ordy();
		ordy.setAmount(100l);
		ordy.setLanguageId(1);
		ordy.setNote("testing");
		ordy.setOrderTime(new Date());
		ordy.setShopId(1);
		ordy.setStatus(EntityStatus.ACTIVE);
		ordy.setOrdyItemList(new HashSet<OrdyItem>());
		OrdyItem item = new OrdyItem();
		item.setAmount(100);
		item.setMenuItemId(212);
		item.setNote("123");
		item.setPrice(100);
		item.setQty(1);
		item.setStatus(EntityStatus.ACTIVE);
		item.setOrdy(ordy);
		ordy.getOrdyItemList().add(item);
		ordyDAO.save(ordy);
	}

	private static MenuOrdyConfig importMenuOrdyConfig(SuburbDAO suburbDAO, File file, Menu menu, Integer minOrdyAmount) throws Exception {
		MenuOrdyConfig result = new MenuOrdyConfig();
		result.setMenu(menu);
		result.setMinOrdyAmount(minOrdyAmount);
		result.setShopSuburbConfigList(new HashSet<MenuOrdyServiceSuburb>());
		result.setStatus(EntityStatus.ACTIVE);
		Reader in = new FileReader(file);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("Code","Min Mins","Price").parse(in);
		for (CSVRecord record : records) {
			System.out.println(record.get("Code") + " - " + record.get("Min Mins") + " - " + record.get("Price"));
			MenuOrdyServiceSuburb sub = new MenuOrdyServiceSuburb();
			sub.setDeliveryPrice(new Integer(record.get("Price")));
			sub.setMinDeliveryMins(new Integer(record.get("Min Mins")));
			sub.setStatus(EntityStatus.ACTIVE);
			sub.setSuburb(suburbDAO.get(record.get("Code")));
			sub.setMenuOrdyConfig(result);
			result.getShopSuburbConfigList().add(sub);
			System.out.println(sub.getSuburb());
		}
		return result;
	}

//	@org.junit.Test
//	public void testYYS() throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
//		MenuDAO menuDAO = context.getBean(MenuDAO.class);
//		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);
//		Menu menuDelivery = menuDAO.get(3);
//		menuDelivery.setMenuOrdyConfig(
//			importMenuOrdyConfig(suburbDAO, "C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/sylvesters/myordy-v2/suburb-config.csv", menuDelivery, 1800)
//		);
//		menuDelivery = menuDAO.save(menuDelivery);
//	}
//
//	@org.junit.Test
	public void testFiles() throws Exception {
		File shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/sylvesters/myordy-v2/");
		File businessFile = new File(shopDir, "business.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"Shop Name","Timezone","Phone CSV","Business Number","Website","Address","Country Code","Geocode Latitude","Geocode Longitude","Postcode","State Code"
		).parse(new FileReader(businessFile));

		for (CSVRecord record : records) {
			System.out.println(record.get("Shop Name"));
			System.out.println(record.get("Timezone"));
			System.out.println(record.get("Phone CSV"));
			System.out.println(record.get("Business Number"));
			System.out.println(record.get("Website"));
			System.out.println(record.get("Address"));
			System.out.println(record.get("Country Code"));
			System.out.println(record.get("Geocode Latitude"));
			System.out.println(record.get("Geocode Longitude"));
			System.out.println(record.get("Postcode"));
			System.out.println(record.get("State Code"));
		}
	}

	private void saveShopFromFile(File shopDir, Shop shop, MessageDAO messageDAO, ShopDAO shopDAO, Business business, Language language) throws Exception {
		File businessFile = new File(shopDir, "business.csv");
		Iterable<CSVRecord> businessFileRecords = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"Shop Name","Timezone","Phone CSV","Business Number","Website","Address","Country Code","Geocode Latitude","Geocode Longitude","Postcode","State Code"
		).parse(new FileReader(businessFile));
		CSVRecord fileRecord = null;
		for (CSVRecord record : businessFileRecords) {
			fileRecord = record;
			break;
		}
		if (null != fileRecord) {
			shop.setBusiness(business);
			shop.setStatus(EntityStatus.ACTIVE);
			shop.setTimezone(fileRecord.get("Timezone"));
			shop.setAddress(new TheUserAddress());
			shop.getAddress().setAddress(fileRecord.get("Address"));
			shop.getAddress().setCountryCode(fileRecord.get("Country Code"));
			shop.getAddress().setGeocodeLatitude(fileRecord.get("Geocode Latitude"));
			shop.getAddress().setGeocodeLongitude(fileRecord.get("Geocode Longitude"));
			shop.getAddress().setPostcode(fileRecord.get("Postcode"));
			shop.getAddress().setStateCode(fileRecord.get("State Code"));
			shop.getAddress().setStatus(EntityStatus.ACTIVE);

			shop = shopDAO.save(shop);

			insertMessage(messageDAO, String.format("shopName%d", shop.getShopId()), fileRecord.get("Shop Name"), business, language);
			insertMessage(messageDAO, String.format("shopAddress%d", shop.getShopId()), fileRecord.get("Address"), business, language);
			insertMessage(messageDAO, String.format("shopPhone%d", shop.getShopId()), fileRecord.get("Phone CSV"), business, language);
			insertMessage(messageDAO, String.format("shopBusinessNumber%d", shop.getShopId()), fileRecord.get("Business Number"), business, language);
			insertMessage(messageDAO, String.format("shopWebsite%d", shop.getShopId()), fileRecord.get("Website"), business, language);

		}
	}

	@org.junit.Test
	public void importShop() throws Exception {
		File shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/PizzaMax/myordy-v2/");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");

        BusinessDAO businessDAO = context.getBean(BusinessDAO.class);
        ProductDAO productDAO = context.getBean(ProductDAO.class);
        CategoryDAO categoryDAO = context.getBean(CategoryDAO.class);
		MessageDAO messageDAO = context.getBean(MessageDAO.class);
		MenuDAO menuDAO = context.getBean(MenuDAO.class);
		MenuItemDAO menuItemDAO = context.getBean(MenuItemDAO.class);
		TagDAO tagDAO = context.getBean(TagDAO.class);
		OrdyDAO ordyDAO = context.getBean(OrdyDAO.class);
		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);

		Business business = businessDAO.get(1);
		if (null == business) {
			business = businessDAO.save(new Business());
		}

		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
		Language language = languageDAO.get(1);
		if (null == language){
			language = new Language();
			language.setName("English (Australia)");
			language.setCode("en-au");
			language.setStatus(EntityStatus.ACTIVE);
			language = languageDAO.save(language);
		}

		Menu menuDelivery = new Menu();
		menuDelivery.setCode("Delivery");
		menuDelivery.setStatus(EntityStatus.ACTIVE);
		menuDelivery.setBusiness(business);
		menuDelivery = menuDAO.save(menuDelivery);
		menuDelivery.setMenuOrdyConfig(
			importMenuOrdyConfig(suburbDAO, new File(shopDir, "suburb-config.csv"), menuDelivery, 1800)
		);
		menuDelivery = menuDAO.save(menuDelivery);

		insertMessage(messageDAO, String.format("menu%dN", menuDelivery.getMenuId()), "Delivery", business, language);
		Menu menuPickup = new Menu();
		menuPickup.setCode("Pickup");
		menuPickup.setStatus(EntityStatus.ACTIVE);
		menuPickup.setBusiness(business);
		menuPickup = menuDAO.save(menuPickup);
		insertMessage(messageDAO, String.format("menu%dN", menuPickup.getMenuId()), "Pickup", business, language);

		ShopDAO shopDAO = context.getBean(ShopDAO.class);
		Shop shop = new Shop();
		shop.setBusiness(business);
		shop.setMenuList(new HashSet<Menu>());
		shop.getMenuList().add(menuDelivery);
		shop.getMenuList().add(menuPickup);
		saveShopFromFile(shopDir, shop, messageDAO, shopDAO, business, language);

		MenuItem menuItemL1DelTemp = null, menuItemL2DelTemp = null, menuItemL3DelTemp = null;
		MenuItem menuItemL1PickTemp = null, menuItemL2PickTemp = null, menuItemL3PickTemp = null;
		Integer menuItemL1PrevDelTemp = null, menuItemL2PrevDelTemp = null, menuItemL3PrevDelTemp = null;
		Integer menuItemL1PrevPickTemp = null, menuItemL2PrevPickTemp = null, menuItemL3PrevPickTemp = null;

//		Reader in = new FileReader("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/sylvesters/myordy-v2/menu.csv");
		Reader in = new FileReader(new File(shopDir, "menu.csv"));
		// 
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("M1","M2","M3","Price-Delivery","Price-Pickup","Description", "Tag", "Tag1", "Tag2").parse(in);
		for (CSVRecord record : records) {
			MenuImportItem item = new MenuImportItem(record);

			Product product = null;
			Category category = null;

			if (item.isProduct()) {
				product = new Product();
				product.setBusiness(business);
				product.setStatus(EntityStatus.ACTIVE);
				product = productDAO.save(product);
//				insertMessage(messageDAO, String.format("prd%dN", product.getProductId()), getItemName(item), business, language);
//				insertMessage(messageDAO, String.format("prd%dD", product.getProductId()), getItemDescription(item), business, language);
//				String itemFullName = getItemFullName(menuItemL1DelTemp, menuItemL2DelTemp, getItemName(item), messageDAO, shop.getShopId(), language.getLanguageId());
//				insertMessage(messageDAO, String.format("prd%dNF", product.getProductId()), itemFullName, business, language);
			} else {
				category = new Category();
				category.setBusiness(business);
				category.setStatus(EntityStatus.ACTIVE);
				category = categoryDAO.save(category);
//				insertMessage(messageDAO, String.format("cat%dN", category.getCategoryId()), getItemName(item), business, language);
//				insertMessage(messageDAO, String.format("cat%dD", category.getCategoryId()), getItemDescription(item), business, language);
			}

			if (StringUtils.isNotBlank(item.menuItemL1)) {
				menuItemL1DelTemp = saveMenuItem(menuItemDAO, tagDAO, category, product, item, menuDelivery, null, item.priceDelivery, menuItemL1PrevDelTemp, shop);
				menuItemL1PrevDelTemp = menuItemL1DelTemp.getMenuItemId();
				menuItemL2PrevDelTemp = null; menuItemL3PrevDelTemp = null;
				menuItemL1PickTemp = saveMenuItem(menuItemDAO, tagDAO, category, product, item, menuPickup, null, item.pricePickup, menuItemL1PrevPickTemp, shop);
				menuItemL1PrevPickTemp = menuItemL1PickTemp.getMenuItemId();
				menuItemL2PrevPickTemp = null; menuItemL3PrevPickTemp = null;
			} else if (StringUtils.isNotBlank(item.menuItemL2)) {
				menuItemL2DelTemp = saveMenuItem(menuItemDAO, tagDAO, category, product, item, null, menuItemL1DelTemp, item.priceDelivery, menuItemL2PrevDelTemp, shop);
				menuItemL2PrevDelTemp = menuItemL2DelTemp.getMenuItemId();
				menuItemL3PrevDelTemp = null;
				menuItemL2PickTemp = saveMenuItem(menuItemDAO, tagDAO, category, product, item, null, menuItemL1PickTemp, item.pricePickup, menuItemL2PrevPickTemp, shop);
				menuItemL2PrevPickTemp = menuItemL2PickTemp.getMenuItemId();
				menuItemL3PrevPickTemp = null;
			} else if (StringUtils.isNotBlank(item.menuItemL3)) {
				menuItemL3DelTemp = saveMenuItem(menuItemDAO, tagDAO, category, product, item, null, menuItemL2DelTemp, item.priceDelivery, menuItemL3PrevDelTemp, shop);
				menuItemL3PrevDelTemp = menuItemL3DelTemp.getMenuItemId();
				menuItemL3PickTemp = saveMenuItem(menuItemDAO, tagDAO, category, product, item, null, menuItemL2PickTemp, item.pricePickup, menuItemL3PrevPickTemp, shop);
				menuItemL3PrevPickTemp = menuItemL3PickTemp.getMenuItemId();
			}

			if (item.isProduct()) {
				insertMessage(messageDAO, String.format("prd%dN", product.getProductId()), getItemName(item), business, language);
				insertMessage(messageDAO, String.format("prd%dD", product.getProductId()), getItemDescription(item), business, language);
				String itemFullName = getItemFullName(menuItemL1DelTemp, menuItemL2DelTemp, getItemName(item), messageDAO, shop.getShopId(), language.getLanguageId());
				insertMessage(messageDAO, String.format("prd%dNF", product.getProductId()), itemFullName, business, language);
			} else {
				insertMessage(messageDAO, String.format("cat%dN", category.getCategoryId()), getItemName(item), business, language);
				insertMessage(messageDAO, String.format("cat%dD", category.getCategoryId()), getItemDescription(item), business, language);
			}
		}

	}

	private static String getItemFullName(MenuItem menuItemL1, MenuItem menuItemL2, String itemName, MessageDAO messageDAO, Integer shopId, Integer languageId) {
		String result = itemName;
		MenuItem menuItem = menuItemL1;
		if (null != menuItemL2) {
			menuItem = menuItemL2;
		}
		if (null != menuItem.getCategory()) {
			Message message = messageDAO.getByCode(shopId, languageId, String.format("cat%dN", menuItem.getCategory().getCategoryId()));
			if (null != message) {
				result = result + " " + message.getMessage();
			}
		}
		return result;
	}

//	@org.junit.Test
//	public void testX() throws Exception {
//		final ObjectMapper om = new ObjectMapper();
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
////        MenuItemDAO mid  = context.getBean(MenuItemDAO.class);
////        MenuItem menuItem = mid.get(8);
////        System.out.println(menuItem.getMenuItemId());
////        System.out.println(menuItem.getTagList());
//
//        ShopDAO shopDAO = context.getBean(ShopDAO.class);
//        LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
//
//        List<Language> languages = languageDAO.list();
//
//        List<Shop> list = shopDAO.list();
//        for (Shop s : list) {
//        	for (Language language : languages) {
//        		s.addLanguageTable(language, s.getMessageList());
//        	}
//        	System.out.println(om.writeValueAsString(s));
//
//
//        	for (Menu m : s.getMenuList()) {
//    			System.out.println(findMessage(s.getMessageList(), String.format("menu%dN", m.getMenuId())));
//        		for (MenuItem mi : m.getMenuItems()) {
//        			System.out.print("\t"); printMenuItemDetails(s, mi);
//        			for (MenuItem mi2 :  mi.getMenuItems()) {
//        				System.out.print("\t\t"); printMenuItemDetails(s, mi2);
//        				if (null != mi2.getCategory()) {
//                			for (MenuItem mi3 : mi2.getMenuItems()) {
//                				System.out.print("\t\t\t"); printMenuItemDetails(s, mi3);
//                			}
//        				}
//        			}
//        		}
//        	}
//
//        }
//	}

//	private static void x(Business s, Language language) {
//    	s.addLanguageTable(language, s.getMessageList());
//
//    	for (Category c : s.getCategoryList()) {
//			System.out.println(findMessage(s.getMessageList(), String.format("cat%dN", c.getCategoryId())));
//    	}
//    	System.out.println("--------------------");
//    	for (Menu m : s.getMenuList()) {
//			System.out.println(findMessage(s.getMessageList(), String.format("menu%dN", m.getMenuId())));
//    		for (MenuItem mi : m.getMenuItems()) {
//    			System.out.print("\t"); printMenuItemDetails(s, mi);
//    			for (MenuItem mi2 :  mi.getMenuItems()) {
//    				System.out.print("\t\t"); printMenuItemDetails(s, mi2);
//    				if (null != mi2.getCategory()) {
//            			for (MenuItem mi3 : mi2.getMenuItems()) {
//            				System.out.print("\t\t\t"); printMenuItemDetails(s, mi3);
//            			}
//    				}
//    			}
//    		}
//    	}
//	}


//	private static Set<Tag> saveTags(TagDAO tagDAO, Set<String> tags, Shop shop) {
//		Set<Tag> result = new HashSet<Tag>();
//		Tag tag = null;
//		for (String t : tags) {
//			tag = tagDAO.getByCode(t, shop.getShopId());
//			if (null == tag) {
//				tag = new Tag();
//				tag.setCode(t);
//				tag.setShopId(shop.getShopId());
//				tag.setStatus(EntityStatus.ACTIVE);
//				tag = tagDAO.save(tag);
//			}
//			result.add(tag);
//		}
//		return result;
//	}

	private static MenuItem saveMenuItem(MenuItemDAO menuItemDAO, TagDAO tagDAO,
			Category category, Product product, MenuImportItem item, Menu menu, MenuItem parent, Integer price, Integer previous, Shop shop) {
		MenuItem result = makeMenuItem(product, category);
		result.setMenu(menu);
		result.setBusiness(shop.getBusiness());
		result.setParentMenuItem(parent);
		result.setPreviousMenuItemId(previous);
		if (null != result.getProduct()) {
			result.setPrice(new Long(price));
		}
		result = menuItemDAO.save(result);
		if (item.tags.size() > 0) {
			Set<Tag> saveTags = TestUtil.saveTags(tagDAO, item.tags, shop);
			result.setTagList(saveTags);
			result = menuItemDAO.save(result);
		}
		return result;
	}

	private static MenuItem makeMenuItem(Product product, Category category) {
		MenuItem result = new MenuItem();
		result.setProduct(product);
		result.setCategory(category);
		result.setStatus(EntityStatus.ACTIVE);
		return result;
	}

	private static void insertMessage(MessageDAO messageDAO, String code, String theMessage, Business sylvesters, Language language) {
		Message message = new Message();
		message.setLanguage(language);
		message.setMessage(theMessage);
		message.setCode(code);
		message.setBusiness(sylvesters);
		message.setStatus(EntityStatus.ACTIVE);
		messageDAO.save(message);
	}

	private static String getItemName(MenuImportItem item) {
		String result = "";
		if (StringUtils.isNotBlank(item.menuItemL1)) {
			result = item.menuItemL1;
		} else if (StringUtils.isNotBlank(item.menuItemL2)) {
			result = item.menuItemL2;
		} else if (StringUtils.isNotBlank(item.menuItemL3)) {
			result = item.menuItemL3;
		}
		return result;
	}

	private static String getItemDescription(MenuImportItem item) {
		String result = "";
		if (StringUtils.isNotBlank(item.description)) {
			result = item.description;
		}
		return result;
	}

	private static final class MenuImportItem {
		String menuItemL1;
		String menuItemL2;
		String menuItemL3;
		Integer priceDelivery;
		Integer pricePickup;
		String description;
		Set<String> tags = new HashSet<String>();
		MenuImportItem(CSVRecord record) {
			try {
				if (StringUtils.isNotEmpty(record.get("M1"))) {
					menuItemL1 = record.get("M1");
				}
				if (StringUtils.isNotEmpty(record.get("M2"))) {
					menuItemL2 = record.get("M2");
				}
				if (StringUtils.isNotEmpty(record.get("M3"))) {
					menuItemL3 = record.get("M3");
				}
				if (StringUtils.isNotEmpty(record.get("Price-Delivery"))) {
					priceDelivery = Integer.valueOf(record.get("Price-Delivery"));
				}
				if (StringUtils.isNotEmpty(record.get("Price-Pickup"))) {
					pricePickup = Integer.valueOf(record.get("Price-Pickup"));
				}
				if (StringUtils.isNotEmpty(record.get("Description"))) {
					description = record.get("Description");
				}
				if (StringUtils.isNotEmpty(record.get("Tag"))) {
					tags.add(record.get("Tag"));
				}
				if (StringUtils.isNotEmpty(record.get("Tag1"))) {
					tags.add(record.get("Tag1"));
				}
				if (StringUtils.isNotEmpty(record.get("Tag2"))) {
					tags.add(record.get("Tag2"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean isProduct() {
			return priceDelivery != null || pricePickup != null;
		}
		public String toString() {return ToStringBuilder.reflectionToString(this);}
	}

	private static void printMenuItemDetails(Business s, MenuItem m) {
		if (null != m.getCategory()) {
			System.out.println(findMessage(s.getMessageList(), String.format("cat%dN", m.getCategory().getCategoryId())));
		} else if (null != m.getProduct()) {
			System.out.print(findMessage(s.getMessageList(), String.format("prd%dN", m.getProduct().getProductId())));
			if (null != m.getTagList()) {
				for (Tag t : m.getTagList()) {
					System.out.print(" " + t.getCode());
				}
			}
			System.out.println(" id: " + m.getMenuItemId());
		}
	}

	private static String findMessage(Set<Message> list, String code) {
		String result = null;
		for (Message m : list) {
			if (m.getCode().equals(code)) {
				result = m.getMessage();
				break;
			}
		}
		return result;
	}

}
