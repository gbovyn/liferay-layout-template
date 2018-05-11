<%@ include file="/init.jsp" %>

<%
    ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    LayoutTemplate layoutTemplate = (LayoutTemplate) row.getObject();
%>

<liferay-ui:icon-menu
	cssClass="dropdown-menu-indicator-end"
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>

    <portlet:renderURL var="editLayoutTemplateURL">
        <portlet:param name="mvcRenderCommandName" value="/tpl/edit_template" />
        <portlet:param name="redirect" value="<%= currentURL %>" />
        <portlet:param name="layoutTemplatePath" value="<%= String.valueOf(layoutTemplate.getTemplatePath()) %>" />
    </portlet:renderURL>

    <liferay-ui:icon
        message="edit"
        url="<%= editLayoutTemplateURL %>"
    />

    <portlet:actionURL name="/tpl/delete_template" var="deleteLayoutTemplateURL">
        <portlet:param name="redirect" value="<%= currentURL %>" />
        <portlet:param name="id" value="<%= String.valueOf(layoutTemplate.getId()) %>" />
        <portlet:param name="name" value="<%= String.valueOf(layoutTemplate.getName()) %>" />
        <portlet:param name="templatePath" value="<%= String.valueOf(layoutTemplate.getTemplatePath()) %>" />
        <portlet:param name="thumbnailPath" value="<%= String.valueOf(layoutTemplate.getThumbnailPath()) %>" />
    </portlet:actionURL>

    <liferay-ui:icon
        message="delete"
        url="<%= deleteLayoutTemplateURL %>"
    />

</liferay-ui:icon-menu>