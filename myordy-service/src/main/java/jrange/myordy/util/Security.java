package jrange.myordy.util;

import jrange.myordy.dao.UserDAO;
import jrange.myordy.entity.StaffRoleOption;
import jrange.myordy.security.SessionIdGenerator;
import jrange.myordy.security.SessionIdGenerator.UserSessionId;

public final class Security {

	public static final void staffUserRoleCheck(final UserDAO userDAO, final String sessionId, final Integer shopId, StaffRoleOption role) {
		if (!isStaffUser(userDAO, sessionId, shopId, role)) {
			throw new RuntimeException("Invalid Request");
		}
	}

	public static final boolean isStaffUser(final UserDAO userDAO, final String sessionId, final Integer shopId, StaffRoleOption role) {
		final UserSessionId decryptedSessionId = SessionIdGenerator.getDecryptedSessionId(sessionId);
		return userDAO.isStaffUser(shopId, decryptedSessionId.getUserId(), role);
	}

	public static final boolean isStaffUser(final UserDAO userDAO, final Integer userId, final Integer shopId, StaffRoleOption role) {
		return userDAO.isStaffUser(shopId, userId, role);
	}

}
