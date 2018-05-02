function serializeFormData(formId) {
    var unindexed_array = $(formId).serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function sendJsonPayload(params) {
    var config = {
        cache: false,
        method: 'POST',
        contentType: 'application/json',
        success: console.log,
        error: processErrorResponse
    };
    $.ajax(Object.assign(config, params));
}

function sendForm(url, formId, success, error) {
    var params = {
        url: url,
        data: JSON.stringify(serializeFormData(formId))
    };
    if (success) {
        params.success = success;
    }
    if (error) {
        params.error = error;
    }
    sendJsonPayload(params);
}

function showErrorMessage(text) {

}

function processErrorResponse(data) {
    console.log(data);
}

function formatDate(date) {
    return moment(date).format('DD MMM YYYY');
}