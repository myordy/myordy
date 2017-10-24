package jrange.myordy.resources;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public final class ResourcesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesController.class);

	@RequestMapping(value = "/terms-of-service", method = RequestMethod.GET)
	public String termsOfService(Locale locale, Model model) {
		return "terms-of-service";
	}

	@RequestMapping(value = "/privacy-policy", method = RequestMethod.GET)
	public String privacyPolicy(Locale locale, Model model) {
		return "privacy-policy";
	}
	
}
