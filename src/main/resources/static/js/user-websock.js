var stompClient = null;
$(document).ready(function () {
    getToken();
});
function connect(response) {
    var socket = new SockJS('/user-sock');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/'+response+'/queue/messages', function () {

            setTimeout(function () {
                $("#show_not_link_down").addClass("cues");
            }, 1040);
            setTimeout(function () {
                $("#show_not_link_down").removeClass("cues");
            }, 1010);
            setTimeout(function(){
                loadNotifications();
            } , 1010);
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