package jrange.myordy.entity.menuitemoption;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="extraoptionmenuitemconfig")
public final class ExtraOptionMenuItemConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="extra_option_menu_item_config_id")
    private Integer extraOptionMenuItemConfigId;

	@Column(name="extra_options_group_code", nullable = false)
	private String extraOptionsGroupCode;

//	@Column(name="remove_options_group_code", nullable = false)
//	private String removeOptionsGroupCode;

	public Integer getExtraOptionMenuItemConfigId() {
		return extraOptionMenuItemConfigId;
	}

	public void setExtraOptionMenuItemConfigId(final Integer extraOptionMenuItemConfigId) {
		this.extraOptionMenuItemConfigId = extraOptionMenuItemConfigId;
	}

	public String getExtraOptionsGroupCode() {
		return extraOptionsGroupCode;
	}

	public void setExtraOptionsGroupCode(final String extraOptionsGroupCode) {
		this.extraOptionsGroupCode = extraOptionsGroupCode;
	}

//	public String getAddOptionsGroupCode() {
//		return addOptionsGroupCode;
//	}
//
//	public void setAddOptionsGroupCode(final String addOptionsGroupCode) {
//		this.addOptionsGroupCode = addOptionsGroupCode;
//	}

//	public String getRemoveOptionsGroupCode() {
//		return removeOptionsGroupCode;
//	}
//
//	public void setRemoveOptionsGroupCode(final String removeOptionsGroupCode) {
//		this.removeOptionsGroupCode = removeOptionsGroupCode;
//	}

}
