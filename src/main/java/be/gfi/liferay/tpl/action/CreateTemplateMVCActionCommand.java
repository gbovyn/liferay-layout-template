package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.configuration.ConfigurationHelper;
import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.model.LayoutTemplate;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import be.gfi.liferay.tpl.util.LiferayUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import io.vavr.control.Try;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.nio.file.Path;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/create_template"
		},
		service = MVCActionCommand.class
)
public class CreateTemplateMVCActionCommand extends BaseMVCActionCommand {
	private static Logger logger = LoggerFactory.getLogger(EditTemplateMVCActionCommand.class.getName());

	private static final String TPL_EXT = ".tpl";
	private static final String PNG_EXT = ".png";

	private ConfigurationHelper configurationHelper;

	@Override
	protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) {
		final String id = actionRequest.getParameter("id");
		final String name = actionRequest.getParameter("name");
		final String content = actionRequest.getParameter("content");

		final String templatePath = id.replace('-', '_') + TPL_EXT;
		final String thumbnailPath = id.replace('-', '_') + PNG_EXT;

		final LayoutTemplate layoutTemplate = new LayoutTemplate(id, name, templatePath, thumbnailPath);

		final Path zipPath = LiferayUtil.getOsgiWarFolder().resolve(
				configurationHelper.getWarName()
		);

		final Try createTry = LayoutTemplateUtil.createLayoutTemplate(
				zipPath, layoutTemplate, content
		);

		if (createTry.isFailure()) {
			SessionErrors.add(actionRequest, "error", createTry.getCause());
			logger.error(createTry.getCause().getMessage(), createTry.getCause());
		}
	}

	@Activate
	private void activate() {
		configurationHelper = new ConfigurationHelper();
	}
}
