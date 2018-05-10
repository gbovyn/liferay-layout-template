package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.model.LayoutTemplate;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import be.gfi.liferay.tpl.util.LiferayUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import io.vavr.control.Try;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/create_template"
		},
		service = MVCActionCommand.class
)
public class CreateTemplateMVCActionCommand extends BaseMVCActionCommand {
	@Override
	protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) {
		final String id = actionRequest.getParameter("id");
		final String name = actionRequest.getParameter("name");
		final String content = actionRequest.getParameter("content");

		final String templatePath = id.replace('-', '_') + ".ftl";
		final String thumbnailPath = id.replace('-', '_') + ".png";

		final LayoutTemplate layoutTemplate = new LayoutTemplate(id, name, templatePath, thumbnailPath);

		final Try createTry = LayoutTemplateUtil.createLayoutTemplate(
				LiferayUtil.getOsgiWarFolder().resolve("my-liferay-layout-layouttpl.war"), layoutTemplate, content
		);

		if (createTry.isFailure()) {
			SessionErrors.add(actionRequest, "error", createTry.getCause());
		}
	}
}
