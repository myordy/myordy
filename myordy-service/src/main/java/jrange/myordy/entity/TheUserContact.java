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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

@Entity
@Table(name="theusercontact")
public final class TheUserContact implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="the_user_contact_id")
    private Integer theUserContactId;

    @Column(name="name", nullable = false)
	private String name;
    @Column(name="email", nullable = true)
    private String email;

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "the_user_address_id", nullable = true, updatable = true, insertable = true)
	@Where(clause="status != 'DELETED'")
	@Fetch(FetchMode.SUBSELECT)
	private Set<TheUserAddress> addressList;

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "the_user_phone_id", nullable = true, updatable = true, insertable = true)
	@Where(clause="status != 'DELETED'")
	@Fetch(FetchMode.SUBSELECT)
	private Set<TheUserPhone> phoneList;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "language_id", nullable = false, updatable = false)
//	private Language language = new Language().setLanguageId(1);

	@Column(name="language_id", nullable = false)
    private Integer languageId;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

    public void setSuburbOnAddresses() {
    	if (null != addressList) {
    		for (TheUserAddress theUserAddress : addressList) {
				theUserAddress.setSuburb(null);
    			if (null != theUserAddress.getSuburbId()) {
    				theUserAddress.setSuburb(new Suburb());
    				theUserAddress.getSuburb().setSuburbId(theUserAddress.getSuburbId());
    			}
    		}
    	}
    }

    public void setSuburbIdOnAddresses() {
    	if (null != addressList) {
    		for (TheUserAddress theUserAddress : addressList) {
				theUserAddress.setSuburbId(null);
    			if (null != theUserAddress.getSuburb()) {
    				theUserAddress.setSuburbId(theUserAddress.getSuburb().getSuburbId());
    			}
    		}
    	}
    }

    public Integer getTheUserContactId() {
		return theUserContactId;
	}

	public void setTheUserContactId(final Integer theUserContactId) {
		this.theUserContactId = theUserContactId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Set<TheUserAddress> getAddressList() {
		return addressList;
	}

	public void setAddressList(final Set<TheUserAddress> addressList) {
		this.addressList = addressList;
	}

	public Set<TheUserPhone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(final Set<TheUserPhone> phoneList) {
		this.phoneList = phoneList;
	}

//	public Language getLanguage() {
//		return language;
//	}
//
//	public void setLanguage(final Language language) {
//		this.language = language;
//	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(final Integer languageId) {
		this.languageId = languageId;
	}

}
