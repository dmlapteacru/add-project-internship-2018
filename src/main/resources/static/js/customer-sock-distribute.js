$("#show_not_link_down").click(function () {
    $(".notification_body").slideToggle("slow");
});
$(document).ready(function () {
    loadNotifications();
});

function loadNotifications() {
    $.ajax(
        {
            url: "/notifications/unread",
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
        "<a href='/notifications/view-all' class='view_all_not'>View all</a>" +
        "</div>");
    var count = parseInt($("#show_not_link_down").attr("value"));
    $.each(data, function (id, object) {
        $("#notification_").append("<div class='notification_item'>" +
            "<span class='not_case' onclick='goThrough(this,"+object.idSearch+")'>" + object.notificationCase.split("_").join(" ")+"!"+"</span>" +
            "<span>"+object.content+"</span>" +
            "<a id='" +object.id+ "' onclick='deleteNotification(this)' style='font-size: 13pt;'>&times;</a>" +
            "</div>");
        $("#show_not_link_down").attr("value", ++count);
        $("#show_not_link_down").text(count);
    });
    if (count === 0){
        $("#show_not_link_down").text("");
        $("#show_not_link_down").css("background-color", "#fff");
        $("#show_not_link_down").addClass("white");
        $("#show_not_link_down").removeClass("red");
    } else {
        $("#show_not_link_down").text(count);
        $("#show_not_link_down").css("background-color", "#f15928");
        $("#show_not_link_down").addClass("red");
        $("#show_not_link_down").removeClass("white");
    }
}

function deleteNotification(link) {
    $(link).parent().remove();
    var count = parseInt($("#show_not_link_down").attr("value"));
    count-=1;
    $("#show_not_link_down").attr("value", count);
    $("#show_not_link_down").text(count);
    if (count === 0){
        $("#show_not_link_down").text("");
        $("#show_not_link_down").css("background-color", "#fff");
        $("#show_not_link_down").toggleClass("white");
        $("#show_not_link_down").toggleClass("red");
    } else {
        $("#show_not_link_down").text(count);
        $("#show_not_link_down").css("background-color", "#f15928");
    }
    $.ajax(
        {
            url: "/notifications/changeStatus?id=" + $(link).attr("id"),
            type: "PUT"
        }
    );
}

$(".mark_as_read button").click(function () {
    $.ajax(
        {
            url: "/notifications/changeStatus?id=" + $(this).attr("id"),
            type: "PUT"
        }
    );
    $(this).css("display", "none");
});

$(".view_notification a").click(function () {
    $.ajax(
        {
            url: "/notifications/changeStatus?id=" + $(this).attr("id"),
            type: "PUT"
        }
    );
});
//GO THROUGH NOTIFICATIONS
//
function goThrough(obj, id) {
    if ($(obj).parent().find(".not_case").text()==="NEW CONTRACT!"){
        location.href = 'contracts?new-contract='+id;
    }
    if ($(obj).parent().find(".not_case").text()==="CONTRACT SIGNED!"){
        location.href ='contracts?contract-signed='+id;
    }
    if ($(obj).parent().find(".not_case").text()==="NEW INVOICE!"){
        location.href = 'invoices?new-invoice='+id;
    }
    if ($(obj).parent().find(".not_case").text()==="INVOICE PAID!"){
        location.href = 'invoices?invoice-paid='+id;
    }
    $(obj).parent().find("a").click();
}
$(document).ready(function () {
    var url = new URL(window.location.href);
    var parContr = url.searchParams.get("new-contract");
    var parInv = url.searchParams.get("new-invoice");
    var parContrSgn = url.searchParams.get("contract-signed");
    var parInvPaid = url.searchParams.get("invoice-paid");

    if (parContr!=null){
        $("#row" + parContr).addClass("info");
    }
    if (parInv!=null){
        $("#row" + parInv).addClass("info");
    }
    if (parContrSgn!=null){
        $("#row" + parContrSgn).addClass("info");
    }
    if (parInvPaid!=null){
        $("#row" + parInvPaid).addClass("info");
    }

});