package jrange.myordy.dao.impl;

import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.MenuDAO;
import jrange.myordy.dao.MenuItemDAO;
import jrange.myordy.entity.MenuItem;
import jrange.myordy.entity.Tag;
import jrange.myordy.entity.menuitemoption.ExtraOptionMenuItemConfig;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class MenuItemDAOImpl extends HibernateDAOImpl implements MenuItemDAO {

	@Autowired
	MenuDAO menuDAO;

	@Override
	public MenuItem saveExtraOptionConfig(final Integer id, final ExtraOptionMenuItemConfig extraOptionConfig) {
		MenuItem menuItem = get(id);
		menuItem.setExtraOptionConfig(extraOptionConfig);
		getCurrentSession().saveOrUpdate(menuItem);
		return menuItem;
	}

	@Override
	public MenuItem save(MenuItem menuItemArg) {
		MenuItem menuItem = menuItemArg;
		if (menuItemArg.getMenuItemId() != null) {
			menuItem = get(menuItemArg.getMenuItemId());
			menuItem.setMenu(menuItemArg.getMenu());
			menuItem.setPreviousMenuItemId(menuItemArg.getPreviousMenuItemId());
//			menuItem.setMenuItemList(menuItemArg.getMenuItemList());
			menuItem.setParentMenuItem(menuItemArg.getParentMenuItem());
			menuItem.setCategory(menuItemArg.getCategory());
//			menuItem.setProduct(menuItemArg.getProduct());
			if (menuItemArg.getTagList() == null) {
				menuItemArg.setTagList(new HashSet<Tag>());
			}
			menuItem.setTagList(menuItemArg.getTagList());
		}

		getCurrentSession().saveOrUpdate(menuItem);
		updateMenuItemOrder(menuItem);
		//printAll();
		return menuItem;
	}

	private void updateMenuItemOrder(final MenuItem menuItem) {
		Criteria criteria = getCurrentSession().createCriteria(MenuItem.class)
				.add(Restrictions.ne("menuItemId", menuItem.getMenuItemId()));

		if (null == menuItem.getPreviousMenuItemId()) {
			criteria.add(Restrictions.isNull("previousMenuItemId"));
		} else {
			criteria.add(Restrictions.eq("previousMenuItemId", menuItem.getPreviousMenuItemId()));
		}

		if (null == menuItem.getMenu()) {
			criteria.add(Restrictions.isNull("menu"));
		} else {
			criteria.add(Restrictions.eq("menu.menuId", menuItem.getMenu().getMenuId()));
		}

		if (null == menuItem.getParentMenuItem()) {
			criteria.add(Restrictions.isNull("parentMenuItem"));
		} else {
			criteria.add(Restrictions.eq("parentMenuItem.menuItemId", menuItem.getParentMenuItem().getMenuItemId()));
		}

		@SuppressWarnings("unchecked")
		List<MenuItem> siblingItems = criteria.list();
		MenuItem previousMenuItem = menuItem;
		for (MenuItem item : siblingItems) {
			item.setPreviousMenuItemId(previousMenuItem.getMenuItemId());
			item = save(item);
			previousMenuItem = item;
		}
	}

	@Override
	public MenuItem get(final Integer id) {
		final MenuItem result = (MenuItem) getCurrentSession().get(MenuItem.class, id);
		Hibernate.initialize(result.getCategory());
		Hibernate.initialize(result.getMenu());
		Hibernate.initialize(result.getMenuItemList());
		Hibernate.initialize(result.getParentMenuItem());
		Hibernate.initialize(result.getTagList());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuItem> getByTag(final Integer businessId, final String tag) {
		Session currentSession = getCurrentSession();
		final Criteria criteria = currentSession.createCriteria(MenuItem.class);
		criteria.createCriteria("tagList").add(Restrictions.ilike("code", tag, MatchMode.EXACT));
		Criteria businessCriteria = criteria.createCriteria("business");
		businessCriteria.add(Restrictions.eq("businessId", businessId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuItem> list() {
		return getCurrentSession().createQuery("from menuitem").list();
	}

	private void printAll() {
		List<MenuItem> list = list();
		for (MenuItem menuItem : list) {
			menuItem = get(menuItem.getMenuItemId());
			System.out.println(String.format("%s | %s | %s | %s", menuItem.getMenuItemId(), menuItem.getPreviousMenuItemId(), menuItem.getMenu(), menuItem.getParentMenuItem()));
		}
	}

}
