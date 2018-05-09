package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/delete_template"
		},
		service = MVCActionCommand.class
)
public class DeleteTemplateMVCActionCommand extends BaseMVCActionCommand {
	@Override
	protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) throws Exception {

	}
}
