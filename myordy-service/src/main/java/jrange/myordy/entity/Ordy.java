package jrange.myordy.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ordy")
public final class Ordy implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="ordy_id")
    private Integer ordyId;

//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
//    private Shop shop;

//	@JsonIgnore
//	@Formula("shop_id")
    @Column(name="shop_id", nullable = false)
    private Integer shopId;

	//	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id", nullable = true, updatable = false)
    private Customer customer;

//	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "pos_operator_id", nullable = true, updatable = false)
    private POSOperator posOperator;

//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
//    private Menu menu;

//	@JsonIgnore
//	@Formula("menu_id")
    @Column(name="menu_id", nullable = false)
    private Integer menuId;

    @Column(name="amount", nullable = false)
    private Long amount;

    @Column(name="fixed_amount", nullable = true)
    private Float fixedAmount;

    @Column(name="discount_percent", nullable = true)
    private Float discountPercent;

    @Column(name="cashback", nullable = true)
    private Long cashback;

    @Column(name="note", nullable = true)
	private String note;

    @JsonIgnore
    @Column(name="device_session_id", nullable = true)
	private String deviceSessionId;

//    @JsonIgnore
    @OneToMany(mappedBy="ordy", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<CustomerPayment> paymentList;

//	@JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "language_id", nullable = false, updatable = false)
//    private Language language;

//	@JsonIgnore
//	@Formula("language_id")
    @Column(name="language_id", nullable = false)
    private Integer languageId;

    @Column(name="order_time", nullable = false)
    private Date orderTime;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	@OneToMany(mappedBy="ordy", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<OrdyItem> ordyItemList;

	@OneToMany(mappedBy="ordy", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<OrdyItemCombo> comboOrdyItemList;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_discount_coupon_id", nullable = true, updatable = false)
    private MenuDiscountCoupon menuDiscountCoupon;

	// TODO comments
	// TODO delivery suburb geocode

	@Column(name="ordy_number", nullable = true)
	private Integer ordyNumber;

	@Column(name="delivery_time", nullable = false)
    private Date deliveryTime;

	@Column(name="delivery_address", nullable = true)
	private String deliveryAddress;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_suburb_id", nullable = true, updatable = true)
    private Suburb deliverySuburb;

	@Column(name="delivery_charge")
    private Integer deliveryCharge;

	@Column(name="customer_name", nullable = true)
	private String customerName;

	@Column(name="customer_mobile_number", nullable = true)
	private String customerMobileNumber;

	@Column(name="customer_email", nullable = true)
	private String customerEmail;

	@Column(name="submission_ipaddress", nullable = true)
	private String submissionIPAddress;

    @Column(name="ordy_status", nullable = true)
	@Enumerated(EnumType.STRING)
	OrdyStatus ordyStatus;

    @Transient
    private String onlinePaymentStripeToken;

    @Transient
    private String onlinePaymentPaypalNonce;

//    @Column(name="audit_log", nullable = true)
//	private String auditLog;

//	@Column(name="discount_amount")
//    private Integer discountAmount;

// Ordy Status 		: Draft > New > Confirmed > In Progress > Ready for Delivery > Picked Up For Delivery > Delivered
//					: Cancelled
// CustomerPayment
		// Payment Mode 	: Cash | Credit Card | Bank Transfer | Cheque
		// Amount 	: 
		// Payment Ref 	: 
		// Received Date 	: 
		// Due Date 	: 
		// Note

// Ordy Log
//	// tax

	public Set<CustomerPayment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(final Set<CustomerPayment> paymentList) {
		this.paymentList = paymentList;
	}

	public OrdyStatus getOrdyStatus() {
		return ordyStatus;
	}

	public void setOrdyStatus(final OrdyStatus ordyStatus) {
		this.ordyStatus = ordyStatus;
	}

//	public String getAuditLog() {
//		return auditLog;
//	}
//
//	public void setAuditLog(final String auditLog) {
//		this.auditLog = auditLog;
//	}

	public Integer getMenuId() {
		return menuId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}

	public void setMenuId(final Integer menuId) {
		this.menuId = menuId;
	}

	public void setLanguageId(final Integer languageId) {
		this.languageId = languageId;
	}

	public Integer getOrdyId() {
		return ordyId;
	}

	public void setOrdyId(final Integer ordyId) {
		this.ordyId = ordyId;
	}

//	public Shop getShop() {
//		return shop;
//	}
//
//	public void setShop(final Shop shop) {
//		this.shop = shop;
//	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(final Long amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

//	public Language getLanguage() {
//		return language;
//	}
//
//	public void setLanguage(final Language language) {
//		this.language = language;
//	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(final Date orderTime) {
		this.orderTime = orderTime;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Set<OrdyItem> getOrdyItemList() {
		return ordyItemList;
	}

	public void setOrdyItemList(final Set<OrdyItem> ordyItemList) {
		this.ordyItemList = ordyItemList;
	}

//	public Menu getMenu() {
//		return menu;
//	}
//
//	public void setMenu(final Menu menu) {
//		this.menu = menu;
//	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	public POSOperator getPosOperator() {
		return posOperator;
	}

	public void setPosOperator(final POSOperator posOperator) {
		this.posOperator = posOperator;
	}

	public Set<OrdyItemCombo> getComboOrdyItemList() {
		return comboOrdyItemList;
	}

	public void setComboOrdyItemList(final Set<OrdyItemCombo> comboOrdyItemList) {
		this.comboOrdyItemList = comboOrdyItemList;
	}

	public Integer getOrdyNumber() {
		return ordyNumber;
	}

	public void setOrdyNumber(final Integer ordyNumber) {
		this.ordyNumber = ordyNumber;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(final Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(final String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Suburb getDeliverySuburb() {
		return deliverySuburb;
	}

	public void setDeliverySuburb(final Suburb deliverySuburb) {
		this.deliverySuburb = deliverySuburb;
	}

	public Integer getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(final Integer deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public Float getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(final Float fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public Float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(final Float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getDeviceSessionId() {
		return deviceSessionId;
	}

	public void setDeviceSessionId(final String deviceSessionId) {
		this.deviceSessionId = deviceSessionId;
	}

	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(final String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(final String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(final String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getSubmissionIPAddress() {
		return submissionIPAddress;
	}

	public void setSubmissionIPAddress(final String submissionIPAddress) {
		this.submissionIPAddress = submissionIPAddress;
	}

	public String getOnlinePaymentStripeToken() {
		return onlinePaymentStripeToken;
	}

	public void setOnlinePaymentStripeToken(final String onlinePaymentStripeToken) {
		this.onlinePaymentStripeToken = onlinePaymentStripeToken;
	}

	public Long getCashback() {
		return cashback;
	}

	public void setCashback(final Long cashback) {
		this.cashback = cashback;
	}

    public MenuDiscountCoupon getMenuDiscountCoupon() {
		return menuDiscountCoupon;
	}

	public void setMenuDiscountCoupon(final MenuDiscountCoupon menuDiscountCoupon) {
		this.menuDiscountCoupon = menuDiscountCoupon;
	}

	public String getOnlinePaymentPaypalNonce() {
		return onlinePaymentPaypalNonce;
	}

	public void setOnlinePaymentPaypalNonce(final String onlinePaymentPaypalNonce) {
		this.onlinePaymentPaypalNonce = onlinePaymentPaypalNonce;
	}

}
