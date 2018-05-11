AUI.add(
    'layout-template-validation',
    function (A) {

        var Lang = A.Lang;

        var Validation = A.Component.create(
            {
                ATTRS: {
                    namespace: {
                        validator: Lang.isString
                    }
                },

                EXTENDS: A.Base,

                NAME: 'layout-template-validation',

                NS: 'layout-template-validation',

                prototype: {

                    generateIdOnInput: function () {
                        var instance = this;
                        var namespace = instance.get('namespace');

                        jQuery('#' + namespace + 'name').on('input', function (e) {
                            var name = jQuery(this).val();
                            var id = '';

                            if (name) {
                                id = name.trim().toLowerCase().replace(/\s+/g, '-');
                            }

                            jQuery('#' + namespace + 'id').val(id);
                        });
                    },

                    validateOnInput: function () {
                        var instance = this;
                        var namespace = instance.get('namespace');

                        jQuery('#' + namespace + 'name').on('input', function (e) {
                            var formValidator = Liferay.Form.get(namespace + 'fm').formValidator;

                            formValidator.validateField(namespace + 'name');
                            formValidator.validateField(namespace + 'id');
                        });
                    },

                    isValidId: function (resourceUrl, val) {
                        var instance = this;

                        return !instance.alreadyExist(resourceUrl, val);
                    },

                    isValidName: function (resourceUrl, val) {
                        var instance = this;

                        return !instance.alreadyExist(resourceUrl, val);
                    },

                    alreadyExist: function (url, val) {
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
                }
            }
        );

        Liferay.Validation = Validation;
    },
    '',
    {
        requires: ['aui-base', 'aui-io-request']
    }
)