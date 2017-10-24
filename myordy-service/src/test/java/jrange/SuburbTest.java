package jrange;

import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import jrange.myordy.dao.BusinessDAO;
import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.SuburbDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.Country;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.State;
import jrange.myordy.entity.Suburb;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class SuburbTest {

//	@org.junit.Test
	public void findSuburbsByCode() throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);
		Reader in = new FileReader("C:/Users/jbaudh/workspace/myordy-shops/dataentry/shop-restaurant/sylvesters/myordy-v2/suburb-config.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("Code","Min Mins","Price").parse(in);
		for (CSVRecord record : records) {
			System.out.println(suburbDAO.get(record.get("Code")).getPostcode());
		}
	}

//	@org.junit.Test
	public void findSuburbs() throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");

//		MessageDAO messageDAO = context.getBean(MessageDAO.class);
		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);
		List<Suburb> suburbs = suburbDAO.findSuburbs("AU", "3011", 1);
		for (Suburb suburb : suburbs) {
			System.out.println(suburb.getSuburbId() + " " + suburb.getPostcode() + " " + suburb.getName());
//			System.out.println("\t" + messageDAO.getByCode(null, 1, String.format("suburb%dN", suburb.getSuburbId())).getMessage());
		}

	}

	@org.junit.Test
	public void setupSuburbs() throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");

		MessageDAO messageDAO = context.getBean(MessageDAO.class);
		SuburbDAO suburbDAO = context.getBean(SuburbDAO.class);
		Country c = new Country();
		c.setCode("AU");
		c.setStatus(EntityStatus.ACTIVE);
		suburbDAO.saveCountry(c);

		LanguageDAO languageDAO = context.getBean(LanguageDAO.class);
		Language language = languageDAO.get(1);
		if (null == language){
			language = new Language();
			language.setName("English (Australia)");
			language.setCode("en-au");
			language.setStatus(EntityStatus.ACTIVE);
			language = languageDAO.save(language);
		}

		insertMessage(messageDAO, String.format("country%dN", c.getCountryId()), "Australia", null, language);


//		BusinessDAO businessDAO = context.getBean(BusinessDAO.class);

		Reader in = new FileReader("C:/Users/jbaudh/git-repository/myordy/myordy-service/src/test/resources/Australian_Post_Codes_Lat_Lon.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withSkipHeaderRecord(true).withHeader("postcode","suburb","state","dc","type","lat","lon").parse(in);
		for (CSVRecord record : records) {
			if ("Delivery Area".equalsIgnoreCase(record.get("type").trim())) {
				State state = suburbDAO.getState(c.getCountryId(), record.get("state").trim());
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
