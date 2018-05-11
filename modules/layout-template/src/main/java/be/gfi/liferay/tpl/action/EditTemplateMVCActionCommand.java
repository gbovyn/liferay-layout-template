package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.configuration.ConfigurationHelper;
import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.util.LiferayUtil;
import be.gfi.liferay.tpl.util.ZipUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
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
				"mvc.command.name=/tpl/edit_template"
		},
		service = MVCActionCommand.class
)
public class EditTemplateMVCActionCommand extends BaseMVCActionCommand {
	private static final Logger logger = LoggerFactory.getLogger(EditTemplateMVCActionCommand.class.getName());

	private ConfigurationHelper configurationHelper;

	@Override
	protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) {
		final String redirect = ParamUtil.getString(actionRequest, "redirect");

		final String name = actionRequest.getParameter("name");
		final String content = actionRequest.getParameter("content");

		final Try<Path> tryToUpdate = ZipUtil.writeFileToZip(
				LiferayUtil.getOsgiWarFolder().resolve(configurationHelper.getWarName()), name, content
		);

		if (tryToUpdate.isFailure()) {
			SessionErrors.add(actionRequest, "error", tryToUpdate.getCause());
			logger.error(tryToUpdate.getCause().getMessage());
		}
	}

	@Activate
	private void activate() {
		configurationHelper = new ConfigurationHelper();
	}
}