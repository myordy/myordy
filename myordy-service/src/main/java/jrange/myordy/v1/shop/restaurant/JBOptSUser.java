package jrange.myordy.v1.shop.restaurant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="the_user")
public class JBOptSUser implements Serializable {

    public static final byte STATUS_DELETED = 0;
    public static final byte STATUS_ACTIVE = 1;
    public static final byte STATUS_MUST_CHANGE_PASSWORD = 2;

	@GeneratedValue
    @Id
	@Column(name="id")
    private Long id;
	@Column(name="email")
    private String email;
	@Column(name="status")
    private Short status;
	@Column(name="name")
    private String name;
	@Column(name="nickname")
    private String nickname;
	@Column(name="password_hash")
    private String passwordHash;
	public Long getId() {
		return id;
	}
	public void setId(final Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(final Short status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(final String nickname) {
		this.nickname = nickname;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(final String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
