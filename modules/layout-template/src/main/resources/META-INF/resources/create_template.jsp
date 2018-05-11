<%@ include file="/init.jsp" %>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);
%>

<portlet:resourceURL id="/tpl/existing_ids" var="existingIdsURL" />
<portlet:resourceURL id="/tpl/existing_names" var="existingNamesURL" />

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
                            <aui:validator errorMessage="Only alphanumeric, space and dash characters are allowed." name="custom">
                                function(val, fieldNode, ruleValue) {
                                    var regex = new RegExp(/^[a-zA-Z0-9-\s]+$/i);

                                    return regex.test(val);
                                }
                            </aui:validator>
                            <aui:validator errorMessage="The name cannot start or end with a dash." name="custom">
                                function(val, fieldNode, ruleValue) {
                                    var regex = new RegExp(/^(?!-).*(?<!-)$/i);

                                    return regex.test(val);
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
                            <aui:validator errorMessage="Only the alphanumeric and the dash characters are allowed." name="custom">
                                function(val, fieldNode, ruleValue) {
                                    var regex = new RegExp(/^[a-zA-Z0-9-]+$/i);

                                    return regex.test(val);
                                }
                            </aui:validator>
                            <aui:validator errorMessage="The id cannot start or end with a dash." name="custom">
                                function(val, fieldNode, ruleValue) {
                                    var regex = new RegExp(/^(?!-).*(?<!-)$/i);

                                    return regex.test(val);
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

<aui:script>
    function generateIdOnInput() {
        var validation = Liferay.component('<portlet:namespace />Validation');

        if (validation) {
            return validation.generateIdOnInput();
        }
    }

    function validateOnInput() {
        var validation = Liferay.component('<portlet:namespace />Validation');

        if (validation) {
            return validation.validateOnInput();
        }
    }

    function isValidName(value) {
        var validation = Liferay.component('<portlet:namespace />Validation');

        if (validation) {
            return validation.isValidName('<%= existingNamesURL %>', value);
        }
    }

    function isValidId(value) {
        var validation = Liferay.component('<portlet:namespace />Validation');

        if (validation) {
            return validation.isValidId('<%= existingIdsURL %>', value);
        }
    }
</aui:script>

<aui:script use="layout-template-validation">
    var validation = Liferay.component(
        '<portlet:namespace />Validation',
        new Liferay.Validation(
            {
                namespace: '<portlet:namespace />'
            }
        )
    );

    Liferay.on('allPortletsReady', function () {
        generateIdOnInput();
        validateOnInput();
    });
</aui:script>