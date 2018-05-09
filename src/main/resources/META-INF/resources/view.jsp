<%@ include file="/init.jsp" %>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	List<LayoutTemplate> templates = (List<LayoutTemplate>) request.getAttribute("templates");
%>

<div class="container-fluid-1280">
	<div class="card main-content-card">
		<div class="card-row card-row-padded">
            <liferay-ui:search-container
                    delta="2"
                    emptyResultsMessage="No layout template found"
                    iteratorURL="<%= iteratorURL %>"
                    total="<%= templates.size() %>"
            >
                <liferay-ui:search-container-results
                        results="<%= ListUtil.subList(templates, searchContainer.getStart(), searchContainer.getEnd()) %>"
                />
                <liferay-ui:search-container-row
                        className="be.gfi.liferay.tpl.model.LayoutTemplate"
                        keyProperty="id"
                        modelVar="layoutTemplate"
                >
                    <liferay-ui:search-container-column-text
                            name="Id"
                            value="${layoutTemplate.id}"
                    />
                    <liferay-ui:search-container-column-text
                            name="Name"
                            value="${layoutTemplate.name}"
                    />
                    <liferay-ui:search-container-column-text
                            name="Template Path"
                            value="${layoutTemplate.templatePath}"
                    />
                    <liferay-ui:search-container-column-text
                            name="Thumbnail Path"
                            value="${layoutTemplate.thumbnailPath}"
                    />
                    <liferay-ui:search-container-column-jsp
                            align="right"
                            cssClass="entry-action"
                            path="/entry_actions.jsp"
                    />
                </liferay-ui:search-container-row>
                <liferay-ui:search-iterator />
            </liferay-ui:search-container>

            <portlet:renderURL var="addLayoutTemplateURL">
                    <portlet:param name="mvcRenderCommandName" value="/tpl/create_template" />
                    <portlet:param name="redirect" value="<%= currentURL %>" />
            </portlet:renderURL>

            <liferay-frontend:add-menu>
                <liferay-frontend:add-menu-item
                        title="Add template"
                        url="<%= addLayoutTemplateURL.toString() %>"
                />
            </liferay-frontend:add-menu>
        </div>
    </div>
</div>