const layer = layui.layer;
$(()=>{
    let $form = $("#register-form");
    let button = $form.find("button");
    button.click(function (e) {
        e.preventDefault();
        let account = $form.find("[name=account]").val();
        let password = $form.find("[name=password]").val();
        let passwordAgain = $form.find("[name=passwordAgain]").val();

        if(password === passwordAgain){
            let url = ctx + "/admin/admin/register";
            //  TODO:需要检查账户和密码是否合法
            console.log("login");
            // login(account,password,url);
        }
    });
});
function login(account,password,url){
    $.ajax({
        url,
        method:"post",
        dataType:"json",
        data:{
            account,
            password
        },
        success(resp){
            // 成功后就会跳转不需要做处理
            // 如果想要跳转这样写
            location.href = ctx + "/admin/index/main";
        },
        error(resp){
            layer.msg(resp.responseJSON.msg);
        }
    });
}
