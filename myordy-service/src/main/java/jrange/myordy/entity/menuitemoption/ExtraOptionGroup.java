package jrange.myordy.entity.menuitemoption;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import jrange.myordy.entity.Business;
import jrange.myordy.entity.EntityStatus;

@Entity
@Table(name="extraoptiongroup")
public final class ExtraOptionGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="extraoptiongroup_id", insertable=false, updatable=false)
    private Integer extraOptionGroupId;

	@Column(name="code", nullable = false)
	private String code;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, updatable = false)
    private Business business;

	@OneToMany(mappedBy="extraOptionGroup", fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<ExtraOptionItem> extraOptionItemList;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getExtraOptionGroupId() {
		return extraOptionGroupId;
	}

	public void setExtraOptionGroupId(final Integer extraOptionGroupId) {
		this.extraOptionGroupId = extraOptionGroupId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(final Business business) {
		this.business = business;
	}

	public Set<ExtraOptionItem> getExtraOptionItemList() {
		return extraOptionItemList;
	}

	public void setExtraOptionItemList(final Set<ExtraOptionItem> extraOptionItemList) {
		this.extraOptionItemList = extraOptionItemList;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
