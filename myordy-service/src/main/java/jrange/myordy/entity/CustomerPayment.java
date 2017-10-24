package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="customerpayment")
public final class CustomerPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="customer_payment_id")
    private Integer customerPaymentId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true, updatable = false)
    private Customer customer;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_id", nullable = true, updatable = false)
    private Ordy ordy;

	@Column(name="amount", nullable = true)
    private Long amount;

	@Column(name="currency_code", nullable = true)
	private String currencyCode;

    @Column(name="payment_type", nullable = true)
	@Enumerated(EnumType.STRING)
    PaymentType paymentType;

	@Column(name="transaction_ref", nullable = true)
	private String transactionRef;

	@Column(name="note", nullable = true)
	private String note;

    public Integer getCustomerPaymentId() {
		return customerPaymentId;
	}

	public void setCustomerPaymentId(final Integer customerPaymentId) {
		this.customerPaymentId = customerPaymentId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(final Shop shop) {
		this.shop = shop;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	public Ordy getOrdy() {
		return ordy;
	}

	public void setOrdy(final Ordy ordy) {
		this.ordy = ordy;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(final Long amount) {
		this.amount = amount;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(final PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(final String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(final String currencyCode) {
		this.currencyCode = currencyCode;
	}


}
