package jrange.myordy.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import jrange.myordy.dao.CustomerDAO;
import jrange.myordy.entity.Customer;
import jrange.myordy.entity.CustomerNumberSequence;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.TheUserAddress;
import jrange.myordy.entity.TheUserContact;
import jrange.myordy.entity.TheUserPhone;
import jrange.myordy.entity.TheUserPhoneType;
import jrange.myordy.entity.list.CustomerSearchRequest;
import jrange.myordy.entity.list.PaginatedResponse;
import jrange.myordy.v1.shop.restaurant.ShopCustomerRestaurant;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class CustomerDAOImpl extends HibernateDAOImpl implements CustomerDAO {

	@Override
	public Customer getByUserId(final Integer shopId, final Integer userId) {
		Customer result = null;
		Session currentSession = getCurrentSession();
		final Criteria criteria = currentSession.createCriteria(Customer.class);
		criteria.add(Restrictions.eq("shopId", shopId));
		criteria.createCriteria("theUser").add(Restrictions.eq("userId", userId));
		criteria.addOrder(Order.asc("customerId"));
		List list = criteria.list();
		if (null != list && list.size() > 0) {
			result = (Customer) list.get(0);
		}
		return result;
	}

	@Override
	public Customer getNewOrdyCustomer(final Ordy ordy) {
		Session currentSession = getCurrentSession();
		String searchNumber = ordy.getCustomerMobileNumber();
		Customer result = findCustomerByPhoneNumber(currentSession, ordy.getShopId(), searchNumber);
		if (null == result && searchNumber.length() > 2) {
			result = findCustomerByPhoneNumber(currentSession, ordy.getShopId(), searchNumber.substring(2));
		}
		if (null == result && searchNumber.length() > 3) {
			result = findCustomerByPhoneNumber(currentSession, ordy.getShopId(), searchNumber.substring(3));
		}
		if (null == result && searchNumber.length() > 3) {
			result = findCustomerByPhoneNumber(currentSession, ordy.getShopId(), searchNumber.substring(4));
		}
		if (null == result) {
			result = new Customer();
			result.setStatus(EntityStatus.ACTIVE);
			result.setShopId(ordy.getShopId());

			result.setContact(new TheUserContact());
			if (StringUtils.isBlank(ordy.getCustomerName())) {
				result.getContact().setName(ordy.getCustomerMobileNumber());
			} else {
				result.getContact().setName(ordy.getCustomerName());
			}
			result.getContact().setStatus(EntityStatus.ACTIVE);
			result.getContact().setLanguageId(ordy.getLanguageId());
			result.getContact().setPhoneList(new HashSet<TheUserPhone>());

			TheUserPhone p = new TheUserPhone();
			p.setPhoneNumber(ordy.getCustomerMobileNumber());
			p.setStatus(EntityStatus.ACTIVE);
			p.setPhoneType(TheUserPhoneType.MOBILE);

			result.getContact().getPhoneList().add(p);

			result = save(result);
		}

		return result;
	}

	private Customer findCustomerByPhoneNumber(Session currentSession, final Integer shopId, final String phoneNumber) {
		Customer result = null;
		final Criteria criteria = currentSession.createCriteria(Customer.class);
		criteria.add(Restrictions.eq("shopId", shopId));
		Criteria contactCriteria = criteria.createCriteria("contact");
		if (StringUtils.isNotEmpty(phoneNumber)) {
			contactCriteria.createCriteria("phoneList").add(Restrictions.ilike("phoneNumber", phoneNumber, MatchMode.END));
			List list = criteria.list();
			if (list.size() > 0) {
				result = (Customer) list.get(0);
			}
		}
		return result;
	}

	@Override
	public PaginatedResponse<Customer> list(final CustomerSearchRequest request) {
		final PaginatedResponse<Customer> result = new PaginatedResponse<Customer>();
		Criteria criteria = getSearchCriteria(request);
		criteria.setFirstResult((request.getCurrentPageNumber() - 1) * request.getMaxResults());
		criteria.setMaxResults(request.getMaxResults());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		result.getItems().addAll(criteria.list());
		if (result.getItems().size() < 1 && StringUtils.isNotEmpty(request.getPhone())) {
			criteria = getSearchCriteria(request);
			criteria.setFirstResult((request.getCurrentPageNumber() - 1) * request.getMaxResults());
			criteria.setMaxResults(request.getMaxResults());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		ArrayList<Customer> customers = result.getItems();
		if (null != customers) {
			for (Customer customer : customers) {
				if (null != customer.getContact()) {
					customer.getContact().setSuburbIdOnAddresses();
				}
			}
		}
		result.setTotalItems(getCount(request));
		return result;
	}

	@Override
	public Customer save(final Customer customerArg) {
		Customer customer = customerArg;
		if (null != customer.getContact()) {
			customer.getContact().setSuburbOnAddresses();
			customer.getContact().setName(makeCustomerName(customer));
		}
		if (customer.getCustomerId() != null) {
			customer = get(customerArg.getCustomerId());
			customer.setContact(customerArg.getContact());
			if (null != customer.getContact()) {
				Set<TheUserPhone> phoneList = customer.getContact().getPhoneList();
				if (null != phoneList) {
					for (TheUserPhone up : phoneList) {
						if (null != up.getPhoneNumber()) {
							up.setPhoneNumber(up.getPhoneNumber().replaceAll("[^\\d+]", ""));
						}
					}
				}
			}
		} else {
			if (null == customer.getCustomerNumber()) {
				customer.setCustomerNumber(CustomerNumberGenerator.getNextCustomerNumber(getCurrentSession(), customer.getShopId()));
			}
		}
		return (Customer) getCurrentSession().merge(customer);
	}

	@Override
	public Customer get(final Integer id) {
		final Customer result = (Customer) getCurrentSession().get(Customer.class, id);
		return result;
	}

	public List<ShopCustomerRestaurant> listCustomerV1(Long shopId) {
		Criteria criteria = getCurrentSessionV1().createCriteria(ShopCustomerRestaurant.class);
		criteria.add(Restrictions.eq("shopId", shopId));
		criteria.add(Restrictions.eq("status", new Short(ShopCustomerRestaurant.STATUS_ACTIVE)));

		return criteria.list();
	}
/*
	private void x() {
        final SQLQuery query = getCurrentSession().createSQLQuery("select id, address, suburb_name, postcode, name, note, shop_customer_number, status from 5_shop_customer_restaurant where status != 0");
        final List queryResult = query.list();
        if (null != queryResult) {
        	for (Object object : queryResult) {
        		Object[] qr = (Object[]) object;
        		if (qr[1] != null && StringUtils.isNoneEmpty(qr[1].toString())) {
        			if (qr[3] == null || StringUtils.isEmpty(qr[3].toString())) {
        				System.out.println(qr[0] + " | " + qr[1] + " | " + qr[2] + " | " + qr[3] + " | " + qr[4] + " | " + qr[5] + " | " + qr[6] + " | " + qr[7]);
        			}
        		}
//        		for (int i = 0; i < qr.length; i++) {
//					System.out.print(qr[i]);
//					System.out.print(" | ");
//				}
//				System.out.println();
        	}
        }
	}
*/
	private Long getCount(final CustomerSearchRequest request) {
		final Criteria criteria = getSearchCriteria(request);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private Criteria getSearchCriteria(final CustomerSearchRequest request) {
		Session currentSession = getCurrentSession();

		final Criteria criteria = currentSession.createCriteria(Customer.class);
		if (null != request.getShopId()) {
//			Criteria shopCriteria = criteria.createCriteria("shop");
			criteria.add(Restrictions.eq("shopId", request.getShopId()));
		}
		if (null != request.getCustomerNumber()) {
			criteria.add(Restrictions.eq("customerNumber", request.getCustomerNumber()));
		}
		Criteria contactCriteria = criteria.createCriteria("contact");
		if (StringUtils.isNotEmpty(request.getName())) {
			contactCriteria.add(Restrictions.ilike("name", request.getName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(request.getPhone())) {
			contactCriteria.createCriteria("phoneList").add(Restrictions.ilike("phoneNumber", request.getPhone(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(request.getAddress())) {
			contactCriteria.createCriteria("addressList").add(Restrictions.ilike("address", request.getAddress(), MatchMode.ANYWHERE));
		}
		criteria.add(Restrictions.eq("status", EntityStatus.ACTIVE));
		criteria.add(Restrictions.ge("customerId", 0));
		
		return criteria;
	}

	@Override
	public void delete(Integer id) {
		if (id != null) {
			Customer customer = get(id);
			if (null != customer) {
				customer.setStatus(EntityStatus.DELETED);
				getCurrentSession().saveOrUpdate(customer);
			}
		}

	}

	private static String makeCustomerName(final Customer customer) {
		String result = customer.getContact().getName();
		if (StringUtils.isEmpty(result)) {
			for (TheUserPhone phone : customer.getContact().getPhoneList()) {
				result = phone.getPhoneNumber();
				break;
			}
		}
		if (StringUtils.isEmpty(result)) {
			for (TheUserAddress address : customer.getContact().getAddressList()) {
				result = address.getAddress();
				break;
			}
		}
		if (result.length() > 100) {
			result = result.substring(0, 100);
		}
		return result;
	}

    private static final class CustomerNumberGenerator {
        private static final int MAX_SEQUENCE_TRY = 100;
        private final Integer shopId;
        private Integer lastCustomerNumber;
        private Integer newCustomerNumber;
        private Integer maxCustomerNumber;
        private int sequenceTryCounter;
        
        private final Session session;

        private SQLQuery lastCustomerNumberPS;
        private SQLQuery lastCustomerNumberUpdatePS;

        private CustomerNumberGenerator(final Session sessionFactoryArg, final Integer shopIdArg) {
        	session = sessionFactoryArg;
            shopId = shopIdArg;
            sequenceTryCounter = 0;
        }
        private static Integer getNextCustomerNumber(final Session sessionArg, final Integer shopIdArg) throws RuntimeException {
            final CustomerNumberGenerator result = new CustomerNumberGenerator(sessionArg, shopIdArg);
            do {
                if (result.sequenceTryCounter > MAX_SEQUENCE_TRY) {
                    throw new RuntimeException("newCustomerNumber MAX_SEQUENCE_TRY Limit exceeded");
                }
                result.setLastAndNewCustomerNumbers();
                result.sequenceTryCounter++;
            } while (!result.isLastCustomerNumberUpdated());
            return result.newCustomerNumber;
        }

        private static final String UPDATE_LAST_CUSTOMER_NUMBER_STMT
            = "UPDATE customernumbersequence SET last_customer_number = :newCustomerNumber"
                + " WHERE shop_id = :shopId AND last_customer_number = :lastCustomerNumber";
        private boolean isLastCustomerNumberUpdated() {
            if (null == lastCustomerNumberUpdatePS) {
            	lastCustomerNumberUpdatePS = session.createSQLQuery(UPDATE_LAST_CUSTOMER_NUMBER_STMT);
            	lastCustomerNumberUpdatePS.setParameter("shopId", shopId);
            }
        	lastCustomerNumberUpdatePS.setParameter("newCustomerNumber", newCustomerNumber);
        	lastCustomerNumberUpdatePS.setParameter("lastCustomerNumber", lastCustomerNumber);
            return lastCustomerNumberUpdatePS.executeUpdate() > 0;
        }
        private static final String LAST_CUSTOMER_NUMBER_STMT
            = "SELECT last_customer_number FROM customernumbersequence WHERE shop_id = :shopId";
        private void setLastAndNewCustomerNumbers() {
            if (null == lastCustomerNumberPS) {
                lastCustomerNumberPS  = session.createSQLQuery(LAST_CUSTOMER_NUMBER_STMT);
                lastCustomerNumberPS.setParameter("shopId", shopId);
            }
            final List lastCustomerNumberList = lastCustomerNumberPS.list();
            if (null != lastCustomerNumberList && lastCustomerNumberList.size() > 0) {
                Object object = lastCustomerNumberList.get(0);
                if (null != object) {
                	lastCustomerNumber = ((Integer)object).intValue();
                }
            }
            if (null == lastCustomerNumber) {
            	final CustomerNumberSequence sequence = new CustomerNumberSequence();
            	sequence.setLastCustomerNumber(0);
            	sequence.setShopId(shopId);
            	lastCustomerNumber = 0;
            	session.merge(sequence);
            }
            maxCustomerNumber = getMaxCustomerNumber();
            if (maxCustomerNumber.intValue() < lastCustomerNumber.intValue()) {
            	newCustomerNumber = lastCustomerNumber + 1;
            } else {
            	newCustomerNumber = maxCustomerNumber + 1;
            }
        }

        private static final String MAX_CUSTOMER_NUMBER_STMT
            = "SELECT MAX(customer_number) FROM customer WHERE shop_id = :shopId";
        private int getMaxCustomerNumber() {
        	Integer result = 0;
            final SQLQuery query = session.createSQLQuery(MAX_CUSTOMER_NUMBER_STMT);
            query.setParameter("shopId", shopId);
            final List queryResult = query.list();
            if (null != queryResult) {
            	final Object maxCustomerNumber = query.list().get(0);
            	if (null != maxCustomerNumber) {
            		result = ((Integer)maxCustomerNumber).intValue();
            	}
            }
            return result;
        }

    }

}
