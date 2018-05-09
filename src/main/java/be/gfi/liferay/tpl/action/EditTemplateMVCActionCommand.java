package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import io.vavr.control.Try;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.nio.file.Paths;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/edit_template"
		},
		service = MVCActionCommand.class
)
public class EditTemplateMVCActionCommand extends BaseMVCActionCommand {
	@Override
	protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) {
		final String redirect = ParamUtil.getString(actionRequest, "redirect");

		final String name = actionRequest.getParameter("name");
		final String content = actionRequest.getParameter("content");

		// TODO refactor
		final Try delete = LayoutTemplateUtil.deleteFileFromZip(getLayoutTemplateWarPath(), name);
		if (delete.isFailure()) {
			SessionErrors.add(actionRequest, "delete-error");
		}
		final Try update = LayoutTemplateUtil.addFileInZip(getLayoutTemplateWarPath(), name, content);
		if (update.isFailure()) {
			SessionErrors.add(actionRequest, "update-error");
		}
	}

	private String getLayoutTemplateWarPath() {
		return Paths.get(
				LayoutTemplateUtil.getOsgiWarFolder().toString(),
				getLayoutTemplateWarName()
		).toString().replace('\\', '/'); // TODO cleaner
	}

	private String getLayoutTemplateWarName() {
		return "my-liferay-layout-layouttpl.war"; // TODO use configuration
	}
}