package jrange;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrange.myordy.dao.ExtraOptionGroupDAO;
import jrange.myordy.dao.MenuItemComboDAO;
import jrange.myordy.dao.MenuItemDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.dao.TagDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.MenuItemCombo;
import jrange.myordy.entity.MenuItemComboOption;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.Tag;
import jrange.myordy.util.MessageUtil;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class MenuItemComboTest {

	private File shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/PizzaMax/myordy-v2/");

	@org.junit.Test
	public void importMenuItemComboOption() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ShopDAO shopDAO = context.getBean(ShopDAO.class);
		MenuItemComboDAO menuItemComboDAO = context.getBean(MenuItemComboDAO.class);
		MessageDAO messageDAO = context.getBean(MessageDAO.class);
		TagDAO tagDAO = context.getBean(TagDAO.class);
		

		int shopId = 1;
		Language language = new Language();
		language.setLanguageId(1);
		Business business = new Business();
		business.setBusinessId(1);

		Shop shop = shopDAO.get(shopId);
        Reader in = new FileReader(new File(shopDir, "combo-item-options.csv"));

//        Combo Item Code	Option Menu Item Tag	Qty Required	Name

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
			
//			System.out.println(combo.getMenuItemComboId());
//			System.out.println("\t" + record.get("Combo Item Code"));
//			System.out.println("\t" + record.get("Option Menu Item Tag"));
//			System.out.println("\t" + record.get("Qty Required"));
//			System.out.println("\t" + record.get("Name"));
		}
	}

//	@org.junit.Test
	public void importMenuItemCombo() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ShopDAO shopDAO = context.getBean(ShopDAO.class);
		MenuItemComboDAO menuItemComboDAO = context.getBean(MenuItemComboDAO.class);
		MessageDAO messageDAO = context.getBean(MessageDAO.class);
		TagDAO tagDAO = context.getBean(TagDAO.class);

		int shopId = 1;
		Language language = new Language();
		language.setLanguageId(1);
		Business business = new Business();
		business.setBusinessId(1);

		Shop shop = shopDAO.get(shopId);
        Reader in = new FileReader(new File(shopDir, "combo-items.csv"));

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader(
			"Menu Code", "Item Code", "Name", "Name Full", "Price Formula", "Description", "Tag"
		).parse(in);
		for (CSVRecord record : records) {
			Menu menuByCode = getMenuByCode(record.get("Menu Code"), shop.getMenuList());
			MenuItemCombo combo = menuItemComboDAO.getByCode(record.get("Item Code"), business.getBusinessId());
			if (null == combo) {
				combo = new MenuItemCombo();
			}

			// name = Half & Half
			// nameFull = Half & Half Medium Pizza
			combo.setComboOptionList(new HashSet<MenuItemComboOption>());
			combo.setCode(record.get("Item Code"));
			combo.setMenu(menuByCode);
//			combo.setOrderItemIngredientQtyMultiplier(new Float(record.get("Ingredient Qty Multiplier")));
//			combo.setPrice(new Integer(record.get("Price")));
			combo.setPriceFormula(record.get("Price Formula"));
			combo.setStatus(EntityStatus.ACTIVE);

			if (StringUtils.isNotEmpty(record.get("Tag"))) {
				Set<String> tags = new HashSet<String>();
				tags.add(record.get("Tag"));
				Set<Tag> saveTags = TestUtil.saveTags(tagDAO, tags, shop);
				combo.setTagList(saveTags);
			}

			combo = menuItemComboDAO.save(combo);

			TestUtil.insertMessage(messageDAO, String.format("menuItemCombo%dN", combo.getMenuItemComboId()), record.get("Name"), business, language);
			TestUtil.insertMessage(messageDAO, String.format("menuItemCombo%dNF", combo.getMenuItemComboId()), record.get("Name Full"), business, language);
			TestUtil.insertMessage(messageDAO, String.format("menuItemCombo%dD", combo.getMenuItemComboId()), record.get("Description"), business, language);

//			System.out.println(
//				record.get("Menu Code")
//				+ " " + record.get("Item Code")
//				+ " " + record.get("Name")
//				+ " " + record.get("Name Full")
//				+ " " + record.get("Ingredient Qty Multiplier")
//				+ " " + record.get("Price")
//				+ " " + record.get("Price Formula")
//				+ " " + record.get("Description")
//			);
		}

