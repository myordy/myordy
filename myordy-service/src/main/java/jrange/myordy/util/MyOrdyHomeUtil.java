package jrange.myordy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class MyOrdyHomeUtil {

	private static final File MYORDY_HOME_DIR = new File((null == System.getProperty("MYORDY_HOME"))?"/var/lib/myordy/":System.getProperty("MYORDY_HOME"));
	private static final File MYORDY_SHOP_DIR = new File(MYORDY_HOME_DIR, "shop");
	private static final Map<Integer, Properties> MYORDY_SHOP_PROPERTIES = new HashMap<Integer, Properties>();

	public static Properties getShopProperties(final Integer shopId) {
		Properties result = MYORDY_SHOP_PROPERTIES.get(shopId);
		if (null == result) {
			synchronized (MyOrdyHomeUtil.class) {
				result = new Properties();
				InputStream input = null;
				try {
					input = new FileInputStream(new File(new File(MYORDY_SHOP_DIR, "" + shopId), "shop.properties"));
					result.load(input);
					MYORDY_SHOP_PROPERTIES.put(shopId, result);
				} catch (final IOException ex) {
					ex.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return result;
	}

}
