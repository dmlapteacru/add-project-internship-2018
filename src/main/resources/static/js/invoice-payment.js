//

$(document).ready(function () {
    $.each($(".filter_status_invoice"),function () {
        if ($(this).text() === "PAID"){
            $(this).addClass("invoice_payed");
        } else if ($(this).text() === "SENT"){
            $(this).addClass("invoice_pending");
        }else if ($(this).text() === "OVERDUE"){
            $(this).addClass("invoice_overdue");
        }
    });
    $(".i_invoices").addClass("active_menu_link");
});
//INVOICE PAYMENT

function payInvoice(btn) {
    if ($(".balance_header").css("display") == "none"){
        $("#error_alert_message").text("No connection with bank.");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        },3000);
    } else {
        var object = {
            "correspondentCount": $(btn).attr("id"),
            "sum": $("#row" + $(btn).attr("id")).find("#sum_to_pay").text(),
            "description": "Invoice from " + $("#row" + $(btn).attr("id")).find("#invoice_date").text() + " is paid. "
        };
        $.ajax(
            {
                url: "/bankAccount/payinvoice?bal=" + $(".balance").text(),
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(object),
                beforeSend: function () {
                    $("#load_icon_wrapper").modal("show");
                },
                success: function () {
                    $("#confirm_alert_message").text("Invoice paid.");
                    $("#alert_success").slideToggle("slow");
                    setTimeout(function () {
                        $("#alert_success").slideToggle("slow");
                    }, 3000);
                    $("#load_icon_wrapper").modal("hide");
                    location.reload();
                },
                error: function (response) {
                    $("#error_alert_message").text(response.responseText);
                    $("#alert_error").slideToggle("slow");
                    setTimeout(function () {
                        $("#alert_error").slideToggle("slow");
                    }, 3000);
                    $("#load_icon_wrapper").modal("hide");
                }
            }
        )
    }
}
/*BULK PAYMENT*/
function getBulkSum() {
    var sum = 0;
    $.each($(".checkbox_invoice_element[checked='checked']"), function () {
        sum += parseInt($("#row" + $(this).attr("value")).find("#sum_to_pay").text());
    });
    if ($(".balance_header").css("display") == "none"){
        $("#bulk_pay_sum").css("color", "red");
        $("#error_alert_message").text("Not connection with bank.");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        },3000);
    }else
        if (sum > parseInt($(".balance").text())){
            $("#bulk_pay_sum").css("color", "red");
            $("#error_alert_message").text("Not enough money.");
            $("#alert_error").slideToggle("slow");
            setTimeout(function () {
                $("#alert_error").slideToggle("slow");
            },3000);
        } else {
            $("#bulk_pay_sum").css("color", "green");
        }
    $("#bulk_pay_sum").text(sum);
}
$("#check_all_invoices").change(function () {
    $(this).attr("checked", !$(this).attr("checked"));
    if ($(this).attr("checked") === "checked") {
        $.each($(".checkbox_invoice_element"), function () {
            $(this).attr("checked", true);
            $(this).prop("checked", true);
        });
        $(".bulk_btn_wrapper").css("display", "block");
        getBulkSum();
    } else {
        $.each($(".checkbox_invoice_element"), function () {
            $(this).removeAttr("checked");
            $(this).prop("checked", false);
        });
        $(".bulk_btn_wrapper").css("display", "none");
    }

});


$(".checkbox_invoice_element").change(function () {
    $(this).attr("checked", !$(this).attr("checked"));
    var isAnyChecked = false;
    $.each($(".checkbox_invoice_element"), function () {
        if ($(this).attr("checked") === "checked"){
            isAnyChecked = true;
        }
    });

    if (isAnyChecked === true){
        $(".bulk_btn_wrapper").css("display", "block");
        getBulkSum();
    } else {
        $(".bulk_btn_wrapper").css("display", "none");
        $("#check_all_invoices").removeAttr("checked");
        $("#check_all_invoices").prop("checked", false);
    }

});

$("#bulk_pay_btn").click(function () {
    if(parseInt($("#bulk_pay_sum").text()) === 0){
        $("#error_alert_message").text("Nothing selected.");
        $(".alert-danger").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        },3000);
    }else if (parseInt($("#bulk_pay_sum").text()) > parseInt($(".balance").text())){
        $("#error_alert_message").text("Not enough money.");
        $(".alert-danger").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        },3000);
    } else if ($(".balance").text() === " -no connection- "){
        $("#error_alert_message").text("No connection with bank.");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        },3000);
    } else {
        var invoiceList = [];
        $.each($(".checkbox_invoice_element[checked='checked']"), function () {
            var correspondentCount = $(this).attr("value");
            var description = "Invoice from " + $("#row" + $(this).attr("value")).find("#invoice_date").text() + " is paid. ";
            var item = {};
            item["correspondentCount"] = correspondentCount;
            item["sum"] = $("#row" + $(this).attr("value")).find("#sum_to_pay").text();
            item["description"] = description;
            invoiceList.push(item);
        });
        $.ajax(
            {
                url: "/bankAccount/payinvoice/bulk?bal=" + $(".balance").text(),
                type: "POST",
                beforeSend: function () {
                    $("#load_icon_wrapper").modal("show");
                },
                contentType: "application/json",
                data: JSON.stringify(invoiceList),
                success: function () {
                    location.reload();
                },
                error: function (response) {
                    $("#error_alert_message").text(response.responseText);
                    $("#alert_error").slideToggle("slow");
                    setTimeout(function () {
                        $("#alert_error").slideToggle("slow");
                    },5000);
                    $("#load_icon_wrapper").modal("hide");
                }
            }
        )
    }

});