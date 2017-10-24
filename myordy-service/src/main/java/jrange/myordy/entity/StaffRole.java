package jrange.myordy.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="staffrole")
public final class StaffRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="staff_role_id")
    private Integer staffRoleId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false, updatable=false)
	private TheUser theUser;

	@Column(name="staff_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private StaffRoleOption staffRole;

    @Column(name="start_time", nullable = true)
    private Date startTime;

    @Column(name="end_time", nullable = true)
    private Date endTime;

    public Integer getStaffRoleId() {
		return staffRoleId;
	}

	public void setStaffRoleId(final Integer staffRoleId) {
		this.staffRoleId = staffRoleId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(final Shop shop) {
		this.shop = shop;
	}

	public TheUser getTheUser() {
		return theUser;
	}

	public void setTheUser(final TheUser theUser) {
		this.theUser = theUser;
	}

	public StaffRoleOption getStaffRole() {
		return staffRole;
	}

	public void setStaffRole(final StaffRoleOption staffRole) {
		this.staffRole = staffRole;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

}
