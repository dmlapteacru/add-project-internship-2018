var stompClient = null;
$(document).ready(function () {
   connect();
});
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

function connect() {
    var socket = new SockJS('/websocket-example');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/user', function (greeting) {
            showGreeting();
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var json = {
        "notificationCase" : "NEW_USER",
        "content" : "Registered new user.",
        "userTo" : "admin"
    };
    stompClient.send("/app/user", {}, JSON.stringify(json));
}

function showGreeting() {
    loadNotifications();

    $("#not_icon").addClass("cue");
    setTimeout(function () {
        $("#not_icon").removeClass("cue");
    }, 1010);
    var count = parseInt($("#not_count").attr("value"));
    $("#not_count").attr("value", ++count);
    if (count === 0){
        $("#not_count").text("");
        $("#not_icon").css("visibility", "hidden");
    } else {
        $("#not_count").text(count);
        $("#not_icon").css("visibility", "visible");
    }
}
//
// $(function () {
//     $("form").on('submit', function (e) {
//         e.preventDefault();
//     });
//     $( "#connect" ).click(function() { connect(); });
//     $( "#disconnect" ).click(function() { disconnect(); });
//     $( "#send" ).click(function() { sendName(); });
// });

//

