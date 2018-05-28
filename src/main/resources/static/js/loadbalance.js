$(document).ready(function () {
    loadBalance();
    $(".balance_header").css("display", "none");
    $(".balance").text(" -no connection- ");
    $(".balance_info").css("font-size", "18px");
    getUserName();
});

function loadBalance() {
    $.ajax(
        {
            statusCode: {
                400: function () {
                }
            },
            url: "/bankAccount/balance",
            type: "POST",
            success: function (result) {
                $(".balance").text(result);
                $(".balance_info").text(result);
                $(".balance_header").css("display","block");
            },
            error: function (result) {
                $(".balance").text(result.responseText);
                $(".balance_info").text(result.responseText);
            }
        }
    )
}

function getUserName() {
    $.ajax(
        {
            url: "/get/username",
            success: function (response) {
                $("#user").text(response);
            }
        }
    )
}
function parseBalance(string) {
    var balance = string.split(":")[1].substring(0, string.split(":")[1].length-1);
    $(".balance_info").text(balance);
    return balance;
}

$(function() {
    // Get page title
    var pageTitle = $("title").text();

    // Change page title on blur
    $(window).blur(function() {
        $("title").text("Miss You..");
    });

    // Change page title back on focus
    $(window).focus(function() {
        $("title").text(pageTitle);
    });
});