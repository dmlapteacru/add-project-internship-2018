var stompClient = null;
var token ="";
$(document).ready(function () {
    getToken();
});
function connect() {
    var socket = new SockJS('/admin-sock');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/'+ token + '/queue/messages', function (greeting) {
            setTimeout(loadNotifications(), 1050);
            $("#show_not_link_down").addClass("cue");
            setTimeout(function () {
                $("#show_not_link_down").removeClass("cue");
            }, 1010);
        });
    });
}

function getToken() {
    $.ajax(
        {
            url: "/getToken",
            success: function (response) {
                token = response;
                connect();
            }
        }
    );
}