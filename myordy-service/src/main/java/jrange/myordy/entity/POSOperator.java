package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name="posoperator",
	uniqueConstraints=@UniqueConstraint(columnNames={"shop_id", "code"})
)
public final class POSOperator implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="pos_operator_id")
    private Integer posOperatorId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

	@Column(name="code", nullable = false)
    private String code;

	@Column(name="name", nullable = false)
    private String name;

	public Integer getPosOperatorId() {
		return posOperatorId;
	}

	public void setPosOperatorId(Integer posOperatorId) {
		this.posOperatorId = posOperatorId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
