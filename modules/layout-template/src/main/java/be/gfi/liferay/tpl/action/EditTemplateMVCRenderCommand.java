package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.configuration.ConfigurationHelper;
import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import be.gfi.liferay.tpl.util.LiferayUtil;
import be.gfi.liferay.tpl.util.ZipUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
				"mvc.command.name=/tpl/edit_template"
		},
		service = MVCRenderCommand.class
)
public class EditTemplateMVCRenderCommand implements MVCRenderCommand {
	private static final Logger logger = LoggerFactory.getLogger(EditTemplateMVCActionCommand.class.getName());

	private volatile ConfigurationHelper configurationHelper;

	@Override
	public String render(final RenderRequest renderRequest, final RenderResponse renderResponse) {
		final String layoutTemplatePath = renderRequest.getParameter("layoutTemplatePath");
		final Path layoutTemplateWar = LiferayUtil.getOsgiWarFolder().resolve(
				configurationHelper.getWarName()
		);

		logger.debug("Editing %s from %s", layoutTemplatePath, layoutTemplateWar.toString());

		final Try<Path> layoutTemplate = ZipUtil.getFileFromZip(layoutTemplateWar, layoutTemplatePath);
		final Try<BasicFileAttributes> layoutTemplateAttributes = ZipUtil.getFileAttributesFromZip(layoutTemplateWar, layoutTemplatePath);
		final Try<String> layoutTemplateContent = LayoutTemplateUtil.getLayoutTemplateContent(layoutTemplateWar, layoutTemplatePath);

		renderRequest.setAttribute("layoutTemplateName",
				layoutTemplate.isSuccess()
						? layoutTemplate.get().toString()
						: StringPool.BLANK
		);
		renderRequest.setAttribute("layoutTemplateLastModifiedTime",
				layoutTemplateAttributes.isSuccess() ?
						getFormattedDateFromFileTime(
								layoutTemplateAttributes.get().lastModifiedTime(),
								getUserZoneId(renderRequest),
								getUserLocale(renderRequest)
						) : StringPool.BLANK
		);
		renderRequest.setAttribute("layoutTemplateSize",
				layoutTemplateAttributes.isSuccess()
						? FileUtils.byteCountToDisplaySize(layoutTemplateAttributes.get().size())
						: StringPool.BLANK
		);
		renderRequest.setAttribute("layoutTemplateContent",
				layoutTemplateContent.isSuccess()
						? layoutTemplateContent.get()
						: StringPool.BLANK
		);

		return "/edit_template.jsp";
	}

	private String getFormattedDateFromFileTime(FileTime time, ZoneId userZoneId, Locale userLocale) {
		final ZonedDateTime localDateTimeDate = ZonedDateTime.ofInstant(
				Instant.ofEpochMilli(time.toMillis()), userZoneId
		);

		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(userLocale);

		return localDateTimeDate.format(formatter);
	}

	private Locale getUserLocale(RenderRequest request) {
		return getUser(request).getLocale();
	}

	private ZoneId getUserZoneId(RenderRequest request) {
		return ZoneId.of(
				getUser(request).getTimeZoneId()
		);
	}

	private User getUser(RenderRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

		return themeDisplay.getUser();
	}

	@Activate
	private void activate() {
		configurationHelper = new ConfigurationHelper();
	}
}