$( document ).ready(function() {
    $.ajax(
        {
            url : "/bankAccount/balance",
            type : "POST",
            success : function (result) {
                parseBalance(result);
            }
        }
    )
});

function parseBalance(string) {
    $(".balance").text(string.split(":")[1].substring(0, string.split(":")[1].length-1) + "MDL");
    $(".balance_info").text(string.split(":")[1].substring(0, string.split(":")[1].length-1) + "MDL");
}