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
                    '<span id="'+ ID + '" class="glyphicon glyphicon-trash delete_category_pencil" data-toggle="modal" data-target="#modalDeleteConfirm" onclick="deleteCategory(this)"></span>' +
                    '</td>');
            });
        }});
});
function editProduct(obj) {
    $("#new_category_id").val(obj.id);
    $("#new_category_name").val($("#name" + obj.id).text());
    $("#new_category_desc").val($("#description" + obj.id).text());
}

$("#delete_btn_category").click(function () {
    $.ajax(
        {
            url: "admin/deleteCategory/" + $("#delete_btn_category").val(),
            type: "DELETE",
            success: function () {
                $("#modalDeleteConfirm").modal("hide");
                $("#categories_btn_req").click();
            }
        }
    )
});
function deleteCategory(obj) {
    $("#delete_btn_category").val(obj.id);
}
// $(".btn-pass-change").click(function () {
//     $("#new_pass_username").text($(this).attr("value"));
//     $("#new_pass_input").val("");
//     $("#new_pass_input_confirm").val("");
//     $(".error_pass_match").text("");
// });
// var passwordPattern =  new RegExp('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$');
// $("#save_btn").click(function () {
//     if ($("#new_pass_input").val()==""){
//         $(".error_pass_match").text("Passwords can't be empty.");
//     } else if (!passwordPattern.test($("#new_pass_input").val())){
//         $(".error_pass_match").text("Password must contain : at least 1 upper case letter," +
//             " 1 lower case letter," +
//             " 1 special character," +
//             " 1 number");
//     } else if (passwordMatches($("#new_pass_input").val(), $("#new_pass_input_confirm").val())){
//         $(".error_pass_match").val("");
//         var object = {
//             "username" : $("#new_pass_username").text(),
//             "password" : $("#new_pass_input").val(),
//             "confirmPassword" : $("#new_pass_input_confirm").val()
//         }
//         $.ajax(
//             {
//                 url: "/admin/newUserPassword",
//                 type: "PUT",
//                 contentType: "application/json",
//                 data: JSON.stringify(object),
//                 success: function () {
//                     $("#modalPassword").modal("hide");
//                 }
//             }
//         );
//     } else {
//         $(".error_pass_match").text("Passwords don't match.");
//     }
// });
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
// function passwordMatches(pass1, pass2){
//     if (pass1 == pass2) return true;
//     else return false;
// }