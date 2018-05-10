package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import be.gfi.liferay.tpl.util.LiferayUtil;
import be.gfi.liferay.tpl.util.ZipUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.StringPool;
import io.vavr.control.Try;
import org.osgi.service.component.annotations.Component;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.File;
import java.nio.file.Path;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/edit_template"
		},
		service = MVCRenderCommand.class
)
public class EditTemplateMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(final RenderRequest renderRequest, final RenderResponse renderResponse) {
		final String layoutTemplatePath = renderRequest.getParameter("layoutTemplatePath");
		final Path layoutTemplateWar = LiferayUtil.getOsgiWarFolder().resolve("my-liferay-layout-layouttpl.war");

		final Try<File> layoutTemplate = ZipUtil.getFileFromZip(layoutTemplateWar, layoutTemplatePath);
		final Try<String> layoutTemplateContent = LayoutTemplateUtil.getLayoutTemplateContent(layoutTemplateWar, layoutTemplatePath);

		renderRequest.setAttribute("layoutTemplate", layoutTemplate.isSuccess() ? layoutTemplate.get() : null);
		renderRequest.setAttribute("layoutTemplateContent",
				layoutTemplateContent.isSuccess()
						? layoutTemplateContent.get()
						: StringPool.BLANK
		);

		return "/edit_template.jsp";
	}
}