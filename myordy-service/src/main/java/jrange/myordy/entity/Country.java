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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="country")
public final class Country implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="country_id")
    private Integer countryId;

	@Column(name="code", nullable = false, unique = true)
	private String code;

	@OneToMany(mappedBy="country", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<State> stateList;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(final Integer countryId) {
		this.countryId = countryId;
	}

	public Set<State> getStateList() {
		return stateList;
	}

	public void setStateList(final Set<State> stateList) {
		this.stateList = stateList;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

}
