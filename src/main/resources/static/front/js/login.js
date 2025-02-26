const form = layui.form;
$(()=>{
    $(".login-btn").click(function (e){
        e.preventDefault();
        let pass = form.validate("#login-form");
        if (!pass){
            return;
        }

        let account = $("[name=account]").val();
        let password = $("[name=password]").val();
        let url = ctx + "/login";
        $.ajax({
            url,
            method:"post",
            dataType:"json",
            data:{
                account,
                password
            },
            success(resp){
                location.href = ctx + "/index";
            },
            error(resp){
                layer.msg(resp.responseJSON.msg);
            }
        });
    });
});
