function openPage(pageName,elmnt) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("tablink_active");
    }
    document.getElementById(pageName).style.display = "block";
    elmnt.classList.add("tablink_active");
    if (elmnt.id == "messages_btn_req"){
        $(".sub_tablink").removeClass("sub_tablink_active");
        $("#messages_read_all_req").addClass("sub_tablink_active");
        $(".sub_tablink").css("display", "block");
    } else $(".sub_tablink").css("display", "none");
}
$("#categories_btn_req").click(function () {
    $.ajax({url: "/admin/categories",
        success: function(result){
            $("#table-categories tbody").html("");
            $.each(result, function (id, object) {
                var ID = object.id;
                $("#table-categories tbody").append('<tr id="row' + ID + '"></tr>');
                $.each(object, function (key, value) {
                    if (key!="id"){
                        $("#table-categories tr[id="+ "row" + ID + "]").append('<td id="'+ key + ID +'">' + value + '</td>');
                    }
                });
                $("#table-categories tr[id="+ "row" + ID + "]").append('<td>' +
                    '<span id="'+ ID + '" class="glyphicon glyphicon-pencil edit_category_pencil" data-toggle="modal" data-target="#modalCategory" onclick="editProduct(this)"></span>' +
                    '<span id="del_category" value="'+ ID + '" class="glyphicon glyphicon-trash delete_category_pencil" data-toggle="modal" data-target="#modalDeleteConfirm" onclick="deleteCM(this)"></span>' +
                    '</td>');
            });
        }});
});

function activateLink(subLink) {
    $(".sub_tablink").removeClass("sub_tablink_active");
    $(subLink).addClass("sub_tablink_active");
}

function editProduct(obj) {
    $("#new_category_id").val(obj.id);
    $("#new_category_name").val($("#name" + obj.id).text());
    $("#new_category_desc").val($("#description" + obj.id).text());
}

$("#delete_btn_confirm").click(function () {
    if ($(this).attr("data-delete-info") === "deleteCategory"){
        $.ajax(
            {
                url: "admin/deleteCategory/" + $("#delete_btn_confirm").val(),
                type: "DELETE",
                success: function () {
                    $("#modalDeleteConfirm").modal("hide");
                    $("#categories_btn_req").click();
                    $("#confirm_alert_message").text("Category deleted.")
                    $("#alert_success").slideToggle("slow");
                    setTimeout(function () {
                        $("#alert_success").slideToggle("slow");
                    },3000);
                }
            }
        )
    } else
    $.ajax(
        {
            url: "admin/message/delete/" + $("#delete_btn_confirm").val(),
            type: "DELETE",
            success: function () {
                $("#modalDeleteConfirm").modal("hide");
                $("#messages_read_all_req").click();
                $("#confirm_alert_message").text("Message deleted.")
                $("#alert_success").slideToggle("slow");
                setTimeout(function () {
                    $("#alert_success").slideToggle("slow");
                },3000);
            }
        }
    )
});

function deleteCM(obj) {
    $("#delete_btn_confirm").val($(obj).attr("value"));
    if (obj.id == "delete_message"){
        $("#delete_btn_confirm").attr("data-delete-info", "deleteMessage");
    } else
    $("#delete_btn_confirm").attr("data-delete-info", "deleteCategory");
}
function resetCM(obj) {
    $("#reset_pass_btn_confirm").val($(obj).attr("value"));
}

$("#reset_pass_btn_confirm").click(function () {
    var object = {
        "username" : $(this).attr("value"),
        "token" : $(this).attr("value") + "tkn"
    }
    $.ajax(
        {
            url: "/resetPassword",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(object),
            success: function () {
                $("#modalResetPassConfirm").modal("hide");
                $("#confirm_alert_message").text("Password reset success.");
                $("#alert_success").slideToggle("slow");
                setTimeout(function () {
                    $("#alert_success").slideToggle("slow");
                },5000);
            }
        }
    )
});


$(".new_category_btn button").click(function () {
    $("#new_category_id").val("");
    $("#new_category_name").val("");
    $("#new_category_desc").val("");
    $("#error_category").text("");
});
$("#save_btn_category").click(function () {
    var object = {
        "id" : $("#new_category_id").val(),
        "name" : $("#new_category_name").val(),
        "description" : $("#new_category_desc").val()
    };
    $.ajax(
        {
            url: "/admin/newCategory",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(object),
            success: function () {
                $("#modalCategory").modal("hide");
                $("#categories_btn_req").click();
            },
            error: function (request) {
                $("#error_alert_message").text(request.responseText);
                $("#alert_error").slideToggle("slow");
                setTimeout(function () {
                    $("#alert_error").slideToggle("slow");
                },5000);
            }
        }
    );
});

