package be.gfi.liferay.tpl.util;

import be.gfi.liferay.tpl.model.LayoutTemplate;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import io.vavr.control.Try;
import org.w3c.dom.NamedNodeMap;

import java.io.InputStream;

public class XmlUtil {

	public static Try<Element> getCustomElement(final Try<InputStream> liferayLayoutTemplatesXml) {
		if (liferayLayoutTemplatesXml.isFailure()) {
			return Try.failure(liferayLayoutTemplatesXml.getCause());
		}

		final Try<Document> document = parseXml(liferayLayoutTemplatesXml.get());

		if (document.isFailure()) {
			return Try.failure(document.getCause());
		}

		final Element rootElement = document.get().getRootElement();

		return Try.success(rootElement.element("custom"));
	}

	public static Try addCustomLayoutTemplate(final Try<InputStream> liferayLayoutTemplatesXml, final LayoutTemplate layoutTemplate) {
		final Try<Document> document = Try.withResources(liferayLayoutTemplatesXml::get).of(
				SAXReaderUtil::read
		);

		if (document.isFailure()) {
			return Try.failure(document.getCause());
		}

		final Element customElement = document.get().getRootElement().element("custom");

		customElement.add(
				createCustomLayoutTemplate(layoutTemplate)
		);

		return Try.success(customElement);
	}

	public static Element createCustomLayoutTemplate(final LayoutTemplate layoutTemplate) {
		final Element layoutTemplateElement = SAXReaderUtil.createElement("layout-template");

		layoutTemplateElement.addAttribute("id", layoutTemplate.getId());
		layoutTemplateElement.addAttribute("name", layoutTemplate.getName());

		final Element templatePath = SAXReaderUtil.createElement("template-path");
		templatePath.addText(layoutTemplate.getTemplatePath());

		final Element thumbnailPath = SAXReaderUtil.createElement("thumbnail-path");
		thumbnailPath.addText(layoutTemplate.getThumbnailPath());

		layoutTemplateElement.add(templatePath);
		layoutTemplateElement.add(thumbnailPath);

		return layoutTemplateElement;
	}

	public static Try<Document> parseXml(final InputStream liferayLayoutTemplatesXml) {
		return Try.withResources(() -> liferayLayoutTemplatesXml).of(
				UnsecureSAXReaderUtil::read
		);
	}

	public static String getAttributeValue(final NamedNodeMap attributes, final String id) {
		return attributes.getNamedItem(id).getNodeValue();
	}

	public static String getElementValue(final org.w3c.dom.Element layoutTemplateNode, final String s) {
		return layoutTemplateNode.getElementsByTagName(s)
				.item(0)
				.getNodeValue();
	}
}
