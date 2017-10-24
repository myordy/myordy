package jrange.myordy.security;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang3.StringUtils;


/**
 * @author Jiten Baudh
 */
public final class DesEncriptionUtil {

	private static final Logger LOGGER = Logger.getLogger(DesEncriptionUtil.class.getName());

    /**
     */
    private final Cipher encrypter;
    /**
     */
    private final Cipher decrypter;

    /**
     */
    private static final byte[] SALT = new byte[] {
        (byte)0xA1, (byte)0x1C, (byte)0xC1, (byte)0x88,
        (byte)0x67, (byte)0x38, (byte)0xB1, (byte)0x02
    };

    private static final int ITERATION_COUNT = 22;

    private static final String PASSWORD = "Frank1e$";
    
    private static DesEncriptionUtil defaultInstance;

    /**
     */
    public DesEncriptionUtil(final String password, final byte[] salt, final int iterationCount) throws Exception {
        final KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount);
        final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        encrypter = Cipher.getInstance(key.getAlgorithm());
        decrypter = Cipher.getInstance(key.getAlgorithm());

        final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        encrypter.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        decrypter.init(Cipher.DECRYPT_MODE, key, paramSpec);

    }

    public static DesEncriptionUtil getDefaultInstance() {
    	DesEncriptionUtil result = defaultInstance;
    	if (null == result) {
    		try {
				result = new DesEncriptionUtil(PASSWORD, SALT, ITERATION_COUNT);
			} catch (Exception e) {
	            LOGGER.logp(Level.SEVERE, LOGGER.getName(), "getDefaultInstance", e.getLocalizedMessage(), e);
			}
    		if (defaultInstance == null) {
    			defaultInstance = result;
    		} else {
    			result = defaultInstance;
    		}
    	}
    	return result;
    }

    /**
     * @param arg .
     * @return .
     */
    public String encrypt(final String arg) {
    	if (StringUtils.isNoneBlank(arg)) {
    		try {
    			return HexEncodeUtil.hexEncode(encrypter.doFinal(arg.getBytes("UTF8")));
    		} catch (final Exception e) {
    			LOGGER.logp(Level.SEVERE, LOGGER.getName(), "encrypt", e.getLocalizedMessage(), e);
    		}
    	}
        return null;
    }
    /**
     * @param arg .
     * @return .
     */
    public String decrypt(final String arg) {
    	if (StringUtils.isNoneBlank(arg)) {
    		try {
    			return new String(decrypter.doFinal(HexEncodeUtil.hexDecode(arg)), "UTF8");
    		} catch (final Exception e) {
    			LOGGER.logp(Level.SEVERE, LOGGER.getName(), "decrypt", e.getLocalizedMessage(), e);
    		}
    	}
        return null;
    }
}
