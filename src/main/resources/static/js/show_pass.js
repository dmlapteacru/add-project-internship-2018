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

