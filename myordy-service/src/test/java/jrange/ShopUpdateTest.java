package jrange;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.DatatypeConverter;

import jrange.myordy.dao.ExtraOptionGroupDAO;
import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.ShopImage;
import jrange.myordy.entity.menuitemoption.ExtraOptionGroup;
import jrange.myordy.entity.menuitemoption.ExtraOptionItem;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class ShopUpdateTest {

	ApplicationContext context;
	Integer shopId;
	MessageDAO messageDAO;
	ExtraOptionGroupDAO extraOptionGroupDAO;
	ShopDAO shopDAO;
	MenuDAO menuDAO;

	File shopDir;

	public ShopUpdateTest() {
		shopId = 1;
		shopDir = new File("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/zestypizza/");

		context = new ClassPathXmlApplicationContext("spring4.xml");

		messageDAO = context.getBean(MessageDAO.class);
		extraOptionGroupDAO = context.getBean(ExtraOptionGroupDAO.class);
		shopDAO = context.getBean(ShopDAO.class);
		menuDAO = context.getBean(MenuDAO.class);
	}
	
	public static void main(String[] args) throws Exception {


		ShopUpdateTest x = new ShopUpdateTest();
		x.addNewMenuItem();
//		x.updateShopLogo();
//		x.createExtraAndMinueOptions(
//			"GroupCode,Name,Add Price,Remove Price\r\n"
//				+ "SMALL-EXTRA,Vegan Cheese,100,0\r\n"
//				+ "MEDIUM-EXTRA,Vegan Cheese,150,0\r\n"
//				+ "LARGE-EXTRA,Vegan Cheese,200,0\r\n"
//		);
	}


	public void addNewMenuItem() throws Exception {
		Menu menu = menuDAO.getByCode(1, "Delivery");
		System.out.println(menu.getStatus());
	}


	public void updateShopLogo() throws Exception {

		final byte[] logoFileBytes = Files.readAllBytes(Paths.get(new File(shopDir, "logo.png").getAbsolutePath()));		
		String logoBase64BinaryString = DatatypeConverter.printBase64Binary(logoFileBytes);
		final Shop shop = new Shop();
		shop.setShopId(shopId);
//		final ShopImage shopImage = new ShopImage();
		final ShopImage shopImage = shopDAO.getShopImage(shopId, "[LOGO-MAIN]");
		shopImage.setContentBase64Binary(logoBase64BinaryString);
		shopImage.setImageCode("[LOGO-MAIN]");
		shopImage.setShop(shop);
		shopImage.setStatus(EntityStatus.ACTIVE);
		shopDAO.saveShopImage(shopImage);
	}


	public void createExtraAndMinueOptions(final String extraAndMinusOptionsCSV) throws Exception {
        Reader in = new StringReader(extraAndMinusOptionsCSV);

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



	private static void insertMessage(MessageDAO messageDAO, String code, String theMessage, Business business, Language language) {
		Message message = new Message();
		message.setLanguage(language);
		message.setMessage(theMessage);
		message.setCode(code);
		message.setBusiness(business);
		message.setStatus(EntityStatus.ACTIVE);
		messageDAO.save(message);
	}

}
