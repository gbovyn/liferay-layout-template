<%@ include file="/init.jsp" %>

<%
	String redirect = ParamUtil.getString(request, "redirect");

	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(redirect);
%>

<portlet:resourceURL id="/tpl/existing_ids" var="existingIdsURL" />
<portlet:resourceURL id="/tpl/existing_names" var="existingNamesURL" />

<script>
var isValidId = function (val) {
    return !alreadyExist('<%= existingIdsURL %>', val);
}

var isValidName = function (val) {
    return !alreadyExist('<%= existingNamesURL %>', val);
}

var alreadyExist = function (url, val) {
    var valid = false;

    jQuery.ajax({
        url: url,
        dataType: 'json',
        method: 'GET',
        async: false,
        success: function (response) {
            valid = response.includes(val);
        },
        error: function (err) {
            console.log(err);
        }
    });

    return valid;
}
</script>

<div class="container-fluid-1280">

    <portlet:actionURL name="/tpl/create_template" var="createLayoutTemplateURL">
            <portlet:param name="redirect" value="<%= currentURL %>" />
    </portlet:actionURL>

	<aui:form action="<%= createLayoutTemplateURL %>" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

		<aui:fieldset-group markupView="lexicon">
			<aui:fieldset>

			<div class="row">
			    <div class="col-md-6">
                    <aui:input name="name" value="<%= StringPool.BLANK %>">
                        <aui:validator name="required" />
                        <aui:validator errorMessage="Name must be unique." name="custom">
                            function(val, fieldNode, ruleValue) {
                                return isValidName(val);
                            }
                        </aui:validator>
                    </aui:input>
                </div>
			    <div class="col-md-6">
                    <aui:input name="id" value="<%= StringPool.BLANK %>">
                        <aui:validator name="required" />
                        <aui:validator errorMessage="Id must be unique." name="custom">
                            function(val, fieldNode, ruleValue) {
                                return isValidId(val);
                            }
                        </aui:validator>
                    </aui:input>
                </div>
            </div>

			<div class="row">
			    <div class="col-md-12">
                    <aui:input name="content" type="textarea" value="<%= StringPool.BLANK %>" />
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