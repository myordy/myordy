package jrange.myordy.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public final class HTTPUtil {

	private static final String HTTP_HEADER_NAME_HOST_DELIMITER = ":";
	private static final String HTTP_HEADER_NAME_HOST = "Host";
	private static final String HTTP_HEADER_NAME_X_FORWARDED_FOR = "X-Forwarded-For";

	public static final String getServerName(final HttpServletRequest request) {
		final String hostHeader = request.getHeader(HTTP_HEADER_NAME_HOST);
		if (null != hostHeader) {
			return hostHeader.split(HTTP_HEADER_NAME_HOST_DELIMITER)[0];
		}
		return null;
	}

	public static final String getRemoteAddr(final HttpServletRequest request) {
		 final String remoteAddr = request.getRemoteAddr();
		 final String xForwardedFor = request.getHeader(HTTP_HEADER_NAME_X_FORWARDED_FOR);
		if (StringUtils.isNoneEmpty(xForwardedFor)) {
			return xForwardedFor;
		}
		return remoteAddr;
	}

}
