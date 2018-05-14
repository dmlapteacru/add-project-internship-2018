$( document ).ready(function () {
    loadBalance()
});
function loadBalance() {
    $.ajax(
        {
            url : "/bankAccount/balance",
            type : "POST",
            success : function (result) {
               return parseBalance(result);
            }
        }
    )
};

function parseBalance(string) {
    var balance = string.split(":")[1].substring(0, string.split(":")[1].length-1);
    $(".balance").text(balance);
    $(".balance_info").text(balance);
    return balance;
}

