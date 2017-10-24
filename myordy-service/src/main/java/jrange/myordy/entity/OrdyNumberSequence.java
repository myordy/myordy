package jrange.myordy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ordynumbersequence")
public class OrdyNumberSequence {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name="shop_id")
    private Integer shopId;

    @Column(name="last_ordy_number")
    private Integer lastOrdyNumber;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getLastOrdyNumber() {
		return lastOrdyNumber;
	}

	public void setLastOrdyNumber(final Integer lastOrdyNumber) {
		this.lastOrdyNumber = lastOrdyNumber;
	}

}
