const $ = layui.jquery;
const form = layui.form;
const layer = layui.layer;
const upload = layui.upload;
const laydate = layui.laydate;
const element = layui.element;
$(() => {
    if(error){
        layer.msg(error);
        return;
    }
    console.log(editAdmin);
    //  渲染时间输入框
    laydate.render({
        elem: "[name=lastLoginTime]"
    });
    // 自定义参数验证
    form.verify({
        account: function (value, elem) {
            if (value.length < 3 || value.length > 16) {
                return "账号的长度必须介于3-16之间";
            }
        },
        password: function (value, elem) {
            if (value.length < 6 || value.length > 16) {
                return "密码的长度必须介于6-16之间";
            }
        }
    });
    // 渲染图片上传组件
    upload.render({
        elem: '#avatar-upload-btn',
        field: "file",
        url: ctx + '/admin/api/v1/upload/image',
        data: {
            type: "admin_avatar"
        },
        exts: "jpg|png|gif|bmp|jpeg|svg",
        size: 1024,
        before: function (obj) {
            // 预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#avatar-upload-img').attr('src', result);
            });

            element.progress('filter-demo', '0%'); // 进度条复位
            layer.msg('上传中', {icon: 16, time: 0});
        },
        done: function (resp) {
            // 成功则显示图片并且将url存储下来后面提交使用
            // TODO:预览图片而不是直接提交图片，只有选择提交后才会把图片提交
            if(resp.success){
                let url = ctx + "/static/" + resp.data;
                $(".avatar-preview").css("background-image", "url(" + url + ")");

                sessionStorage.setItem("avatar_url",resp.data);
            }else{
                layer.msg("上传图片失败");
            }
        },
        error: function (resp) {
            layer.msg("上传图片失败!");
        },
        progress: function(n, elem, e){
            element.progress('filter-demo', n + '%'); // 可配合 layui 进度条元素使用
            if(n == 100){
                layer.msg('上传完毕', {icon: 1});
            }
        }
    });
    // TODO:数据回填
    form.val("edit-form",editAdmin);
    // 删除密码
    $("[name=password]").val("");
    // 头像数据回填
    if(editAdmin.avatarUrl && editAdmin.avatarUrl!=="") {
        let url = ctx + "/static/" + editAdmin.avatarUrl;
        $(".avatar-preview").css("background-image", "url(" + url + ")");
    }
});
// 添加事件
function doEdit(cb) {
    // 提交前在前端进行参数验证
    let pass = form.validate("#edit-form")
    if (!pass) {
        return;
    }
    // 通过则提交信息到后台
    let formData = form.val("edit-form");
    // 获取头像数据
    let avatar_url = sessionStorage.getItem("avatar_url")
    if (avatar_url) {
        formData.avatarUrl = avatar_url;
        sessionStorage.removeItem("avatar_url");
    }
    formData.id = editAdmin.id;

    const url = ctx + "/admin/api/v1/admin";
    $.ajax({
        url,
        method: "put",
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
