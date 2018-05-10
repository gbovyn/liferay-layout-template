package be.gfi.liferay.tpl.util;

import be.gfi.liferay.tpl.model.LayoutTemplate;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import io.vavr.control.Try;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LayoutTemplateUtil {

	private static final String WEB_INF = "WEB-INF";
	private static final String XML_CONFIG = "liferay-layout-templates.xml";

	private static final String PROPERTIES_FILE = "liferay-plugin-package.properties";

	public static Try<List<LayoutTemplate>> getCustomLayoutTemplates(final Path zipPath) {
		final Try<Element> customElement = XmlUtil.getCustomElement(getLayoutTemplatesXml(zipPath));

		if (customElement.isFailure()) {
			return Try.failure(customElement.getCause());
		}

		final List<LayoutTemplate> layoutTemplates = new ArrayList<>(0);

		customElement.get().elements("layout-template").forEach(element -> {
			final String id = element.attribute("id").getValue();
			final String name = element.attribute("name").getValue();
			final String templatePath = element.element("template-path").getStringValue();
			final String thumbnailPath = element.element("thumbnail-path").getStringValue();

			layoutTemplates.add(
					new LayoutTemplate(id, name, templatePath, thumbnailPath)
			);
		});

		return Try.success(layoutTemplates);
	}

	/**
	 * Returns the content of a layout template file (.tpl).
	 *
	 * @param zipPath            the war file containing the layout template.
	 * @param layoutTemplatePath the path to the layout template.
	 * @return the layout template content.
	 */
	public static Try<String> getLayoutTemplateContent(final Path zipPath, final String layoutTemplatePath) {
		return ZipUtil.readFileFromZip(zipPath, layoutTemplatePath);
	}

	/**
	 * Add a new layout template in zipPath. To be successful the layout template content needs to be added at the root
	 * of the zipPath and the WEB-INF/liferay-layout-templates.xml needs to be adapted to reference the created file.
	 *
	 * @param zipPath        the war file where the layout template will be added.
	 * @param layoutTemplate contains the layout template metadata (id, name).
	 * @param content        the layout template xml
	 * @return whether the try to add the layout template in the war was successful or not.
	 */
	public static Try createLayoutTemplate(final Path zipPath, final LayoutTemplate layoutTemplate, final String content) {
		final Try<String> layoutTemplatesXml = getLayoutTemplatesXml(zipPath);

		if (layoutTemplatesXml.isFailure()) {
			return Try.failure(layoutTemplatesXml.getCause());
		}

		final Try<Document> tryToAddXml = XmlUtil.addCustomLayoutTemplate(layoutTemplatesXml.get(), layoutTemplate);

		if (tryToAddXml.isFailure()) {
			return Try.failure(tryToAddXml.getCause());
		}

		final Try<String> xml = Try.of(() ->
				tryToAddXml.get().formattedString()
		);

		if (xml.isFailure()) {
			return Try.failure(xml.getCause());
		}

		final Try<Path> tryToUpdateXmlConfig = ZipUtil.writeFileToZip(zipPath, getXmlConfigPathInZip(), xml.get());

		if (tryToUpdateXmlConfig.isFailure()) {
			return Try.failure(tryToUpdateXmlConfig.getCause());
		}

		final Try tryToAddZip = ZipUtil.writeFileToZip(zipPath, layoutTemplate.getTemplatePath(), content);

		if (tryToAddZip.isFailure()) {
			return Try.failure(tryToAddZip.getCause());
		}

		return Try.success(layoutTemplate);
	}

	/**
	 * Delete an existing layout template from the zip passed as parameter.
	 *
	 * @param zipPath        the zip file containing the template to be deleted.
	 * @param layoutTemplate the template to be deleted.
	 * @return whether the try to delete the template was successful or not.
	 */
	public static Try deleteLayoutTemplate(final Path zipPath, final LayoutTemplate layoutTemplate) {
		return Try.success(null);
	}

	private static Try<String> getLayoutTemplatesXml(final Path zipPath) {
		return ZipUtil.readFileFromZip(zipPath, getXmlConfigPathInZip());
	}

	private static String getXmlConfigPathInZip() {
		return WEB_INF + "/" + XML_CONFIG;
	}
}