$("#messages_btn_req , #messages_read_all_req").click(function () {
    resetCheckBoxMessages();
    $.ajax(
        {
            url: "/admin/messages",
            success: function (result) {
                loadMessages(result);
            }
        }
    )
});
$("#messages_unread_req").click(function () {
    $.ajax(
        {
            url: "/admin/messages/unread",
            success: function (result) {
                loadMessages(result);
            }
        }
    )
});
$("#messages_read_req").click(function () {
    $.ajax(
        {
            url: "/admin/messages/read",
            success: function (result) {
                loadMessages(result);
            }
        }
    )
});
function loadMessages(data) {
    $("#table-messages tbody").html("");
    console.log(data);
    $.each(data, function (id, object) {
        var messageID = object.id;
        if (object.status.toLowerCase()=="unread"){
            $("#table-messages tbody")
                .append('<tr id="' + messageID + '" class="unread_message_bold">' +
                    '<td><input type="checkbox" class="checkbox_mess_element" onclick="checkBTN(this)"/></td></td><td><span class="circle circle_active" onclick="changeMessageStatus(this)"></span></td></tr>');
            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.userEmail + "</td>");

            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.subject + " <div style='display: none;' id='mess"+messageID+"'>" + object.message +"</div></td>");
        }else{
            $("#table-messages tbody")
                .append('<tr id="' + messageID + '">' +
                    '<td><input type="checkbox" class="checkbox_mess_element" onclick="checkBTN(this)"/></td><td><span class="circle circle_inactive" onclick="changeMessageStatus(this)"></span></td></tr>');
            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.userEmail + "</td>");

            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.subject + " <div style='display: none;' id='mess"+messageID+"'>" + object.message + "</div></td>");
        }
        $("#table-messages tr[id="+ "" + messageID + "]").append("<td>" + parseDate(object.date) + "</td>");
        $("#table-messages tr[id="+ "" + messageID + "]").append("<td><span id='delete_message' value='" + messageID +"' " +
            "data-toggle='modal' data-target='#modalDeleteConfirm'" +
            "class='glyphicon glyphicon-trash delete_category_pencil' onclick='deleteCM(this)'></span></td>");
    });
    setTitleForMessageCircles();
}
function setTitleForMessageCircles() {
    $.each($(".circle_active"),function () {
        $(this).append("<span>Mark as read</span>");
    });
    $.each($(".circle_inactive"),function () {
        $(this).append("<span>Mark as unread</span>");
    });
}

function changeMessageStatus(circle) {
    $(circle).toggleClass("circle_active");
    $(circle).toggleClass("circle_inactive");
    $(circle).parent().parent().toggleClass("unread_message_bold");
    setTitleForMessageCircles();
    $.ajax(
        {
            url : "/admin/message/changeStatus/" + $(circle).parent().parent().attr("id"),
            type : "PUT"
        }
    )
}
function showMessage(col) {
    $("#mess"+$(col).parent().attr("id")).slideToggle("slow");

    $(col).parent().removeClass("act");
    if ($(col).parent().hasClass("unread_message_bold")) {
        changeMessageStatus($(col).parent().find("td > span.circle"));
    }
}

function parseDate(date) {
    var minutes = "";
    if (date[4]<10){
        minutes = "0" + date[4];
    } else minutes = date[4];
    return date[2] + "-" + date[1] + "-" + date[0] + " " + date[3] + ":" + minutes;
}

$("#check_all_users").change(function () {
    $(this).attr("checked", !$(this).attr("checked"));
    if ($(this).attr("checked") === "checked") {
        $.each($(".checkbox_users_element"), function () {
            $(this).attr("checked", true);
            $(this).prop("checked", true);
        });
        $(".change_status_all_btn").css("display", "block");
    } else {
        $.each($(".checkbox_users_element"), function () {
            $(this).removeAttr("checked");
            $(this).prop("checked", false);
        });
        $(".change_status_all_btn").css("display", "none");
    }
});


$(".checkbox_users_element").change(function () {
    $(this).attr("checked", !$(this).attr("checked"));
    var isAnyChecked = false;
    $.each($(".checkbox_users_element"), function () {
        if ($(this).attr("checked") === "checked"){
            isAnyChecked = true;
        }
    });

    if (isAnyChecked === true){
        $(".change_status_all_btn").css("display", "block");
    } else {
        $(".change_status_all_btn").css("display", "none");
        resetCheckBoxUsers();
    }
});

