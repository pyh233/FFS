const $ = layui.jquery;
const table = layui.table;
const form = layui.form;
const layer = layui.layer;

$(() => {
    // 渲染表格
    table.render({
        elem: "#tbl",
        url: ctx + "/admin/api/v1/admin",
        method: "get",
        toolbar: "#toolbar",
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true, fixed: "left"},
            {field: 'account', title: '账号', width: 120, fixed: "left"},
            {field: 'password', title: '密码', minWidth: 160},
            {
                field: 'avatarUrl', title: '头像', width: 80, templet: function (row) {
                    let url = ctx + "/static/" + row.avatarUrl;
                    return "<div class='avatar' style='background-image: url(" + url + ")'></div>";
                }
            },
            {field: 'description', title: '简介', minWidth: 160},
            {field: 'lastLoginTime', title: '上次登录时间', width: 160},
            {field: 'lastLoginIp', title: '上次登录IP', width: 160},
            {field: 'createTime', title: '创建时间', width: 160},
            {field: 'createdBy', title: '创建人', width: 100},
            {field: 'lastModifyTime', title: '修改时间', width: 160},
            {field: 'lastModifiedBy', title: '修改人', width: 100},
        ]],
        page: { // 支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
            layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局
            //curr: 5, //设定初始在第 5 页
            groups: 3, //只显示 1 个连续页码
        },
        limits: [5, 10],
        limit: 5 // 每页默认显示的数量
    });
    // 渲染表格工具栏
    table.on("toolbar(tbl)", function (obj) {
        if (obj.event === "reset") {
            $("#search-form")[0].reset();
        } else if (obj.event === "search") {
            reload();
        } else if (obj.event === "del") {
            let status = table.checkStatus("tbl");
            let check = status.data;
            let ids = [];
            check.forEach(it => ids.push(it.id));
            if (ids.length === 0) {
                layer.msg("请选中您要删除的行");
            } else {
                layer.confirm("确认删除选中数据?", function (index) {
                    deleteByIds(ids);
                    layer.close(index);
                    reload();
                });
            }
        } else if (obj.event === "add") {
            layer.open({
                type: 2,
                area: ['680px', '520px'],
                title: "添加管理员",
                content: ctx + '/admin/admin/add',
                btn: ['添加', '取消'],
                btnAlign: 'c',
                yes: function (index, layero) {
                    let iframeWin = layero.find("iframe")[0].contentWindow;
                    iframeWin.doSubmit(function (success) {
                        if (success) {
                            layer.msg("新增管理员操作成功");
                            layer.close(index);
                            reload();
                        } else {
                            layer.msg("新增管理员操作失败!");
                        }
                    })
                }
            });
        } else if (obj.event === "edit") {
            let status = table.checkStatus("tbl");
            let check = status.data;
            if (check.length === 0) {
                layer.msg("请选择要修改的项");
            } else if (check.length > 1) {
                layer.msg("一次只能修改一项");
            } else {
                layer.open({
                    type: 2,
                    area: ['680px', '520px'],
                    title: "修改管理员",
                    content: ctx + '/admin/admin/edit?id=' + check[0].id,
                    btn: ['添加', '取消'],
                    btnAlign: 'c',
                    yes: function (index, layero) {
                        let iframeWin = layero.find("iframe")[0].contentWindow;
                        iframeWin.doEdit(function (success) {
                            if (success) {
                                layer.msg("修改管理员操作成功");
                                layer.close(index);
                                reload();
                            } else {
                                layer.msg("修改管理员操作失败!");
                            }
                        })
                    }
                });
            }
        }
    });

})

// 表格重载函数
function reload() {
    let formData = form.val("search-form");
    table.reload("tbl", {
        where: formData
    });
}

// 删除行
function deleteByIds(ids) {
    const url = ctx + "/admin/api/v1/admin";
    $.ajax({
        url,
        method: "delete",
        data: {ids},
        traditional: true,
        success(resp) {
            if (resp.success) {
                layer.msg(resp.msg || "成功删除" + resp.data + "条数据")
                reload();
            } else {
                layer.msg(resp.msg || "删除失败!");
            }
        },
        error(resp) {
            if(resp.status === 401){
                location = ctx + "/admin/login";
            }else {
                layer.msg(resp.responseJSON.msg);
            }
        }
    })
}