//		ExtraOptionGroupDAO bean = context.getBean(ExtraOptionGroupDAO.class);
//		MenuItemDAO menuItemDAO = context.getBean(MenuItemDAO.class);
//		MessageDAO messageDAO = context.getBean(MessageDAO.class);
//		TagDAO tagDAO = context.getBean(TagDAO.class);
//		String[] codes = {"MEDIUM-PIZZA-ALL-EXCEPT-SUPER-SUPREAM", "LARGE-PIZZA-ALL-EXCEPT-SUPER-SUPREAM", "FAMILY-PIZZA-ALL-EXCEPT-SUPER-SUPREAM", "GF-PIZZA-ALL-EXCEPT-SUPER-SUPREAM"};
//		for (String code : codes) {
//			Tag byCode = tagDAO.getByCode(code, 1);
//			System.out.println(byCode.getTagId());
//		}
		
//		List<Message> list = messageDAO.list();
//		List<MenuItem> byTag = menuItemDAO.getByTag(1, "FAMILY-PIZZA-ALL-EXCEPT-SUPER-SUPREAM");
//		for (MenuItem menuItem : byTag) {
//			System.out.println(menuItem.getMenuItemId() + " : " + MessageUtil.findMessage(String.format("prd%dNF", menuItem.getProduct().getProductId()), list).getMessage());
//		}
//		MenuItemCombo halfHalf = new MenuItemCombo();
//		// name = Half & Half
//		// nameFull = Half & Half Medium Pizza
//		halfHalf.setComboOptionList(new HashSet<MenuItemComboOption>());
//		halfHalf.setMenu(null);
//		halfHalf.setOrderItemIngredientQtyMultiplier(new Float(.5));
//		halfHalf.setPrice(new Integer(0));
//		halfHalf.setPriceFormula("");
//		halfHalf.setStatus(EntityStatus.ACTIVE);
//		MenuItemComboOption option = new MenuItemComboOption();
//		// name : Pizza Choice
//		option.setMenuItemCombo(halfHalf);
//		option.setMenuItemTag(menuItemTag); // MEDIUM-PIZZA-ALL-EXCEPT-SUPER-SUPREAM
//		option.setQtyRequired(new Integer(2)); // 2
//		option.setStatus(EntityStatus.ACTIVE);
//		halfHalf.getComboOptionList().add(option);

//		MEDIUM-PIZZA-ALL-EXCEPT-SUPER-SUPREAM
//		LARGE-PIZZA-ALL-EXCEPT-SUPER-SUPREAM
//		FAMILY-PIZZA-ALL-EXCEPT-SUPER-SUPREAM
//		GF-PIZZA-ALL-EXCEPT-SUPER-SUPREAM

		
	}
	
	private Menu getMenuByCode(String code, Set<Menu> menuList) {
		for (Menu menu : menuList) {
			if (code.equals(menu.getCode())) {
				return menu;
			}
		}
		return null;
	}

//	private void x(Tag byCode) {
//		MenuItemCombo halfHalf = new MenuItemCombo();
//		// name = Half & Half
//		// nameFull = Half & Half Medium Pizza
//		halfHalf.setComboOptionList(new HashSet<MenuItemComboOption>());
//		halfHalf.setMenu(null);
////		halfHalf.setOrderItemIngredientQtyMultiplier(new Float(.5));
////		halfHalf.setPrice(new Integer(0));
//		halfHalf.setPriceFormula("");
//		halfHalf.setStatus(EntityStatus.ACTIVE);
//		MenuItemComboOption option = new MenuItemComboOption();
//		// name : Pizza Choice
//		option.setMenuItemCombo(halfHalf);
//		option.setMenuItemTag(byCode); // MEDIUM-PIZZA-ALL-EXCEPT-SUPER-SUPREAM
//		option.setQtyRequired(new Integer(2)); // 2
//		option.setStatus(EntityStatus.ACTIVE);
//		halfHalf.getComboOptionList().add(option);
//	}

}