$("#users_btn_req").click(function () {
    resetCheckBoxUsers();
});
function resetCheckBoxUsers() {
    $.each($(".checkbox_users_element"), function () {
        $(this).removeAttr("checked");
        $(this).prop("checked", false);
    });
    $(".change_status_all_btn").css("display", "none");
    $("#check_all_users").removeAttr("checked");
    $("#check_all_users").prop("checked", false);
}
$("#change_status_all_btn_active").click(function () {
    var usernameArray = [];
    $.each($(".checkbox_users_element[checked='checked']"), function () {
        var username = $(this).attr("value");
        var item = {};
        item["username"] = username;
        usernameArray.push(item);
    });
    $.ajax(
        {
            url: "/admin/changeUserStatus/active",
            type: "POST",
            beforeSend: function () {
                $("#load_icon_wrapper").modal("show");
            },
            contentType: "application/json",
            data: JSON.stringify(usernameArray),
            success: function () {
                location.reload();
            }
        }
    )
});
$("#change_status_all_btn_inactive").click(function () {
    var usernameArray = [];
    $.each($(".checkbox_users_element[checked='checked']"), function () {
        var username = $(this).attr("value");
        var item = {};
        item["username"] = username;
        usernameArray.push(item);
    });
    $.ajax(
        {
            url: "/admin/changeUserStatus/inactive",
            type: "POST",
            beforeSend: function () {
                $("#load_icon_wrapper").modal("show");
            },
            contentType: "application/json",
            data: JSON.stringify(usernameArray),
            success: function () {
                location.reload();
            }
        }
    )
});

//    CHECK MESSAGES
$("#check_all_messages").change(function () {
    $(this).attr("checked", !$(this).attr("checked"));
    if ($(this).attr("checked") === "checked") {
        $.each($(".checkbox_mess_element"), function () {
            $(this).attr("checked", true);
            $(this).prop("checked", true);
        });
        $(".change_status_all_btn_mess").css("display", "block");
    } else {
        $.each($(".checkbox_mess_element"), function () {
            $(this).removeAttr("checked");
            $(this).prop("checked", false);
        });
        $(".change_status_all_btn_mess").css("display", "none");
    }
});

function checkBTN(obj) {
    $(obj).attr("checked", !$(obj).attr("checked"));
    var isAnyChecked = false;
    $.each($(".checkbox_mess_element"), function () {
        if ($(this).attr("checked") === "checked"){
            isAnyChecked = true;
        }
    });

    if (isAnyChecked === true){
        $(".change_status_all_btn_mess").css("display", "block");
    } else {
        $(".change_status_all_btn_mess").css("display", "none");
        resetCheckBoxMessages();
    }
}

function resetCheckBoxMessages() {
    $.each($(".checkbox_mess_element"), function () {
        $(this).removeAttr("checked");
        $(this).prop("checked", false);
    });
    $(".change_status_all_btn_mess").css("display", "none");
    $("#check_all_messages").removeAttr("checked");
    $("#check_all_messages").prop("checked", false);
}

$("#change_status_all_btn_read").click(function () {
    var idArray = [];
    $.each($(".checkbox_mess_element[checked='checked']"), function () {
        var id = $(this).parent().parent().attr("id");
        var item = {};
        item["id"] = id;
        idArray.push(item);
    });
    $.ajax(
        {
            url: "/admin/message/changeStatus/read",
            type: "PUT",
            beforeSend: function () {
                $("#load_icon_wrapper").modal("show");
            },
            contentType: "application/json",
            data: JSON.stringify(idArray),
            success: function () {
                $("#load_icon_wrapper").modal("hide");
                $("#messages_btn_req").click();
                $("#check_all_messages").click();
                resetCheckBoxMessages();
            }
        }
    )
});
$("#change_status_all_btn_unread").click(function () {
    var idArray = [];
    $.each($(".checkbox_mess_element[checked='checked']"), function () {
        var id = $(this).parent().parent().attr("id");
        var item = {};
        item["id"] = id;
        idArray.push(item);
    });
    $.ajax(
        {
            url: "/admin/message/changeStatus/unread",
            type: "PUT",
            beforeSend: function () {
                $("#load_icon_wrapper").modal("show");
            },
            contentType: "application/json",
            data: JSON.stringify(idArray),
            success: function () {
                $("#load_icon_wrapper").modal("hide");
                $("#messages_btn_req").click();
                $("#check_all_messages").click();
                resetCheckBoxMessages();
            }
        }
    )
});
$("#messages_all_btn_delete").click(function () {
    var idArray = [];
    $.each($(".checkbox_mess_element[checked='checked']"), function () {
        var id = $(this).parent().parent().attr("id");
        var item = {};
        item["id"] = id;
        idArray.push(item);
    });
    console.log(idArray);
    $.ajax(
        {
            url: "/admin/message/bulkDelete",
            type: "DELETE",
            beforeSend: function () {
                $("#load_icon_wrapper").modal("show");
            },
            contentType: "application/json",
            data: JSON.stringify(idArray),
            success: function () {
                $("#load_icon_wrapper").modal("hide");
                $("#messages_btn_req").click();
                $("#check_all_messages").click();
                resetCheckBoxMessages();
            }
        }
    )
});