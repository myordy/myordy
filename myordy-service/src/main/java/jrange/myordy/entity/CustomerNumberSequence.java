package jrange.myordy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customernumbersequence")
public class CustomerNumberSequence {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name="shop_id")
    private Integer shopId;

    @Column(name="last_customer_number")
    private Integer lastCustomerNumber;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getLastCustomerNumber() {
		return lastCustomerNumber;
	}

	public void setLastCustomerNumber(final Integer lastCustomerNumber) {
		this.lastCustomerNumber = lastCustomerNumber;
	}

}
