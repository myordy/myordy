package jrange.myordy.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="category")
public final class Category implements Serializable {
    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="category_id")
    private Integer categoryId;

//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
//    private Shop shop;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, updatable = false)
    private Business business;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final Integer categoryId) {
		this.categoryId = categoryId;
	}

//	public Shop getShop() {
//		return shop;
//	}
//
//	public void setShop(final Shop shop) {
//		this.shop = shop;
//	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(final Business business) {
		this.business = business;
	}

}
