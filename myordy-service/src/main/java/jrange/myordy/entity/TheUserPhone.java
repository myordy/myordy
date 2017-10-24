package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="theuserphone")
public final class TheUserPhone implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="the_user_phone_id")
    private Integer theUserPhoneId;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

	@Column(name="phone_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TheUserPhoneType phoneType;
	
	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getTheUserPhoneId() {
		return theUserPhoneId;
	}

	public void setTheUserPhoneId(final Integer theUserPhoneId) {
		this.theUserPhoneId = theUserPhoneId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public TheUserPhoneType getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(final TheUserPhoneType phoneType) {
		this.phoneType = phoneType;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
