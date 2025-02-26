layui.use(['table', 'form', 'jquery'], function () {
    let $ = layui.jquery;
    let form = layui.form;
    let table = layui.table;
    let layer = layui.layer;
    let dropdown = layui.dropdown;
    let tree = layui.tree;
    table.render({
        elem: "#goodTable",
        url: ctx + '/admin/api/v1/good',
        method: "get",
        toolbar: "#toolbar",
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 80, sort: true, fixed: "left"},
            {field: 'skuNo', title: '商品编号', width: 120, fixed: "left"},
            {field: 'name', title: '商品名称', minWidth: 160},
            {field: 'alias', title: '商品别名', width: 160},
            {field: 'summary', title: '商品摘要', width: 160},
            {field: 'categoryId', title: '种类ID', width: 160},
            {
                field: 'category', title: '类别', width: 160, templet: function (row) {
                    let content = row.category.name;
                    return "<div>" + content + "</div>";
                }
            },
            {field: 'brandId', title: '品牌ID', width: 160},
            {
                field: 'brand', title: '品牌', width: 100, templet: function (row) {
                    let content = row.brand.name;
                    return "<div>" + content + "</div>";
                }
            },
            {field: 'markPrice', title: '建议零售价', width: 160},
            {field: 'price', title: '实际价格', width: 100},
            {field: 'qty', title: '库存', width: 100},
            {
                field: 'pic', title: '主要图片', width: 100, templet: function (row) {
                    let url = ctx + "/static/" + row.pic;
                    return "<div class='pic' style='background-image: url(" + url + ")'></div>";
                }
            },
            {field: 'otherPics', title: '其他图片', width: 100},
            {field: 'detail', title: '详情', width: 100},
            {field: 'description', title: '备注', width: 100},
            {title: "操作", width: 190, fixed: "right", toolbar: "#tool", align: "center"}
        ]],
        page: {
            layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'],
            groups: 3,
        },
        limits: [5, 10, 15, 20],
        limit: 10
    });
    // 渲染类别下拉列表
    dropdown.render({
        id: 'dw',
        elem: "[name=categoryName]",
        content: '<div id="category-tree"></div>',
        // 渲染结束
        ready: function (elemPanel, elem) {
            let url = ctx + "/admin/api/v1/category/all";
            $.ajax({
                url,
                method: "get",
                success(resp) {
                    tree.render({
                        elem: "#category-tree",
                        data: resp.data,
                        onlyIconControl: true,
                        customName: {
                            id: "id",
                            title: "name",
                            children: "children"
                        },
                        click: function (obj) {
                            let data = obj.data;
                            $("[name=categoryName]").val(data.name);
                            $("[name=categoryId]").val(data.id);
                            dropdown.close("dw");
                        }
                    });
                },
                error(resp) {
                    let json = resp.responseJSON;
                    layer.msg(json.msg || "渲染失败");
                }
            });
        }
    });
    // 渲染品牌下拉列表
    dropdown.render({
        id: 'dw2',
        elem: "[name=brandName]",
        content: '<div id="brand-tree"></div>',
        // 渲染结束
        ready: function (elemPanel, elem) {
            let url = ctx + "/admin/api/v1/brand/all";
            $.ajax({
                url,
                method: "get",
                success(resp) {
                    tree.render({
                        elem: "#brand-tree",
                        data: resp.data,
                        customName: {
                            id: "id",
                            title: "name"
                        },
                        click: function (obj) {
                            let data = obj.data;
                            $("[name=brandName]").val(data.name);
                            $("[name=brandId]").val(data.id);
                            dropdown.close("dw2");
                        }
                    });
                },
                error(resp) {
                    console.log("拉取品牌列表失败");
                }
            });
        }
    });
    // 添加表格工具栏事件
    table.on("toolbar(goodTable)", function (obj) {
        if (obj.event === "search") {
            reload();
        } else if (obj.event === "del") {
            let check = getCheckData();
            if (check.length === 0) {
                layer.msg("请选择要删除的行");
            } else {
                let ids = [];
                check.forEach(it => ids.push(it.id));
                layer.confirm("确认删除?", function (index) {
                    deleteByIds(ids);
                    layer.close(index);
                    reload();
                });
            }
        } else if (obj.event === "add") {
            toAdd()
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
    table.on("tool(goodTable)", function (obj) {
        if (obj.event === "show") {
            layer.open({
                type: 2,
                title: "商品展示",
                area: ["85%", '90%'],
                content: ctx + '/admin/good/show?id=' + obj.data.id,
                btn: ['关闭'],
                btnAlign: 'c',
                yes: function (index, layero) {
                    layer.close(index);
                },

            });
        } else if (obj.event === "edit") {
            toEdit(obj.data.id);
        } else if (obj.event === "del") {
            layer.confirm("确认删除此项?", function (index) {
                deleteByIds([obj.data.id]);
                layer.close(index);
                reload();
            });
        }
    });

    // 重载函数
    function reload() {
        let formData = form.val("search-form");
        table.reload("goodTable", {
            where: formData
        });
    }

    // 获取选中状态
    function getCheckData() {
        let status = table.checkStatus("goodTable");
        return status.data;
    }

    // 删除函数
    function deleteByIds(ids) {
        let url = ctx + "/admin/api/v1/good"
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

    // 打开添加页面函数
    function toAdd() {
        layer.open({
            type: 2,
            title: "添加商品信息",
            area: ["85%", '90%'],
            content: ctx + '/admin/good/add',
            btn: ['添加', '取消'],
            btnAlign: 'c',
            yes: function (index, layero) {
                // 获取子窗口的iframe
                let iframeWindow = layero.find("iframe")[0].contentWindow;
                // 调用子窗口的函数doSubmit
                iframeWindow.doSubmit(function (success) {
                    if (success) {
                        layer.msg("新增商品操作成功");
                        layer.close(index);
                        reload();
                    } else {
                        layer.msg("新增商品操作失败");
                    }
                });
            }
        });
    }

    // 打开编辑页面函数
    function toEdit(id) {
        layer.open({
            type: 2,
            title: "编辑商品信息",
            area: ["85%", '90%'],
            content: ctx + '/admin/good/edit?id=' + id,
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

});
