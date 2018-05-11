var generateIdOnInput = function (namespace) {
    jQuery('#' + namespace + 'name').on('input', function (e) {
       var name = jQuery(this).val();
       var id = name.toLowerCase().replace(/ /g, '-');

       jQuery('#' + namespace + 'id').val(id);
    });
}

var validateOnInput = function (namespace) {
    jQuery('#' + namespace + 'name').on('input', function (e) {
        var formValidator = Liferay.Form.get(namespace + 'fm').formValidator;

        formValidator.validateField(namespace + 'name');
        formValidator.validateField(namespace + 'id');
    });
}

var isValidId = function (resourceUrl, val) {
    return !alreadyExist(resourceUrl, val);
}

var isValidName = function (resourceUrl, val) {
    return !alreadyExist(resourceUrl, val);
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