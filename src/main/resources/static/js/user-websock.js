var stompClient = null;
$(document).ready(function () {
    getToken();
});
function connect(response) {
    var socket = new SockJS('/user-sock');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/'+response+'/queue/messages', function () {
            setTimeout(loadNotifications(), 1100);
            $("#show_not_link_down").addClass("cues");
            setTimeout(function () {
                $("#show_not_link_down").removeClass("cues");
            }, 1010);
        });
    });
}

function getToken() {
    $.ajax(
        {
            url: "/getToken",
            success: function (response) {
                connect(response);
            }
        }
    );
}