package jrange.myordy.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.transaction.Transactional;

import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.OrdyItem;
import jrange.myordy.entity.OrdyItemCombo;
import jrange.myordy.entity.OrdyItemComboSub;
import jrange.myordy.entity.OrdyItemExtraOption;
import jrange.myordy.entity.OrdyItemExtraOptionAdd;
import jrange.myordy.entity.OrdyItemExtraOptionRemove;
import jrange.myordy.entity.OrdyNumberSequence;
import jrange.myordy.entity.OrdyStatus;
import jrange.myordy.entity.Suburb;
import jrange.myordy.entity.TheUserAddress;
import jrange.myordy.entity.list.OrdySearchRequest;
import jrange.myordy.entity.list.OrdySearchResponse;
import jrange.myordy.entity.list.OrdySearchResponse.OrdySearchListItem;
import jrange.myordy.entity.list.SalesSummaryReportRequest;
import jrange.myordy.entity.list.SalesSummaryReportResponse;
import jrange.myordy.entity.list.SalesSummaryReportResponse.SalesSummaryReportListItem;
import jrange.myordy.v1.shop.restaurant.OrderDetailsVO;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class OrdyDAOImpl extends HibernateDAOImpl implements OrdyDAO {

	private static final String ORDY_PENDING_CONFIRMATION_SHOPID_LIST_STMT = ""
		+ " SELECT a.shop_id, MAX(a.order_time)"
		+ " FROM ordy a, shop b"
		+ " WHERE a.shop_id = b.shop_id"
		+ "     AND a.status = '" + EntityStatus.ACTIVE + "'"
		+ "     AND b.status = '" + EntityStatus.ACTIVE + "'"
		+ "     AND a.ordy_status = '" + OrdyStatus.NEWORDY + "'"
		+ "     AND a.order_time < (NOW() - INTERVAL 2 MINUTE)"
		+ "     AND a.order_time > (NOW() - INTERVAL 5 MINUTE)"
		+ " GROUP BY a.shop_id"
    ;

	@Override
	public List<Integer> getOrdyPendingConfirmationShopIdList() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
        final SQLQuery ordyPendingConfirmationShopIdListPS = getCurrentSession().createSQLQuery(ORDY_PENDING_CONFIRMATION_SHOPID_LIST_STMT);
        final List queryResult = ordyPendingConfirmationShopIdListPS.list();
        if (null != queryResult) {
        	for (Object object : queryResult) {
        		result.add(((Integer)object));
        	}
        }
		return result;
	}

	@Override
	public Ordy save(Ordy ordyArg) {
		Ordy ordy = ordyArg;
		if (ordy.getOrdyId() == null) {
			ordy.setOrdyNumber(OrdyNumberGenerator.getNextOrdyNumber(getCurrentSession(), ordy.getShopId()));
		}
		getCurrentSession().saveOrUpdate(ordy);
		setOrdyLite(ordy);
		return ordy;
	}

	@Override
	public OrderDetailsVO getV1(final Long id) {
		return (OrderDetailsVO) getCurrentSessionV1().get(OrderDetailsVO.class, id);
	}

	private static final String ORDYSTATUS_UPDATE_STMS
		= "UPDATE Ordy SET ordyStatus = :newOrdyStatus"
			+ " WHERE ordyStatus = :currentOrdyStatus"
			+ " AND ordyId = :ordyId"
			+ " AND shopId = :shopId";

	@Override
	public Ordy confirmOrdy(final Integer id, final Integer shopId) {
        Query query = getCurrentSession().createQuery(ORDYSTATUS_UPDATE_STMS);
        query.setParameter("newOrdyStatus", OrdyStatus.CONFIRMED);
        query.setParameter("currentOrdyStatus", OrdyStatus.NEWORDY);
        query.setParameter("ordyId", id);
        query.setParameter("shopId", shopId);

        query.executeUpdate();

        return get(id, shopId);
	}

	@Override
	public Ordy get(final Integer id, final Integer shopId, final String deviceSessionId) {
		final Criteria criteria = getCurrentSession().createCriteria(Ordy.class);
		criteria.add(Restrictions.eq("deviceSessionId", deviceSessionId));
		return get(criteria, id, shopId);
	}

	@Override
	public Ordy get(final Integer id, final Integer shopId) {
		final Criteria criteria = getCurrentSession().createCriteria(Ordy.class);
		return get(criteria, id, shopId);
	}

	@Override
	public Ordy getCustomerLastOrdy(final Integer customerId, final Integer shopId) {
		final Criteria criteria = getCurrentSession().createCriteria(Ordy.class);
		criteria.add(Restrictions.eq("shopId", shopId));
		criteria.createCriteria("customer").add(Restrictions.eq("customerId", customerId));
		criteria.add(Restrictions.ne("status", EntityStatus.DELETED));
		criteria.addOrder(Order.desc("orderTime")).setMaxResults(1);
//		criteria.set
//		criteria.setProjection(Projections.max("orderTime"));

		final Ordy result = (Ordy) criteria.uniqueResult();
		if (null != result) {
			Hibernate.initialize(result.getCustomer());
			Hibernate.initialize(result.getPosOperator());
			Hibernate.initialize(result.getOrdyItemList());
			Hibernate.initialize(result.getComboOrdyItemList());
			Hibernate.initialize(result.getPaymentList());
			setOrdyLite(result);
		}
		return result;
	}

	private Ordy get(final Criteria criteria, final Integer id, final Integer shopId) {
		criteria.add(Restrictions.eq("ordyId", id));
		criteria.add(Restrictions.eq("shopId", shopId));

		final Ordy result = (Ordy) criteria.uniqueResult();
		if (null != result) {
			Hibernate.initialize(result.getCustomer());
			if (null != result.getCustomer()) {
				if (null != result.getCustomer().getContact()) {
					if (null != result.getCustomer().getContact().getAddressList()) {
						for (TheUserAddress theUserAddress : result.getCustomer().getContact().getAddressList()) {
							Hibernate.initialize(theUserAddress.getSuburb());
							if (null != theUserAddress.getSuburb()) {
								theUserAddress.setSuburbId(theUserAddress.getSuburb().getSuburbId());
							}
						}
					}
				}
			}
			Hibernate.initialize(result.getPosOperator());
			Hibernate.initialize(result.getOrdyItemList());
			Hibernate.initialize(result.getComboOrdyItemList());
			Hibernate.initialize(result.getPaymentList());
			Hibernate.initialize(result.getMenuDiscountCoupon());
			setOrdyLite(result);
		}
		return result;
	}

	private void setOrdyLite(Ordy ordy) {
		Ordy ordyLite = new Ordy();
		ordyLite.setOrdyId(ordy.getOrdyId());
		setLiteRelObjects(ordy.getOrdyItemList(), ordyLite, null);
		for (OrdyItemCombo coi : ordy.getComboOrdyItemList()) {
			OrdyItemCombo coiLite = new OrdyItemCombo();
			coiLite.setOrdyItemComboId(coi.getOrdyItemComboId());
			coi.setOrdy(ordyLite);
			setLiteRelObjects(coi.getOrderItems(), null, coiLite);
			for (OrdyItemComboSub ordyItemComboSub : coi.getOrdyItemsComboSub()) {
				//ordyItemComboSub.setMenuItemComboId(menuItemComboId);;
				//ordyItemComboSub.setNote(note);
				OrdyItemComboSub ordyItemComboSubLite = new OrdyItemComboSub();
				ordyItemComboSubLite.setSubOrdyItemComboSubId(ordyItemComboSub.getMenuItemComboId());
				ordyItemComboSub.setOrdyItemCombo(coiLite);
				//ordyItemComboSub.setOrderItems(orderItems);
				//coiSubLite.setOrdyItemComboId(ordyItemComboSub.getOrdyItemCombo().getOrdyItemComboId());
				for (OrdyItem oi : ordyItemComboSub.getOrderItems()) {
					oi.setOrdyItemComboSub(ordyItemComboSubLite);
				}
				setLiteRelObjects(ordyItemComboSub.getOrderItems(), null, null);

				//ordyItemComboSub.setPriceFormulaExtraOptions("");
				//ordyItemComboSub.setQty(qty);
				//ordyItemComboSub.setStatus(status);
				//ordyItemComboSub.setSubOrdyItemComboSubId(subOrdyItemComboSubId);

//				OrdyItemComboSub coiSubLite = new OrdyItemComboSub();
//				coiSubLite.setSubOrdyItemComboSubId(ordyItemComboSub.getSubOrdyItemComboSubId());
//				coiSubLite.setOrdyItemCombo(new OrdyItemCombo());
//				coiSubLite.getOrdyItemCombo().setOrdyItemComboId(coiLite.getOrdyItemComboId());
//				setLiteRelObjects(ordyItemComboSub.getOrderItems(), null, coiSubLite.getOrdyItemCombo());
			}
		}
	}

	private void setLiteRelObjects(final Set<OrdyItem> ordyItemList, final Ordy ordyLite, final OrdyItemCombo coiLite) {
		for (OrdyItem oi : ordyItemList) {
			oi.setOrdy(ordyLite);
			oi.setOrdyItemCombo(coiLite);
			if (null != oi.getExtraOptions()) {
				OrdyItem oiLite = new OrdyItem();
				oiLite.setOrdyItemId(oi.getOrdyItemId());
				oi.getExtraOptions().setOrdyItem(oiLite);
				OrdyItemExtraOption extraOptionLite = new OrdyItemExtraOption();
				extraOptionLite.setOrdyItemExtraOptionsId(oi.getExtraOptions().getOrdyItemExtraOptionsId());
				if (null != oi.getExtraOptions().getAddOptions()) {
					for (OrdyItemExtraOptionAdd a : oi.getExtraOptions().getAddOptions()) {
						a.setOrdyItemExtraOption(extraOptionLite);
					}
				}
				if (null != oi.getExtraOptions().getRemoveOptions()) {
					for (OrdyItemExtraOptionRemove a : oi.getExtraOptions().getRemoveOptions()) {
						a.setOrdyItemExtraOption(extraOptionLite);
					}
				}
			}
		}
	}

	private static final String SALES_SUMMARY_REPORT
		= "SELECT a.menu_id, COUNT(a.ordy_id), SUM(a.amount), SUM(a.amount * (discount_percent / 100))"
			+ " FROM ordy a "
			+ " WHERE a.order_time >= :orderTimeStart AND a.order_time <= :orderTimeEnd"
			+ " AND a.shop_id = :shopId"
			+ " AND a.status = '" + EntityStatus.ACTIVE + "'"
			+ " GROUP BY a.menu_id";

	@Override
	public SalesSummaryReportResponse salesSummaryReport(final SalesSummaryReportRequest request) {
		final SalesSummaryReportResponse result = new SalesSummaryReportResponse();
        final SQLQuery query = getCurrentSession().createSQLQuery(SALES_SUMMARY_REPORT);
        query.setParameter("shopId", request.getShopId());
        query.setParameter("orderTimeStart", request.getOrderDates().getStartDate());
        query.setParameter("orderTimeEnd", request.getOrderDates().getEndDate());
        final List queryResult = query.list();
        if (null != queryResult) {
    		Object[] queryResultItemTemp = null;
    		Suburb suburbTemp = null;
        	for (Object object : queryResult) {
        		queryResultItemTemp = (Object[]) object;
        		SalesSummaryReportListItem item = new SalesSummaryReportListItem();
        		item.setMenuId((Integer)queryResultItemTemp[0])
        				.setOrderTotalAmount(((BigDecimal)queryResultItemTemp[2]).longValue())
        				.setOrderTotalCount(((BigInteger)queryResultItemTemp[1]).longValue())
        				.setOrderTotalDiscountAmount(((Double)queryResultItemTemp[3]).longValue());
        		result.getItems().add(item);
        	}
        }
        return result;
	}

	@Override
	public OrdySearchResponse list(final OrdySearchRequest request) {
		final OrdySearchResponse result = new OrdySearchResponse();
		final Criteria criteria = getSearchCriteria(request);
		criteria.setFirstResult((request.getCurrentPageNumber() - 1) * request.getMaxResults());
		criteria.setMaxResults(request.getMaxResults());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		for (Object object : criteria.list()) {
			Ordy ordy = (Ordy) object;
			OrdySearchListItem item = new OrdySearchListItem();
			item.setAmount(ordy.getAmount());
			item.setDeliveryCharge(ordy.getDeliveryCharge());
			if (null != ordy.getCustomer()) {
				item.setCustomerName(ordy.getCustomer().getContact().getName());
			}
			item.setMenuId(ordy.getMenuId());
			item.setOrderTime(ordy.getOrderTime());
			item.setOrdyId(ordy.getOrdyId());
			item.setDiscountPercent(ordy.getDiscountPercent());
			item.setFixedAmount(ordy.getFixedAmount());
			item.setCashback(ordy.getCashback());
			item.setOrdyStatus(ordy.getOrdyStatus());
			result.getItems().add(item);
		}
		result.setTotalItems(getCount(request));
		return result;
	}


	private Long getCount(final OrdySearchRequest request) {
		final Criteria criteria = getSearchCriteria(request);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private Criteria getSearchCriteria(final OrdySearchRequest request) {
		Session currentSession = getCurrentSession();

		final Criteria criteria = currentSession.createCriteria(Ordy.class);
		if (null != request.getShopId()) {
			criteria.add(Restrictions.eq("shopId", request.getShopId()));
//			Criteria shopCriteria = criteria.createCriteria("shop");
//			shopCriteria.add(Restrictions.eq("shopId", request.getShopId()));
		}
		if (null != request.getOrderTimeFrom()) {
			criteria.add(Restrictions.ge("orderTime", request.getOrderTimeFrom()));
		}
		if (null != request.getDeviceSessionId()) {
			criteria.add(Restrictions.eq("deviceSessionId", request.getDeviceSessionId()));
		}
		if (null != request.getOrdyStatus()) {
			criteria.add(Restrictions.ge("ordyStatus", request.getOrdyStatus()));
		}
		if (null != request.getCustomerId()) {
			criteria.createCriteria("customer").add(Restrictions.eq("customerId", request.getCustomerId()));
		}
		if (null != request.getCustomerNumber()) {
			criteria.createCriteria("customer").add(Restrictions.eq("customerNumber", request.getCustomerNumber()));
		}

//		Criteria contactCriteria = criteria.createCriteria("contact");
//		if (StringUtils.isNotEmpty(request.getName())) {
//			contactCriteria.add(Restrictions.ilike("name", request.getName(), MatchMode.ANYWHERE));
//		}
//		if (StringUtils.isNotEmpty(request.getPhone())) {
//			contactCriteria.createCriteria("phoneList").add(Restrictions.ilike("phoneNumber", request.getPhone(), MatchMode.ANYWHERE));
//		}
//		if (StringUtils.isNotEmpty(request.getAddress())) {
//			contactCriteria.createCriteria("addressList").add(Restrictions.ilike("address", request.getAddress(), MatchMode.ANYWHERE));
//		}
		if (null != request.getOrderByLatestOrderFirst() && request.getOrderByLatestOrderFirst().booleanValue()) {
			criteria.addOrder(Order.desc("orderTime"));
		}
		criteria.add(Restrictions.eq("status", EntityStatus.ACTIVE));
		
		return criteria;
	}








    private static final class OrdyNumberGenerator {
        private static final int MAX_SEQUENCE_TRY = 100;
        private final Integer shopId;
        private Integer lastOrdyNumber;
        private Integer newOrdyNumber;
        private Integer maxOrdyNumber;
        private int sequenceTryCounter;
        private Date dayStartTimeInShopTimezone;

        private final Session session;

        private SQLQuery lastOrdyNumberPS;
        private SQLQuery lastOrdyNumberUpdatePS;

        private OrdyNumberGenerator(final Session sessionFactoryArg, final Integer shopIdArg) {
        	session = sessionFactoryArg;
            shopId = shopIdArg;
            sequenceTryCounter = 0;
        }
        private static Integer getNextOrdyNumber(final Session sessionArg, final Integer shopIdArg) throws RuntimeException {
            final OrdyNumberGenerator result = new OrdyNumberGenerator(sessionArg, shopIdArg);
            do {
                if (result.sequenceTryCounter > MAX_SEQUENCE_TRY) {
                    throw new RuntimeException("newOrdyNumber MAX_SEQUENCE_TRY Limit exceeded");
                }
                result.dayStartTimeInShopTimezone = result.getShopStartOfTheDayTime();
                result.setLastAndNewOrdyNumbers();
                result.sequenceTryCounter++;
            } while (!result.isLastOrdyNumberUpdated());
            return result.newOrdyNumber;
        }

        private static final String UPDATE_LAST_ORDY_NUMBER_STMT
            = "UPDATE ordynumbersequence SET last_ordy_number = :newOrdyNumber"
                + " WHERE shop_id = :shopId AND last_ordy_number = :lastOrdyNumber";
        private boolean isLastOrdyNumberUpdated() {
            if (null == lastOrdyNumberUpdatePS) {
            	lastOrdyNumberUpdatePS = session.createSQLQuery(UPDATE_LAST_ORDY_NUMBER_STMT);
            	lastOrdyNumberUpdatePS.setParameter("shopId", shopId);
            }
        	lastOrdyNumberUpdatePS.setParameter("newOrdyNumber", newOrdyNumber);
        	lastOrdyNumberUpdatePS.setParameter("lastOrdyNumber", lastOrdyNumber);
            return lastOrdyNumberUpdatePS.executeUpdate() > 0;
        }

        private static final String LAST_ORDY_NUMBER_STMT
            = "SELECT last_ordy_number FROM ordynumbersequence WHERE shop_id = :shopId";
        private void setLastAndNewOrdyNumbers() {
            if (null == lastOrdyNumberPS) {
                lastOrdyNumberPS  = session.createSQLQuery(LAST_ORDY_NUMBER_STMT);
                lastOrdyNumberPS.setParameter("shopId", shopId);
            }
            final List lastOrdyNumberList = lastOrdyNumberPS.list();
            if (null != lastOrdyNumberList && lastOrdyNumberList.size() > 0) {
                Object object = lastOrdyNumberList.get(0);
                if (null != object) {
                	lastOrdyNumber = ((Integer)object).intValue();
                }
            }
            if (null == lastOrdyNumber) {
            	final OrdyNumberSequence sequence = new OrdyNumberSequence();
            	sequence.setLastOrdyNumber(0);
            	sequence.setShopId(shopId);
            	lastOrdyNumber = 0;
            	session.merge(sequence);
            }
            maxOrdyNumber = getMaxOrdyNumber();
            newOrdyNumber = maxOrdyNumber + 1;
        }

        private static final String MAX_ORDY_NUMBER_STMT
            = "SELECT MAX(ordy_number) FROM ordy WHERE shop_id = :shopId and order_time >= :dayStartTimeInShopTimezone";
        private int getMaxOrdyNumber() {
        	Integer result = 0;
            final SQLQuery query = session.createSQLQuery(MAX_ORDY_NUMBER_STMT);
            query.setParameter("shopId", shopId);
            query.setParameter("dayStartTimeInShopTimezone", dayStartTimeInShopTimezone);
            final List queryResult = query.list();
            if (null != queryResult) {
            	final Object maxCustomerNumber = query.list().get(0);
            	if (null != maxCustomerNumber) {
            		result = ((Integer)maxCustomerNumber).intValue();
            	}
            }
            return result;
        }

        private static final String SHOP_TIMEZONE_STMT = "SELECT timezone FROM shop where shop_id = :shopId";
        private Date getShopStartOfTheDayTime() {

    		final Date now = Calendar.getInstance(TimeZone.getTimeZone("+00:00")).getTime();
        	final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    		Date result = null;

        	String shopTimezone = null;
            final SQLQuery query = session.createSQLQuery(SHOP_TIMEZONE_STMT);
            query.setParameter("shopId", shopId);
            final List queryResult = query.list();
            if (null != queryResult) {
            	shopTimezone = (String) query.list().get(0);
            }

            if (null != shopTimezone) {
            	df.setTimeZone(TimeZone.getTimeZone(shopTimezone));
            }
            try {
	            result = df.parse(df.format(now));
            } catch (ParseException e) {
            	e.printStackTrace();
            }

            return result;
        }

    }


}
