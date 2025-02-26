//TODO:用户注册功能
$(() => {
    $(".btn").click(function (e) {
        let $ckBox = $("#agree");
        if ($ckBox.prop("checked")) {
            let account = $("[name=account]").val();
            let password = $("[name=password]").val();
            let email = $("[name=email]").val();
            let phone = $("[name=phone]").val();
            let url = ctx + "/register";
            $.ajax({
                url,
                method: "post",
                dataType: "json",
                data: {
                    account,
                    password,
                    email,
                    phone
                },
                success(resp) {
                    if (resp.success) {
                        layer.msg(resp.msg);
                        setTimeout("location.href = ctx + '/index'",2000);
                    } else {
                        layer.msg(resp.msg);
                    }
                },
                error(resp) {
                    layer.msg(resp.responseJSON.msg);
                }
            });
        } else {
            layer.msg("请先阅读并同意本站用户注册协议");
        }
    });
});
