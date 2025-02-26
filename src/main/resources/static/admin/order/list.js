layui.use(['table', 'form', 'jquery'], function () {
    let $ = layui.jquery;
    let form = layui.form;
    let table = layui.table;
    let layer = layui.layer;
    let dropdown = layui.dropdown;
    let tree = layui.tree;
    table.render({
        elem: "#orderTable",
        url: ctx + '/admin/api/v1/order',
        method: "get",
        toolbar: "#toolbar",
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true, fixed: "left"},
            {field: 'orderNo', title: '订单编号', width: 120, fixed: "left"},
            {field: 'account', title: '用户账号', width: 160},
            {field: 'state', title: '订单状态', width: 160},
            {field: 'createdTime', title: '订单创建时间', width: 160},
            {field: 'totalPay', title: '总计支付', width: 160},
            {field: 'note', title: '用户备注', width: 160},
            {field: 'receiver', title: '收货人', width: 160},
            {field: 'receiverPhone', title: '收货人手机', width: 160},
            {field: 'receiverDetailAddress', title: '收货人详细地址', width: 160},
            {field: 'payType', title: '付款方式', width: 160},
            {field: 'payTime', title: '付款时间', width: 160},
            {field: 'putTime', title: '发货时间', width: 160},
            {field: 'confirmTime', title: '确认收获时间', width: 160},
            {field: 'applyRefundTime', title: '申请退款时间', width: 160},
            {field: 'agreeRefundTime', title: '同意退款时间', width: 160},
            {field: 'refundCause', title: '退款原因', width: 160},
            {title: "操作", width: 190, fixed: "right", toolbar: "#tool", align: "center"}
        ]],
        page: {
            layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'],
            groups: 3,
        },
        limits: [5, 10, 15, 20],
        limit: 2
    });

    // 添加表格工具栏事件
    table.on("toolbar(orderTable)", function (obj) {
        if (obj.event === "search") {
            reload();
        } else if (obj.event === "edit") {
            let check = getCheckData();
            if (check.length === 0) {
                layer.msg("请选择要修改的项!");
            } else {
                if (check.length > 1) {
                    layer.msg("一次只能修改一项!");
                } else {
                    toEdit(check[0].id);
                }
            }
        } else if (obj.event === "reset") {
            $("#search-form")[0].reset();
        }
    });
    //  行工具栏事件
    table.on("tool(orderTable)", function (obj) {
        if (obj.event === "refund") {
            if (obj.data.applyRefundTime) {
                layer.confirm("确认同意退款吗?", function (index) {
                    agreeRefund(obj.data.id);
                    layer.close(index);
                });
            } else {
                layer.msg("此商品买家未申请退款");
            }
        } else if (obj.event === "put") {
            if (obj.data.state === "PAYED" || obj.data.state === "REFUNDING") {
                layer.confirm("确认发货?", function (index) {
                    putOrder(obj.data.id)
                    layer.close(index);
                    reload();
                });
            } else if (obj.data.state === "NOT_PAY" || obj.data.state === "REFUNDED" || obj.data.state === "RECEIVED") {
                layer.msg("此订单不需要发货");
            } else if (obj.data.state === "SHIPPED") {
                layer.msg("此订单已发货");
            }
        }
    });

    // 重载函数
    function reload(page = 1) {
        let formData = form.val("search-form");
        table.reload("orderTable", {
            where: formData
        });
    }

    // 获取选中状态
    function getCheckData() {
        let status = table.checkStatus("orderTable");
        return status.data;
    }

    // 打开编辑页面函数
    function toEdit(id) {
        layer.open({
            type: 2,
            title: "编辑部分订单信息",
            area: ["85%", '90%'],
            content: ctx + '/admin/order/edit?orderId=' + id,
            btn: ['修改', '取消'],
            btnAlign: 'c',
            yes: function (index, layero) {
                // 获取子窗口的iframe
                let iframeWindow = layero.find("iframe")[0].contentWindow;
                // 调用子窗口的函数doSubmit
                iframeWindow.doEdit(function (success) {
                    if (success) {
                        layer.msg("修改商品操作成功");
                        layer.close(index);
                        reload();
                    } else {
                        layer.msg("修改商品操作失败");
                    }
                });
            }
        });
    }

    // 发获
    function putOrder(orderId) {
        let url = ctx + "/admin/order/put";
        $.ajax({
            url,
            method: "patch",
            data: {
                orderId
            },
            dataType: "json",
            success(resp) {
                if (resp.success) {
                    layer.msg("发货成功");
                    reload();
                }
            },
            error(resp) {
                if (resp.status === 401) {
                    location = ctx + "/admin/login";
                } else {
                    layer.msg(resp.responseJSON.msg);
                }
            }
        });
    }

    // 同意退款
    function agreeRefund(orderId) {
        let url = ctx + "/admin/order/refund/agree";
        $.ajax({
            url,
            method: "patch",
            dataType: "json",
            data: {
                orderId
            },
            success(resp) {
                if(resp.success){
                    layer.msg(resp.msg);
                    reload();
                }else {
                    layer.msg(resp.msg);
                }
            },
            error(resp) {
                if(resp.status === 401){
                    location.href = ctx + "/admin/login";
                }else {
                    layer.msg(resp.responseJSON.msg);
                }
            }
        });
    }
});
