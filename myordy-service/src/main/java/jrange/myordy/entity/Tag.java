package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tag", uniqueConstraints={ @UniqueConstraint( columnNames = { "code", "shop_id" } ) })
public final class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="tag_id")
    private Integer tagId;

//	@JsonIgnore
	@Column(name="code", nullable = false)
	private String code;

	@JsonIgnore
	@Column(name="shop_id", nullable = true)
    private Integer shopId;

//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop_id", nullable = true, updatable = false)
//    private Shop shop;

//	public Shop getShop() {
//		return shop;
//	}
//
//	public void setShop(final Shop shop) {
//		this.shop = shop;
//	}

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(final Integer tagId) {
		this.tagId = tagId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
