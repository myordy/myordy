package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="theuserloginemail")
public final class TheUserLoginEmail implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name="user_id", insertable=true)
    private Integer userId;

    @Column(name="login_email", nullable = false, unique = true)
    private String loginEmail;

    public Integer getUserId() {
		return userId;
	}

	public void setUserId(final Integer userId) {
		this.userId = userId;
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(final String loginEmail) {
		this.loginEmail = loginEmail;
	}

}
