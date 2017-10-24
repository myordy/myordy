package jrange.myordy.dao;

import jrange.myordy.entity.StaffRoleOption;
import jrange.myordy.entity.TheUser;
import jrange.myordy.entity.TheUserLoginEmail;
import jrange.myordy.security.LoginResponse;
import jrange.myordy.v1.shop.restaurant.JBOptSUser;

public interface UserDAO {
	public TheUser get(final Integer id);
	public TheUser get(final String email);
	public TheUser save(final TheUser theUserArg);
	public TheUserLoginEmail saveUserLoginEmail(final TheUserLoginEmail theUserLoginEmail);
	public LoginResponse login(final String emailArg, final String passwordArg);
    public LoginResponse restoreUserSession(final String sessionIdArg);
    public boolean isStaffUser(final Integer shopId, final Integer userId, final StaffRoleOption role);

	public JBOptSUser getV1(Long userId);
}
