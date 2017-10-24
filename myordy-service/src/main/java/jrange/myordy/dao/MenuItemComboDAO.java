package jrange.myordy.dao;

import jrange.myordy.entity.MenuItemCombo;
import jrange.myordy.entity.MenuItemComboOption;

public interface MenuItemComboDAO {

	public MenuItemCombo save(MenuItemCombo menuItemCombo);

	public MenuItemCombo get(final Integer id);

	public MenuItemCombo getByCode(final String code, Integer businessId);

	public MenuItemComboOption saveMenuItemComboOption(MenuItemComboOption menuItemComboOption);

}
