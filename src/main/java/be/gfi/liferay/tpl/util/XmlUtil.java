package be.gfi.liferay.tpl.util;

import be.gfi.liferay.tpl.model.LayoutTemplate;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import io.vavr.control.Try;
import org.w3c.dom.NamedNodeMap;

public class XmlUtil {

	private static final String CUSTOM_ELEMENT = "custom";
	private static final String LAYOUT_TEMPLATE = "layout-template";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String TEMPLATE_PATH = "template-path";
	private static final String THUMBNAIL_PATH = "thumbnail-path";

	public static Try<Element> getCustomElement(final Try<String> liferayLayoutTemplatesXml) {
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

	public static Try<Document> addCustomLayoutTemplate(final String liferayLayoutTemplatesXml, final LayoutTemplate layoutTemplate) {
		final Try<Document> document = Try.of(() ->
				SAXReaderUtil.read(liferayLayoutTemplatesXml)
		);

		if (document.isFailure()) {
			return Try.failure(document.getCause());
		}

		final Element customElement = document.get().getRootElement().element(CUSTOM_ELEMENT);

		customElement.add(
				createCustomLayoutTemplate(layoutTemplate)
		);

		return document;
	}

	public static Element createCustomLayoutTemplate(final LayoutTemplate layoutTemplate) {
		final Element layoutTemplateElement = SAXReaderUtil.createElement(LAYOUT_TEMPLATE);

		layoutTemplateElement.addAttribute(ID, layoutTemplate.getId());
		layoutTemplateElement.addAttribute(NAME, layoutTemplate.getName());

		final Element templatePath = SAXReaderUtil.createElement(TEMPLATE_PATH);
		templatePath.addText(layoutTemplate.getTemplatePath());

		final Element thumbnailPath = SAXReaderUtil.createElement(THUMBNAIL_PATH);
		thumbnailPath.addText(layoutTemplate.getThumbnailPath());

		layoutTemplateElement.add(templatePath);
		layoutTemplateElement.add(thumbnailPath);

		return layoutTemplateElement;
	}

	public static Try<Document> parseXml(final String liferayLayoutTemplatesXml) {
		return Try.of(() ->
				UnsecureSAXReaderUtil.read(liferayLayoutTemplatesXml)
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
