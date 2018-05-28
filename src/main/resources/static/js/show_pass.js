$(".show_pass_btn").click(function () {
    if ($(".pass_wrapper input").attr("type")=="password"){
        $(".pass_wrapper input").attr("type", "text");
        $(".show_pass_btn").text("HIDE");
    } else {
        $(".pass_wrapper input").attr("type", "password");
        $(".show_pass_btn").text("SHOW");
    }
});

$(".show_confirm_pass_btn").click(function () {
    if ($(".confirm_pass_wrapper input").attr("type")=="password"){
        $(".confirm_pass_wrapper input").attr("type", "text");
        $(".show_confirm_pass_btn").text("HIDE");
    } else {
        $(".confirm_pass_wrapper input").attr("type", "password");
        $(".show_confirm_pass_btn").text("SHOW");
    }
});

var passwordPattern =  new RegExp('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$');
$(".btn").click(function () {
    if ($("#new_pass_input").val()==""){
        $(".error_pass_match").text("Passwords can't be empty.");
    } else if (!passwordPattern.test($("#new_pass_input").val())){
        $(".error_pass_match").text("Password must contain : at least 1 upper case letter," +
            " 1 lower case letter," +
            " 1 special character," +
            " 1 number");
    } else if (passwordMatches($("#new_pass_input").val(), $("#new_pass_input_confirm").val())){
        $(".error_pass_match").val("");
        var url = new URL(window.location.href);
        var object = {
            "username": url.searchParams.get("username"),
            "password" : $("#new_pass_input").val()
        }
        $.ajax(
            {
                url: "/newUserPassword",
                type: "PUT",
                contentType: "application/json",
                data: JSON.stringify(object),
                success: function () {
                    location.href = "http://localhost:8080/login";
                }
            }
        )
    } else {
        $(".error_pass_match").text("Passwords don't match.");
    }
});

function passwordMatches(pass1, pass2){
    if (pass1 == pass2) return true;
    else return false;
}