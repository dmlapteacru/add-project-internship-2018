$("#show_not_link_down").click(function () {
    $(".notification_body").slideToggle("slow");
});
$(document).ready(function () {
    loadNotifications();
    $.each($(".filter_status"),function () {
        if ($(this).children().text() === "ACTIVE"){
            $(this).children().addClass("user-status-active");
        } else {
            $(this).children().addClass("user-status-inactive");
        }
    })
});

function loadNotifications() {
    $.ajax(
        {
            url: "/admin/notifications/unread",
            success: function (result) {
                printNotifications(result);
            }
        }
    )
}
function printNotifications(data) {
    $("#notification_").html("");
    $("#show_not_link_down").attr("value", "0");
    $("#notification_").append("<div class='notification_view_all'>" +
        "<button>View all</button>" +
        "</div>");
    var count = parseInt($("#show_not_link_down").attr("value"));
    $.each(data, function (id, object) {
        $("#notification_").append("<div class='notification_item' >" +
            "<span class='not_case' style='cursor: pointer;' onclick='goThrough(this,"+object.idSearch+")'>"+object.notificationCase.split("_").join(" ")+"!"+"</span>" +
            "<span>"+object.content+"</span>" +
            "<a id='" +object.id+ "' onclick='deleteNotification(this)'>&times;</a>" +
            "</div>");

        $("#show_not_link_down").attr("value", ++count);
    });
    if (count === 0){
        $("#show_not_link_down").text("");
    } else {
        $("#show_not_link_down").text(count);
    }
}

function goThrough(obj, id) {
    if ($(obj).text()==="NEW USER!"){
        $("#users_btn_req").click();
    } else {
        $("#messages_btn_req").click();
        showNotificatedMessage(id);
    }
    $(obj).parent().find("a").click();
}
function showNotificatedMessage(id) {
    setTimeout(function () {
        $("#table-messages tr[id='"+id+"']").toggleClass("act");
    }, 500);

}
function deleteNotification(link) {
    $(link).parent().remove();
    var count = parseInt($("#show_not_link_down").attr("value"));
    $("#show_not_link_down").attr("value", --count);
    $("#show_not_link_down").text(count);
    if (count === 0){
        $("#show_not_link_down").text(0);
    } else {
        $("#show_not_link_down").text(count);
    }
    $.ajax(
        {
            url: "/admin/notifications/changeStatus?id=" + $(link).attr("id"),
            type: "PUT"
        }
    );
}