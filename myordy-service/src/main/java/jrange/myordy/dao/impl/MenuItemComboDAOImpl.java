package jrange.myordy.dao.impl;

import javax.transaction.Transactional;

import jrange.myordy.dao.MenuItemComboDAO;
import jrange.myordy.entity.MenuItemCombo;
import jrange.myordy.entity.MenuItemComboOption;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class MenuItemComboDAOImpl extends HibernateDAOImpl implements MenuItemComboDAO {

	@Override
	public MenuItemCombo save(MenuItemCombo menuItemComboArg) {
		MenuItemCombo menuItemCombo = menuItemComboArg;
		if (menuItemCombo.getMenuItemComboId() != null) {
//			tag.setCode(tagArg.getCode());
//			tag.setShopId(tagArg.getShopId());
		}
		getCurrentSession().saveOrUpdate(menuItemCombo);
		return menuItemCombo;
	}

	@Override
	public MenuItemCombo get(Integer id) {
		final MenuItemCombo result = (MenuItemCombo) getCurrentSession().get(MenuItemCombo.class, id);
		return result;
	}

	@Override
	public MenuItemCombo getByCode(String code, Integer businessId) {
		final Criteria criteria = getCurrentSession().createCriteria(MenuItemCombo.class)
				.add(Restrictions.eq("code", code));

		Criteria menuCriteria = criteria.createCriteria("menu");
		Criteria businessCriteria = menuCriteria.createCriteria("business");
		businessCriteria.add(Restrictions.eq("businessId", businessId));

		MenuItemCombo result = (MenuItemCombo) criteria.uniqueResult();;
		result = (MenuItemCombo) criteria.uniqueResult();
		return result;
	}

	@Override
	public MenuItemComboOption saveMenuItemComboOption(final MenuItemComboOption menuItemComboOption) {
		getCurrentSession().saveOrUpdate(menuItemComboOption);
		return menuItemComboOption;
	}

}
