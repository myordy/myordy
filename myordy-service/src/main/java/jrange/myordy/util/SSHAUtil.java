package jrange.myordy.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * TODO methods should not be synchronized .. use a pool
 * @author Jiten Baudh
 */
public final class SSHAUtil {
    /**
     */
    private SSHAUtil() { }
    /**
     */
    private static final Logger LOGGER = Logger.getLogger(SSHAUtil.class.getName());
    /**
     */
    public static final String SSHA_LABEL = "{SSHA}";
    /**
     */
    private static final Random RANDOM = new Random();
    /**
     */
    private static final byte[] SALT = new byte[4];
    /**
     */
    private static final MessageDigest SHA;
    /**
     */
    private static final byte SHA_DIGEST_BYTE_LENGTH = 20;

    static {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (java.security.NoSuchAlgorithmException e) {
            LOGGER.logp(Level.SEVERE, SSHAUtil.class.getName(), "static block", e.getLocalizedMessage(), e);
        }
        SHA = sha;
    }
    /**
     * @param message .
     * @return .
     * @throws UnsupportedEncodingException .
     */
    public static synchronized String getDigest(final String message) throws UnsupportedEncodingException {
        final StringBuffer result = new StringBuffer(SSHA_LABEL);
        RANDOM.nextBytes(SALT);

        SHA.reset();
        SHA.update(message.getBytes("UTF-8"));
        SHA.update(SALT);
        final byte[] digest = SHA.digest();

        final byte[] digestWithSalt = new byte[digest.length + SALT.length];
        System.arraycopy(digest, 0, digestWithSalt, 0, digest.length);
        System.arraycopy(SALT, 0, digestWithSalt, digest.length, SALT.length);

        result.append(new String(Base64.encode(digestWithSalt)));
        return result.toString();
    }
    /**
     * @param message .
     * @param digest .
     * @return .
     * @throws UnsupportedEncodingException .
     */
    public static synchronized boolean isMessageDigest(final String message, final String digest) throws UnsupportedEncodingException {
        if (message == null || digest == null) {
            return false;
        }
        final byte[] digestBytes = Base64.decode(digest.substring(SSHA_LABEL.length()));
        byte[] messageDiagest = null;
        byte[] salt = null;
        if (digestBytes.length < SHA_DIGEST_BYTE_LENGTH) {
            return false;
        } else {
            messageDiagest = new byte[SHA_DIGEST_BYTE_LENGTH];
            salt = new byte[digestBytes.length - SHA_DIGEST_BYTE_LENGTH];
            System.arraycopy(digestBytes, 0, messageDiagest, 0, SHA_DIGEST_BYTE_LENGTH);
            System.arraycopy(digestBytes, SHA_DIGEST_BYTE_LENGTH, salt, 0, salt.length);
        }

        SHA.reset();
        SHA.update(message.getBytes("UTF-8"));
        SHA.update(salt);

        final byte[] messaegDigestBytes = SHA.digest();

        return MessageDigest.isEqual(messageDiagest, messaegDigestBytes);
    }

    /**
     * @param args .
     * @throws Exception .
     */
    public static void main(final String[] args) throws Exception {
//        String digest = SSHAUtil.getDigest("qwerty123");
//        System.out.println("|" + digest + "|");
        System.out.println(isMessageDigest("qwerty123", "{SSHA}ie3Kb3NYx/XA6STnhGBHSB90PmipPZ88"));
    }
}
