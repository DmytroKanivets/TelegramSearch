function sendJsonPayload(params) {
    var config = {
        cache: false,
        method: 'POST',
        contentType: 'application/json',
        success: console.log,
        nok: processErrorResponse,
        error: function(response) {
            processErrorResponse(response.responseJSON);
        }
    };

    var request = Object.assign(config, params);

    request.success = function (response) {
        if (response.status == 'ok') {
            request.ok(response);
        } else {
            request.nok(response);
        }
    };

    $.ajax(request);
}

function showErrorMessage(text) {
    var html =
            '<div class="alert alert-danger">\
                <button type="button" class="close" data-dismiss="alert">&times;</button>' +
                text + '\
            </div>';

    var element = $(html);

    $('#alert-container').append(element);

    setTimeout(() => element.alert('close'), 2000);
}

function processErrorResponse(data) {
    showErrorMessage(data.message);
}

function formatDate(date) {
    return moment(date).format('DD MMM YYYY');
}

function handleClear(element) {
    $(element)[0].parentElement.parentElement.parentElement.childNodes[0].childNodes[1].childNodes[0].click();
}