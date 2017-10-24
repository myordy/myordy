package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jrange.myordy.entity.menuitemoption.ExtraOptionItem;

@Entity
@Table(name="ordyitemextraoptionadd")
public final class OrdyItemExtraOptionAdd implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="ordyitemextraoptionadd_id", insertable=false, updatable=false)
    private Integer ordyItemExtraOptionAddId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extraoptionitem_id", nullable = false, updatable = false)
    private ExtraOptionItem option;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_item_extra_options_id", nullable = false, updatable = false)
	private OrdyItemExtraOption ordyItemExtraOption;

	@Column(name="price")
    private Integer price;

    @Column(name="amount")
    private Integer amount;

    @Column(name="qty")
    private Integer qty;

	public Integer getOrdyItemExtraOptionAddId() {
		return ordyItemExtraOptionAddId;
	}

	public void setOrdyItemExtraOptionAddId(final Integer ordyItemExtraOptionAddId) {
		this.ordyItemExtraOptionAddId = ordyItemExtraOptionAddId;
	}

	public ExtraOptionItem getOption() {
		return option;
	}

	public void setOption(final ExtraOptionItem option) {
		this.option = option;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(final Integer price) {
		this.price = price;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(final Integer amount) {
		this.amount = amount;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(final Integer qty) {
		this.qty = qty;
	}

	public OrdyItemExtraOption getOrdyItemExtraOption() {
		return ordyItemExtraOption;
	}

	public void setOrdyItemExtraOption(final OrdyItemExtraOption ordyItemExtraOption) {
		this.ordyItemExtraOption = ordyItemExtraOption;
	}


}
