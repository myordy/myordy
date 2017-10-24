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

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
@Entity
@Table(name="theuser")
public class TheUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="user_id")
    private Integer userId;

	@JsonIgnore
    @Column(name="password_hash")
    private String passwordHash;

	@JsonIgnore
    @Column(name="session_id")
    private String sessionId;

	@JsonIgnore
	@Column(name="import_ref")
    private String importRef;

	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="the_user_contact_id", nullable=false, insertable=true, updatable=true)
	private TheUserContact contact;

	@OneToMany(mappedBy="theUser", fetch = FetchType.EAGER)
	private Set<StaffRole> staffRoleList;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

    public EntityStatus getStatus() {
		return status;
	}
	public void setStatus(final EntityStatus status) {
		this.status = status;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(final Integer userId) {
		this.userId = userId;
	}
	public TheUserContact getContact() {
		return contact;
	}
	public void setContact(final TheUserContact contact) {
		this.contact = contact;
	}

	public Language getLanguage() {
		// TODO make this dynamic
		return new Language().setLanguageId(1);
	}

	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(final String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getImportRef() {
		return importRef;
	}
	public void setImportRef(final String importRef) {
		this.importRef = importRef;
	}
	public Set<StaffRole> getStaffRoleList() {
		return staffRoleList;
	}
	public void setStaffRoleList(final Set<StaffRole> staffRoleList) {
		this.staffRoleList = staffRoleList;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

}
