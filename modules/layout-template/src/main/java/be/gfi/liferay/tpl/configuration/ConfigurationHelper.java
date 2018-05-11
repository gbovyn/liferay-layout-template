package be.gfi.liferay.tpl.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;

@Component(
		immediate = true
)
public class ConfigurationHelper {
	private static volatile LayoutTemplateConfiguration configuration;

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		configuration = ConfigurableUtil.createConfigurable(
				LayoutTemplateConfiguration.class, properties
		);
	}

	public String getWarName() {
		return configuration.warName();
	}
}
