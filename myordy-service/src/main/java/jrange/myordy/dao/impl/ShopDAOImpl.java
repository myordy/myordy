package jrange.myordy.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.CustomerNumberSequence;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Menu;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.OrdyNumberSequence;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.ShopImage;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class ShopDAOImpl extends HibernateDAOImpl implements ShopDAO {

	private static final String SERVER_NAME_TO_SHOP_STMT
		= "SELECT shop_id FROM shopservername WHERE upper(server_name) = :serverName";
	@Override
	public Integer getShopId(final String serverName) {
		if (null != serverName) {			
			final SQLQuery query = getCurrentSession().createSQLQuery(SERVER_NAME_TO_SHOP_STMT);
			query.setParameter("serverName", serverName.toUpperCase());
			final Object shopId = query.uniqueResult();
			if (null != shopId) {
				return (Integer) shopId;
			}
		}
		return null;
	}

	@Override
	@Cacheable("ShopImages")
	public ShopImage getShopImage(final Integer shopId, final String imageCode) {
		ShopImage result = null;
		final Session currentSession = getCurrentSession();
		final Criteria criteria = currentSession.createCriteria(ShopImage.class);
		criteria.add(Restrictions.eq("imageCode", imageCode));
		criteria.createCriteria("shop").add(Restrictions.eq("shopId", shopId));

		final List list = criteria.list();
		if (null != list && list.size() > 0) {
			result = (ShopImage) list.get(0);
		}
		return result;
	}

	@Override
	@CacheEvict(value = "ShopImages", allEntries = true)
	public ShopImage saveShopImage(final ShopImage shopImage) {
		getCurrentSession().saveOrUpdate(shopImage);
		return shopImage;
	}

	@Override
	@CacheEvict(value = "Shops", allEntries = true)
	public Shop save(final Shop shopArg) {
		Shop shop = shopArg;
		boolean createRequest = false;
		if (shopArg.getShopId() != null) {
			shop = get(shopArg.getShopId());
			shop.setStatus(shopArg.getStatus());
		} else {
			createRequest = true;
		}
		getCurrentSession().saveOrUpdate(shop);
		if (createRequest) {
			final CustomerNumberSequence seqCust = new CustomerNumberSequence();
			seqCust.setLastCustomerNumber(0);
			seqCust.setShopId(shop.getShopId());
			getCurrentSession().saveOrUpdate(seqCust);
			final OrdyNumberSequence seqOrdy = new OrdyNumberSequence();
			seqOrdy.setLastOrdyNumber(0);
			seqOrdy.setShopId(shop.getShopId());
			getCurrentSession().saveOrUpdate(seqOrdy);
		}
		
		return shop;
	}

	@Override
	public Shop getLiteNoMenu(final Integer id) {
		final Shop result = (Shop) getCurrentSession().get(Shop.class, id);
		Hibernate.initialize(result.getBusiness());
		return result;
	}

	@Override
	public Shop getLite(final Integer id) {
		return (Shop) getCurrentSession().get(Shop.class, id);
	}

	@Override
	public CustomerNumberSequence getCustomerNumberSequence(final Integer shopId) {
		CustomerNumberSequence result = null;
		if (null != shopId) {
//            do {
//                if (sequenceTryCounter > MAX_SEQUENCE_TRY) {
//                    throw new Exception("newCustomerNumber MAX_SEQUENCE_TRY Limit exceeded");
//                }
//                
//            } while (!isNewCustomerNumberUnique());

			result = (CustomerNumberSequence) getCurrentSession().get(CustomerNumberSequence.class, shopId);
			if (null != result) {
				final Integer lastCustomerNumber = result.getLastCustomerNumber();
				final Integer newCustomerNumber = new Integer(1 + lastCustomerNumber.intValue());
				final Query customerNumberUpdateQuery = getCurrentSession().createQuery(
					"UPDATE CustomerNumberSequence SET lastCustomerNumber = :newCustomerNumber WHERE shopId = :shopId AND lastCustomerNumber = :lastCustomerNumber");
				customerNumberUpdateQuery.setParameter("newCustomerNumber", newCustomerNumber);
				customerNumberUpdateQuery.setParameter("shopId", shopId);
				customerNumberUpdateQuery.setParameter("lastCustomerNumber", lastCustomerNumber);
				customerNumberUpdateQuery.executeUpdate();
			}
//			Query query = session.createQuery("update Stock set stockName = :stockName" +
//			" where stockCode = :stockCode");
//		query.setParameter("stockName", "DIALOG1");
//		query.setParameter("stockCode", "7277");
//		int result = query.executeUpdate();
			
		}
		return result;
	}

	@Override
	@Cacheable("Shops")
	public Shop get(final Integer id) {
		final Shop result = (Shop) getCurrentSession().get(Shop.class, id);
//		Hibernate.initialize(result.getProductList());
//		Hibernate.initialize(result.getCategoryList());
		Hibernate.initialize(result.getMenuList());
		Hibernate.initialize(result.getBusiness());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shop> list() {
		return getCurrentSession().createQuery("from Shop").list();
	}

	@Override
	public List<Shop> listLite(final Language language) {
		final ArrayList<Shop> result = new ArrayList<Shop>();
		final SQLQuery sQLQuery = getCurrentSession().createSQLQuery(
				"select s.shop_id, m.message, m.code, m.message_id, s.status"
					+ " from shop s, message m, business b"
					+ " where s.business_id = b.business_id and b.business_id = m.business_id and m.code like 'shopName%' and m.language_id=" + language.getLanguageId());
		final List<Object> list = sQLQuery.list();
		final Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			final Object[] cols = (Object[])iterator.next();
			final Set<Message> messageList = new HashSet<Message>();
			messageList.add(new Message()
				.setMessageId((Integer)cols[3])
				.setCode((String)cols[2])
				.setMessage((String)cols[1])
				.setLanguage(language)
			);
			final Shop shop = new Shop()
				.setShopId((Integer)cols[0])
				.setMenuList(new HashSet<Menu>())
				.setStatus(EntityStatus.valueOf((String)cols[4]));;
			shop.addLanguageTable(language, messageList);
			result.add(shop);
		}
		return result;
	}

	public Session getCurrentSessionTemp() {
		final Session session = getCurrentSession();
        final SQLQuery query = session.createSQLQuery("SELECT timezone FROM shop where shop_id = :shopId");
        query.setParameter("shopId", 1);
        final List queryResult = query.list();
        if (null != queryResult) {
        	final Object maxCustomerNumber = query.list().get(0);
        	System.out.println(maxCustomerNumber.getClass());
        }

		return null;
	}

}
