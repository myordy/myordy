package jrange.myordy.entity.list;

import java.io.Serializable;

public final class SuburbSearchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String countryCode;
	private String postcode;

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

}
