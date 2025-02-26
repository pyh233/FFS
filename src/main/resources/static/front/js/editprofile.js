const upload = layui.upload;
$(()=>{
    upload.render({
        elem: '.fa-camera',
        field: "file",
        url: ctx + '/admin/api/v1/upload/image',
        data: {
            type: "user_avatar"
        },
        exts: "jpg|png|gif|bmp|jpeg|svg",
        size: 1024,

        done: function (resp) {
            // 成功则显示图片并且将url存储下来后面提交使用
            // TODO:预览图片而不是直接提交图片，只有选择提交后才会把图片提交
            if(resp.success){
                $(".profile-img>img").attr('src', "/static/"+resp.data).data("img-url", resp.data);
            }else{
                layer.msg("上传图片失败");
            }
        },
        error: function (resp) {
            layer.msg("上传图片失败!");
        }
    });
    $(".btn").click(function (e) {
        e.preventDefault();
       layer.confirm("确定修改?",function (index) {
           doEditMyProfile();
           layer.close(index);
       })
    });
});
function doEditMyProfile(){
    let name = $("[name=name]").val();
    let sex = $("[name=sex]").val();
    let avatar = $(".profile-img>img").data("img-url");
    let url = ctx +"/my/profile/edit";
    $.ajax({
        url,
        method:"put",
        dataType:"json",
        data: {
            name,
            sex,
            avatar
        },
        success(resp){
            if(resp.success){
                layer.msg(resp.msg);
                setTimeout("location.href = ctx+ '/my/profile';",2000);
            }else {
                layer.msg(resp.msg);
            }
        },
        error(resp){
            if(resp.status === 401){
                location.href = ctx + "/login";
            }else {
                layer.msg(resp.responseJSON.msg);
            }
        }
    });
}
