package be.gfi.liferay.tpl.action;

import be.gfi.liferay.tpl.configuration.ConfigurationHelper;
import be.gfi.liferay.tpl.constants.LayoutTemplatePortletKeys;
import be.gfi.liferay.tpl.util.LayoutTemplateUtil;
import be.gfi.liferay.tpl.util.LiferayUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import io.vavr.control.Try;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component(
        property = {
                "javax.portlet.name=" + LayoutTemplatePortletKeys.LayoutTemplate,
                "mvc.command.name=/tpl/existing_ids",
                "mvc.command.name=/tpl/existing_names"
        },
        service = MVCResourceCommand.class
)
public class CreateTemplateMVCResourceCommand implements MVCResourceCommand {

    private static final Logger logger = LoggerFactory.getLogger(EditTemplateMVCActionCommand.class.getName());

    private ConfigurationHelper configurationHelper;

    @Override
    public boolean serveResource(final ResourceRequest resourceRequest, final ResourceResponse resourceResponse) {
        final Path zipPath = LiferayUtil.getOsgiWarFolder().resolve(
                configurationHelper.getWarName()
        );

        final List<String> existing = getExistingList(
                zipPath,
                resourceRequest.getResourceID()
        );

        final JSONSerializer serializer = JSONFactoryUtil.createJSONSerializer();
        final String jsonArray = serializer.serialize(existing);

        final Try<Void> tryGetWriter = Try.run(() -> {
            final PrintWriter writer = resourceResponse.getWriter();

            writer.write(jsonArray);
        });

        return tryGetWriter.isFailure();
    }

    private List<String> getExistingList(final Path zipPath, final String resourceId) {
        switch (resourceId) {
            case "/tpl/existing_ids":
                return LayoutTemplateUtil.getExistingTemplateIds(zipPath);
            case "/tpl/existing_names":
                return LayoutTemplateUtil.getExistingTemplateNames(zipPath);
            default:
                return Collections.emptyList();
        }
    }

    @Activate
    private void activate() {
        configurationHelper = new ConfigurationHelper();
    }
}
