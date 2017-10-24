package jrange;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Shop;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

public final class QueryTest {

	@org.junit.Test
	public void testX() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		ShopDAO shopDAO = context.getBean(ShopDAO.class);

        Session session = shopDAO.getCurrentSessionTemp();
	}

}
