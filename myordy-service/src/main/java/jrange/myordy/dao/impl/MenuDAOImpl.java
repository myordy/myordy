package jrange.myordy.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.MenuDAO;
import jrange.myordy.entity.Customer;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.MenuDiscountCoupon;
import jrange.myordy.entity.Suburb;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class MenuDAOImpl extends HibernateDAOImpl implements MenuDAO {

	@Override
	public Menu save(Menu menuArg) {
		Menu menu = menuArg;
		if (menuArg.getMenuId() != null) {
			menu = get(menuArg.getMenuId());
			menu.setMenuOrdyConfig(menuArg.getMenuOrdyConfig());
			menu.setStatus(menuArg.getStatus());
		}
		getCurrentSession().saveOrUpdate(menu);
		return menu;
	}

	@Override
	public Menu getByCode(Integer businessId, String code) {
		Menu result = null;

		final SQLQuery query = getCurrentSession().createSQLQuery("select menu_id from menu where code=:code and business_id=:businessId");
        query.setParameter("code", code);
        query.setParameter("businessId", businessId);

		List list = query.list();
		if (null != list && list.size() > 0) {
			result = get((Integer)list.get(0));
		}
		return result;
	}

	@Override
	public MenuDiscountCoupon saveMenuDiscountCoupon(final MenuDiscountCoupon menuDiscountCoupon) {
		getCurrentSession().saveOrUpdate(menuDiscountCoupon);
		return menuDiscountCoupon;
	}

	@Override
	public Menu get(final Integer id) {
		final Menu result = (Menu) getCurrentSession().get(Menu.class, id);
		Hibernate.initialize(result.getMenuItemList());
		Hibernate.initialize(result.getBusiness());
		return result;
	}

	@Override
	public Menu getLite(final Integer id) {
		final Menu result = (Menu) getCurrentSession().get(Menu.class, id);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> list() {
		return getCurrentSession().createQuery("from menu").list();
	}

	private static final String FIND_SERVICE_SUBURB_STMT = 
			"SELECT c.suburb_id, c.postcode, m.message"
			+ " FROM menuordyconfig a, menuordyservicesuburb b, suburb c, message m"
			+ " WHERE a.menu_ordy_config_id = b.menu_ordy_config_id"
			+ " AND b.suburb_id = c.suburb_id"
			+ " AND a.menu_id = :menuId"
			+ " AND m.code = concat('suburb', c.suburb_id, 'N') and m.language_id = :languageId"
			+ " ORDER BY m.message";

	public List<Suburb> getMenuServiceSuburbList(final Integer menuId, final Integer languageId) {
        final SQLQuery query = getCurrentSession().createSQLQuery(FIND_SERVICE_SUBURB_STMT);
        query.setParameter("menuId", menuId);
        query.setParameter("languageId", languageId);
        return getSuburbQueryResult(query);
    }


	private static final String FIND_BUSINESS_SERVICE_SUBURB_STMT = 
		"SELECT distinct c.suburb_id, c.postcode, m.message"
		+ " FROM menuordyconfig a, menuordyservicesuburb b, suburb c, menu d, message m"
		+ " WHERE a.menu_ordy_config_id = b.menu_ordy_config_id"
		+ " AND b.suburb_id = c.suburb_id"
		+ " AND a.menu_id = d.menu_id"
		+ " AND d.business_id = :businessId"
		+ " AND m.code = concat('suburb', c.suburb_id, 'N') and m.language_id = :languageId"
		+ " ORDER BY m.message";
	public List<Suburb> getBusinessMenuServiceSuburbList(final Integer businessId, final Integer languageId) {
        final SQLQuery query = getCurrentSession().createSQLQuery(FIND_SERVICE_SUBURB_STMT);
        query.setParameter("businessId", businessId);
        query.setParameter("languageId", languageId);
        return getSuburbQueryResult(query);
	}

	private List<Suburb> getSuburbQueryResult(final SQLQuery query) {
		final List<Suburb> result = new ArrayList<Suburb>();
        final List queryResult = query.list();
        if (null != queryResult) {
    		Object[] queryResultItemTemp = null;
    		Suburb suburbTemp = null;
        	for (Object object : queryResult) {
        		queryResultItemTemp = (Object[]) object;
        		suburbTemp = new Suburb();
        		suburbTemp.setSuburbId((Integer)queryResultItemTemp[0]);
        		suburbTemp.setPostcode((String)queryResultItemTemp[1]);
        		suburbTemp.setName((String)queryResultItemTemp[2]);
        		result.add(suburbTemp);
        	}
        }
        return result;
	}


/*

SELECT distinct c.suburb_id, c.postcode, m.message
 FROM menuordyconfig a, menuordyservicesuburb b, suburb c, menu d, message m
 WHERE a.menu_ordy_config_id = b.menu_ordy_config_id
 AND b.suburb_id = c.suburb_id
 AND a.menu_id = d.menu_id
 AND d.business_id = 1
 AND m.code = concat('suburb', c.suburb_id, 'N') and m.language_id = 1
 ORDER BY m.message;

*/
}
