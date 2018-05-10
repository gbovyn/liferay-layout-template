package be.gfi.liferay.tpl.util;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LiferayUtil {
	private static final String OSGI_WAR = "osgi/war";

	public static Path getOsgiWarFolder() {
		return Paths.get(getLiferayHome(), OSGI_WAR);
	}

	private static String getLiferayHome() {
		return PropsUtil.get(PropsKeys.LIFERAY_HOME);
	}
}
