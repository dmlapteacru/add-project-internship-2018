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
    $("#not_count").attr("value", "0");
    $("#notification_").append("<div class='notification_view_all'>" +
        "<button>View all</button>" +
        "</div>");
    var count = parseInt($("#not_count").attr("value"));
    $.each(data, function (id, object) {
        $("#notification_").append("<div class='notification_item' >" +
            "<span class='not_case' style='cursor: pointer;' onclick='goThrough(this,"+object.idSearch+")'>"+object.notificationCase.split("_").join(" ")+"!"+"</span>" +
            "<span>"+object.content+"</span>" +
            "<a id='" +object.id+ "' onclick='deleteNotification(this)'>&times;</a>" +
            "</div>");

        $("#not_count").attr("value", ++count);
        $("#not_count").text(count);
    });
    if (count === 0){
        $("#not_count").text("");
        $("#show_not_link_down").css("color", "#fff");
    } else {
        $("#not_count").text(count);
        $("#show_not_link_down").css("color", "#f15928");
    }
}

function deleteNotification(link) {
    $(link).parent().remove();
    var count = parseInt($("#not_count").attr("value"));
    $("#not_count").attr("value", --count);
    $("#not_count").text(count);
    if (count === 0){
        $("#not_count").text("");
        $("#show_not_link_down").css("color", "#fff");
    } else {
        $("#not_count").text(count);
        $("#show_not_link_down").css("color", "#f15928");
    }
    $.ajax(
        {
            url: "/notifications/changeStatus?id=" + $(link).attr("id"),
            type: "PUT"
        }
    );
}

//GO THROUGH NOTIFICATIONS
//
function goThrough(obj, id) {
    if ($(obj).text()==="NEW CONTRACT!"){
        location.href = '/customer/contracts?new-contract='+id;
    }
    if ($(obj).text()==="CONTRACT SIGNED!"){
        location.href = '/customer/contracts?contract-signed='+id;
    }
    if ($(obj).text()==="NEW INVOICE!"){
        location.href = '/customer/invoices?new-invoice='+id;
    }
    if ($(obj).text()==="INVOICE PAID!"){
        location.href = '/customer/invoices?invoice-paid='+id;
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