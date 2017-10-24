package jrange.myordy.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Jiten Baudh
 */
public final class SessionIdGenerator {
    /**
     */
    private static final SecureRandom PRNG;
    /**
     */
    private static final MessageDigest SHA;
    /**
     */
    private static final byte[] SALT = new byte[8];
    /**
     */
    private static final Logger LOGGER = Logger.getLogger(SessionIdGenerator.class.getName());

    static {
        SecureRandom prng = null;
        try {
            prng = SecureRandom.getInstance("SHA1PRNG");
        } catch (final Exception e) {
            LOGGER.logp(Level.SEVERE, SessionIdGenerator.class.getName(), "static block", e.getLocalizedMessage(), e);
        }
        PRNG = prng;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (final java.security.NoSuchAlgorithmException e) {
            LOGGER.logp(Level.SEVERE, SessionIdGenerator.class.getName(), "static block", e.getLocalizedMessage(), e);
        }
        SHA = sha;
    }
    /**
     */
    private SessionIdGenerator() { }
    /**
     * @return .
     */
    public static synchronized String newSessionId() {
        final String randomNum = new Long(PRNG.nextLong()).toString();
        PRNG.nextBytes(SALT);
        SHA.reset();
        SHA.update(randomNum.getBytes());
        SHA.update(SALT);
        return new String(HexEncodeUtil.hexEncode(SHA.digest()));
    }

    public static synchronized String encryptSessionId(final Integer userId, final String sessionId) {
        return DesEncriptionUtil.getDefaultInstance().encrypt(userId + ":" + sessionId);
    }

    public static synchronized String newDeviceSessionId(final String remotrAddr) {
        return DesEncriptionUtil.getDefaultInstance().encrypt(remotrAddr + "|" + newSessionId());
    }

    public static DeviceSessionId getDecryptedDeviceSessionId(final String encryptedSessionId) {
    	if (null != encryptedSessionId) {
    		final String sessionId = DesEncriptionUtil.getDefaultInstance().decrypt(encryptedSessionId);
    		final String[] split = sessionId.split("\\|");
    		
    		return new DeviceSessionId(split[0], split[1]);
    	}
    	return new DeviceSessionId(null, null);
    }

    /**
     * @param encryptedSessionId .
     * @return .
     */
    public static UserSessionId getDecryptedSessionId(final String encryptedSessionId) {
    	if (StringUtils.isNoneBlank(encryptedSessionId)) {
    		final String sessionId = DesEncriptionUtil.getDefaultInstance().decrypt(encryptedSessionId);
    		final String[] split = sessionId.split(":");
    		return new UserSessionId(new Integer(split[0]), split[1]);
    	}
    	return new UserSessionId(null, null);
    }
     /**
      * @param args .
      */
    public static void main(final String[] args) {
//        String encryptSessionId = SessionIdGenerator.encryptSessionId(Integer.MIN_VALUE, "202.124.12.34");
//        System.out.println(encryptSessionId);
//        UserSessionId decryptedSessionId = SessionIdGenerator.getDecryptedSessionId(encryptSessionId);
//        System.out.println(decryptedSessionId.getUserId());
//        System.out.println(decryptedSessionId.getUserSessionId());
//        System.out.println(decryptedSessionId.getUserSessionId());
//        System.out.println(decryptedSessionId.getUserid());

        String encryptSessionId = SessionIdGenerator.newDeviceSessionId("202.124.12.34");
        System.out.println(encryptSessionId);
        DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(encryptSessionId);
        System.out.println(decryptedDeviceSessionId.getRemoteAddr());
        System.out.println(decryptedDeviceSessionId.getUserSessionId());

    }

    public static final class UserSessionId {
    	private final Integer userId;
    	private final String userSessionId;
    	public UserSessionId(final Integer userIdArg, final String userSessionIdArg) {
        	userId = userIdArg;
        	userSessionId = userSessionIdArg;
    	}
		public Integer getUserId() {
			return userId;
		}
		public String getUserSessionId() {
			return userSessionId;
		}
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
    }

    public static final class DeviceSessionId {
    	private final String remoteAddr;
    	private final String userSessionId;
    	public DeviceSessionId(final String remoteAddrArg, final String userSessionIdArg) {
        	remoteAddr = remoteAddrArg;
        	userSessionId = userSessionIdArg;
    	}
		public String getUserSessionId() {
			return userSessionId;
		}
		public String getRemoteAddr() {
			return remoteAddr;
		}
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
    }

}
