package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import org.osgi.service.component.annotations.Component;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/create_template"
		},
		service = MVCRenderCommand.class
)
public class CreateTemplateMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(final RenderRequest renderRequest, final RenderResponse renderResponse) {
		return "/create_template.jsp";
	}
}