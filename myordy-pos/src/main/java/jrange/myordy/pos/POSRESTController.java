package jrange.myordy.pos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import jrange.myordy.dao.CustomerDAO;
import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.dao.PaymentDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.dao.SuburbDAO;
import jrange.myordy.dao.UserDAO;
import jrange.myordy.entity.Customer;
import jrange.myordy.entity.CustomerPayment;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.OrdyItem;
import jrange.myordy.entity.OrdyItemCombo;
import jrange.myordy.entity.OrdyItemComboSub;
import jrange.myordy.entity.OrdyItemExtraOptionAdd;
import jrange.myordy.entity.OrdyItemExtraOptionRemove;
import jrange.myordy.entity.OrdyStatus;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.StaffRoleOption;
import jrange.myordy.entity.Suburb;
import jrange.myordy.entity.list.CustomerSearchRequest;
import jrange.myordy.entity.list.OrdySearchRequest;
import jrange.myordy.entity.list.OrdySearchResponse;
import jrange.myordy.entity.list.PaginatedResponse;
import jrange.myordy.entity.list.SuburbSearchRequest;
import jrange.myordy.entity.update.OrdyConfirmRequest;
import jrange.myordy.model.EntityDTO;
import jrange.myordy.model.ReferenceData;
import jrange.myordy.security.LoginEmailRequest;
import jrange.myordy.security.LoginResponse;
import jrange.myordy.security.SessionIdGenerator;
import jrange.myordy.security.SessionIdGenerator.DeviceSessionId;
import jrange.myordy.security.SessionIdGenerator.UserSessionId;
import jrange.myordy.service.NotificationService;
import jrange.myordy.util.HTTPUtil;
import jrange.myordy.util.Security;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/pos-rest")
public class POSRESTController {

    private static final Logger LOGGER = Logger.getLogger(POSRESTController.class.getName());

    @Autowired
    private ShopDAO shopDAO;
	
	@Autowired
	private OrdyDAO ordyDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private SuburbDAO suburbDAO;

	@Autowired
	private PaymentDAO paymentDAO;

	@Autowired
	private NotificationService notificationService;

	// TODO make this dynamic
	private static final Language LANGUAGE = new Language().setLanguageId(1);

	@Autowired
	private MenuDAO menuDAO;

	@Autowired
	private UserDAO userDAO;

//	private RapidClientConfig ewayRapidClientConfig;

