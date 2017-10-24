package jrange.myordy.entity;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="customer")
public final class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="customer_id")
    private Integer customerId;

	@Column(name="customer_number", nullable = true)
	private Integer customerNumber;

    @Column(name="shop_id")
    private Integer shopId;

	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="the_user_contact_id", nullable=false, insertable=true, updatable=true)
	private TheUserContact contact;

	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="user_id", nullable=true, insertable=false, updatable=false)
	private TheUser theUser;

	@Column(name="balance")
    private Integer balance;

	@JsonIgnore
	@OneToMany(mappedBy="customer", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<CustomerPayment> paymentList;

	@Column(name="note", nullable = true)
	private String note;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(final Integer customerId) {
		this.customerId = customerId;
	}

//	public Shop getShop() {
//		return shop;
//	}
//
//	public void setShop(final Shop shop) {
//		this.shop = shop;
//	}

	public TheUserContact getContact() {
		return contact;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}

	public void setContact(final TheUserContact contact) {
		this.contact = contact;
	}

	public TheUser getTheUser() {
		return theUser;
	}

	public void setTheUser(final TheUser theUser) {
		this.theUser = theUser;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(final Integer balance) {
		this.balance = balance;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}


	public Integer getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(final Integer customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public Set<CustomerPayment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(final Set<CustomerPayment> paymentList) {
		this.paymentList = paymentList;
	}

}
