$( document ).ready(function () {
    loadBalance();
    $(".balance_header").css("display", "none");
    $(".balance").text(" -no connection- ");
    $(".balance_info").text(" -no connection- ");
    $(".balance_info").css("font-size", "12px");
    $("#mdl").css("display", "none");
    getUserName();
});
function loadBalance() {
    $.ajax(
        {
            statusCode: {
              400: function() {
              }
            },
            url : "/bankAccount/balance",
            type : "POST",
            success : function (result) {
               return parseBalance(result);
            }
        }
    )
}
function getUserName() {
    $.ajax(
        {
            url : "/get/username",
            success : function (response) {
                $("#user").text(response);
            }
        }
    )
}
function parseBalance(string) {
    $("#mdl").css("display", "block");
    $(".balance_header").css("display", "block");
    var balance = string.split(":")[1].substring(0, string.split(":")[1].length-1);
    $(".balance").text(balance);
    $(".balance_info").text(balance);
    return balance;
}

