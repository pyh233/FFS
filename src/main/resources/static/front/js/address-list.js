$(() => {
    // TODO:添加地址请求或许可以写在这里

    // 删除地址
    $(".table").on("click", ".delete-action", function (e) {
        e.preventDefault();
        let $deleteBtn = $(this);
        layer.confirm("确认删除此条地址信息?", function (index) {
            let delAddrId = [];
            delAddrId.push($deleteBtn.data("addr-id"));
            let url = ctx + "/my/address/delete";
            //  发送删除请求，删除结束返回给此处地址列表信息
            $.ajax({
                url,
                method: "delete",
                data: {
                    ids: delAddrId
                },
                traditional: true,
                success(resp) {
                    if (resp.success) {
                        // TODO:根据返回的地址信息重新加载地址表
                        layer.msg(resp.msg);
                    } else {
                        layer.msg(resp.msg);
                    }
                },
                error(resp) {
                    if (resp.status === 401) {
                        location.href = ctx + "/login";
                    } else {
                        layer.msg(resp.responseJSON.msg);
                    }
                }
            });
            layer.close(index);
        });
    });
});
