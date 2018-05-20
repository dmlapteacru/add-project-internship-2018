var stompClient = null;
$(document).ready(function () {
    connect();
});

function connect() {
    var socket = new SockJS('/user-sock');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/'+$("#user").text()+'/queue/messages', function () {
            loadNotifications();
            $("#not_icon").addClass("cue");
            setTimeout(function () {
                $("#not_icon").removeClass("cue");
            }, 1010);
        });
    });
}

