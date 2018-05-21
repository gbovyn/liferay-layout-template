package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.configuration.ConfigurationHelper;
import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.model.LayoutTemplate;
import be.gfi.liferay.tpl.portlet.LayoutTemplatePortlet;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import be.gfi.liferay.tpl.util.LiferayUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.Validator;
import io.vavr.control.Try;
import org.apache.commons.io.input.NullInputStream;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.InputStream;
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

    private static final Logger logger = LoggerFactory.getLogger(EditTemplateMVCActionCommand.class.getName());

    private ConfigurationHelper configurationHelper;

    @Override
    protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) {
        final String id = actionRequest.getParameter("id");
        final String name = actionRequest.getParameter("name");
        final String content = actionRequest.getParameter("content");
        final String thumbnailFileName = actionRequest.getParameter("selectUploadedFile");

        final InputStream thumbnail = getThumbnailFile(thumbnailFileName).getOrElse(new NullInputStream(0));

        final String templatePath = LayoutTemplateUtil.getTemplatePath(id);
        final String thumbnailPath = LayoutTemplateUtil.getThumbnailPath(id);

        final LayoutTemplate layoutTemplate = new LayoutTemplate(id, name, templatePath, thumbnailPath);

        final Path zipPath = LiferayUtil.getOsgiWarFolder().resolve(
                configurationHelper.getWarName()
        );

        final Try createTry = LayoutTemplateUtil.createLayoutTemplate(
                zipPath,
                layoutTemplate,
                content,
                thumbnail
        );

        if (createTry.isFailure()) {
            SessionErrors.add(actionRequest, "error", createTry.getCause());
            logger.error(createTry.getCause().getMessage(), createTry.getCause());
        }
    }

    private Try<InputStream> getThumbnailFile(final String filename) {
        if (Validator.isBlank(filename)) {
            return Try.success(new NullInputStream(0));
        }

        final long companyId = PortalUtil.getDefaultCompanyId();

        final Try<Long> groupId = Try.of(() ->
                GroupLocalServiceUtil.getCompanyGroup(companyId).getGroupId()
        );

        if (groupId.isFailure()) {
            return Try.failure(groupId.getCause());
        }

        final Try<Long> userId = Try.of(() ->
                UserLocalServiceUtil.getDefaultUser(companyId).getUserId()
        );

        if (userId.isFailure()) {
            return Try.failure(userId.getCause());
        }

        final Try<FileEntry> tempFileEntry = Try.of(() ->
                TempFileEntryUtil.getTempFileEntry(
                        groupId.get(),
                        userId.get(),
                        LayoutTemplatePortlet.TEMP_FOLDER_NAME,
                        filename
                )
        );

        if (tempFileEntry.isFailure()) {
            return Try.failure(tempFileEntry.getCause());
        }

        final Try<InputStream> thumbnailStream = Try.of(() -> tempFileEntry.get().getContentStream());

        if (thumbnailStream.isFailure()) {
            return Try.failure(thumbnailStream.getCause());
        }

        return thumbnailStream;
    }

    @Activate
    private void activate() {
        configurationHelper = new ConfigurationHelper();
    }
}
