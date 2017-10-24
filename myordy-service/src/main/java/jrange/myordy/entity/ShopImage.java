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
@Table(name="shopimage")
public final class ShopImage implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="shop_image_id")
    private Integer shopImageId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @Column(name="image_code", nullable = true)
    private String imageCode;

    @Column(name="content_base64_binary", nullable = false, columnDefinition="TEXT")
    private String contentBase64Binary;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

    public Integer getShopImageId() {
		return shopImageId;
	}

	public void setShopImageId(final Integer shopImageId) {
		this.shopImageId = shopImageId;
	}

	public Shop getShop() {
		return shop;
	}

	public ShopImage setShop(final Shop shop) {
		this.shop = shop;
		return this;
	}

	public String getContentBase64Binary() {
		return contentBase64Binary;
	}

	public void setContentBase64Binary(final String contentBase64Binary) {
		this.contentBase64Binary = contentBase64Binary;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(final String imageCode) {
		this.imageCode = imageCode;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
