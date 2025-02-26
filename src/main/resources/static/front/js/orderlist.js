$(() => {
    /*找到所有，为未支付订单添加样式*/
    let $orders = $(".order-status");
    //
    for (let order of $orders) {
        if ($(order).text() === "已支付") {
            $(order).addClass("bg-primary");
        } else if ($(order).text() === "未支付") {
            $(order).addClass("bg-secondary");
            // 前往支付页面
            $(order).click(function () {
                let orderId = $(order).data("order-id");
                location.href = ctx + "/order/checkout?id=" + orderId;
            });
        } else if ($(order).text() === "已发货") {
            $(order).addClass("bg-success");
            // TODO:前往物流追踪页面
        } else if ($(order).text() === "已收货") {
            $(order).addClass("bg-success");
            // 删除后方的收货按钮
            $(order).parent().next().next().children().first().remove();
            // TODO:前往结算订单展示页面，提供退款功能
        } else if ($(order).text() === "退款中") {
            $(order).addClass("bg-danger");
            $(order).parent().next().next().children().last().remove();
            // TODO:前往物流追踪页面
        } else if ($(order).text() === "已退款") {
            $(order).parent().next().next().children().remove();
            $(order).addClass("bg-primary");
        } else if ($(order).text() === "收货") {
            $(order).click(function (e) {
                layer.confirm("确认收货?", function (index) {
                    let url = ctx + "/order/finish"
                    let orderId = $(order).data("order-id");
                    $.ajax({
                        url,
                        method: "patch",
                        dataType: "json",
                        data: {
                            orderId
                        },
                        success(resp) {
                            layer.msg(resp.msg);
                        },
                        error(resp) {
                            if (resp.status === 401) {
                                location.href = ctx + "/login";
                            }
                        }
                    });
                    layer.close(index);
                });
            })
        } else if ($(order).text() === "退款") {
            let id = $(order).data("order-id");
            $(order).click(function (index) {
                layer.open({
                    type: 2,
                    title: "编辑退款信息",
                    area: ["85%", '90%'],
                    content: ctx + '/order/refund?orderId=' + id,
                    btn: ['确认', '取消'],
                    btnAlign: 'c',
                    yes: function (index, layero) {
                        // 获取子窗口的iframe
                        let iframeWindow = layero.find("iframe")[0].contentWindow;
                        // 调用子窗口的函数doSubmit
                        iframeWindow.doRefund(function (success) {
                            if (success) {
                                layer.msg("提交成功");
                                layer.close(index);
                                reload();
                            } else {
                                layer.msg("提交失败");
                            }
                        });
                    }
                });
            });
        }
    }
    // TODO:或许可以为tr添加hover
});
