$(".btn_add_money_toggle").click(function () {
    $(".add_money").slideToggle("slow");
    $(".fa-caret-up").toggleClass("inactive");
    $(".fa-sort-down").toggleClass("inactive");
    $("#money_sum").val("0.0");
});
$(document).ready(function () {
    date_from.max = new Date().toISOString().split("T")[0];
});
$("#create_account").click(function () {
    $.ajax(
        {
            url : "/bankAccount/create",
            type : "POST",
            success: function () {
                location.reload();
            }
        }
    )
});
$("#btn_add_money_req").click(function () {
    $.ajax(
        {
            url : "/bankAccount/addmoney?sum=" + $("#money_sum").val(),
            type : "POST",
            success: function () {
                $("#confirm_alert_message").text("Money added.");
                $("#alert_success").slideToggle("slow");
                setTimeout(function () {
                    $("#alert_success").slideToggle("slow");
                },5000);
                $(".btn_add_money_toggle").click();
                loadBalance();
            },
            error: function (response) {
                $("#error_alert_message").text(response.responseText);
                $("#alert_error").slideToggle("slow");
                setTimeout(function () {
                    $("#alert_error").slideToggle("slow");
                },5000);
            }
        }
    )
});
$("#btn_statement_req").click(function () {
    if (($("#date_from").val() === "") || ($("#date_to").val() === "")){
        $("#error_alert_message").text("Dates can't be empty.");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        },5000);
    } else {
        var body = {
            "date" : $("#date_from").val(),
            "dateTo" : $("#date_to").val()
        };
        $.ajax(
            {
                url: "/bankAccount/statement",
                type: "POST",
                contentType: "application/json",
                data : JSON.stringify(body),
                success: function (result) {
                    loadStatement(result);
                },
                error : function (response) {
                    $("#error_alert_message").text(response.responseText);
                    $("#alert_error").slideToggle("slow");
                    setTimeout(function () {
                        $("#alert_error").slideToggle("slow");
                    },5000);
                }
            }
        )
    }
});
function loadStatement(data) {
    $.each(data, function (id, object) {
        $(".table_statement tbody").append("<tr id='"+id+"'></tr>");
        $(".table_statement tbody tr[id='"+id+"']").append("<td>"+ object.date.split("-").reverse().join("-")+"</td><td>"+object.description+"</td>");
        if (object.correspondentCount === 1){
            $(".table_statement tbody tr[id='"+id+"']").append("<td class='income'>"+ object.sum+"</td>");
        } else {
            $(".table_statement tbody tr[id='"+id+"']").append("<td class='loss'>"+ object.sum+"</td>");
        }
    });
}
