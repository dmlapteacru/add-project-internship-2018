function openPage(pageName, elmnt) {
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
$(".new_category_btn button").click(function () {
    $("#new_category_id").val("");
    $("#new_category_name").val("");
    $("#new_category_desc").val("");
});
$("#save_btn_category").click(function () {
    var object = {
        "id" : $("#new_category_id").val(),
        "name" : $("#new_category_name").val(),
        "description" : $("#new_category_desc").val()
    }
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
            error: function () {
                $("#error_category").text("Category already exists");
            }
        }
    );
});


$(".btn-pass-reset").click(function () {
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
                alert("Password reset");
            }
        }
    )
});

$("#messages_btn_req , #messages_read_all_req").click(function () {
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
    $.each(data, function (id, object) {
        var messageID = object.id;
        if (object.status.toLowerCase()=="unread"){
            $("#table-messages tbody")
                .append('<tr id="' + messageID + '" class="unread_message_bold"><td><span class="circle circle_active" onclick="changeMessageStatus(this)"></span></td></tr>');
            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.user_email + "</td>");

            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.subject + " <div style='display: none;' id='mess"+messageID+"'>" + object.message +"</div></td>");
        }else{
            $("#table-messages tbody")
                .append('<tr id="' + messageID + '"><td><span class="circle" onclick="changeMessageStatus(this)"></span></td></tr>');
            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.user_email + "</td>");

            $("#table-messages tr[id="+ "" + messageID + "]")
                .append("<td onclick='showMessage(this)'>" + object.subject + " <div style='display: none;' id='mess"+messageID+"'>" + object.message + "</div></td>");
        }
        $("#table-messages tr[id="+ "" + messageID + "]").append("<td>" + parseDate(object.date) + "</td>");
        $("#table-messages tr[id="+ "" + messageID + "]").append("<td><span id='delete_message' value='" + messageID +"' " +
            "data-toggle='modal' data-target='#modalDeleteConfirm'" +
            "class='glyphicon glyphicon-trash delete_category_pencil' onclick='deleteCM(this)'></span></td>");
    })
}

function changeMessageStatus(circle) {
    $(circle).toggleClass("circle_active");
    $(circle).parent().parent().toggleClass("unread_message_bold");

    $.ajax(
        {
            url : "/admin/message/changeStatus/" + $(circle).parent().parent().attr("id"),
            type : "PUT"
        }
    )
}
function showMessage(col) {
    $("#mess"+$(col).parent().attr("id")).slideToggle("slow");

    if ($(col).parent().hasClass("unread_message_bold")) {
        changeMessageStatus($(col).parent().find("td > span.circle"));
    }
}

function parseDate(date) {
    var date_time_array = date.toString().split("T");
    var date = date_time_array[0].split("-");
    var reformated_date = date[1] + "-" + date[2]+ "-" + date[0];
    var time_array = date_time_array[1].split(":");

    if (new Date().toDateString() === new Date(date_time_array[0]).toDateString()){
        return time_array[0]+ ":" +time_array[1];
    } else return reformated_date;
}