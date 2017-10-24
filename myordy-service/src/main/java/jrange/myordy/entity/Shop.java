package jrange.myordy.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Entity
@Table(name="shop")
public final class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="shop_id")
    private Integer shopId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, updatable = false)
    private Business business;

//	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY)
//	private Set<Product> productList;
//
//	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY)
//	private Set<Category> categoryList;

	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="shop_menu", joinColumns=@JoinColumn(name="shop_id"), inverseJoinColumns=@JoinColumn(name="menu_id")) 
	private Set<Menu> menuList;

//	@JsonIgnore
//	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
//	private Set<MenuOrdyServiceSuburb> suburbConfigList;

    @Column(name="timezone", nullable = false)
	private String timezone;

    @Column(name="currency_code", nullable = false)
	private String currencyCode;

              @OneToOne (cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="the_user_address_id", nullable=false, insertable=true, updatable=true)
    private TheUserAddress address;

	@JsonIgnore
	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<ShopServerName> serverNameList;

	@JsonIgnore
	@OneToMany(mappedBy="shop", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<CustomerPayment> paymentList;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	@Transient
	private Set<LanguageTable> languageTables = new HashSet<LanguageTable>();

	public LanguageTable addLanguageTable(final Language language, final Set<Message> messageList) {
    	final ObjectMapper mapper = new ObjectMapper();
    	final ObjectNode ml = mapper.createObjectNode();
		final LanguageTable languageTable = new LanguageTable();
		languageTable.setLanguage(language);
		for (final Message m : messageList) {
			if (m.getLanguage().getLanguageId().equals(language.getLanguageId())) {
		    	ml.put(m.getCode(), m.getMessage());
			}
		}
    	languageTable.setLanguageTable(ml);
		if (null == languageTables) {
			languageTables = new HashSet<LanguageTable>();
		}
		languageTables.add(languageTable);
		return languageTable;
	}

	public Integer getShopId() {
		return shopId;
	}

	public Shop setShopId(final Integer shopId) {
		this.shopId = shopId;
		return this;
	}

//	public Set<Product> getProductList() {
//		return productList;
//	}
//
//	public void setProductList(final Set<Product> productList) {
//		this.productList = productList;
//	}
//
//	public Set<Category> getCategoryList() {
//		return categoryList;
//	}
//
//	public void setCategoryList(final Set<Category> categoryList) {
//		this.categoryList = categoryList;
//	}

	public Set<Menu> getMenuList() {
		return menuList;
	}

	public Shop setMenuList(final Set<Menu> menuList) {
		this.menuList = menuList;
		return this;
	}

	public Set<LanguageTable> getLanguageTables() {
		return languageTables;
	}

	public Shop setLanguageTables(final Set<LanguageTable> languageTables) {
		this.languageTables = languageTables;
		return this;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public Shop setStatus(final EntityStatus status) {
		this.status = status;
		return this;
	}

//	public String getShopName(final Language language) {
//		return getMessage(language, String.format("shopName%d", this.shopId));
//	}
//
//	private String getMessage(Language language, String code) {
//		System.out.println(code);
//		String altValue = null;
//		String result = null;
//		for (Message m : messageList) {
//			if (m.getCode().equals(code)) {
//				altValue = m.getMessage();
//				if (m.getLanguage().getLanguageId().equals(language.getLanguageId())) {
//					result = m.getMessage();
//					break;
//				}
//			}
//		}
//		if (null == result) {
//			result = altValue;
//		}
//		return result;
//	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(final Business business) {
		this.business = business;
	}

	public String toString() {
		return "Shop Id: " + this.shopId;
		//return ToStringBuilder.reflectionToString(this);
	}

//	public Set<MenuOrdyServiceSuburb> getSuburbConfigList() {
//		return suburbConfigList;
//	}
//
//	public void setSuburbConfigList(final Set<MenuOrdyServiceSuburb> suburbConfigList) {
//		this.suburbConfigList = suburbConfigList;
//	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(final String timezone) {
		this.timezone = timezone;
	}

	public TheUserAddress getAddress() {
		return address;
	}

	public void setAddress(final TheUserAddress address) {
		this.address = address;
	}

	public Set<ShopServerName> getServerNameList() {
		return serverNameList;
	}

	public void setServerNameList(final Set<ShopServerName> serverNameList) {
		this.serverNameList = serverNameList;
	}

	public Set<CustomerPayment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(final Set<CustomerPayment> paymentList) {
		this.paymentList = paymentList;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(final String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
