package be.gfi.liferay.tpl.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
        category = "platform",
        scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
        id = "be.gfi.liferay.tpl.configuration.LayoutTemplateConfiguration",
        localization = "content/Language",
        name = "layout-template-portlet-configuration-name"
)
public interface LayoutTemplateConfiguration {

    @Meta.AD(deflt = "my-liferay-layout-layouttpl.war", name = "war-name", required = false)
    String warName();

    @Meta.AD(deflt = "osgi/war", name = "osgi-war-folder", required = false)
    String osgiWarFolder();

}
