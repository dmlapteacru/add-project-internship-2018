$( document ).ready(function () {
    loadBalance();
    $(".balance").text(" -no connection- ");
    $(".balance").css("font-size", "13px");
    $(".balance_info").text(" -no connection- ");
    $(".balance_info").css("font-size", "12px");
    $("#mdl").css("display", "none");
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

function parseBalance(string) {
    $("#mdl").css("display", "block");
    var balance = string.split(":")[1].substring(0, string.split(":")[1].length-1);
    $(".balance").text(balance);
    $(".balance_info").text(balance);
    return balance;
}

