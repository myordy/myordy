package jrange.myordy.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.SuburbDAO;
import jrange.myordy.entity.Country;
import jrange.myordy.entity.State;
import jrange.myordy.entity.Suburb;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class SuburbDAOImpl extends HibernateDAOImpl implements SuburbDAO {

	@Override
	public Suburb saveSuburb(Suburb suburb) {
		getCurrentSession().saveOrUpdate(suburb);
		return suburb;
	}

	@Override
	public Suburb get(Integer id) {
		return (Suburb) getCurrentSession().get(Suburb.class, id);
	}

	@Override
	public Suburb get(String code) {
		Suburb result = (Suburb) getCurrentSession().createCriteria(Suburb.class)
				.add(Restrictions.eq("code", code)).uniqueResult();
		return result;
	}

	@Override
	public List<Suburb> listSuburbs() {
		return getCurrentSession().createQuery("from suburb").list();
	}

	@Override
	public State saveState(State state) {
		getCurrentSession().saveOrUpdate(state);
		return state;
	}

	@Override
	public State getState(Integer id) {
		return (State) getCurrentSession().get(State.class, id);
	}

	@Override
	public List<State> listStates() {
		return getCurrentSession().createQuery("from state").list();
	}

	@Override
	public State getState(Integer countryId, String code) {
		State result = (State) getCurrentSession().createCriteria(State.class)
				.add(Restrictions.eq("country.countryId", countryId))
				.add(Restrictions.eq("code", code)).uniqueResult();
		return result;
	}

	@Override
	public Country saveCountry(Country country) {
		getCurrentSession().saveOrUpdate(country);
		return country;
	}

	@Override
	public Country getCountry(Integer id) {
		return (Country) getCurrentSession().get(Country.class, id);
	}

	@Override
	public List<Country> listCountries() {
		return getCurrentSession().createQuery("from country").list();
	}

	private static final String FIND_SUBURB_STMT
    	= "SELECT a.suburb_id, a.postcode, m.message, a.geocode_latitude, a.geocode_longitude"
    		+ " FROM suburb a, state b, country c, message m"
    		+ " WHERE a.postcode = :postcode AND c.code = :countryCode"
    		+ " AND a.state_id = b.state_id AND b.country_id = c.country_id"
    		+ " AND m.code = concat('suburb', a.suburb_id, 'N') and m.language_id = :languageId";

	@Override
	public List<Suburb> findSuburbs(final String countryCode, final String postcode, final Integer languageId) {
		final List<Suburb> result = new ArrayList<Suburb>();
        final SQLQuery query = getCurrentSession().createSQLQuery(FIND_SUBURB_STMT);
        query.setParameter("countryCode", countryCode);
        query.setParameter("postcode", postcode);
        query.setParameter("languageId", languageId);
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
        		suburbTemp.setGeocodeLatitude((String)queryResultItemTemp[3]);
        		suburbTemp.setGeocodeLongitude((String)queryResultItemTemp[4]);
        		result.add(suburbTemp);
			}
        }
        return result;
//		Criteria suburbCriteria = getCurrentSession().createCriteria(Suburb.class);
//		suburbCriteria.add(Restrictions.eq("postcode", postcode));
//		suburbCriteria.createCriteria("state").createCriteria("country").add(Restrictions.eq("code", countryCode));
//		return suburbCriteria.list();
//				.add(Restrictions.eq("state.country.code", countryCode))
//				.add(Restrictions.eq("postcode", postcode)).list();
	}

}
