var stompClient = null;
var token = "";
$(document).ready(function () {
    getToken();
});

function connect() {
    var socket = new SockJS('/user-sock');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/'+token+'/queue/messages', function () {
            setTimeout(function () {
                loadNotifications();
                $("#not_icon").addClass("cue");
                setTimeout(function () {
                    $("#not_icon").removeClass("cue");
                }, 1010);
            }, 1000);
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