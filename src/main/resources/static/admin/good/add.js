const layer = layui.layer;
const form = layui.form;
const $ = layui.jquery;
const upload = layui.upload;
const dropdown = layui.dropdown;
const element = layui.element;
const tree = layui.tree;
$(() => {
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
    // 渲染单张图片
    upload.render({
        elem: "#main-pic-upload-btn",
        field: "file",
        url: ctx + '/admin/api/v1/upload/image',
        data: {
            type: "good_pic"
        },
        exts: "jpg|png|gif|bmp|jpeg|svg",
        size: 1024,

        before: function (obj) {
            // 预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                // 上传之前先在img中显示
                $('#pic-upload-img').attr('src', result);
            });

            element.progress('filter-demo', '0%'); // 进度条复位
            layer.msg('上传中', {icon: 16, time: 0});
        },
        done: function (resp) {
            // 成功则显示图片并且将url存储下来后面提交使用
            // TODO:预览图片而不是直接提交图片，只有选择提交后才会把图片提交
            if (resp.success) {
                let url = ctx + "/static/" + resp.data;
                $(".pic-preview").css("background-image", "url(" + url + ")");

                //存储头像的地址
                sessionStorage.setItem("pic", resp.data);
            } else {
                layer.msg("上传图片失败");
            }
        },
        error: function (resp) {
            layer.msg("上传图片失败");
        },
        progress: function (n, elem, e) {
            element.progress('filter-demo', n + '%'); // 可配合 layui 进度条元素使用
            if (n == 100) {
                layer.msg('上传完毕', {icon: 1});
            }
        }
    });
    // 渲染多图片上传
    upload.render({
        elem: '#otherPics-upload-btn',
        url: ctx + '/admin/api/v1/upload/image',
        field: "file",
        multiple: true,
        data: {
            type: "good_other_pics"
        },
        exts: "jpg|png|gif|bmp|jpeg|svg",
        size: 1024,
        done: function (resp) {
            // 上传成功后
            if (resp.success) {
                //  为每张图片添加自己的id，添加点击图片删除事件
                //  TODO:删除功能待完善
                let src = ctx + "/static/" + resp.data;
                //  执行一次添加一个照片
                $('#otherPic-upload-preview').append('<img src="' + src + '" style="width: 90px; height: 90px;">')
                // $('#otherPic-upload-preview>img').on("click", function (d) {
                //     //  要删除的图片的id(也就是后台路径)
                //     console.log(this);
                //     let src = $(this).attr("src");
                //     let $del = $(this);
                //     layer.confirm("确定删除这张照片?", function (index) {
                //         deleteImg(src);
                //         $del.remove();
                //         layer.close(index);
                //     });
                // });
            } else {
                layer.msg("上传图片失败");
            }
        },
        allDone: function (obj) {
            $('#otherPic-upload-preview>img').on("click", function (d) {
                //  要删除的图片的id(也就是后台路径)
                let src = $(this).attr("src");
                let $del = $(this);
                layer.confirm("确定删除这张照片?", function (index) {
                    // 后台删除
                    deleteImg(src);
                    // 前端删除
                    $del.remove();
                    layer.close(index);
                });
            });
        },
        error(resp) {
            layer.msg("上传图片失败");
        }
    });
    //渲染富文本编辑器
    let ue = UE.getEditor('editor', {
        serverUrl: ctx + "/admin/api/v1/ue",
        toolbars: [
            [
                "fullscreen",   // 全屏
                "source",       // 源代码
                "|",
                "undo",         // 撤销
                "redo",         // 重做
                "|",
                "bold",         // 加粗
                "italic",       // 斜体
                "underline",    // 下划线
                "fontborder",   // 字符边框
                "strikethrough",// 删除线
                "superscript",  // 上标
                "subscript",    // 下标
                "removeformat", // 清除格式
                "formatmatch",  // 格式刷
                "autotypeset",  // 自动排版
                "blockquote",   // 引用
                "pasteplain",   // 纯文本粘贴模式
                "|",
                "forecolor",    // 字体颜色
                "backcolor",    // 背景色
                "insertorderedlist",   // 有序列表
                "insertunorderedlist", // 无序列表
                "selectall",    // 全选
                "cleardoc",     // 清空文档
                "|",
                "rowspacingtop",// 段前距
                "rowspacingbottom",    // 段后距
                "lineheight",          // 行间距
                "|",
                "customstyle",         // 自定义标题
                "paragraph",           // 段落格式
                "fontfamily",          // 字体
                "fontsize",            // 字号
                "|",
                "indent",              // 首行缩进
                "|",
                "justifyleft",         // 居左对齐
                "justifycenter",       // 居中对齐
                "justifyright",
                "justifyjustify",      // 两端对齐
                "|",
                "touppercase",         // 字母大写
                "tolowercase",         // 字母小写
                "|",
                "link",                // 超链接
                "unlink",              // 取消链接
                "anchor",              // 锚点
                "|",
                "imagenone",           // 图片默认
                "imageleft",           // 图片左浮动
                "imagecenter",         // 图片居中
                "imageright",          // 图片右浮动
                "|",
                "simpleupload",        // 单图上传
                "emotion",             // 表情
                "|",
                "horizontal",          // 分隔线
                "date",                // 日期
                "time",                // 时间
                "spechars",            // 特殊字符
            ]
        ]
    });
    window.ue = ue;
});


function doSubmit(cb) {
    let pass = form.validate("#add-form");
    if (!pass) {
        return;
    }
    // 获取表单数据
    let formData = form.val("add-form");
    // 添加主要图片路径
    let pic = "";
    pic = $(".pic-preview").css("background-image");
    if(pic && pic!=""){
        pic = pic.substring(5, pic.length - 2);
        pic = pic.replace("http://localhost:9991/static/","")
        formData.pic = pic;
    }
        // 取出后从session中删除
        // sessionStorage.removeItem("pic")
    // 添加次要多张图片路径
    let otherPics = "";
    for (let i of $("#otherPic-upload-preview img")) {
        let tmp = $(i).attr("src");
        if (tmp.startsWith(ctx + "/static/")) {
            tmp = tmp.substring((ctx + "/static/").length, tmp.length);
        }
        otherPics = otherPics + "," + tmp;
    }

    if (otherPics && otherPics !== "") {
        if (otherPics.startsWith(",")) {
            otherPics = otherPics.substring(1, otherPics.length);
        }
        formData.otherPics = otherPics;
    }

    // 添加富文本内容
    formData.detail = window.ue.getContent();
    let url = ctx + "/admin/api/v1/good";
    $.ajax({
        url,
        method: "post",
        dataType: "json",
        data: formData,
        success(resp) {
            if (typeof cb === "function") {
                cb(resp.success);
            }
        },
        error(resp){
            if(resp.status === 401){
                location.href = ctx +"/admin/login";
            }else {
                layer.msg(resp.responseJSON.msg);
            }
        }
    });
}

//  删除图片事件
function deleteImg(src) {
    // src精确给后台 让后台删除服务端图片
    console.log(src);
}
