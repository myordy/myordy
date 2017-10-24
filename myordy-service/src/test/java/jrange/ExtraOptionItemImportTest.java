package jrange;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrange.myordy.dao.ExtraOptionGroupDAO;
import jrange.myordy.dao.MenuItemDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.menuitemoption.ExtraOptionGroup;
import jrange.myordy.entity.menuitemoption.ExtraOptionItem;
import jrange.myordy.entity.menuitemoption.ExtraOptionMenuItemConfig;
import jrange.myordy.util.MessageUtil;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class ExtraOptionItemImportTest {

	private File shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/PizzaMax/myordy-v2/");

	@org.junit.Test
	public void xxx() throws Exception {
        Reader in = new FileReader(new File(shopDir, "extra-and-minus-options-mapping.csv"));
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ExtraOptionGroupDAO bean = context.getBean(ExtraOptionGroupDAO.class);
		MenuItemDAO menuItemDAO = context.getBean(MenuItemDAO.class);
		MessageDAO messageDAO = context.getBean(MessageDAO.class);
		List<Message> list = messageDAO.list();

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("Menu Item Tag","Extra Option Group Code").parse(in);
		for (CSVRecord record : records) {
			//System.out.println(record.get("Menu Item Tag") + " : " + record.get("Extra Option Group Code"));
			x(menuItemDAO, list, record.get("Menu Item Tag"), record.get("Extra Option Group Code"));
		}
		
//		String[] tags = {"PIZZA-SMALL", "PIZZA-MEDUM", "PIZZA-LARGE", "PIZZA-FAMILY", "PIZZA-GF", "CALZONE", "SALAD", "PASTA", "MAINS"};
//		String[] extra = {"SMALL-EXTRA", "MEDUM-EXTRA", "LARGE-EXTRA", "FAMILY-EXTRA", "MEDUM-EXTRA", "OTHER-EXTRA", "OTHER-EXTRA", "PASTA-EXTRA", "PASTA-EXTRA"};
//		for (int i = 0; i < tags.length; i++) {
//			System.out.println("\r\n------------------------------------------------------------------");
//			x(menuItemDAO, list, tags[i], extra[i]);
//		}
	}

	private void x(MenuItemDAO menuItemDAO, List<Message> list, String tag, String addOptionsGroupCode) {
		List<MenuItem> byTag = menuItemDAO.getByTag(1, tag);
		for (MenuItem menuItem : byTag) {
			if (null != menuItem.getProduct()) {
				System.out.println(menuItem.getMenuItemId() + " : " + MessageUtil.findMessage(String.format("prd%dNF", menuItem.getProduct().getProductId()), list).getMessage());
				ExtraOptionMenuItemConfig extraOptionConfig = new ExtraOptionMenuItemConfig();
				extraOptionConfig.setExtraOptionsGroupCode(addOptionsGroupCode);
//				extraOptionConfig.setAddOptionsGroupCode(addOptionsGroupCode);
//				extraOptionConfig.setRemoveOptionsGroupCode("ALL-MINUS");
				menuItemDAO.saveExtraOptionConfig(menuItem.getMenuItemId(), extraOptionConfig);
			} else if (null != menuItem.getCategory()) {
				throw new RuntimeException("Category not expected here ...");
			}
		}
	}

//	@org.junit.Test
	public void createSylvestersOptions() throws Exception {
        Reader in = new FileReader(new File(shopDir, "extra-and-minus-options.csv"));

		ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ExtraOptionGroupDAO bean = context.getBean(ExtraOptionGroupDAO.class);
		MessageDAO messageDAO = context.getBean(MessageDAO.class);

		Language language = new Language();
		language.setLanguageId(1);
		Business business = new Business();
		business.setBusinessId(1);

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("GroupCode","Name","Add Price","Remove Price").parse(in);
		for (CSVRecord record : records) {
			ExtraOptionGroup byCode = bean.getByCode(record.get("GroupCode"), business.getBusinessId());
			if (null == byCode) {
				byCode = new ExtraOptionGroup();
				byCode.setBusiness(business);
				byCode.setCode(record.get("GroupCode"));
				byCode.setStatus(EntityStatus.ACTIVE);
				bean.save(byCode);
			}
//			Set<ExtraOptionItem> extraOptionItemList = byCode.getExtraOptionItemList();
//			if (null == extraOptionItemList) {
//				extraOptionItemList = new HashSet<ExtraOptionItem>();
//				byCode.setExtraOptionItemList(extraOptionItemList);
//			}
			ExtraOptionItem item = new ExtraOptionItem();
			item.setExtraOptionGroup(byCode);
			item.setStatus(EntityStatus.ACTIVE);
			if (StringUtils.isNotBlank(record.get("Add Price"))) {
				item.setAddPrice(Integer.valueOf(record.get("Add Price")));
			}
			if (StringUtils.isNotBlank(record.get("Remove Price"))) {
				item.setRemovePrice(Integer.valueOf(record.get("Remove Price")));
			}
			bean.saveExtraOptionItem(item);

			insertMessage(messageDAO, String.format("extOpt%dN", item.getExtraOptionItemId()), record.get("Name"), business, language);

//			extraOptionItemList.add(item);
			
//			System.out.println(byCode);
		}
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


}
