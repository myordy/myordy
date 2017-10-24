package jrange;

import jrange.myordy.security.SessionIdGenerator;
import jrange.myordy.security.SessionIdGenerator.DeviceSessionId;

public class Temp {

	public static void main(String[] args) {
//		final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId("8b7dbfa44b0dc238a59d6a7c43109f0c02bb0ed644e48087e849f207e973dad74167504c1e1d1d9d726423bbfdf741d1d63974cb59454657");
		final DeviceSessionId decryptedDeviceSessionId = SessionIdGenerator.getDecryptedDeviceSessionId(null);
		System.out.println(decryptedDeviceSessionId.getUserSessionId());
		System.out.println(decryptedDeviceSessionId.getRemoteAddr());

	}
	
}
