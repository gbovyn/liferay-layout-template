<%@ include file="/init.jsp" %>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);

    String layoutTemplateName = (String) request.getAttribute("layoutTemplateName");
    String layoutTemplateLastModifiedTime = (String) request.getAttribute("layoutTemplateLastModifiedTime");
    String layoutTemplateSize = (String) request.getAttribute("layoutTemplateSize");
    String layoutTemplateContent = (String) request.getAttribute("layoutTemplateContent");
%>

<div class="container-fluid-1280">

    <portlet:actionURL name="/tpl/edit_template" var="updateLayoutTemplateURL">
            <portlet:param name="redirect" value="<%= currentURL %>" />
    </portlet:actionURL>

    <aui:form action="<%= updateLayoutTemplateURL %>" method="post" name="fm">
        <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
        <aui:input name="redirect" type="hidden" value="<%= redirect %>" />

        <aui:fieldset-group markupView="lexicon">
            <aui:fieldset>

            <div class="row">
                <div class="col-md-6">
                    <aui:input readonly="<%= true %>" name="name" value="<%= layoutTemplateName %>" />
                </div>
                <div class="col-md-2">
                    <aui:input readonly="<%= true %>" name="size" value="<%= layoutTemplateSize %>" />
                </div>
                <div class="col-md-4">
                    <aui:input readonly="<%= true %>" name="last-modified-time" value="<%= layoutTemplateLastModifiedTime %>" />
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <aui:input name="content" type="textarea" value="<%= layoutTemplateContent %>" />
                </div>
            </div>

            </aui:fieldset>
        </aui:fieldset-group>

        <aui:button-row>
            <aui:button type="submit" />

            <aui:button href="<%= redirect %>" type="cancel" />
        </aui:button-row>
    </aui:form>

</div>