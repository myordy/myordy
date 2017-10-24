package jrange.myordy.posadmin;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import jrange.myordy.dao.BusinessDAO;
import jrange.myordy.dao.CategoryDAO;
import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.MenuItemDAO;
import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.dao.ProductDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.dao.UserDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.Category;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Product;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.StaffRoleOption;
import jrange.myordy.entity.TheUser;
import jrange.myordy.entity.list.OrdySearchRequest;
import jrange.myordy.entity.list.OrdySearchResponse;
import jrange.myordy.entity.list.SalesSummaryReportRequest;
import jrange.myordy.entity.list.SalesSummaryReportResponse;
import jrange.myordy.entity.list.SalesSummaryReportResponse.SalesSummaryReportListItem;
import jrange.myordy.model.EntityDTO;
import jrange.myordy.model.ReferenceData;
import jrange.myordy.security.LoginEmailRequest;
import jrange.myordy.security.LoginResponse;
import jrange.myordy.util.Security;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posadmin-rest")
public class POSAdminRESTController {

	@Autowired
	ShopDAO shopDAO;

	@Autowired
	BusinessDAO businessDAO;

	@Autowired
	OrdyDAO ordyDAO;

	@Autowired
	private TheUser user;

	@Autowired
	private UserDAO userDAO;

