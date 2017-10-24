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
@Table(name="menuordyservicesuburb")
public final class MenuOrdyServiceSuburb implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_ordy_service_suburb_id")
    private Integer menuOrdyServiceSuburbId;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "suburb_id", nullable = false, updatable = false)
    private Suburb suburb;

    @Column(name="min_delivery_mins", nullable = true)
    private Integer minDeliveryMins;

    @Column(name="delivery_price", nullable = true)
    private Integer deliveryPrice;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_ordy_config_id", nullable = false, updatable = false)
    private MenuOrdyConfig menuOrdyConfig;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMenuOrdyServiceSuburbId() {
		return menuOrdyServiceSuburbId;
	}

	public void setMenuOrdyServiceSuburbId(final Integer menuOrdyServiceSuburbId) {
		this.menuOrdyServiceSuburbId = menuOrdyServiceSuburbId;
	}

	public Suburb getSuburb() {
		return suburb;
	}

	public void setSuburb(final Suburb suburb) {
		this.suburb = suburb;
	}

	public Integer getMinDeliveryMins() {
		return minDeliveryMins;
	}

	public void setMinDeliveryMins(final Integer minDeliveryMins) {
		this.minDeliveryMins = minDeliveryMins;
	}

	public Integer getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(final Integer deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public MenuOrdyConfig getMenuOrdyConfig() {
		return menuOrdyConfig;
	}

	public void setMenuOrdyConfig(final MenuOrdyConfig menuOrdyConfig) {
		this.menuOrdyConfig = menuOrdyConfig;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
