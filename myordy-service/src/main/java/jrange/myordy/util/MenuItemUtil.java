package jrange.myordy.util;

import java.util.ArrayList;
import java.util.Set;

import jrange.myordy.entity.MenuItem;

public final class MenuItemUtil {

	public static ArrayList<MenuItem> sortMenuItemList(Set<MenuItem> menuItemList) {
		ArrayList<MenuItem> result = new ArrayList<MenuItem>();
		MenuItem menuItemTemp = findFirstMenuItem(menuItemList);
		while (null != menuItemTemp) {
			result.add(menuItemTemp);
			menuItemTemp = findNextMenuItem(menuItemList, menuItemTemp);
		}
		return result;
	}

	private static MenuItem findNextMenuItem(Set<MenuItem> menuItemList, MenuItem thisMenuItem) {
		MenuItem result = null;
		for (MenuItem mi : menuItemList) {
			if (thisMenuItem.getMenuItemId().equals(mi.getPreviousMenuItemId())) {
				result = mi;
				break;
			}
		}
		return result;
	}

	private static MenuItem findFirstMenuItem(Set<MenuItem> menuItemList) {
		MenuItem result = null;
		if (null != menuItemList) {
			for (MenuItem mi : menuItemList) {
				if (null == mi.getPreviousMenuItemId()) {
					result = mi;
					break;
				}
			}
		}
		return result;
	}

}