	@RequestMapping(value = "/restoreUserSession", method = RequestMethod.GET)
	public LoginResponse restoreUserSession(@CookieValue(value = "plt", required = false) final String sesisonId) {
		return userDAO.restoreUserSession(sesisonId);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponse login(@RequestBody @Valid LoginEmailRequest loginEmailRequest) {
		return userDAO.login(loginEmailRequest.getEmail(), loginEmailRequest.getPassword());
	}

	@RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
	public Shop getShop(final String sesisonId, @PathVariable("id") int id) {
		final Shop shop = shopDAO.get(id);
		shop.addLanguageTable(LANGUAGE, shop.getBusiness().getMessageList());
		return shop;
	}

	@RequestMapping(value = "/confirmOrdy", method = RequestMethod.POST)
	public Ordy getConfirmOrdy(@RequestBody @Valid final OrdyConfirmRequest r,
			@CookieValue(value = "plt", required = true) final String sessionId) {
		Security.staffUserRoleCheck(userDAO, sessionId, r.getShopId(), StaffRoleOption.SHOP_POS_OPERATOR);
		final Ordy result = ordyDAO.confirmOrdy(r.getOrdyId(), r.getShopId());

		notificationService.sendCustomerOrderConfirmedNotification(result);

		return result;
	}

	@RequestMapping(value = "/ordyDUS/{id}/{shopId}", method = RequestMethod.GET)
	public Ordy getOrdyDUS(@CookieValue(value = "dus", required = true) final String deviceSessionId,
			@PathVariable("id") Integer id, @PathVariable("shopId") final Integer shopId) {
		final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(deviceSessionId);
		if (StringUtils.isBlank(decryptedDeviceSessionId.getUserSessionId())) {
			return null;
		} else {
			final Ordy result = ordyDAO.get(id, shopId, decryptedDeviceSessionId.getUserSessionId());
			return result;
		}
	}

	@RequestMapping(value = "/customerLastOrdy/{customerId}/{shopId}", method = RequestMethod.GET)
	public Ordy getCustomerLastOrdy(@CookieValue(value = "plt", required = true) final String sessionId,
			@PathVariable("customerId") Integer customerId, @PathVariable("shopId") final Integer shopId) {
		Security.staffUserRoleCheck(userDAO, sessionId, shopId, StaffRoleOption.SHOP_POS_OPERATOR);
		final Ordy result = ordyDAO.getCustomerLastOrdy(customerId, shopId);
		return result;
	}

	@RequestMapping(value = "/ordy/{id}/{shopId}", method = RequestMethod.GET)
	public Ordy getOrdy(@CookieValue(value = "plt", required = true) final String sessionId,
			@PathVariable("id") Integer id, @PathVariable("shopId") final Integer shopId) {
		Security.staffUserRoleCheck(userDAO, sessionId, shopId, StaffRoleOption.SHOP_POS_OPERATOR);
		final Ordy result = ordyDAO.get(id, shopId);
		return result;
	}

	@RequestMapping(value = "/submitOrdy", method = RequestMethod.POST)
	public EntityDTO<Ordy> submitOrdy(final HttpServletRequest request,
			@CookieValue(value = "plt", required = false) final String sessionId,
			@CookieValue(value = "dus", required = false) final String deviceSessionId,
			@RequestBody @Valid EntityDTO<Ordy> ordyDTO) {
		ordyDTO.getEntity().setOrderTime(new Date());
		ordyDTO.getEntity().setStatus(EntityStatus.ACTIVE);
		ordyDTO.getEntity().setSubmissionIPAddress(HTTPUtil.getRemoteAddr(request));

		for (OrdyItem oi : ordyDTO.getEntity().getOrdyItemList()) {
			setupOrdyItem(oi, ordyDTO.getEntity(), null);
		}
		for (OrdyItemCombo coi : ordyDTO.getEntity().getComboOrdyItemList()) {
			coi.setOrdy(ordyDTO.getEntity());
			coi.setStatus(EntityStatus.ACTIVE);
			for (OrdyItem oi : coi.getOrderItems()) {
				setupOrdyItem(oi, null, coi);
			}
			for (OrdyItemComboSub oics : coi.getOrdyItemsComboSub()) {
				oics.setOrdyItemCombo(coi);
				for (OrdyItem oi : oics.getOrderItems()) {
					setupOrdyItem(oi, null, null);
					oi.setOrdyItemComboSub(oics);
				}
				oics.setStatus(EntityStatus.ACTIVE);;
			}
		}

		if ((null == ordyDTO.getEntity().getCustomer()) || (null != ordyDTO.getEntity().getCustomer()  && null == ordyDTO.getEntity().getCustomer().getCustomerId())) {
			if (null != ordyDTO.getEntity().getCustomerMobileNumber()) {
				ordyDTO.getEntity().setCustomer(customerDAO.getNewOrdyCustomer(ordyDTO.getEntity()));
			}
		}
		if (null != ordyDTO.getEntity().getCustomer() && null == ordyDTO.getEntity().getCustomer().getCustomerId()) {
			ordyDTO.getEntity().setCustomer(null);
		}
//		ordyDTO.getEntity().getMenuId();
//		ordyDTO.getEntity().setDeliveryTime(new Date()); // TODO make this dynamic
		ordyDTO.getEntity().setOrdyStatus(OrdyStatus.NEWORDY);

		final UserSessionId decryptedSessionId = SessionIdGenerator.getDecryptedSessionId(sessionId);
		if (null != decryptedSessionId) {
			final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(deviceSessionId);
			ordyDTO.getEntity().setDeviceSessionId(decryptedDeviceSessionId.getUserSessionId());			
			if (null != decryptedSessionId.getUserId()) {
				final Customer customer = customerDAO.getByUserId(ordyDTO.getEntity().getShopId(), decryptedSessionId.getUserId());
				ordyDTO.getEntity().setCustomer(customer);
			}
		}
		final boolean userShopPOSOperator = Security.isStaffUser(userDAO, decryptedSessionId.getUserId(), ordyDTO.getEntity().getShopId(), StaffRoleOption.SHOP_POS_OPERATOR);
		if (userShopPOSOperator) {
			ordyDTO.getEntity().setOrdyStatus(OrdyStatus.CONFIRMED);
			// TODO set pos operator here
		}

		final CustomerPayment customerPayment = paymentDAO.onSubmitOrder(ordyDTO.getEntity());
		ordyDAO.save(ordyDTO.getEntity());
		paymentDAO.save(customerPayment);
		
//		if (!userShopPOSOperator) {
//			notificationService.sendCustomerOrderReceivedNotification(ordyDTO.getEntity());
//		}

		LOGGER.logp(Level.INFO, LOGGER.getName(), "submitOrdy",
				"'{'ordyId:{0},remoteAddr:\"{1}\",userId:{2}'}'", 
				new Object[] {ordyDTO.getEntity().getOrdyId(), ordyDTO.getEntity().getSubmissionIPAddress(), decryptedSessionId.getUserId()});
		return ordyDTO;
	}

	private void setupOrdyItem(final OrdyItem oi, final Ordy ordy, final OrdyItemCombo coi) {
		if (ordy != null && coi != null) {
			throw new RuntimeException("Cant set both Ordy and OrdyItemCombo. One of them must be null.");
		}
		oi.setOrdy(ordy);
		oi.setOrdyItemCombo(coi);
		oi.setStatus(EntityStatus.ACTIVE);
		if (null != oi.getExtraOptions()) {
			oi.getExtraOptions().setOrdyItem(oi);
			if (null != oi.getExtraOptions().getAddOptions()) {
				for (OrdyItemExtraOptionAdd x : oi.getExtraOptions().getAddOptions()) {
					x.setOrdyItemExtraOption(oi.getExtraOptions());
				}
			}
			if (null != oi.getExtraOptions().getRemoveOptions()) {
				for (OrdyItemExtraOptionRemove x : oi.getExtraOptions().getRemoveOptions()) {
					x.setOrdyItemExtraOption(oi.getExtraOptions());
				}
			}
		}
	}
	
/*
	@RequestMapping(value = "/paymentCompleteEway/{orderId}/{shopId}/{transactionID}/", method = RequestMethod.POST)
	public void paymentCompleteEway(final HttpServletRequest request,
			@CookieValue(value = "dus", required = true) final String deviceSessionId,
			@PathVariable("orderId") final Integer orderId, @PathVariable("shopId") final Integer shopId,
			@PathVariable("transactionID") final Integer transactionID) {
		
		final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(deviceSessionId);
		final Ordy ordy = ordyDAO.get(orderId, shopId, decryptedDeviceSessionId.getUserSessionId());
		ordy.getShopId();

	}
	
	@RequestMapping(value = "/payOnlineEway/{orderId}/{shopId}", method = RequestMethod.GET)
	public EwayCreateTransactionResponse payOnlineEway(final HttpServletRequest request,
			@CookieValue(value = "dus", required = true) final String deviceSessionId,
			@PathVariable("orderId") final Integer orderId, @PathVariable("shopId") final Integer shopId) {

		final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(deviceSessionId);
		final Ordy ordy = ordyDAO.get(orderId, shopId, decryptedDeviceSessionId.getUserSessionId());

		final EwayPaymentTransaction t = new EwayPaymentTransaction();
		t.setCustomerDeviceIP(HTTPUtil.getRemoteAddr(request));

		t.setCustomerEmail(ordy.getCustomerEmail());
		t.setCustomerName(ordy.getCustomerName());
		t.setCustomerPhoneNumber(ordy.getCustomerMobileNumber());
		t.setInvoiceReference(OrdyUtil.getInvoiceReference(ordy));
		t.setPaymentTotalAmount(ordy.getAmount().intValue());

		t.setRapidClientConfig(getEwayRapidClientConfig());

		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2"); //TODO set it at system level
		final CreateTransactionResponse response = paymentDAO.ewayCreateTransaction(t);
		final EwayCreateTransactionResponse result = new EwayCreateTransactionResponse();
		result.setSharedPaymentUrl(response.getSharedPaymentUrl());

		if (null != response && null != response.getErrors()) {
			for (String e : response.getErrors()) {
				LOGGER.logp(Level.INFO, LOGGER.getName(), "payOnlineEway", "Eway Create Transaction Failed - Error: {0}", new Object[] {e});
			}
		}
		
		return result;
	}

	private RapidClientConfig getEwayRapidClientConfig() {
		if (null == ewayRapidClientConfig) {
			synchronized (this) {
				final RapidClientConfig result = new RapidClientConfig();
				InitialContext initialContext;
				try {
					initialContext = new javax.naming.InitialContext();
//					result.setApiKey((String)initialContext.lookup("java:comp/env/ewayApiKey"));
//					result.setPassword((String)initialContext.lookup("java:comp/env/ewayPassword"));
//					result.setRapidEndPoint((String)initialContext.lookup("java:comp/env/ewayRapidEndPoint"));
					// TODO remove this hardcoding
					result.setApiKey("A1001CescrFwv5Y5J6AonX9+pWj186ZHgZVCIdHV00b1EG1fsJdTr/cd77qdaTSUSou+ho");
					result.setPassword("Open1234567");
					result.setRapidEndPoint("Sandbox");
				} catch (NamingException e) {
					e.printStackTrace();
				}
				this.ewayRapidClientConfig = result;
			}
		}
		
		return ewayRapidClientConfig;
	}
*/
	@RequestMapping(value = "/customerSearch", method = RequestMethod.POST)
	public PaginatedResponse<Customer> customerSearch(@RequestBody @Valid final CustomerSearchRequest r,
			@CookieValue(value = "plt", required = false) final String sessionId) {
		Security.staffUserRoleCheck(userDAO, sessionId, r.getShopId(), StaffRoleOption.SHOP_POS_OPERATOR);
		return customerDAO.list(r);
	}

	@RequestMapping(value = "/referenceData", method = RequestMethod.GET)
	public ReferenceData getReferenceData() {
		return ReferenceData.INSTANCE;
	}

	@RequestMapping(value = "/customerDelete/{id}/{shopId}", method = RequestMethod.GET)
	public void customerDelete(@CookieValue(value = "plt", required = false) final String sessionId,
			@PathVariable("id") final Integer id, @PathVariable("shopId") final Integer shopId) {
		Security.staffUserRoleCheck(userDAO, sessionId, shopId, StaffRoleOption.SHOP_POS_OPERATOR);
		customerDAO.delete(id);
	}

	@RequestMapping(value = "/customerUpdate", method = RequestMethod.PUT)
	public EntityDTO<Customer> customerUpdate(@CookieValue(value = "plt", required = false) final String sessionId,
			@RequestBody @Valid EntityDTO<Customer> dto) {
		Security.staffUserRoleCheck(userDAO, sessionId, dto.getEntity().getShopId(), StaffRoleOption.SHOP_POS_OPERATOR);
		customerDAO.save(dto.getEntity());
		return dto;
	}

	@RequestMapping(value = "/customerCreate", method = RequestMethod.POST)
	public EntityDTO<Customer> customerCreate(@CookieValue(value = "plt", required = false) final String sessionId,
			@RequestBody @Valid EntityDTO<Customer> dto) {
		Security.staffUserRoleCheck(userDAO, sessionId, dto.getEntity().getShopId(), StaffRoleOption.SHOP_POS_OPERATOR);
		dto.setEntity(customerDAO.save(dto.getEntity()));
		return dto;
	}

	@RequestMapping(value = "/ordySearchDUS", method = RequestMethod.POST)
	public OrdySearchResponse ordySearchDUS(@CookieValue(value = "dus", required = true) final String deviceSessionId,
			@RequestBody @Valid OrdySearchRequest r) {
		final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(deviceSessionId);
		if (StringUtils.isBlank(decryptedDeviceSessionId.getUserSessionId())) {
			return null;
		} else {
			r.setDeviceSessionId(decryptedDeviceSessionId.getUserSessionId());
			return ordyDAO.list(r);
		}
	}

	@RequestMapping(value = "/ordyInbox/{shopId}/{lastSearchStartTime}", method = RequestMethod.GET)
	public OrdySearchResponse ordyInbox(@CookieValue(value = "plt", required = true) final String sessionId,
			@PathVariable("shopId") final Integer shopId, @PathVariable("lastSearchStartTime") final Long lastSearchStartTime) {
		Security.staffUserRoleCheck(userDAO, sessionId, shopId, StaffRoleOption.SHOP_POS_OPERATOR);
		final OrdySearchRequest r = new OrdySearchRequest();
		r.setShopId(shopId);
		r.setOrdyStatus(OrdyStatus.NEWORDY);
		r.setOrderTimeFrom(new Date(lastSearchStartTime));
		final long searchStartTime = System.currentTimeMillis();
		final OrdySearchResponse result = ordyDAO.list(r);
		result.setSearchStartTime(searchStartTime);
		return result; 
	}

	@RequestMapping(value = "/ordySearch", method = RequestMethod.POST)
	public OrdySearchResponse ordySearch(@CookieValue(value = "plt", required = false) final String sessionId,
			@RequestBody @Valid OrdySearchRequest r) {
		Security.staffUserRoleCheck(userDAO, sessionId, r.getShopId(), StaffRoleOption.SHOP_POS_OPERATOR);
		if (null != r.getOrderTimeFrom()) {
			r.setOrderTimeFrom(DateUtils.truncate(r.getOrderTimeFrom(), Calendar.DATE));
		}
		return ordyDAO.list(r);
	}

	@RequestMapping(value = "/suburbSearch", method = RequestMethod.POST)
	public List<Suburb> suburbSearch(@RequestBody @Valid SuburbSearchRequest r) {
		return suburbDAO.findSuburbs(r.getCountryCode(), r.getPostcode(), LANGUAGE.getLanguageId());
	}

	@RequestMapping(value = "/menuServiceSuburbList/{menuId}", method = RequestMethod.GET)
	public List<Suburb> menuServiceSuburbList(@PathVariable("menuId") final Integer menuId) {
		return menuDAO.getMenuServiceSuburbList(menuId, LANGUAGE.getLanguageId());
	}

	@RequestMapping(value = "/businessServiceSuburbList", method = RequestMethod.GET)
	public List<Suburb> shopServiceSuburbList() {
		return menuDAO.getMenuServiceSuburbList(1, LANGUAGE.getLanguageId());  // TODO make business ID dynamic
	}

}
