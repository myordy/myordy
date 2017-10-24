package jrange.myordy.v1.shop.restaurant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="5_shop_order_restaurant")
public final class OrderDetailsVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="id")
    private Long id;
	@Column(name="order_details_html")
	@Lob
    private byte[] orderDetailsHtml;	
	@Column(name="user_id_fk")
    private Long userId;
	@Column(name="shop_id_fk")
    private Long shopId;

	public Long getId() {
		return id;
	}
	public void setId(final Long id) {
		this.id = id;
	}
	public byte[] getOrderDetailsHtml() {
		return orderDetailsHtml;
	}
	public void setOrderDetailsHtml(final byte[] orderDetailsHtml) {
		this.orderDetailsHtml = orderDetailsHtml;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(final Long shopId) {
		this.shopId = shopId;
	}

}