	@RequestMapping(value = "/restoreUserSession", method = RequestMethod.GET)
	public LoginResponse restoreUserSession(@CookieValue(value = "plt", required = false) final String sesisonId) {
		return userDAO.restoreUserSession(sesisonId);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponse login(@RequestBody @Valid LoginEmailRequest loginEmailRequest) {
		return userDAO.login(loginEmailRequest.getEmail(), loginEmailRequest.getPassword());
	}

	@RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
	public Shop getShop(@PathVariable("id") int id) {
		Shop shop = shopDAO.getLiteNoMenu(id);
		shop.addLanguageTable(user.getLanguage(), shop.getBusiness().getMessageList());
		shop.setMenuList(new HashSet<Menu>());
		return shop;
	}

	@RequestMapping(value = "/salesSummaryReport", method = RequestMethod.POST)
	public SalesSummaryReportResponse salesSummaryReport(@RequestBody @Valid SalesSummaryReportRequest r,
			@CookieValue(value = "plt", required = true) final String sessionId) {
		r.setShopId(1);  // TODO make this dynamic
		Security.staffUserRoleCheck(userDAO, sessionId, r.getShopId(), StaffRoleOption.SHOP_MANAGER);
		if (null != r.getOrderDates()) {
			if (null != r.getOrderDates().getStartDate()) {
				r.getOrderDates().setStartDate(DateUtils.truncate(r.getOrderDates().getStartDate(), Calendar.DATE));
			}
			if (null != r.getOrderDates().getEndDate()) {
				r.getOrderDates().setEndDate(DateUtils.ceiling(r.getOrderDates().getEndDate(), Calendar.DATE));
			}
		}
		return ordyDAO.salesSummaryReport(r);
	}

/*
	@Autowired
	BusinessDAO businessDAO;

	@Autowired
	ShopDAO shopDAO;

	@Autowired
	MessageDAO messageDAO;

	@Autowired
	ProductDAO productDAO;

	@Autowired
	MenuDAO menuDAO;

	@Autowired
    CategoryDAO categoryDAO;

	@Autowired
	MenuItemDAO menuItemDAO;

//	@Autowired
//	LanguageDAO languageDAO;

	@Autowired
	private TheUser user;

//	private final ReferenceData refData = new ReferenceData().setEntityStatusList(EntityStatus.values());

	@RequestMapping(value = "/referenceData", method = RequestMethod.GET)
	public ReferenceData getReferenceData() {
		return ReferenceData.INSTANCE;
	}

	@RequestMapping(value = "/shop", method = RequestMethod.POST)
	public EntityDTO<Shop> createShop(@RequestBody @Valid EntityDTO<Shop> shopDTO) {
		// TODO make business id dynamic
		Business business = businessDAO.get(1);
		shopDTO.getEntity().setBusiness(business);
		Shop savedShop = shopDAO.save(shopDTO.getEntity());
		business.setMessageList(new HashSet<Message>());
		savedShop.addLanguageTable(user.getLanguage(), business.getMessageList());
		shopDTO.setEntity(savedShop);
		return shopDTO;
	}

	@RequestMapping(value = "/shop", method = RequestMethod.GET)
	public List<Shop> listShop() {
		return shopDAO.listLite(user.getLanguage());
	}

	@RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
	public Shop getShop(@PathVariable("id") int id) {
		Shop shop = shopDAO.get(id);
		shop.addLanguageTable(user.getLanguage(), shop.getBusiness().getMessageList());
		return shop;
	}

	@RequestMapping(value = "/shop", method = RequestMethod.PUT)
	public EntityDTO<Shop> updateShop(@RequestBody @Valid EntityDTO<Shop> shopDTO) {
		Integer businessId = shopDAO.getLite(shopDTO.getEntity().getShopId()).getBusiness().getBusinessId();
		messageDAO.updateMessageByCode(businessId, shopDTO.getLanguageId(), shopDTO.getMessages());
		shopDAO.save(shopDTO.getEntity());
		return shopDTO;
	}

	@RequestMapping(value = "/menu/{shopId}", method = RequestMethod.PUT)
	public EntityDTO<Menu> updateMenu(@PathVariable("shopId") int shopId, @RequestBody @Valid EntityDTO<Menu> menuDTO) {
		Integer businessId = shopDAO.getLite(shopId).getBusiness().getBusinessId();
		messageDAO.updateMessageByCode(businessId, menuDTO.getLanguageId(), menuDTO.getMessages());
		menuDTO.setEntity(menuDAO.save(menuDTO.getEntity()));
		return menuDTO;
	}

	@RequestMapping(value = "/menu/{shopId}", method = RequestMethod.POST)
	public EntityDTO<Menu> createMenu(@PathVariable("shopId") int shopId, @RequestBody @Valid EntityDTO<Menu> menuDTO) {
		// TODO make business id dynamic
		Business business = businessDAO.get(1);
		Shop shop = shopDAO.getLite(shopId);
		menuDTO.getEntity().setBusiness(business);
		menuDTO.setEntity(menuDAO.save(menuDTO.getEntity()));
		return menuDTO;
	}

	@RequestMapping(value = "/category/{shopId}", method = RequestMethod.POST)
	public EntityDTO<Category> createCategory(@PathVariable("shopId") int shopId, @RequestBody @Valid EntityDTO<Category> dto) {
		// TODO make business id dynamic
		Business business = businessDAO.get(1);
		Shop shop = shopDAO.getLite(shopId);
		dto.getEntity().setBusiness(business);
		dto.setEntity(categoryDAO.save(dto.getEntity()));
		return dto;
	}

	@RequestMapping(value = "/category/{shopId}", method = RequestMethod.PUT)
	public EntityDTO<Category> updateCategory(@PathVariable("shopId") int shopId, @RequestBody @Valid EntityDTO<Category> dto) {
		Integer businessId = shopDAO.getLite(shopId).getBusiness().getBusinessId();
		messageDAO.updateMessageByCode(businessId, dto.getLanguageId(), dto.getMessages());
		dto.setEntity(categoryDAO.save(dto.getEntity()));
		return dto;
	}

	@RequestMapping(value = "/product/{shopId}", method = RequestMethod.POST)
	public EntityDTO<Product> createProduct(@PathVariable("shopId") int shopId, @RequestBody @Valid EntityDTO<Product> dto) {
		// TODO make business id dynamic
		Business business = businessDAO.get(1);
		Shop shop = shopDAO.getLite(shopId);
		dto.getEntity().setBusiness(business);
		dto.setEntity(productDAO.save(dto.getEntity()));
		return dto;
	}

	@RequestMapping(value = "/product/{shopId}", method = RequestMethod.PUT)
	public EntityDTO<Product> updateProduct(@PathVariable("shopId") int shopId, @RequestBody @Valid EntityDTO<Product> dto) {
		Integer businessId = shopDAO.getLite(shopId).getBusiness().getBusinessId();
		messageDAO.updateMessageByCode(businessId, dto.getLanguageId(), dto.getMessages());
		dto.setEntity(productDAO.save(dto.getEntity()));
		return dto;
	}

	@RequestMapping(value = "/menu-item", method = RequestMethod.POST)
	public EntityDTO<MenuItem> createMenuItem(@RequestBody @Valid EntityDTO<MenuItem> dto) {
		return saveMenuItem(dto);
	}

	@RequestMapping(value = "/menu-item", method = RequestMethod.PUT)
	public EntityDTO<MenuItem> updateMenuItem(@RequestBody @Valid EntityDTO<MenuItem> dto) {
		return saveMenuItem(dto);
	}

	private EntityDTO<MenuItem> saveMenuItem(EntityDTO<MenuItem> dto) {
		if (null != dto.getRelatedEntityRef()) {
			final Integer parentMenuItemId = dto.getRelatedEntityRef().get("parentMenuItemId");
			if (null != parentMenuItemId) {
				dto.getEntity().setParentMenuItem(menuItemDAO.get(parentMenuItemId));
			}
			final Integer menuId = dto.getRelatedEntityRef().get("menuId");
			if (null != menuId) {
				dto.getEntity().setMenu(menuDAO.get(menuId));
			}
		}
		if (null != dto.getEntity().getCategory()) {
			dto.getEntity().setCategory(categoryDAO.get(dto.getEntity().getCategory().getCategoryId()));
		}
		if (null != dto.getEntity().getProduct()) {
			dto.getEntity().setProduct(productDAO.get(dto.getEntity().getProduct().getProductId()));
		}
		
		dto.setEntity(menuItemDAO.save(dto.getEntity()));
		return dto;
	}
*/
}
