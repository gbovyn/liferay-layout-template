package be.gfi.liferay.tpl.configuration;

import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;

public class LayoutTemplatePortletConfigurationBeanDeclaration implements ConfigurationBeanDeclaration {

	@Override
	public Class getConfigurationBeanClass() {
		return LayoutTemplateConfiguration.class;
	}
}
