var subject;
var title;
var message;

function promptsca() {
    $('#sca').modal('show');

    $("#sca-img").removeClass().addClass("sca-pending");

    var settings = {
        "async": true,
        "url": "https://" + window.location.hostname + "/pingidsdk-server/push",
        "method": "POST",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": "{ \"subject\": \"" + subject + "\", \"title\": \"" + title + "\", \"message\": \"" + message + "\" }"
    }

    $.ajax(settings).done(function (response) {
        setTimeout(function(){checkstatus(response.id);}, 1000);
    });
}

function checkstatus(id) {
    var settings = {
        "async": true,
        "url": "https://" + window.location.hostname + "/pingidsdk-server/push/status",
        "method": "POST",
        "headers": {
            "Content-Type": "application/json",
        },
        "data": "{\"subject\": \"" + subject + "\", \"id\": \"" + id + "\"}"
    }

    $.ajax(settings).done(function (response) {
        if (response.status === "in progress") {
            setTimeout(function(){checkstatus(response.id);}, 1000);
        } else if (response.status === "approved") {
            $("#sca-img").removeClass().addClass("sca-approved");
            setTimeout(function(){$("#consent").submit();}, 2000);
        } else if (response.status === "rejected") {
            $("#sca-img").removeClass().addClass("sca-denied");
        } else if (response.status === "timeout") {
            $("#sca-img").removeClass().addClass("sca-timeout");
        } else {
            $("#sca-img").removeClass().addClass("sca-error");
        }
    });
}