package jrange;

import java.util.List;

import jrange.myordy.dao.CustomerDAO;
import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.MenuOrdyConfig;
import jrange.myordy.entity.MenuOrdyServiceSuburb;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.Suburb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MenuTest {

	@org.junit.Test
	public void testY() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		//ShopDAO shopDAO = context.getBean(ShopDAO.class);
		//CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
		//Shop shop = shopDAO.get(1);
        MenuDAO menuDAO = context.getBean(MenuDAO.class);
        List<Suburb> menuServiceSuburbList = menuDAO.getMenuServiceSuburbList(1, 1);
        for (Suburb suburb : menuServiceSuburbList) {
        	System.out.println(suburb.getPostcode() + " " + suburb.getName());
		}
	}

}
