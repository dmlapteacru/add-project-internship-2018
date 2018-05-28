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
    $("#load_icon_wrapper").modal("show");
    $.ajax(
        {
            url: "/bankAccount/create",
            type: "POST",
            success: function () {
                $("#load_icon_wrapper").modal("hide");
                showAlert('Success', 'Bank account was created');
                location.reload();
            },
            error: function () {
                $("#load_icon_wrapper").modal("hide");
                showAlert('Error', 'No connection with bank');
            }
        }
    )
});
$(".add_money").submit(function (e) {
    e.preventDefault();
    $("#load_icon_wrapper").modal("show");
    $.ajax(
        {
            url: "/bankAccount/addmoney?sum=" + $("#money_sum").val(),
            type: "POST",
            success: function () {
                $("#load_icon_wrapper").modal("hide");
                showAlert('Success', 'Money was added!');
                $(".btn_add_money_toggle").click();
                $("#money_sum").val("0.0");
                loadBalance();
                location.reload();
            },
            error: function (response) {
                $("#load_icon_wrapper").modal("hide");
                showAlert('Error', response.responseText);
            }
        }
    )
});

$("#btn_statement_req").click(function () {
    if (($("#date_from").val() === "") || ($("#date_to").val() === "")) {
        showAlert('Error', 'Dates can not be empty');
    } else {
        $("#load_icon_wrapper").modal("show");
        var body = {
            "date": $("#date_from").val(),
            "dateTo": $("#date_to").val()
        };
        $.ajax(
            {
                url: "/bankAccount/statement",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(body),
                success: function (result) {
                    $("#load_icon_wrapper").modal("hide");
                    loadStatement(result);
                },
                error: function (response) {
                    showAlert('error', response.responseText);
                    $("#load_icon_wrapper").modal("hide");
                }
            }
        )
    }
});

function loadStatement(data) {
    $(".table_statement tbody").html("");
    //$("#curr_balance").append("<br><span style='color: orange'>" + data.startBalance + " MDL</span>");
    addRowToTable("a" , "", "", "start balance", "", data.startBalance.toFixed(2));
    var endBalance = data.startBalance;
    $.each(data.listOfTransactions, function (id, object) {
        addRowToTable(id, object.date, object.partnerName, object.description, object.sum.toFixed(2), object.currentBalance.toFixed(2));
        endBalance = object.currentBalance;
    });
    addRowToTable("z", "", "", "end balance", "", endBalance.toFixed(2));
}

function addRowToTable(id, date, name, description, sum, balance) {
    $(".table_statement tbody").append("<tr id='" + id + "'></tr>");
    $(".table_statement tbody tr[id='" + id + "']").append("<td>" + date + "</td>" +
                                                           "<td>" + name + "</td>" +
                                                           "<td>" + description + "</td>");
    if (sum > 0) {
        $(".table_statement tbody tr[id='" + id + "']").append("<td class='income'>" + sum + "</td>");
    } else {
        $(".table_statement tbody tr[id='" + id + "']").append("<td class='loss'>" + sum + "</td>");
    }
    $(".table_statement tbody tr[id='" + id + "']").append("<td style='color: orange; font-weight: bold;'>" + balance + "</td>");
}

function dateParse(date) {
    var dateList = date.split('-');
    var year = dateList[0];
    var month = dateList[1];
    var day = dateList[2].split("T")[0];
    return day + "." + month + "." + year;
}

function showAlert(title, message) {
    var alertWindow = $('#modalAlert');
    $('#alertTitle').text(title);
    var alertBody = alertWindow.find('.modal-body');
    alertBody.find('p').empty();
    var strings = message.split('\n');
    for (i = 0; i < strings.length; i++) {
        alertBody.append('<p>' + strings[i] + '</p>');
    }
    alertWindow.modal({backdrop: "static"});
    alertWindow.modal('show');
}