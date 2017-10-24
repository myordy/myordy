package jrange;

import java.io.StringWriter;

import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.list.OrdySearchRequest;
import jrange.myordy.entity.list.OrdySearchResponse;
import jrange.myordy.entity.list.OrdySearchResponse.OrdySearchListItem;
import jrange.myordy.model.Enviornment;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrdyTest {

//	@org.junit.Test
	public void testY() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
        Enviornment env = context.getBean(Enviornment.class);
        System.out.println(">>> XXX: " + env.getXxx());
        OrdyDAO ordyDAO = context.getBean(OrdyDAO.class);
        final OrdySearchRequest request = new OrdySearchRequest();
        OrdySearchResponse list = ordyDAO.list(request);
        for (OrdySearchListItem ordy : list.getItems()) {
			System.out.print(ordy.getOrdyId() + " : " + ordy.getOrderTime() + " : ");
			System.out.print(ordy.getCustomerName());
			System.out.println(" : " + ordy.getAmount());
			if (null != ordy.getCustomerName()) {
				Ordy ordy2 = ordyDAO.get(ordy.getOrdyId(), 1);
				System.out.println(ordy2.getCustomer());
			}
		}

	}

//	@org.junit.Test
	public void testYY() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
        OrdyDAO ordyDAO = context.getBean(OrdyDAO.class);
        Ordy ordy = ordyDAO.get(5, 1);
        System.out.println(ordy.getCustomer().getContact().getName());
        System.out.println(ordy.getOrdyItemList().size());
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, ordy);
        System.out.println(sw.toString());
	}

}
