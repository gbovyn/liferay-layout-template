<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.portlet.PortletURL" %>

<%@ page import="com.liferay.document.library.configuration.DLConfiguration" %>
<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ page import="com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil" %>
<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.PortalUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>
<%@ page import="com.liferay.portal.kernel.util.TempFileEntryUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>

<%@ page import="be.gfi.liferay.tpl.model.LayoutTemplate" %>
<%@ page import="be.gfi.liferay.tpl.portlet.LayoutTemplatePortlet" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
    String currentURL = PortalUtil.getCurrentURL(request);
%>