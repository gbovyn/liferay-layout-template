package be.gfi.liferay.tpl.helper;

import be.gfi.liferay.tpl.model.LayoutTemplate;
import com.liferay.portal.kernel.util.StringPool;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.zip.ZipFile;

public class WarHelper {

	private static final String WEB_INF = "WEB-INF";
	private static final String XML_CONFIG = "liferay-layout-templates.xml";
	private static final String PROPERTIES_FILE = "liferay-plugin-package.properties";

	private static final String TEMPLATE_EXT = ".tpl";
	private static final String THUMBNAIL_EXT = ".png";

	private static Logger logger;


	private final String warPath;

	public WarHelper(final String warPath) {
		logger = LoggerFactory.getLogger(getClass().getName());

		this.warPath = "C:\\Projects\\liferay\\themes\\my-liferay-layout-layouttpl\\dist\\my-liferay-layout-layouttpl.war";
	}

	public List<LayoutTemplate> getLayoutTemplatesMetadata() {
		List<LayoutTemplate> layoutTemplates = Collections.emptyList();

		return layoutTemplates;
	}

	private boolean warExists() {
		final Try<ZipFile> zipFile = getZipFile();

		zipFile.onFailure(ex ->
				logger.error(ex.getMessage(), ex)
		);

		return zipFile.isSuccess();
	}

	private Try<ZipFile> getZipFile() {
		return Try.of(() ->
				new ZipFile(this.warPath)
		);
	}

	private String getLiferayLayoutTemplatesXml() {
		return StringPool.BLANK;
	}

}
