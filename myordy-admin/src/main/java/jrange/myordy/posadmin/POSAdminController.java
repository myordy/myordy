package jrange.myordy.posadmin;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.ShopImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public final class POSAdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(POSAdminController.class);
	private static final POSAdminConfig CONFIG = new POSAdminConfig();

	@Autowired
	ShopDAO shopDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		LOGGER.info("POS ADMIN locale is {}.", locale);

		TimeZone.setDefault(TimeZone.getTimeZone("+00:00")); // TODO move this to server configuration

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("config", CONFIG);
		model.addAttribute("shopId", 1);

		return "index";
	}
	
	@RequestMapping(value = "/shopImage/{shopId}/{imageCode}", method = RequestMethod.GET)
	public void showImage(@PathVariable("shopId") final int shopId, @PathVariable("imageCode") final String imageCode, 
			final HttpServletResponse response, final HttpServletRequest request) throws ServletException, IOException {

		final ShopImage shopImage = shopDAO.getShopImage(shopId, imageCode);
		if (null != shopImage) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		    response.getOutputStream().write(DatatypeConverter.parseBase64Binary(shopImage.getContentBase64Binary()));

		    response.getOutputStream().close();
		}
	}

}
