const $ = layui.jquery;
const table = layui.table;
const laydate = layui.laydate;
const form = layui.form;
const layer = layui.layer;
$(() => {
    //渲染表格
    table.render({
        elem: '#tbl',
        url: ctx + "/admin/api/v1/user",//向后台提交数据的地址
        method: "get",
        toolbar: "#toolbar",
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true, fixed: "left"},
            {field: 'account', title: '账号', width: 120, fixed: "left"},
            {field: 'password', title: '密码', minWidth: 160},
            {
                field: 'avatar', title: '头像', width: 80, templet: function (row) {
                    let url = ctx + "/static/" + row.avatar;
                    return "<div class='avatar' style='background-image: url(" + url + ")'></div>";
                }
            },
            {field: 'name', title: '姓名', width: 120},
            {field: 'sex', title: '性别', width: 80},
            {field: 'birthday', title: '出生日期', width: 140},
            {field: 'phone', title: '电话', width: 140, sort: true},
            {field: 'email', title: '邮箱', width: 160},
            {field: 'cardId', title: '身份证号', width: 160},
            {field: 'description', title: '简介', width: 160},
            {field: 'createdTime', title: '创建时间', width: 160},
            {field: 'createdBy', title: '创建人', width: 100},
            {field: 'updatedTime', title: '修改时间', width: 160},
            {field: 'updatedBy', title: '修改人', width: 100},
        ]],
        page: { // 支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
            layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局
            //curr: 5, //设定初始在第 5 页
            groups: 3, //只显示 1 个连续页码
        },
        limits: [10, 15, 20, 30, 100],
        limit: 10
        // error: function (e, msg) {
        //     let formData = form.val("search-form");
        //     let page = this.page - 1;
        //     if (page > 0) {
        //         table.reload("tbl", {
        //             where: formData,
        //             page:{
        //                 curr:this.curr - 1
        //             }
        //         });
        //     }
        // }
    });

    //渲染日期输入框
    laydate.render({
        elem: "[name=birthdayRange]",
        range: "~"
    });

    //添加工具栏事件
    table.on("toolbar(tbl)", function (obj) {
        if (obj.event === "reset") {
            //重置
            $("#search-form")[0].reset();
        } else if (obj.event === "search") {
            reload();
        } else if (obj.event === "del") {
            // 获取选择项以及选中信息
            let status = table.checkStatus("tbl");
            // 获取选择项
            let checked = status.data;
            // 获取选择项的id
            let ids = [];
            checked.forEach(it => ids.push(it.id));
            if (ids.length === 0) {
                layer.msg("请选中您要删除的行")
            } else {
                layer.confirm("是否确认删除选中的行?", function (index) {
                    deleteByIds(ids);
                    layer.close(index);
                });
            }
        } else if (obj.event === "add") {
            toAdd();
        } else if (obj.event === "edit") {
            // 选中并且单击编辑按钮
            let status = table.checkStatus("tbl");
            let checked = status.data;

            if (checked.length === 0) {
                layer.msg("请选择要修改的行!");
            } else if (checked.length > 1) {
                layer.msg("一次仅支持修改一行!");
            } else {
                edit(checked[0].id);
            }

        }
    });
});

function reload() {
    let formData = form.val("search-form");
    table.reload("tbl", {
        where: formData,
    });
}

//批量删除
function deleteByIds(ids) {
    const url = ctx + "/admin/api/v1/user";
    $.ajax({
        url,
        method: "delete",
        data: {
            ids
        },
        traditional: true,//向后台提交数据必须指定此属性
        success(resp) {//200
            if (resp.success) {
                layer.msg(resp.msg || "删除操作成功，共删除" + resp.data + "条");
                //刷新表格
                reload();
            } else {
                layer.msg(resp.msg || "删除操作失败");
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

// 添加
function toAdd() {
    layer.open({
        type: 2,
        title: "添加用户信息",
        area: ['620px', '650px'],
        content: ctx + '/admin/user/add',
        btn: ['添加', '取消'],
        btnAlign: 'c',
        yes: function (index, layero) {
            // 获取子窗口的iframe
            let iframeWindow = layero.find("iframe")[0].contentWindow;
            // 调用子窗口的函数doSubmit
            iframeWindow.doSubmit(function (success) {
                if (success) {
                    layer.msg("新增用户操作成功");
                    layer.close(index);
                    reload();
                } else {
                    layer.msg("新增用户操作失败");
                }
            });
        }
    });
}

//修改
function edit(id) {
    layer.open({
        type: 2,
        title: "修改用户信息",
        area: ['620px', '650px'],
        content: ctx + '/admin/user/edit?id=' + id,
        btn: ['确定', '取消'],
        btnAlign: 'c',
        yes: function (index, layero) {
            let iframeWindow = layero.find("iframe")[0].contentWindow;
            iframeWindow.doEdit(function (success) {
                if (success) {
                    layer.msg("修改用户操作成功");
                    layer.close(index);
                    reload();
                } else {
                    layer.msg("修改用户操作失败");
                }
            });
        }
    });
}
