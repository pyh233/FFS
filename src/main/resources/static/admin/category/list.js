let treeTable = layui.treeTable;
let layer = layui.layer;
let $ = layui.jquery;

$(() => {
    // 渲染table
    treeTable.render({
        elem: '#category-table',
        url: ctx + '/admin/api/v1/category/all',
        method: "get",
        tree: {
            data: {
                cascade: "none"
            }
            /*
            // 异步加载子节点
            async: {
              enable: true,
              url: '/static/json/2/treeTable/demo-async.json', // 此处为静态模拟数据，实际使用时需换成真实接口
              autoParam: ["parentId=id"]
            }
            */
        },
        toolbar: '#toolbar',
        defaultToolbar: ['filter', 'exports', 'print'],
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true, fixed: 'left'},
            {field: 'name', title: '类名', width: 180},
            {field: 'alias', title: '别名', width: 80},
            {field: 'iconCls', title: '图标', width: 90},
            {field: 'seq', title: '序号', width: 100, sort: true},
            {field: 'parentId', title: '父类ID', width: 100, sort: true},
            {field: 'parentTreeName', title: '所属父类', width: 100},
            {field: "description", title: "简介", width: 190},
            {field: "description", title: "操作", width: 190, fixed: "right", toolbar: "#tool", align: "center"}
        ]]
    });
    // 渲染table工具栏
    treeTable.on("toolbar(category-table)", function (obj) {
        if (obj.event === "add") {
            let status = treeTable.checkStatus("category-table")
            let checked = status.data;
            // 添加的时候回填父类名称,默认为根类作为父类
            let parentId = 1;
            // 检查是否选中一项
            if (checked.length >= 2) {
                layer.msg("请不要同时选择两个或以上种类作为新建类的父类");
            } else {
                if (checked.length === 1) {
                    parentId = checked[0].id;
                }
                toAdd(parentId);
            }

        } else if (obj.event === "edit") {
            let check = getCheckData();
            if (check.length === 0) {
                layer.msg("请选择要修改的项目");
                return;
            }
            if (check.length > 1) {
                layer.msg("一次只能修改一项");
            } else {
                toEdit(check[0].id);
            }
        } else if (obj.event === "del") {
            let check = getCheckData();
            let ids = [];
            check.forEach(it => ids.push(it.id));
            if (ids.length === 0) {
                layer.msg("请选择要删除的类别");
            } else {
                layer.confirm("确认删除选中项吗？", function (index) {
                    deleteByIds(ids);
                    layer.close(index);
                    reload();
                });
            }
        } else if (obj.event === "search") {
            reload();
        }
    });
    // 渲染行工具栏
    treeTable.on("tool(category-table)", function (obj) {
        if (obj.event === "edit") {
            toEdit(obj.data.id)
        } else {
            layer.confirm("确认删除此类?", function (index) {
                deleteByIds([obj.data.id]);
                layer.close(index);
                // TODO: 为什么单单这里的reload刷新生效但表格显示不生效，因为缓存？
                reload();
            });
        }
    });
});

//
function reload() {
    console.log("reload category-table");
    treeTable.reload("category-table");
}

// 直接发送删除请求 传递删除ids数组
function deleteByIds(ids) {
    let url = ctx + "/admin/api/v1/category";
    $.ajax({
        url,
        method: "delete",
        data: {
            ids
        },
        traditional: true,
        success(resp) {
            layer.msg(resp.msg || "删除成功");
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

// 跳转添加页面，传递id
function toAdd(parentId) {
    layer.open({
        type: 2,
        area: ['680px', '520px'],
        content: ctx + '/admin/category/add?id=' + parentId,
        title: "添加类别",
        btn: ['添加', '取消'],
        btnAlign: 'c',
        yes: function (index, layero) {
            let iframeWin = layero.find("iframe")[0].contentWindow;
            iframeWin.doSubmit(function (success) {
                if (success) {
                    layer.msg("新增种类操作成功");
                    layer.close(index);
                    reload();
                } else {
                    layer.msg("新增种类操作失败!");
                }
            });
        }
    });
}

function toEdit(id) {
    layer.open({
        type: 2,
        area: ['680px', '520px'],
        content: ctx + '/admin/category/edit?id=' + id,
        title: "修改类别",
        btn: ['确认修改', '取消'],
        btnAlign: 'c',
        yes: function (index, layero) {
            let iframeWin = layero.find("iframe")[0].contentWindow;
            iframeWin.doEdit(function (resp) {
                if (resp.success) {
                    layer.msg("修改种类操作成功");
                    layer.close(index);
                    reload();
                } else {
                    layer.msg(resp.msg);
                }
            });
        }
    });
}

function getCheckData() {
    let status = treeTable.checkStatus("category-table");
    return status.data;
}
