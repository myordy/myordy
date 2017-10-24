package jrange.myordy.entity.menuitemoption;

import java.io.Serializable;

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

import jrange.myordy.entity.EntityStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="extraoptionitem")
public final class ExtraOptionItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="extraoptionitem_id", insertable=false, updatable=false)
    private Integer extraOptionItemId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extra_option_group_id", nullable = false, updatable = false)
    private ExtraOptionGroup extraOptionGroup;

	@Column(name="add_price", nullable = true)
	private Integer addPrice;

	@Column(name="remove_price", nullable = true)
	private Integer removePrice;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getExtraOptionItemId() {
		return extraOptionItemId;
	}

	public void setExtraOptionItemId(final Integer extraOptionItemId) {
		this.extraOptionItemId = extraOptionItemId;
	}

	public ExtraOptionGroup getExtraOptionGroup() {
		return extraOptionGroup;
	}

	public void setExtraOptionGroup(final ExtraOptionGroup extraOptionGroup) {
		this.extraOptionGroup = extraOptionGroup;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Integer getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(final Integer addPrice) {
		this.addPrice = addPrice;
	}

	public Integer getRemovePrice() {
		return removePrice;
	}

	public void setRemovePrice(final Integer removePrice) {
		this.removePrice = removePrice;
	}

}
