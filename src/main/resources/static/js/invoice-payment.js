$(document).ready(function () {
    $.each($(".filter_status_invoice"), function () {
        if ($(this).text() === "PAID") {
            $(this).addClass("invoice_payed");
        } else if ($(this).text() === "SENT") {
            $(this).addClass("invoice_pending");
        } else if ($(this).text() === "OVERDUE") {
            $(this).addClass("invoice_overdue");
        }
    });
    $(".i_invoices").addClass("active_menu_link");
});

//INVOICE PAYMENT
function payInvoice(btn) {
    $("#load_icon_wrapper").modal("show");
    $.ajax(
        {
            url: "/bankAccount/payinvoice",
            type: "POST",
            contentType: "application/json",
            data: btn.id,
            success: function () {
                showAlert('Success', 'Invoice was paid');
                $("#load_icon_wrapper").modal("hide");
                location.reload();
            },
            error: function (response) {
                showAlert('Error', response.responseText);
                $("#load_icon_wrapper").modal("hide");
            }
        }
    )
}

/*BULK PAYMENT*/
function getBulkSum() {
    var sum = 0;
    $.each($(".checkbox_invoice_element[checked='checked']"), function () {
        sum += parseInt($("#row" + $(this).attr("value")).find("#sum_to_pay").text());
    });
    if ($(".balance_header").css("display") == "none") {
        $("#bulk_pay_sum").css("color", "red");
        $("#error_alert_message").text("Not connection with bank.");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        }, 3000);
    } else if (sum > parseInt($(".balance").text())) {
        $("#bulk_pay_sum").css("color", "red");
        $("#error_alert_message").text("Not enough money.");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        }, 3000);
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
        if ($(this).attr("checked") === "checked") {
            isAnyChecked = true;
        }
    });

    if (isAnyChecked === true) {
        $(".bulk_btn_wrapper").css("display", "block");
        getBulkSum();
    } else {
        $(".bulk_btn_wrapper").css("display", "none");
        $("#check_all_invoices").removeAttr("checked");
        $("#check_all_invoices").prop("checked", false);
    }

});

$("#bulk_pay_btn").click(function () {
    var invoiceIdList = [];
    $.each($(".checkbox_invoice_element[checked='checked']"), function () {
        var invoiceId = $(this).attr("value");
        invoiceIdList.push(invoiceId);
    });
    if (invoiceIdList.length === 0) {
        $("#error_alert_message").text("Nothing selected");
        $("#alert_error").slideToggle("slow");
        setTimeout(function () {
            $("#alert_error").slideToggle("slow");
        }, 2000);
    }
    $("#load_icon_wrapper").modal("show");
    $.ajax(
        {
            url: "/bankAccount/payinvoice/bulk",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(invoiceIdList),
            success: function (result) {
                $("#load_icon_wrapper").modal("hide");
                showAlert('Success', result.responseText);
                location.reload();
            },
            error: function (result) {
                $("#load_icon_wrapper").modal("hide");
                showAlert('Error', result.responseText);
            }
        }
    )
});