package jrange.myordy.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="ordyitemextraoption")
public final class OrdyItemExtraOption implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="ordy_item_extra_options_id")
    private Integer ordyItemExtraOptionsId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_item_id", nullable = false, updatable = false)
    private OrdyItem ordyItem;

	@OneToMany(mappedBy="ordyItemExtraOption", fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<OrdyItemExtraOptionAdd> addOptions;

	@OneToMany(mappedBy="ordyItemExtraOption", fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<OrdyItemExtraOptionRemove> removeOptions;

	public Integer getOrdyItemExtraOptionsId() {
		return ordyItemExtraOptionsId;
	}
	public void setOrdyItemExtraOptionsId(final Integer ordyItemExtraOptionsId) {
		this.ordyItemExtraOptionsId = ordyItemExtraOptionsId;
	}
	public OrdyItem getOrdyItem() {
		return ordyItem;
	}
	public void setOrdyItem(final OrdyItem ordyItem) {
		this.ordyItem = ordyItem;
	}
	public Set<OrdyItemExtraOptionAdd> getAddOptions() {
		return addOptions;
	}
	public void setAddOptions(final Set<OrdyItemExtraOptionAdd> addOptions) {
		this.addOptions = addOptions;
	}
	public Set<OrdyItemExtraOptionRemove> getRemoveOptions() {
		return removeOptions;
	}
	public void setRemoveOptions(final Set<OrdyItemExtraOptionRemove> removeOptions) {
		this.removeOptions = removeOptions;
	}

}
