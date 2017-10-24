package jrange.myordy.pos;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.ShopImage;
import jrange.myordy.security.SessionIdGenerator;
import jrange.myordy.util.HTTPUtil;
import jrange.myordy.util.MyOrdyHomeUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.braintreegateway.BraintreeGateway;

@Controller
public final class POSController {

	private static final Logger LOGGER = LoggerFactory.getLogger(POSController.class);
	private static final POSConfig CONFIG = new POSConfig();

	@Autowired
	LanguageDAO languageDAO;

	@Autowired
	ShopDAO shopDAO;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(final HttpServletRequest request, final HttpServletResponse response,
			@CookieValue(value = "dus", required = false) final String dusSesisonIdArg,
			final Locale locale, final Model model) {
		LOGGER.info("Welcome home! The client locale is {}.", locale);

		TimeZone.setDefault(TimeZone.getTimeZone("+00:00")); // TODO move this to server configuration

		final Date date = new Date();
		final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		final String formattedDate = dateFormat.format(date);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		Integer shopId = shopDAO.getShopId(HTTPUtil.getServerName(request));
		if (null == shopId) {
			shopId = 1;
		}
		final String remoteAddr = HTTPUtil.getRemoteAddr(request);

		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("config", CONFIG);
		model.addAttribute("shopId", shopId);
		model.addAttribute("userIPAddr", remoteAddr);
		model.addAttribute("STRIPE_MYORDY_publishableKey", MyOrdyHomeUtil.getShopProperties(shopId).getProperty("STRIPE_MYORDY.publishableKey"));

		if (StringUtils.isNoneEmpty(MyOrdyHomeUtil.getShopProperties(shopId).getProperty("PAYPAL_MYORDY.clientToken"))) {
			try {
				BraintreeGateway gateway = new BraintreeGateway(MyOrdyHomeUtil.getShopProperties(shopId).getProperty("PAYPAL_MYORDY.clientToken"));
				model.addAttribute("PAYPAL_MYORDY_clientToken", gateway.clientToken().generate());
				model.addAttribute("PAYPAL_MYORDY_env", MyOrdyHomeUtil.getShopProperties(shopId).getProperty("PAYPAL_MYORDY.env"));
			} catch (final Throwable t) {
				LOGGER.error(t.getClass().getCanonicalName(), t);
			}
		}

		String dusSesisonId = dusSesisonIdArg;
		if (null == dusSesisonId) {
			dusSesisonId = SessionIdGenerator.newDeviceSessionId(remoteAddr);
		}
		model.addAttribute("dus", dusSesisonId);

//		Shop shop = shopDAO.get(1);
//		shop.addLanguageTable(languageDAO.list().get(0), shop.getMessageList());
//		System.out.println(shop);

		return "index";
//		return "temp";
	}

	@RequestMapping(value = "/temp", method = RequestMethod.GET)
	public String temp(final HttpServletRequest request, final HttpServletResponse response,
			@CookieValue(value = "dus", required = false) final String dusSesisonIdArg,
			final Locale locale, final Model model) {

		BraintreeGateway gateway = new BraintreeGateway("PLEASE USE CORRECT KEY HERE");
		model.addAttribute("PAYPAL_clientToken", gateway.clientToken().generate());

		return "temp";
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



/*
	@RequestMapping(value = "/create-payment", method = RequestMethod.POST)
	@ResponseBody
	public Payment createPayment(final HttpServletRequest request, final HttpServletResponse response) {
System.out.println(">>>>> create-payment called");
//		Enumeration parameterNames = request.getParameterNames();
//		while (parameterNames.hasMoreElements()) {
//			System.out.println(">>>> " + request.getParameter((String)parameterNames.nextElement()));
//		}
//		//	//return "temp";

//Set payer details
Payer payer = new Payer();
payer.setPaymentMethod("paypal");

//Set redirect URLs
RedirectUrls redirectUrls = new RedirectUrls();
redirectUrls.setCancelUrl("http://localhost:8080/myordy-pos/cancel");
redirectUrls.setReturnUrl("http://localhost:8080/myordy-pos/process");

//Set payment details
Details details = new Details();
details.setShipping("1");
details.setSubtotal("5");
details.setTax("1");

//Payment amount
Amount amount = new Amount();
amount.setCurrency("AUD");
//Total must be equal to sum of shipping, tax and subtotal.
amount.setTotal("7");
amount.setDetails(details);

//Transaction information
Transaction transaction = new Transaction();
transaction.setAmount(amount);
transaction
.setDescription("This is the payment transaction description.");

//Add transaction to a list
List<Transaction> transactions = new ArrayList<Transaction>();
transactions.add(transaction);

//Add payment details
Payment payment = new Payment();
payment.setIntent("sale");
payment.setPayer(payer);
payment.setRedirectUrls(redirectUrls);
payment.setTransactions(transactions);

String clientId = "AdAo6aTAfNvJkdtdYpUg3iKMQS25pcpiUCrOXRSCvo35iWkkB6DHuEd549-563WCd_LTpjV0N7Rj-4U5";
String clientSecret = "EF79q1KSPQs81yBukN_CddVa11xjL8Tv5BQLaOpPAL54cAkMlxsdg7Ii-4lCnuBujm0tcNB2iETM_Gx5";

APIContext context = new APIContext(clientId, clientSecret, "sandbox");
Payment createdPayment = null;

//Create payment
try {
createdPayment = payment.create(context);

Iterator links = createdPayment.getLinks().iterator();
while (links.hasNext()) {
 Links link = (Links)links.next();
 if (link.getRel().equalsIgnoreCase("approval_url")) {
   // REDIRECT USER TO link.getHref()
 }
}
} catch (PayPalRESTException e) {
 System.err.println(e.getDetails());
}
return createdPayment;
	}



	private static Payment x() {
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		// Set redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://localhost:8080/myordy-pos/cancel");
		redirectUrls.setReturnUrl("http://localhost:8080/myordy-pos/process");

		// Set payment details
		Details details = new Details();
		details.setShipping("1");
		details.setSubtotal("5");
		details.setTax("1");

		// Payment amount
		Amount amount = new Amount();
		amount.setCurrency("AUD");
		// Total must be equal to sum of shipping, tax and subtotal.
		amount.setTotal("7");
		amount.setDetails(details);

		// Transaction information
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("This is the payment transaction description.");

		// Add transaction to a list
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		// Add payment details
		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setRedirectUrls(redirectUrls);
		payment.setTransactions(transactions);

		String clientId = "AdAo6aTAfNvJkdtdYpUg3iKMQS25pcpiUCrOXRSCvo35iWkkB6DHuEd549-563WCd_LTpjV0N7Rj-4U5";
		String clientSecret = "EF79q1KSPQs81yBukN_CddVa11xjL8Tv5BQLaOpPAL54cAkMlxsdg7Ii-4lCnuBujm0tcNB2iETM_Gx5";

		APIContext context = new APIContext(clientId, clientSecret, "sandbox");
		Payment createdPayment = null;

		// Create payment
		try {
			createdPayment = payment.create(context);
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());
		}
		return createdPayment;
	}
*/


}
