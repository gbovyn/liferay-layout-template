package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.portlet.LayoutTemplatePortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.File;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
                "mvc.command.name=/tpl/upload_thumbnail"
        },
        service = MVCActionCommand.class
)
public class UploadThumbnailMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(final ActionRequest actionRequest, final ActionResponse actionResponse) throws Exception {

        final long companyId = PortalUtil.getDefaultCompanyId();

        final long groupId = GroupLocalServiceUtil.getCompanyGroup(companyId).getGroupId();
        final long userId = UserLocalServiceUtil.getDefaultUser(companyId).getUserId();

        final UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
        final File file = uploadPortletRequest.getFile("file");
        final String filename = uploadPortletRequest.getFullFileName("file");

        TempFileEntryUtil.addTempFileEntry(
                groupId,
                userId,
                LayoutTemplatePortlet.TEMP_FOLDER_NAME,
                filename,
                file,
                MimeTypesUtil.getContentType(file)
        );
    }
}
