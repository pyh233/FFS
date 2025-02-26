const $ = layui.jquery;
const laydate = layui.laydate;
const form = layui.form;
const layer = layui.layer
const upload = layui.upload;
const element = layui.element;
$(() => {
    // 渲染出生年月
    laydate.render({
        elem: "[name=birthday]"
    });
    //  渲染图片上传组件
    upload.render({
        elem: '#avatar-upload-btn',
        field: "file",
        url: ctx + '/admin/api/v1/upload/image',
        data: {
            type: "user_avatar"
        },
        exts: "jpg|png|gif|bmp|jpeg|svg",
        size: 1024,
        before: function (obj) {
            // 预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                // 上传之前先在img中显示
                $('#avatar-upload-img').attr('src', result);
            });

            element.progress('filter-demo', '0%'); // 进度条复位
            layer.msg('上传中', {icon: 16, time: 0});
        },
        done: function (resp) {
            // 成功则显示图片并且将url存储下来后面提交使用
            // TODO:预览图片而不是直接提交图片，只有选择提交后才会把图片提交
            if (resp.success) {
                let url = ctx + "/static/" + resp.data;
                $(".avatar-preview").css("background-image", "url(" + url + ")");
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
    // 自定义laiui前台参数验证
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
        },
        cardId: function (value, elem) {
            if (value && value.length !== 18) {
                return "身份证号必须是18位";
            }
        }
    });
})


function doSubmit(cb) {
    // 提交后后台进行参数校验
    let pass = form.validate("form")
    if (!pass) {
        return;
    }
    // 获取表单数据
    let formData = form.val("form");
    // 获取头像数据
    let avatar = "";
    avatar = $(".avatar-preview").css("background-image");
    if (avatar && avatar != "") {
        avatar = avatar.substring(5, avatar.length - 2);
        avatar = avatar.replace("http://localhost:9991/static/", "")
        formData.avatar = avatar;
    }
    const url = ctx + "/admin/api/v1/user";
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
    })
}
