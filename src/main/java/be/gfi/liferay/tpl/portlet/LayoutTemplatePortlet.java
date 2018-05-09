package be.gfi.liferay.tpl.portlet;

import aQute.bnd.annotation.metatype.Configurable;
import be.gfi.liferay.tpl.configuration.LayoutTemplateConfiguration;
import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.model.LayoutTemplate;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import io.vavr.control.Try;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import javax.portlet.*;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component(
		configurationPid = "be.gfi.liferay.tpl.configuration.LayoutTemplateConfiguration",
		immediate = true,
		property = {
				"com.liferay.portlet.display-category=category.sample",
				"com.liferay.portlet.instanceable=true",
				"javax.portlet.display-name=Layout Template Portlet",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
)
public class LayoutTemplatePortlet extends MVCPortlet {

	private volatile LayoutTemplateConfiguration configuration;

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		configuration = ConfigurableUtil.createConfigurable(
				LayoutTemplateConfiguration.class, properties
		);
	}

	@Override
	public void doView(final RenderRequest renderRequest, final RenderResponse renderResponse) throws IOException, PortletException {
		String warName = getLayoutTemplateWarName();

		final Try<List<LayoutTemplate>> customLayoutTemplates = LayoutTemplateUtil.getCustomLayoutTemplates(warName);

		renderRequest.setAttribute("templates", customLayoutTemplates.isSuccess()
				? customLayoutTemplates.get()
				: Collections.emptyList()
		);

		super.doView(renderRequest, renderResponse);
	}

	private String getLayoutTemplateWarName() {
		return configuration.warName(); // "my-liferay-layout-layouttpl.war";//StringPool.BLANK;
	}

}