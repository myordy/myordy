package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.menuitemoption.ExtraOptionMenuItemConfig;

public interface MenuItemDAO {
    public MenuItem save(MenuItem menuItem);
    public MenuItem saveExtraOptionConfig(final Integer id, final ExtraOptionMenuItemConfig extraOptionConfig);
	public MenuItem get(final Integer id);
    public List<MenuItem> list();
	public List<MenuItem> getByTag(final Integer businessId, final String tag);
}
