package jrange.myordy.security;

public final class HexEncodeUtil {

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * The byte[] returned by MessageDigest does not have a nice textual
	 * representation, so some form of encoding is usually performed.
	 *
	 * This implementation follows the example of David Flanagan's book
	 * "Java In A Nutshell", and converts a byte array into a String of hex
	 * characters.
	 *
	 * Another popular alternative is to use a "Base64" encoding.
	 * 
	 * @param aInput
	 *            .
	 * @return .
	 */
	public static String hexEncode(final byte[] aInput) {
		final StringBuffer result = new StringBuffer();
		for (int idx = 0; idx < aInput.length; ++idx) {
			final byte b = aInput[idx];
			result.append(DIGITS[(b & 0xf0) >> 4]);
			result.append(DIGITS[b & 0x0f]);
		}
		return result.toString();
	}

	/**
	 * A convenience method to convert in the other direction, from a string of
	 * hexadecimal digits to an array of bytes.
	 * 
	 * @param s
	 *            .
	 * @return .
	 */
	public static byte[] hexDecode(final String s) {
		try {
			final int len = s.length();
			final byte[] r = new byte[len / 2];
			for (int i = 0; i < r.length; i++) {
				int digit1 = s.charAt(i * 2), digit2 = s.charAt(i * 2 + 1);
				if ((digit1 >= '0') && (digit1 <= '9')) {
					digit1 -= '0';
				} else if ((digit1 >= 'a') && (digit1 <= 'f')) {
					digit1 -= 'a' - 10;
				}
				if ((digit2 >= '0') && (digit2 <= '9')) {
					digit2 -= '0';
				} else if ((digit2 >= 'a') && (digit2 <= 'f')) {
					digit2 -= 'a' - 10;
				}
				r[i] = (byte) ((digit1 << 4) + digit2);
			}
			return r;
		} catch (final Exception e) {
			throw new IllegalArgumentException("hexDecode(): invalid input");
		}
	}

}
