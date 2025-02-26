const layer = layui.layer;
const form = layui.form;
const $ = layui.jquery;
$(()=>{
    let button = $(".btn-primary");
    button.click(function (e) {
        e.preventDefault();
        login();
    });
    $(".captcha-img").click(function () {
        $(this).attr("src", ctx + "/admin/captcha?t="+Math.random());
    });
});

function login(){
    let pass = form.validate("#login-form");
    if(!pass){
        return;
    }
    let account = $("[name=account]").val();
    let password = $("[name=password]").val();
    let captcha = $("[name=captcha]").val();
    let url = ctx + "/admin/login";
    $.ajax({
        url,
        method:"post",
        dataType:"json",
        data:{
            account,
            password,
            captcha
        },
        success(resp){
            $(".captcha-img").click();
            if(resp.success){
                location.href = ctx + "/admin/index";
            }else{
                layer.msg(resp.msg);
            }
        },
        error(resp){
            $(".captcha-img").click();
            layer.msg(resp.responseJSON.msg);
        }
    });
}
