package jrange;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import jrange.myordy.dao.BusinessDAO;
import jrange.myordy.dao.CategoryDAO;
import jrange.myordy.dao.CustomerDAO;
import jrange.myordy.dao.ExtraOptionGroupDAO;
import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.MenuItemComboDAO;
import jrange.myordy.dao.MenuItemDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.dao.ProductDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.dao.SuburbDAO;
import jrange.myordy.dao.TagDAO;
import jrange.myordy.dao.UserDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.Category;
import jrange.myordy.entity.Country;
import jrange.myordy.entity.Customer;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.MenuDiscountCoupon;
import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.MenuItemCombo;
import jrange.myordy.entity.MenuItemComboOption;
import jrange.myordy.entity.MenuOrdyConfig;
import jrange.myordy.entity.MenuOrdyServiceSuburb;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Product;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.ShopImage;
import jrange.myordy.entity.ShopServerName;
import jrange.myordy.entity.State;
import jrange.myordy.entity.Suburb;
import jrange.myordy.entity.Tag;
import jrange.myordy.entity.TheUser;
import jrange.myordy.entity.TheUserAddress;
import jrange.myordy.entity.TheUserContact;
import jrange.myordy.entity.TheUserLoginEmail;
import jrange.myordy.entity.TheUserPhone;
import jrange.myordy.entity.TheUserPhoneType;
import jrange.myordy.entity.menuitemoption.ExtraOptionGroup;
import jrange.myordy.entity.menuitemoption.ExtraOptionItem;
import jrange.myordy.entity.menuitemoption.ExtraOptionMenuItemConfig;
import jrange.myordy.util.MessageUtil;
import jrange.myordy.v1.shop.restaurant.JBOptSUser;
import jrange.myordy.v1.shop.restaurant.ShopCustomerRestaurant;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class ShopSetupTest {

	public static void main(String[] args) throws Exception {

		ShopSetupTest x = new ShopSetupTest();
//		x.setupSuburbs();

//		x.importShop();
//		x.createExtraAndMinueOptions();
//		x.createExtraAndMinusOptionsMapping();
		
//		x.importMenuItemCombo();
//		x.importMenuItemComboOption();
//		x.createMenuDiscountCoupon();
//		x.importShopImages();

		x.importCustomers();
	}

	ApplicationContext context;
    BusinessDAO businessDAO;
    ProductDAO productDAO;
    CategoryDAO categoryDAO;
	MessageDAO messageDAO;
	MenuDAO menuDAO;
	MenuItemDAO menuItemDAO;
	TagDAO tagDAO;
	OrdyDAO ordyDAO;
	SuburbDAO suburbDAO;
	ExtraOptionGroupDAO extraOptionGroupDAO;
	ShopDAO shopDAO;
	MenuItemComboDAO menuItemComboDAO;
	CustomerDAO customerDAO;
	UserDAO userDAO;
	Integer shopIdV1;
	Integer shopId;
	File shopDir;
//	Integer deliveryMinAmount;
	private boolean countryIndia;

	public ShopSetupTest() {
//		shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/PizzaMax/myordy-v2/");
//		shopIdV1 = 1084;

//		shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/sylvesters/myordy-v2/");
//		shopIdV1 = 55;

//		shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/VeggieChoicePizza/myordy-v2/");
//		shopIdV1 = 1083;

		shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/zestypizza/");
		shopIdV1 = 1083;


//		shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/smita/");
//		shopIdV1 = -1;
//		countryIndia = true;


		//deliveryMinAmount = 1500; // veggie
		//deliveryMinAmount = 1800; // sylvesters
		

		shopId = 1;

		context = new ClassPathXmlApplicationContext("spring4.xml");
        businessDAO = context.getBean(BusinessDAO.class);
        productDAO = context.getBean(ProductDAO.class);
        categoryDAO = context.getBean(CategoryDAO.class);
		messageDAO = context.getBean(MessageDAO.class);
		menuDAO = context.getBean(MenuDAO.class);
		menuItemDAO = context.getBean(MenuItemDAO.class);
		tagDAO = context.getBean(TagDAO.class);
		ordyDAO = context.getBean(OrdyDAO.class);
		suburbDAO = context.getBean(SuburbDAO.class);
		extraOptionGroupDAO = context.getBean(ExtraOptionGroupDAO.class);
		shopDAO = context.getBean(ShopDAO.class);
		menuItemComboDAO = context.getBean(MenuItemComboDAO.class);
 		customerDAO = context.getBean(CustomerDAO.class);
		userDAO = context.getBean(UserDAO.class);
	}

	public void importShopImages() throws Exception {

		final byte[] logoFileBytes = Files.readAllBytes(Paths.get(new File(shopDir, "logo.png").getAbsolutePath()));		
		String logoBase64BinaryString = DatatypeConverter.printBase64Binary(logoFileBytes);
		final Shop shop = new Shop();
		shop.setShopId(shopId);
		final ShopImage shopImage = new ShopImage();
		shopImage.setContentBase64Binary(logoBase64BinaryString);
		shopImage.setImageCode("[LOGO-MAIN]");
		shopImage.setShop(shop);
		shopImage.setStatus(EntityStatus.ACTIVE);
		shopDAO.saveShopImage(shopImage);
	}


	public void importCustomers() throws Exception {
		Integer languageId = 1;

		List<ShopCustomerRestaurant> customerV1 = customerDAO.listCustomerV1(new Long(shopIdV1));
		for (ShopCustomerRestaurant cust : customerV1) {
			TheUser theUser = null;
			TheUserLoginEmail ule = null;
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
					
					ule = new TheUserLoginEmail();
					ule.setLoginEmail(v1.getEmail());
					ule.setUserId(theUser.getUserId());
					userDAO.saveUserLoginEmail(ule);
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
				phoneNumbers[i] = phoneNumbers[i].replaceAll("[^\\d+]", "");
				TheUserPhone p = new TheUserPhone();
				p.setPhoneNumber(phoneNumbers[i]);
				p.setStatus(EntityStatus.ACTIVE);
				if (phoneNumbers[i].startsWith("4") || phoneNumbers[i].startsWith("04") || phoneNumbers[i].startsWith("+614")) {
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

	public void importMenuItemComboOption() throws Exception {
		int shopId = 1;
		Language language = new Language();
		language.setLanguageId(1);
		Business business = new Business();
		business.setBusinessId(1);

		Shop shop = shopDAO.get(shopId);
        Reader in = new FileReader(new File(shopDir, "combo-item-options.csv"));

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"Combo Item Code", "Option Menu Item Tag", "Qty Required", "Name"
		).parse(in);
		for (CSVRecord record : records) {
			MenuItemCombo combo = menuItemComboDAO.getByCode(record.get("Combo Item Code"), business.getBusinessId());
			MenuItemComboOption option = new MenuItemComboOption();
			option.setMenuItemCombo(combo);
			option.setMenuItemTag(tagDAO.getByCode(record.get("Option Menu Item Tag"), shopId));
			option.setQtyRequired(new Integer(record.get("Qty Required")));
			option.setStatus(EntityStatus.ACTIVE);
			option = menuItemComboDAO.saveMenuItemComboOption(option);

			TestUtil.insertMessage(messageDAO, String.format("menuItemComboOption%dN", option.getMenuItemComboOptionId()), record.get("Name"), business, language);
			
		}
	}

	public void importMenuItemCombo() throws Exception {

		int shopId = 1;
		Language language = new Language();
		language.setLanguageId(1);
		Business business = new Business();
		business.setBusinessId(1);

		Shop shop = shopDAO.get(shopId);
        Reader in = new FileReader(new File(shopDir, "combo-items.csv"));

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"Menu Code", "Item Code", "Name", "Name Full", "Price Formula", "Description", "Tag", "Tag1", "Tag2"
		).parse(in);
		for (CSVRecord record : records) {
			Menu menuByCode = getMenuByCode(record.get("Menu Code"), shop.getMenuList());
			MenuItemCombo combo = menuItemComboDAO.getByCode(record.get("Item Code"), business.getBusinessId());
			if (null == combo) {
				combo = new MenuItemCombo();
			}

			combo.setComboOptionList(new HashSet<MenuItemComboOption>());
			combo.setCode(record.get("Item Code"));
			combo.setMenu(menuByCode);
			combo.setPriceFormula(record.get("Price Formula"));
			combo.setStatus(EntityStatus.ACTIVE);

			if (StringUtils.isNotEmpty(record.get("Tag")) || StringUtils.isNotEmpty(record.get("Tag1")) || StringUtils.isNotEmpty(record.get("Tag2"))) {
				Set<String> tags = new HashSet<String>();
				if (StringUtils.isNotEmpty(record.get("Tag"))) {
					tags.add(record.get("Tag"));
				}
				if (StringUtils.isNotEmpty(record.get("Tag1"))) {
					tags.add(record.get("Tag1"));
				}
				if (StringUtils.isNotEmpty(record.get("Tag2"))) {
					tags.add(record.get("Tag2"));
				}
				Set<Tag> saveTags = TestUtil.saveTags(tagDAO, tags, shop);
				combo.setTagList(saveTags);
			}

			combo = menuItemComboDAO.save(combo);

			TestUtil.insertMessage(messageDAO, String.format("menuItemCombo%dN", combo.getMenuItemComboId()), record.get("Name"), business, language);
			TestUtil.insertMessage(messageDAO, String.format("menuItemCombo%dNF", combo.getMenuItemComboId()), record.get("Name Full"), business, language);
			TestUtil.insertMessage(messageDAO, String.format("menuItemCombo%dD", combo.getMenuItemComboId()), record.get("Description"), business, language);

		}
		
	}

	private Menu getMenuByCode(String code, Set<Menu> menuList) {
		for (Menu menu : menuList) {
			if (code.equals(menu.getCode())) {
				return menu;
			}
		}
		return null;
	}

	public void createExtraAndMinusOptionsMapping() throws Exception {
        Reader in = new FileReader(new File(shopDir, "extra-and-minus-options-mapping.csv"));
		List<Message> list = messageDAO.list();
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("Menu Item Tag","Extra Option Group Code").parse(in);
		Hashtable<String, List<MenuItem>> tagMenuItemsMap = new Hashtable<String, List<MenuItem>>();
		for (CSVRecord record : records) {
			String tag = record.get("Menu Item Tag");
			List<MenuItem> byTag = tagMenuItemsMap.get(tag);
			if (null == byTag) {
				byTag = menuItemDAO.getByTag(1, tag);
				tagMenuItemsMap.put(tag, byTag);
			}
			saveExtraOptionConfig(menuItemDAO, list, record.get("Extra Option Group Code"), byTag);
		}
	}

	private void saveExtraOptionConfig(MenuItemDAO menuItemDAO, List<Message> list, String addOptionsGroupCode, List<MenuItem> byTag) {
//		List<MenuItem> byTag = menuItemDAO.getByTag(1, tag);
		for (MenuItem menuItem : byTag) {
			if (null != menuItem.getProduct()) {
				System.out.println(menuItem.getMenuItemId() + " : " + MessageUtil.findMessage(String.format("prd%dNF", menuItem.getProduct().getProductId()), list).getMessage());
				ExtraOptionMenuItemConfig extraOptionConfig = new ExtraOptionMenuItemConfig();
				extraOptionConfig.setExtraOptionsGroupCode(addOptionsGroupCode);
				menuItemDAO.saveExtraOptionConfig(menuItem.getMenuItemId(), extraOptionConfig);
			} else if (null != menuItem.getCategory()) {
				throw new RuntimeException("Category not expected here ...");
			}
		}
	}

	public void createMenuDiscountCoupon() throws Exception {
		final File inputFile = new File(shopDir, "menu-discount-coupon.csv");
		if (inputFile.exists()) {
			Shop shop = shopDAO.get(shopId);
			Reader in = new FileReader(new File(shopDir, "menu-discount-coupon.csv"));
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("Menu Code","Coupon Code","Description","Ordy Cashback Formula").parse(in);
			for (CSVRecord record : records) {
				Menu menuByCode = getMenuByCode(record.get("Menu Code"), shop.getMenuList());
				MenuDiscountCoupon c = new MenuDiscountCoupon();
				c.setCouponCode(record.get("Coupon Code"));
				c.setDescription(record.get("Description"));
				c.setMenu(menuByCode);
				c.setOrdyCashbackFormula(record.get("Ordy Cashback Formula"));
				c.setStatus(EntityStatus.ACTIVE);
				menuDAO.saveMenuDiscountCoupon(c);
			}
		}
	}

	public void createExtraAndMinueOptions() throws Exception {
        Reader in = new FileReader(new File(shopDir, "extra-and-minus-options.csv"));

		Language language = new Language();
		language.setLanguageId(1);
		Business business = new Business();
		business.setBusinessId(1);

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("GroupCode","Name","Add Price","Remove Price").parse(in);
		for (CSVRecord record : records) {
			ExtraOptionGroup byCode = extraOptionGroupDAO.getByCode(record.get("GroupCode"), business.getBusinessId());
			if (null == byCode) {
				byCode = new ExtraOptionGroup();
				byCode.setBusiness(business);
				byCode.setCode(record.get("GroupCode"));
				byCode.setStatus(EntityStatus.ACTIVE);
				extraOptionGroupDAO.save(byCode);
			}
			ExtraOptionItem item = new ExtraOptionItem();
			item.setExtraOptionGroup(byCode);
			item.setStatus(EntityStatus.ACTIVE);
			if (StringUtils.isNotBlank(record.get("Add Price"))) {
				item.setAddPrice(Integer.valueOf(record.get("Add Price")));
			}
			if (StringUtils.isNotBlank(record.get("Remove Price"))) {
				item.setRemovePrice(Integer.valueOf(record.get("Remove Price")));
			}
			extraOptionGroupDAO.saveExtraOptionItem(item);

			insertMessage(messageDAO, String.format("extOpt%dN", item.getExtraOptionItemId()), record.get("Name"), business, language);
		}
	}

	public void importShop() throws Exception {

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

// menu-ordy-config.csv
// Menu Code	Min Ordy Amount	Discount Percent Online Ordy	Discount Percent First Ordy

		Menu menuDelivery = new Menu();
		menuDelivery.setCode("Delivery");
		menuDelivery.setStatus(EntityStatus.ACTIVE);
		menuDelivery.setBusiness(business);
		menuDelivery = menuDAO.save(menuDelivery);
		menuDelivery.setMenuOrdyConfig(
			importMenuOrdyConfigWithSuburbConfig(suburbDAO, new File(shopDir, "suburb-config.csv"), menuDelivery)
		);
		menuDelivery = menuDAO.save(menuDelivery);
		insertMessage(messageDAO, String.format("menu%dN", menuDelivery.getMenuId()), "Delivery", business, language);

		Menu menuPickup = new Menu();
		menuPickup.setCode("Pickup");
		menuPickup.setStatus(EntityStatus.ACTIVE);
		menuPickup.setBusiness(business);
		menuPickup = menuDAO.save(menuPickup);
		menuPickup.setMenuOrdyConfig(getMenuOrdyConfig(new File(shopDir, "menu-ordy-config.csv"), menuPickup));
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

	private static String getItemFullName(MenuItem menuItemL1, MenuItem menuItemL2, String itemName, MessageDAO messageDAO, Integer shopId, Integer languageId) {
		String result = itemName;
		MenuItem menuItem = menuItemL1;
		if (null != menuItemL2) {
			menuItem = menuItemL2;
		}
		if (null != menuItem.getCategory()) {
			Message message = messageDAO.getByCode(shopId, languageId, String.format("cat%dN", menuItem.getCategory().getCategoryId()));
			if (null != message) {
//				result = result + " " + message.getMessage();
				result = message.getMessage() + " : " + result;
			}
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

	public void setupSuburbs() throws Exception {
		if (countryIndia) {
			setupSuburbsIndia();
		} else {
			setupSuburbsAustralia();
		}
	}

	public void setupSuburbsIndia() throws Exception {
		setupSuburbs("IN", "India", "English (Australia)", "en-au",
				"C:/Users/jbaudh/git-repository/myordy/myordy-service/src/test/resources/India_Post_Codes_Lat_Lon.csv");
	}

	public void setupSuburbsAustralia() throws Exception {
		setupSuburbs("AU", "Australia", "English (Australia)", "en-au",
				"C:/Users/jbaudh/git-repository/myordy/myordy-service/src/test/resources/Australian_Post_Codes_Lat_Lon.csv");
	}

	public void setupSuburbs(final String countryCode, final String countryName, final String languageName,
			final String languageCode, final String suburbFilePath) throws Exception {

		Country c = new Country();
		c.setCode(countryCode);
		c.setStatus(EntityStatus.ACTIVE);
		suburbDAO.saveCountry(c);

		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
		Language language = languageDAO.get(1);
		if (null == language){
			language = new Language();
			language.setName(languageName);
			language.setCode(languageCode);
			language.setStatus(EntityStatus.ACTIVE);
			language = languageDAO.save(language);
		}

		insertMessage(messageDAO, String.format("country%dN", c.getCountryId()), countryName, null, language);

		Reader in = new FileReader(suburbFilePath);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("postcode","suburb","state","dc","type","lat","lon").parse(in);
		HashMap<String, State> stateCodeToStateMap = new HashMap<String, State>();
		int counter = 0;
		for (CSVRecord record : records) {
			if ("Delivery Area".equalsIgnoreCase(record.get("type").trim())) {
				if (counter % 100 == 0) {
					System.gc();
					Thread.sleep(5000);
				}
				counter++;
				State state = stateCodeToStateMap.get(record.get("state").trim());
				if (null == state) {
					state = suburbDAO.getState(c.getCountryId(), record.get("state").trim());
					stateCodeToStateMap.put(record.get("state").trim(), state);
				}
				if (null == state) {
					state = new State();
					state.setCode(record.get("state").trim());
					state.setCountry(c);
					state.setStatus(EntityStatus.ACTIVE);
					suburbDAO.saveState(state);
					insertMessage(messageDAO, String.format("state%dN", state.getStateId()), state.getCode(), null, language);
				}
				Suburb suburb = new Suburb();
				suburb.setPostcode(record.get("postcode").trim());
				suburb.setState(state);
				suburb.setGeocodeLatitude(record.get("lat").trim());
				suburb.setGeocodeLongitude(record.get("lon").trim());
				suburb.setCode(c.getCode() + "-" + suburb.getPostcode() + "-" + record.get("suburb").trim().toUpperCase().replaceAll("[^A-Za-z0-9]", ""));
				suburb.setStatus(EntityStatus.ACTIVE);
				suburbDAO.saveSuburb(suburb);
				insertMessage(messageDAO, String.format("suburb%dN", suburb.getSuburbId()), record.get("suburb").trim(), null, language);
//				System.out.println(record.get("postcode").trim() + " - " + record.get("suburb").trim() + " - " + record.get("state").trim());
			}
		}
	}

	private static MenuOrdyConfig getMenuOrdyConfig(File menuOrdyConfigFile, Menu menu) throws Exception {
		MenuOrdyConfig result = new MenuOrdyConfig();
		result.setMenu(menu);
		result.setStatus(EntityStatus.ACTIVE);

		Reader in = new FileReader(menuOrdyConfigFile);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("Menu Code","Min Ordy Amount","Discount Percent Online Ordy","Discount Percent First Ordy","Print Copies Count", "Default Delivery Mins").parse(in);
		for (CSVRecord record : records) {
			if (menu.getCode().equalsIgnoreCase(record.get("Menu Code"))) {
				result.setMinOrdyAmount(new Integer(record.get("Min Ordy Amount")));
				result.setDiscountPercentFirstOrdy(new Float(record.get("Discount Percent First Ordy")));
				result.setDiscountPercentOnlineOrdy(new Float(record.get("Discount Percent Online Ordy")));
				result.setPrintCopiesCount(new Integer(record.get("Print Copies Count")));
				result.setDefaultDeliveryMins(new Integer(record.get("Default Delivery Mins")));
				break;
			}
		}

		return result;
	}

	private static MenuOrdyConfig importMenuOrdyConfigWithSuburbConfig(SuburbDAO suburbDAO, File suburbConfigFile, Menu menu) throws Exception {
		MenuOrdyConfig result = getMenuOrdyConfig(new File(suburbConfigFile.getParentFile(), "menu-ordy-config.csv"), menu);
		result.setShopSuburbConfigList(new HashSet<MenuOrdyServiceSuburb>());
		Reader in = new FileReader(suburbConfigFile);
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

	private void saveShopFromFile(File shopDir, Shop shop, MessageDAO messageDAO, ShopDAO shopDAO, Business business, Language language) throws Exception {
		File businessFile = new File(shopDir, "business.csv");
		Iterable<CSVRecord> businessFileRecords = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"Shop Name","Timezone","Phone CSV","Business Number","Website","Address","Country Code","Geocode Latitude","Geocode Longitude","Postcode","State Code","Currency Code"
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
			shop.setCurrencyCode(fileRecord.get("Currency Code"));
			shop.setAddress(new TheUserAddress());
			shop.getAddress().setAddress(fileRecord.get("Address"));
			shop.getAddress().setCountryCode(fileRecord.get("Country Code"));
			shop.getAddress().setGeocodeLatitude(fileRecord.get("Geocode Latitude"));
			shop.getAddress().setGeocodeLongitude(fileRecord.get("Geocode Longitude"));
			shop.getAddress().setPostcode(fileRecord.get("Postcode"));
			shop.getAddress().setStateCode(fileRecord.get("State Code"));
			shop.getAddress().setStatus(EntityStatus.ACTIVE);

			shop.setServerNameList(new HashSet<ShopServerName>());
			shop.getServerNameList().add(new ShopServerName().setShop(shop).setServerName("localhost"));

			shop = shopDAO.save(shop);

			insertMessage(messageDAO, String.format("shopName%d", shop.getShopId()), fileRecord.get("Shop Name"), business, language);
			insertMessage(messageDAO, String.format("shopAddress%d", shop.getShopId()), fileRecord.get("Address"), business, language);
			insertMessage(messageDAO, String.format("shopPhone%d", shop.getShopId()), fileRecord.get("Phone CSV"), business, language);
			insertMessage(messageDAO, String.format("shopBusinessNumber%d", shop.getShopId()), fileRecord.get("Business Number"), business, language);
			insertMessage(messageDAO, String.format("shopWebsite%d", shop.getShopId()), fileRecord.get("Website"), business, language);

		}
	}

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

}
