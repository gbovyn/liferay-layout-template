<%@ include file="/init.jsp" %>

<%
	String redirect = ParamUtil.getString(request, "redirect");

	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(redirect);

    File layoutTemplate = (ZipEntry) request.getAttribute("layoutTemplate");
	String templateContent = (String) request.getAttribute("templateContent");
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
			        <aui:input readonly="<%= true %>" name="name" value="<%= (layoutTemplate != null) ? layoutTemplate.getName() : StringPool.BLANK %>" />
				</div>
                <div class="col-md-6">
                    <aui:input readonly="<%= true %>" name="last-modified-time" value="<%= (layoutTemplate != null) ? layoutTemplate.getLastModifiedTime() : StringPool.BLANK %>" />
                </div>
            </div>

			<div class="row">
			    <div class="col-md-12">
                    <aui:input name="content" type="textarea" value="<%= (templateContent != null) ? templateContent : StringPool.BLANK %>" />
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