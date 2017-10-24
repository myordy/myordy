package jrange.myordy.security;

import java.io.Serializable;

import jrange.myordy.entity.TheUser;

public final class LoginResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private TheUser user;

	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

	public TheUser getUser() {
		return user;
	}

	public void setUser(final TheUser user) {
		this.user = user;
	}

}
