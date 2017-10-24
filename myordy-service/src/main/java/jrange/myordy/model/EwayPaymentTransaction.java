package jrange.myordy.model;

import java.io.Serializable;

public final class EwayPaymentTransaction implements Serializable {

	private static final long serialVersionUID = 1L;

	private RapidClientConfig rapidClientConfig;

	private String customerName;
	private String customerPhoneNumber;
	private String customerDeviceIP;
	private String customerEmail;
	private Integer paymentTotalAmount;
	private String invoiceReference;		

	public RapidClientConfig getRapidClientConfig() {
		return rapidClientConfig;
	}

	public void setRapidClientConfig(final RapidClientConfig rapidClientConfig) {
		this.rapidClientConfig = rapidClientConfig;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(final String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(final String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getCustomerDeviceIP() {
		return customerDeviceIP;
	}

	public void setCustomerDeviceIP(final String customerDeviceIP) {
		this.customerDeviceIP = customerDeviceIP;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(final String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Integer getPaymentTotalAmount() {
		return paymentTotalAmount;
	}

	public void setPaymentTotalAmount(final Integer paymentTotalAmount) {
		this.paymentTotalAmount = paymentTotalAmount;
	}

	public String getInvoiceReference() {
		return invoiceReference;
	}

	public void setInvoiceReference(final String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public static final class EwayCreateTransactionResponse implements Serializable {
		private static final long serialVersionUID = 1L;
		private String sharedPaymentUrl;
		public String getSharedPaymentUrl() {
			return sharedPaymentUrl;
		}
		public void setSharedPaymentUrl(final String sharedPaymentUrl) {
			this.sharedPaymentUrl = sharedPaymentUrl;
		}
	}

	public static final class RapidClientConfig implements Serializable {
		private static final long serialVersionUID = 1L;
		private String apiKey;
		private String password;
		private String rapidEndPoint;
		public String getApiKey() {
			return apiKey;
		}
		public void setApiKey(final String apiKey) {
			this.apiKey = apiKey;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(final String password) {
			this.password = password;
		}
		public String getRapidEndPoint() {
			return rapidEndPoint;
		}
		public void setRapidEndPoint(final String rapidEndPoint) {
			this.rapidEndPoint = rapidEndPoint;
		}
	}

}
