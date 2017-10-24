package jrange.myordy.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import jrange.myordy.dao.UserDAO;
import jrange.myordy.entity.StaffRoleOption;
import jrange.myordy.entity.TheUser;
import jrange.myordy.entity.TheUserLoginEmail;
import jrange.myordy.security.LoginResponse;
import jrange.myordy.security.SessionIdGenerator;
import jrange.myordy.security.SessionIdGenerator.UserSessionId;
import jrange.myordy.util.SSHAUtil;
import jrange.myordy.v1.shop.restaurant.JBOptSUser;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class UserDAOImpl extends HibernateDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    private static final String STAFF_ROLE_CHECK_STMT = ""
    		+ "SELECT staff_role_id FROM staffrole"
    		+ " WHERE shop_id = :shopId"
    		+ " AND user_id = :userId"
    		+ " AND staff_role = :staffRole"
    		+ " AND (start_time IS NULL OR start_time < :now)"
    		+ " AND (end_time IS NULL OR end_time > :now)";
    @Override
    public boolean isStaffUser(final Integer shopId, final Integer userId, final StaffRoleOption role) {
    	final Date now = Calendar.getInstance().getTime();
    	SQLQuery query = getCurrentSession().createSQLQuery(STAFF_ROLE_CHECK_STMT);
        query.setParameter("shopId", shopId);
        query.setParameter("userId", userId);
        query.setParameter("staffRole", role.name());
        query.setParameter("now", now);
        final Integer staffRoleId = (Integer) query.uniqueResult();
        return null != staffRoleId;
    }

    @Override
	public TheUser get(final Integer id) {
		final TheUser result = (TheUser) getCurrentSession().get(TheUser.class, id);
		return result;
	}
	public TheUser get(final String email) {
		Session currentSession = getCurrentSession();
		final Criteria criteria = currentSession.createCriteria(TheUser.class);
		criteria.createCriteria("contact").add(Restrictions.ilike("email", email, MatchMode.EXACT));
		return (TheUser) criteria.uniqueResult();
	}

	@Override
	public TheUser save(final TheUser theUserArg) {
		return (TheUser) getCurrentSession().merge(theUserArg);
	}

    public JBOptSUser getV1(final Long id) {
    	return (JBOptSUser) getCurrentSessionV1().get(JBOptSUser.class, id);
    }

    private static final String USER_SESSION_RESTORE_STMT
		= "SELECT user_id FROM theuser WHERE session_id = :sessionId AND user_id = :userId";
    public LoginResponse restoreUserSession(final String encryptedSessionId) {
    	LoginResponse result = null;
		final UserSessionId decryptedSessionId = SessionIdGenerator.getDecryptedSessionId(encryptedSessionId);
    	final SQLQuery query = getCurrentSession().createSQLQuery(USER_SESSION_RESTORE_STMT);
        query.setParameter("sessionId", decryptedSessionId.getUserSessionId());
        query.setParameter("userId", decryptedSessionId.getUserId());
        final Integer userId = (Integer) query.uniqueResult();
        if (null != userId) {
			final TheUser theUser = get(userId);
			result = new LoginResponse();
			result.setUser(theUser);
//			if (null != theUser.getContact()) {
//				result.setUserName(theUser.getContact().getName());
//			}
        }
    	return result;
    }

    private static final String USER_PW_HASH_SELECT_BY_EMAIL_STMT
	    = "SELECT a.password_hash, a.user_id"
	    		+ " FROM theuser a, theuserloginemail b"
	    		+ " WHERE b.login_email = :email"
	    		+ " AND a.user_id = b.user_id"
	    		+ " AND a.status='ACTIVE'";
    private static final String USER_SESSION_ID_UPDATE_STMT
    	= "UPDATE theuser SET session_id = :sessionId WHERE user_id = :userId";
    public LoginResponse login(final String emailArg, final String passwordArg) {
    	LoginResponse result = null;
    	final Session session = getCurrentSession();
    	SQLQuery query = session.createSQLQuery(USER_PW_HASH_SELECT_BY_EMAIL_STMT);
        query.setParameter("email", emailArg.toLowerCase().trim());
        Object userPWResult = query.uniqueResult();
        if (null != userPWResult) {
        	final String pwDigest = (String) ((Object[])userPWResult)[0];
        	try {
				if (SSHAUtil.isMessageDigest(passwordArg, pwDigest)) {
					final TheUser theUser = get((Integer) ((Object[])userPWResult)[1]);
					result = new LoginResponse();
					result.setUser(theUser);
//					if (null != theUser.getContact()) {
//						result.setUserName(theUser.getContact().getName());
//					}
					final String userSessionId = SessionIdGenerator.newSessionId();
					final SQLQuery updateSessionIdSTMT = session.createSQLQuery(USER_SESSION_ID_UPDATE_STMT);
					updateSessionIdSTMT.setParameter("sessionId", userSessionId);
					updateSessionIdSTMT.setParameter("userId", theUser.getUserId());
					updateSessionIdSTMT.executeUpdate();
					result.setSessionId(SessionIdGenerator.encryptSessionId(theUser.getUserId(), userSessionId));
				}
			} catch (final Exception e) {
	            LOGGER.logp(Level.SEVERE, LOGGER.getName(), "login", e.getLocalizedMessage(), e);
			}
        }
    	return result;
    }

	@Override
	public TheUserLoginEmail saveUserLoginEmail(final TheUserLoginEmail theUserLoginEmailArg) {
		return (TheUserLoginEmail) getCurrentSession().merge(theUserLoginEmailArg);
	}

}
