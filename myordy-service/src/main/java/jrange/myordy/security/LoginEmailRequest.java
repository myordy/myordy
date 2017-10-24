package jrange.myordy.security;

import java.io.Serializable;

public final class LoginEmailRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;

	public String getEmail() {
		return email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(final String password) {
		this.password = password;
	}

}
